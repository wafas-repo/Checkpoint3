package absyn;

public class ReturnExp extends Exp {

    public Exp exp;

    public ReturnExp(int pos, Exp exp) {
        this.pos = pos;
        this.exp = exp;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddr, int scope) {
        visitor.visit( this, level, false, scope );

    }
    
}
