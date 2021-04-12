import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


import absyn.*;

public class SemanticAnalyzer implements AbsynVisitor {

  HashMap<String, ArrayList<NodeType>> table;
  private static String tempParams = "";
  private static String currFunc = "";
  public static int x;
  ArrayList<String> temp = new ArrayList<String>();
  public static boolean SEMANTIC_ERROR = false;
  public SemanticAnalyzer() {

    table = new HashMap<String, ArrayList<NodeType>>();

  } 

  final static int SPACES = 4;

  private void indent( int level ) {
    for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
  }
  
  public void visit( ExpList expList, int level, boolean isAddr, int offset ) {
    while( expList != null ) {
      if (expList.head != null){
        expList.head.accept( this, level, false, offset);
        expList = expList.tail;
      }  
    } 
  }

  public void visit( IntExp exp, int level, boolean isAddr, int offset ) {

  }

  @Override
  public void visit(NilExp exp, int level, boolean isAddr, int offset) {

    
  }

  @Override
  public void visit(NameTy exp, int level, boolean isAddr, int offset) {
  
  }

  @Override
  public void visit(CompoundExp exp, int level, boolean isAddr, int offset) {
     VarDecList dec = exp.decs;
     while( dec != null ) {
      if(dec.head != null) {
        dec.head.accept( this, level, false, offset );
      }
        dec = dec.tail;
     } 
     ExpList ex = exp.exps;
     while( ex != null ) {
        if(ex.head != null) {
          ex.head.accept( this, level, false, offset );
        }
        ex = ex.tail;
     } 
  }

  @Override
  public void visit(VarDecList exp, int level, boolean isAddr, int offset) {
      while( exp != null ) {
        if (exp.head != null) {
          exp.head.accept( this, level, false, offset );
          exp = exp.tail;
        }
      } 
  }


  public void visit( AssignExp exp, int level, boolean isAddr, int offset ) {
    exp.lhs.accept( this, level, false, offset );
    exp.rhs.accept( this, level, false, offset );
    String var = "";
    if (exp.rhs instanceof CallExp) {
      var = ((CallExp) exp.rhs).func;
      if (!isInt(var)){
        SEMANTIC_ERROR = true;
        System.err.printf("line %d: error: illegal use of function call must be int.\n", exp.rhs.pos+1);
      }

    }

  }

  public void visit( OpExp exp, int level, boolean isAddr, int offset ) {
    exp.left.accept( this, level, false, offset);
    exp.right.accept( this, level, false, offset );
    String var = "";
    if (exp.left instanceof CallExp) {
      var = ((CallExp) exp.left).func;
      if (!isInt(var)) {
        SEMANTIC_ERROR = true;
        System.err.printf("line %d: error: illegal use of function call must be int.\n", exp.left.pos+1);
      }
    } else if (exp.right instanceof CallExp) {
      var = ((CallExp) exp.right).func;
      if (!isInt(var)){
        SEMANTIC_ERROR = true;
        System.err.printf("line %d: error: illegal use of function call must be int.\n", exp.right.pos+1);
      }
    }
  }

  public void visit( VarExp exp, int level, boolean isAddr, int offset ) {
    exp.variable.accept(this, level, false, offset);

  }

  
  @Override
  public void visit(SimpleVar exp, int level, boolean isAddr, int offset) {
    
    if (findSymbol(exp.name)) {
      if (!isInt(exp.name)) {
        SEMANTIC_ERROR = true;
        System.err.printf("line %d: error: invalid data type. Variable must be int -> %s\n", exp.pos+1, exp.name);
      }
    } else {
      SEMANTIC_ERROR = true;
      System.err.printf("line %d: error: cannot find symbol %s\n", exp.pos+1, exp.name);
    }

  }

  
  @Override
  public void visit(ReturnExp exp, int level, boolean isAddr, int offset) {
   
    if (exp.exp instanceof NilExp) {
      if (isInt(currFunc)) {
        SEMANTIC_ERROR = true;
        System.err.printf("line %d: error: incompatible types: missing return value\n", exp.pos+1);
      }
    } else {
      exp.exp.accept(this, level, false, offset);
      if (!isInt(currFunc)) {
        if (!(exp.exp instanceof NilExp)) {
          SEMANTIC_ERROR = true;
          System.err.printf("line %d: error: incompatible types: unexpected return value\n", exp.pos+1);
        }
      }
    }
  }

  @Override
  public void visit(IndexVar exp, int level, boolean isAddr, int offset) {
    exp.index.accept(this, level, false, offset);
    String var = "";
    if (exp.index instanceof CallExp) var = ((CallExp) exp.index).func;
    else if (((VarExp) exp.index).variable instanceof SimpleVar) var = ((SimpleVar) ((VarExp) exp.index).variable).name;
    else if (((VarExp) exp.index).variable instanceof IndexVar) var = ((IndexVar) ((VarExp) exp.index).variable).name;
    if (findSymbol(var)){
      if (!isInt(var)) {
        SEMANTIC_ERROR = true;
        System.err.printf("line %d: error: invalid data type. index must be int\n", exp.pos+1);
      }  
    } else {
      SEMANTIC_ERROR = true;
      System.err.printf("line %d: error: cannot find symbol %s\n", exp.pos+1, exp.name);
    }
  }

  @Override
  public void visit(CallExp exp, int level, boolean isAddr, int offset) {
    ExpList ex = exp.args;
    while( ex != null ) {
      ex.head.accept( this, level, false, offset );
      ex = ex.tail;
    } 

    if (!findSymbol(exp.func)) {
      SEMANTIC_ERROR = true;
      System.err.printf("line %d: error: function %s is undeclared\n", exp.pos+1, exp.func); 
    } 
    
  }

  @Override
  public void visit(WhileExp exp, int level, boolean isAddr, int offset) {
    level++;
    indent(level);
    System.out.println("Entering a new while block: ");
    exp.test.accept( this, level, false, offset);
    exp.body.accept( this, level, false, offset ); 

    printMap(level);
    delete(level);
    indent(level);
    System.out.println("leaving while block: ");
    String var = "";
    if (exp.test instanceof CallExp) {
      var = ((CallExp) exp.test).func;
      if (!isInt(var)) {
        SEMANTIC_ERROR = true;
        System.err.printf("line %d: error: Test expression %s is not an integer.\n", exp.pos+1, var);
      }
    }
  }
  
  public void visit( IfExp exp, int level, boolean isAddr, int offset ) {
    level++;
    indent( level );
    System.out.println("Entering a new if block: ");
    exp.test.accept( this, level, false, offset );
    exp.thenpart.accept( this, level, false, offset );
    String var = "";
    printMap(level);
    delete(level);
    indent(level);
    System.out.println("Leaving the if block:  ");
    
    if (exp.elsepart != null ) {
      indent(level);
      System.out.println("Entering a new else block: ");
      exp.elsepart.accept( this, level, false, offset );
      printMap(level);
      delete(level);
      indent(level);
      System.out.println("Leaving the else block: ");
    }

    if ((exp.test instanceof VarExp) || (exp.test instanceof CallExp)) {
      if (exp.test instanceof CallExp) var = ((CallExp) exp.test).func;
      else if (((VarExp) exp.test).variable instanceof SimpleVar) var = ((SimpleVar) ((VarExp) exp.test).variable).name;
      else if (((VarExp) exp.test).variable instanceof IndexVar) var = ((IndexVar) ((VarExp) exp.test).variable).name;
          if (!isInt(var)){
            SEMANTIC_ERROR = true;
            System.err.printf("line %d: error: Test variable must be int.\n", exp.pos+1);
          }
    }
       
  }
  
  @Override
  public void visit(FunctionDec exp, int level, boolean isAddr, int offset) {
    NodeType entry = new NodeType(exp.func, exp.result, level, "");
    if (!varExistsInCurrentScope(exp.func, level)){
      level++;
      indent(level);
      System.out.println("Entering the scope for function: "+ exp.func);
      currFunc = exp.func;
      VarDecList ex = exp.params;
      tempParams = "";
      while( ex != null ) {
        ex.head.accept( this, level, false, offset );
        ex = ex.tail;
      }
      entry.params = tempParams;
     
      if (table.get(exp.func) == null) {
        table.put(exp.func, new ArrayList<NodeType>());
      }
      
      table.get(exp.func).add(0, entry);
      exp.body.accept(this, level, false, offset);
      printMap(level);
      delete(level);
      indent(level);
      System.out.println("Leaving the scope for function: " + exp.func);
      removeFuncVars(table.entrySet().iterator());
      for (int i = 0; i < temp.size(); i++) 
        table.put(temp.get(i), new ArrayList<NodeType>());
        
      currFunc = "";
    } else {
      SEMANTIC_ERROR = true;
      System.err.printf("line %d: error: function %s is already defined within scope.\n", exp.pos+1, exp.func);
    }
  }

  @Override
  public void visit(SimpleDec exp, int level, boolean isAddr, int offset) {

    boolean err;
    err = insert(exp.name, level, exp.typ);
    tempParams += exp.typ.typ + " ";
    if (err == false) {
      SEMANTIC_ERROR = true;
      System.err.printf("line %d: error: variable %s is already defined within scope.\n", exp.pos+1, exp.name);
    }

  }

  @Override
  public void visit(ArrayDec exp, int level, boolean isAddr, int offset) {
    String name = "";
    name = exp.name + "[";
    if (exp.size != null)
        name = name + exp.size.value + "";
    name = name + "]";

    if (level == 0) {
      temp.add(exp.name);
    }

    NodeType entry = new NodeType(exp.name, exp.typ, level, "");
    if (table.get(exp.name) == null) {
      table.put(name, new ArrayList<NodeType>());
      table.put(exp.name, new ArrayList<NodeType>());
      
    } 
    if (!varExistsInCurrentScope(exp.name, level)){
      table.get(name).add(entry);
    } else {
      SEMANTIC_ERROR = true;
      System.err.printf("line %d: error: variable %s is already defined within scope.\n", exp.pos+1, exp.name);
    }
   
    tempParams += exp.typ.typ + " ";

  }

  @Override
  public void visit(DecList exp, int level, boolean isAddr, int offset) { 

    NameTy typeInput = new NameTy(exp.pos, 0);
    NameTy typeOutput = new NameTy(exp.pos, 1);
    NodeType input = new NodeType("input", typeInput, level, "1");
    NodeType output = new NodeType("output", typeOutput, level, "0");
    if (table.get("input") == null) {
      table.put("input", new ArrayList<NodeType>());
    }
    if (table.get("output") == null) {
      table.put("output", new ArrayList<NodeType>());
    }
    table.get("input").add(0, input);
    table.get("output").add(0, output);
    System.out.println("Entering global scope: ");
     while( exp != null ) {
      exp.head.accept( this, level, false , offset);
      exp = exp.tail;
    }
    printMap(level);
    delete(level);
    System.out.println("Leaving global scope: ");
  }

  public void printMap(int level) {
   // System.out.println(table);
    for (Entry<String, ArrayList<NodeType>> ee : table.entrySet()) {
      for (NodeType node : ee.getValue()) {
        if (node.level == level) {
          level++;
          indent(level);
          level--;
          System.out.print(ee.getKey() + ": ");
          if (!node.params.isEmpty()) { 
            String[] tokens = node.params.split(" ");
            System.out.print("( ");
            for (String s : tokens) {
              if (s.equals("0"))
                  System.out.print("int ");
              else if (s.equals("1"))
                  System.out.print("void ");
            }
            System.out.print(") -> ");
          } 
            if (node.level == level) {
              if(node.type.typ == 1) {
                System.out.println("void");
              } else if (node.type.typ == 0) {
                System.out.println("int");
              }
            }  
        }
      } 
    }
  }

  public boolean insert(String name, int level, NameTy type){  
    NodeType entry = new NodeType(name, type, level, ""); 
      if (table.get(name) == null) {
        table.put(name, new ArrayList<NodeType>());
      } 
      if (!varExistsInCurrentScope(name, level)){
        table.get(name).add(entry);
        return true;
      } 
      return false;  
  }

  public boolean varExistsInCurrentScope(String name, int level){
    NodeType node = new NodeType(name, level);
		for (Entry<String, ArrayList<NodeType>> ee : table.entrySet()) {
      ArrayList<NodeType> values = ee.getValue();
      for (NodeType obj : values) {
        if (obj.equals(node)) {
             return true;   
        }
      }
    }
    return false;
  }

  public boolean findSymbol(String name) { 
    if (table.containsKey(name)) {
      return true;
    }
    return false;
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

  public boolean isInt(String name){
    for (Entry<String, ArrayList<NodeType>> ee : table.entrySet()) {
      String key = ee.getKey();
      ArrayList<NodeType> values = ee.getValue();
      for (NodeType obj : values) {
        if (obj.name.equals(name)) {
          if(obj.type.typ == 0) {
            return true; 
          } 
        }
      }
    }
    return false;
  }
}



