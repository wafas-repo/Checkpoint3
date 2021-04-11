package absyn;

public class NilExp extends Exp {

    public NilExp(int pos) {
        this.pos = pos;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddr, int scope) {
        visitor.visit( this, level, false, scope );

    }
    
}
