package absyn;

public interface AbsynVisitor {

  public void visit( ExpList exp, int level, boolean isAddr, int scope);

  public void visit( AssignExp exp, int level, boolean isAddr, int scope );

  public void visit( IfExp exp, int level, boolean isAddr, int scope );

  public void visit( IntExp exp, int level, boolean isAddr, int scope );

  public void visit( OpExp exp, int level, boolean isAddr, int scope );

  public void visit( VarExp exp, int level, boolean isAddr, int scope );

  public void visit( NilExp exp, int level, boolean isAddr, int scope );

  public void visit( CallExp exp, int level,boolean isAddr, int scope);

  public void visit( WhileExp exp, int level,boolean isAddr, int scope);

  public void visit( ReturnExp exp, int level, boolean isAddr, int scope);

  public void visit( CompoundExp exp, int level, boolean isAddr, int scope);

  public void visit( NameTy exp, int level, boolean isAddr, int scope);

  public void visit( SimpleVar exp, int level, boolean isAddr, int scope);

  public void visit( IndexVar exp, int level, boolean isAddr, int scope);

  public void visit( FunctionDec exp, int level, boolean isAddr, int scope);

  public void visit( SimpleDec exp, int level, boolean isAddr, int scope);

  public void visit( ArrayDec exp, int level, boolean isAddr, int scope);

  public void visit( DecList exp, int level, boolean isAddr, int scope);

  public void visit( VarDecList exp, int level,boolean isAddr, int scope);
}
