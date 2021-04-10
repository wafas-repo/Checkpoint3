package absyn;

public class ArrayDec extends VarDec {

    public NameTy typ;
    public String name;
    public IntExp size;
    public int offset;
    

    public ArrayDec(int pos, NameTy typ, String name, IntExp size, int offset) {
        this.pos = pos;
        this.typ = typ;
        this.name = name;
        this.size = size;
        this.offset = offset;
    }


    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
        visitor.visit( this, level, false );

    }
    
}
