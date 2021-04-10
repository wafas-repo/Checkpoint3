package absyn;

public class SimpleDec extends VarDec {

    public NameTy typ;
    public String name;
    public int offset;


    public SimpleDec(int pos, NameTy typ, String name, int offset) {
        this.pos = pos;
        this.typ = typ;
        this.name = name;
        this.offset = offset;

    }

    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
        visitor.visit( this, level, false );

    }
    
}
