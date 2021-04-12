import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import absyn.*;

public class CodeGenerator implements AbsynVisitor {
    public static int ac = 0;
    public static int ac1 = 1;
    public static int pc = 7;
    public static int gp = 6;
    public static int fp = 5;

    public static int globalOffset;
    public static int mainEntry;

    public static int ofpFO = 0;
	public static int retFO = -1;
    public static int initFO = -2;
    
    public static int emitLoc = 0;
    public static int highEmitLoc;
    HashMap<String, ArrayList<NodeType>> table;

    public CodeGenerator() {

        mainEntry = 0;
        globalOffset = 0;
        emitLoc = 0;
        highEmitLoc = 0;
        table = new HashMap<String, ArrayList<NodeType>>();

    }

    public void visit(DecList decs) {

        emitComment("C-Minus Compilation to TM Code");
        emitComment("File: ");
		emitComment("Standard prelude:");
        // generate prelude
        emitRM("LD", 6, 0, 0, "load gp with maxaddr");
		emitRM("LDA", 5, 0, 6, "Copy gp to fp");
        emitRM("ST", 0, 0, 0, "clear location 0");
        int savedLoc = emitSkip(1);
        emitComment("Jump around i/o routines");
        emitComment("Code for input routine");
        emitRM("ST", ac, -1, fp, "store return");
        emitRO("IN", ac, 0, 0, "input");
        emitRM("LD", pc, -1, fp, "return to caller");
        emitComment("Code for output routine");
        emitRM("ST", ac, -1, fp, "store return");
        emitRM("LD", ac, -2, fp, "load output value");
        emitRO("OUT", ac, 0, 0, "output");
        emitRM("LD", pc, -1, fp, "return to caller");
        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("LDA", pc, savedLoc2, "jump around i/o code");
        emitComment("end of standard prelude.");
        emitRestore();

        visit(decs, 0 , false, 0);

        // check if main is 0 terminate
        if (mainEntry == 0) {
            System.err.println("fatal error: program requires main to compile.");
            System.exit(0);
        }

        /// generate finale
        emitRM("ST", fp, globalOffset+ofpFO, fp, "push ofp");
        emitRM("LDA", fp, globalOffset, fp, "push frame");
        emitRM("LDA", ac, 1, pc, "load ac with ret ptr");
        emitRM_Abs("LDA", pc, mainEntry, "jump to main loc");
        emitRM("LD", fp, ofpFO, fp, "pop frame");
        emitComment("end of execution.");
        emitRO("HALT", 0, 0, 0, "");
               
    }


    @Override
    public void visit(DecList exp, int offset, boolean isAddr, int level) {
        
        NodeType output = new NodeType("output", 7, 1, level);
        if (table.get("output") == null) {
            table.put("output", new ArrayList<NodeType>());
        } 
        table.get("output").add(output);

        NodeType input = new NodeType("input", 4, 1, level);
        if (table.get("input") == null) {
            table.put("input", new ArrayList<NodeType>());
        } 
        table.get("input").add(input);
        while( exp != null ) {
            if (exp.head instanceof VarDec){
                VarDec var = (VarDec) exp.head;
                if (var instanceof SimpleDec){
                    SimpleDec sVar = (SimpleDec)var;
                    NodeType entry = new NodeType(sVar.name, globalOffset, 0, level);
                    if (table.get(sVar.name) == null) {
                        table.put(sVar.name, new ArrayList<NodeType>());
                    } 
                    table.get(sVar.name).add(entry);
                    emitComment("Allocating global var: " + sVar.name);
                    emitComment("<- vardecl");
                    globalOffset--;
                } else if (var instanceof ArrayDec) {
                    ArrayDec aVar = (ArrayDec)var;
                    NodeType entry = new NodeType(aVar.name, globalOffset, 0, level);
                    if (table.get(aVar.name) == null) {
                        table.put(aVar.name, new ArrayList<NodeType>());
                    } 
                    table.get(aVar.name).add(entry);
                    emitComment("Allocating global var: " + aVar.name);
                    emitComment("<- vardecl");
                    globalOffset = globalOffset - Integer.parseInt(aVar.size.value);
                }
            } 
            exp.head.accept( this, offset, isAddr, level);
            exp = exp.tail;
          } 
        
    }


    @Override
    public void visit(ExpList expList, int offset, boolean isAddr, int level) {
        while( expList != null ) {
            if (expList.head != null){
              expList.head.accept( this, offset, false, level );
              expList = expList.tail;
            }  
        }    
    }

    @Override
    public void visit(AssignExp exp, int offset, boolean isAddr, int level) {
        emitComment("-> op");
        if(exp.lhs instanceof SimpleVar){
            visit((SimpleVar)exp.lhs, offset-1, true, level);
            emitComment("<- id"); 
            emitRM("ST", ac, offset, fp, "op: push left");
        } else if(exp.lhs instanceof IndexVar) {
            visit((IndexVar)exp.lhs, offset-1, false, level);
        }

        if (exp.rhs instanceof OpExp)
        {
            visit((OpExp)exp.rhs, offset-2, false, level);

        }else if(exp.rhs instanceof IntExp){

            visit((IntExp)exp.rhs, offset-2, false, level);
      
        } else if(exp.rhs instanceof CallExp) {

            visit((CallExp)exp.rhs, offset-2, false, level);

        } else if(exp.rhs instanceof OpExp) {

            visit((OpExp)exp.rhs, offset-2, false, level);

        } else if (exp.rhs instanceof VarExp) {

            visit((VarExp)exp.rhs, offset-2, false, level);
        }

        emitRM("LD", 1, offset, fp, "op: load left");
        emitRM("ST", 0, 0, 1, "assign: store value");
        emitComment("<- op");
    }

    @Override
    public void visit(SimpleVar exp, int offset, boolean isAddr, int level) {

        emitComment("-> id");
        emitComment("looking up id: " + exp.name); 
        int x = getAddress(exp.name);
        int lev = getNestLevel(exp.name);
        if (lev == 0) {

            if(isAddr == true) {
         
                emitRM("LDA", 0, x, gp, "load id address");

            } else {
    
                emitRM("LD", 0, x, gp, "load id value");
            }
            

        } else {

            if(isAddr == true) {
         
                emitRM("LDA", 0, x, fp, "load id address");

            } else {
                
                emitRM("LD", 0, x, fp, "load id value");
            }  

        }
        emitComment("<- id");
       
    }

    @Override
    public void visit(IndexVar exp, int offset, boolean isAddr, int level) {
        emitComment("-> subs");
        int x = getAddress(exp.name);
        int lev = getNestLevel(exp.name);
        if (lev == 0) {

            emitRM("LD", ac, x, gp, "load id value");
            emitRM("ST", ac, offset--, gp, "store array addr");
            exp.index.accept(this, offset, false, level);
            emitComment("<- subs");

        } else {

            emitRM("LD", ac, x, fp, "load id value");
            emitRM("ST", ac, offset--, fp, "store array addr");
            exp.index.accept(this, offset, false, level);
            emitComment("<- subs");

        }
       
    }

    @Override
    public void visit(IfExp exp, int offset, boolean isAddr, int level) {
        level++;
        emitComment("-> if");
        exp.test.accept( this, offset, false, level );
        int savedLoc = emitSkip(1);
        exp.thenpart.accept( this, offset, false, level );
        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("JEQ", 0, savedLoc2, "if: jump to else part");
        emitRestore();
        delete(level);
        if (exp.elsepart != null ) {
           emitComment("if: jump to else belongs here");
           exp.elsepart.accept( this, offset, false, level );
           delete(level);
        }
        emitComment("<- if");
    }

    @Override
    public void visit(IntExp exp, int offset, boolean isAddr, int level) {
        emitComment("-> constant");
        emitRM("LDC", ac, Integer.parseInt(exp.value), 0, "load const");
        emitComment("<- constant");
        
    }

    @Override
    public void visit(OpExp exp, int offset, boolean isAddr, int level) {
         emitComment("-> op");

        if(exp.left instanceof IntExp){

            visit((IntExp)exp.left, offset-1, false, level);
            emitRM("ST", ac, offset--, fp, "op: push left");

        } else if(exp.left instanceof VarExp){
            
            visit((VarExp)exp.left, offset-1, false, level);
            emitRM("ST", ac, offset--, fp, "op: push left");
        } else if(exp.left instanceof OpExp){
            
            visit((OpExp)exp.left, offset, false, level);
            emitRM("ST", ac, offset--, fp, "");
        }
      
        if(exp.right instanceof IntExp){

            visit((IntExp)exp.right, offset-2, false, level);
      
        } else if(exp.right instanceof VarExp){

            visit((VarExp)exp.right, offset-2, false, level);
        } else if(exp.right instanceof OpExp){

            visit((OpExp)exp.right, offset-2, false, level);

        }  else if(exp.right instanceof CallExp){
            visit((CallExp)exp.right, offset-2, false, level);
          }

        emitRM("LD", 1, ++offset, fp, "op: load left");

        switch( exp.op ) {
        case OpExp.PLUS:
            emitRO("ADD", ac, 1, ac, "op +");
            break;
        case OpExp.MINUS:
            emitRO("SUB", ac, 1, ac, "op -");
            break;
        case OpExp.TIMES:
            emitRO("MUL", ac, 1, ac, "op *");
            break;
        case OpExp.OVER:
            emitRO("DIV", ac, 1, ac, "op /");
            break;
        case OpExp.EQ:
            emitRO("EQU", ac, 1, ac, "op =");
            break;
        case OpExp.NEQ:
            emitRO("SUB", ac, 1, ac, "op !=");
            emitRM("JNE", ac, 2, pc, "br if true");
            emitRM("LDC", ac, 0, 0, "false case");
            emitRM("LDA", pc, 1, pc, "unconditional jump");
            emitRM("LDC", ac, 1, 0, "true case");
            break;
        case OpExp.LT:
            emitRO("SUB", ac, 1, ac, "op <");
            emitRM("JLT", ac, 2, pc, "br if true");
            emitRM("LDC", ac, 0, 0, "false case");
            emitRM("LDA", pc, 1, pc, "unconditional jump");
            emitRM("LDC", ac, 1, 0, "true case");
            break;
        case OpExp.LTE:
            emitRO("SUB", ac, 1, ac, "op <=");
            emitRM("JLE", ac, 2, pc, "br if true");
            emitRM("LDC", ac, 0, 0, "false case");
            emitRM("LDA", pc, 1, pc, "unconditional jump");
            emitRM("LDC", ac, 1, 0, "true case");
            break;
        case OpExp.GT:
            emitRO("SUB", ac, 1, ac, "op >");
            emitRM("JGT", ac, 2, pc, "br if true");
            emitRM("LDC", ac, 0, 0, "false case");
            emitRM("LDA", pc, 1, pc, "unconditional jump");
            emitRM("LDC", ac, 1, 0, "true case");
            break;
        case OpExp.GTE:
            emitRO("SUB", ac, 1, ac, "OP >=");
            emitRM("JLE", ac, 2, pc, "br if true");
            emitRM("LDC", ac, 0, 0, "false case");
            emitRM("LDA", pc, 1, pc, "unconditional jump");
            emitRM("LDC", ac, 1, 0, "true case");
            break;
        case OpExp.COMPARE:
            emitRO("SUB", ac, 1, ac, "op ==");
            emitRM("JEQ", ac, 2, pc, "br if true");
            emitRM("LDC", ac, 0, 0, "false case");
            emitRM("LDA", pc, 1, pc, "unconditional jump");
            emitRM("LDC", ac, 1, 0, "true case");
            break;
        default:
        }

        emitComment("<- op");
        
    }

    @Override
    public void visit(VarExp exp, int offset, boolean isAddr, int level) {

        if (exp.variable instanceof SimpleVar) {

            visit((SimpleVar)exp.variable, offset, false, level);

        } else if (exp.variable instanceof IndexVar) {

            visit((IndexVar)exp.variable, offset, false, level);
        }

    }

    @Override
    public void visit(NilExp exp, int offset, boolean isAddr, int level) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(CallExp exp, int offset, boolean isAddr, int level) {
        int i = -2;
        emitComment("-> call of function: " + exp.func);
        int x = getAddress(exp.func);
        ExpList ex = exp.args;
        while( ex != null ) {
            if (exp.args.head != null){
                ex.head.accept( this, offset, false, level);
                emitRM("ST", ac, offset+i, fp, "op: push left");
                i--;
              }
            ex = ex.tail;
        }
        emitRM("ST", fp, offset, fp, "push ofp");
        emitRM("LDA", fp, offset, fp, "Push frame");
        emitRM("LDA", 0, 1, pc, "Load ac with ret ptr");
        emitRM_Abs("LDA", pc, x, "jump to fun loc");
        emitRM("LD", fp, 0, fp, "Pop frame");
        emitComment("<- call");
    }

    @Override
    public void visit(WhileExp exp, int offset, boolean isAddr, int level) {
        level++;
        emitComment("-> while");
        emitComment("while: jump after body comes back here");
        int savedLoc3 = emitSkip(0);
        exp.test.accept( this, offset, false, level );
        int savedLoc = emitSkip(1);
        exp.body.accept( this, offset, false, level ); 
        emitRM_Abs("LDA", pc, savedLoc3, "While: absolute jmp to test");
        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("JEQ", 0, savedLoc2, "While: jmp to end");
        emitRestore();
        emitComment("<- while");
        delete(level);
    }

    @Override
    public void visit(ReturnExp exp, int offset, boolean isAddr, int level) {
        emitComment("-> return");
        exp.exp.accept(this, offset, false, level);
        emitRM("LD", pc, -1, fp, "return to caller");
        emitComment("<- return");
    }

    @Override
    public void visit(CompoundExp exp, int offset, boolean isAddr, int level) {
        emitComment("-> compound statement");
        VarDecList dec = exp.decs;
        while( dec != null ) {
        if(dec.head != null) {
            dec.head.accept( this, --offset, false, level);
        }
            dec = dec.tail;
        } 
        ExpList ex = exp.exps;
        while( ex != null ) {
            if(ex.head != null) {
            ex.head.accept( this, --offset, false, level);
            }
            ex = ex.tail;
        } 
        emitComment("<- compound statement");
    }

    @Override
    public void visit(NameTy exp, int offset, boolean isAddr, int level) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(FunctionDec exp, int offset, boolean isAddr, int level) {
        offset = 0;
        level++;
        emitLoc = emitLoc + 1;
        NodeType entry = new NodeType(exp.func, emitLoc, 1, 0);
        if (table.get(exp.func) == null) {
            table.put(exp.func, new ArrayList<NodeType>());
        } 
        table.get(exp.func).add(entry);
        emitLoc--;
        emitComment("Processing function: " + exp.func);
        emitComment("jump around function body here");
        int savedLoc = emitSkip(1);
        if (exp.func.equals("main"))
            mainEntry = emitLoc;
        emitRM("ST", 0, --offset, fp, "store return");
        exp.result.accept(this, offset, false, level);
        VarDecList ex = exp.params;
        while( ex != null ) {
          if (ex.head instanceof SimpleDec){
            visit((SimpleDec)ex.head, --offset, true, level);
          } else if (ex.head instanceof ArrayDec) {
            visit((ArrayDec)ex.head, --offset, true, level);
          }
         
          ex = ex.tail;
        } 
        exp.body.accept(this, offset, false, level);
        emitRM("LD", pc, -1, fp, "return to caller");
        int savedLoc2 = emitSkip(0);
        emitBackup(savedLoc);
        emitRM_Abs("LDA", pc, savedLoc2, "Jump around function body");
        emitRestore();
        delete(level);
        removeFuncVars(table.entrySet().iterator());
        emitComment("<- fundecl");
    }

    @Override
    public void visit(SimpleDec exp, int offset, boolean isParameter, int level) {

        if (isParameter == true) {
            NodeType entry = new NodeType(exp.name, offset, 1, level);
            if (table.get(exp.name) == null) {
                table.put(exp.name, new ArrayList<NodeType>());
            } 
            table.get(exp.name).add(entry);
        } else {

            NodeType entry = new NodeType(exp.name, offset, 1, level);
            if (table.get(exp.name) == null) {
                table.put(exp.name, new ArrayList<NodeType>());
            } 
            table.get(exp.name).add(entry);
            emitComment("processing local var: " + exp.name);
            exp.typ.accept( this, offset, false, level);

        }

    }

    @Override
    public void visit(ArrayDec exp, int offset, boolean isParameter, int level) {

        if (isParameter == true) {
            NodeType entry = new NodeType(exp.name, offset, 1, level);
            if (table.get(exp.name) == null) {
                table.put(exp.name, new ArrayList<NodeType>());
            } 
            table.get(exp.name).add(entry);
        } else {

            NodeType entry = new NodeType(exp.name, offset, 1, level);
            if (table.get(exp.name) == null) {
                table.put(exp.name, new ArrayList<NodeType>());
            } 
            table.get(exp.name).add(entry);
            emitComment("processing local var: " + exp.name);

        }

        if (exp.size != null) {
        } 
        exp.typ.accept( this, offset, false, level);
    
    }

    @Override
    public void visit(VarDecList exp, int offset, boolean isAddr, int level) {
        while( exp != null ) {
            if (exp.head != null) {
              exp.head.accept( this, offset, false, level);
              exp = exp.tail;
            }
        } 
    }

    public int getAddress(String name) {
        int addy = 0;
        for(ArrayList<NodeType> node : table.values()) {
            for(int j=node.size()-1;j>=0;j--) {
                if (node.get(j).name.equals(name))
                    addy = node.get(j).address;
            }
        }
        return addy;
    }

    public int getNestLevel(String name) {
        int lev = 0;
        for(ArrayList<NodeType> node : table.values()) {
            for(int j=node.size()-1;j>=0;j--) {
                if (node.get(j).name.equals(name))
                    lev = node.get(j).nestLevel;
            }
        }
        return lev;
    }

    public void removeFuncVars(Iterator it) {
        while (it.hasNext()) {
          Entry<String, ArrayList<NodeType>> e = (Entry<String, ArrayList<NodeType>>) it.next();
          if(e.getValue().isEmpty()) {
            it.remove();
          } 
        } 
      }
      
      public void delete(int level) {
        for(ArrayList<NodeType> node : table.values()) {
          for(int j=node.size()-1;j>=0;j--) {
              if (node.get(j).level == level)
                  node.remove(j);
          }
        }
      }

    public static void emitRM(String op, int r, int d, int s, String msg) {
        System.out.println(emitLoc + ": " + op + " " + r + ", " + d + "(" + s + ") \t" + msg );
        emitLoc++;

        if (highEmitLoc < emitLoc)
            highEmitLoc = emitLoc;
    }

    public static void emitRO(String op, int r, int s, int t, String msg) {
        System.out.println(emitLoc + ": " + op + " " + r + ", " + s + ", " + t + " " + msg);
        emitLoc++;

        if (highEmitLoc < emitLoc)
            highEmitLoc = emitLoc;
    }

    public static void emitRM_Abs(String op, int r, int a, String msg) {
        System.out.println(emitLoc + ": " + op + " " + r + ", " + (a - (emitLoc + 1)) + "(" + pc + ") \t" + msg);
        emitLoc++;

        if (highEmitLoc < emitLoc)
            highEmitLoc = emitLoc;
    }

    public static void emitComment(String comment)
	{
		System.out.println("* " + comment);
    }
    
    public static int emitSkip(int distance)
	{
		int i = emitLoc;
		if (highEmitLoc < emitLoc)
			highEmitLoc = emitLoc;

		emitLoc += distance;
		return i;
    }
    
    public static void emitRestore()
	{
		emitLoc = highEmitLoc;
	}

	public static void emitBackup(int loc)
	{
		if (loc > highEmitLoc)
			emitComment("BUG in emitBackup");
		emitLoc = loc;
    }
    
}
    
