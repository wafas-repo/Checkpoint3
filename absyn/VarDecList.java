package absyn;

public class VarDecList extends Absyn {

    public VarDec head;
    public VarDecList tail;

    public VarDecList(VarDec head, VarDecList tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
        visitor.visit( this, level, false );

    }
    
}
