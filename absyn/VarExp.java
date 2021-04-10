package absyn;

public class VarExp extends Exp {

  public Var variable;

  public VarExp(int pos, Var variable) {
    this.pos = pos;
    this.variable = variable;
  }

  public void accept( AbsynVisitor visitor, int level, boolean isAddr ) {
    visitor.visit( this, level, false );
  }
} 