package absyn;

public class AssignExp extends Exp {
  public Var lhs;
  public Exp rhs;

  public AssignExp( int pos, Var lhs, Exp rhs ) {
    this.pos = pos;
    this.lhs = lhs;
    this.rhs = rhs;
  }
  
  public void accept( AbsynVisitor visitor, int level, boolean isAddr, int scope ) {
    visitor.visit( this, level, false , scope);
  }
}
