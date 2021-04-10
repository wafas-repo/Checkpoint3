package absyn;

public class FunctionDec extends Dec {

    public NameTy result;
    public String func;
    public VarDecList params;
    public CompoundExp body;
    public int funcAddr;

    public FunctionDec(int pos, NameTy result, String func, VarDecList params, CompoundExp body) {
        this.pos = pos;
        this.result = result;
        this.func = func;
        this.params = params;
        this.body = body;
    }


    @Override
    public void accept(AbsynVisitor visitor, int level, boolean isAddr) {
        visitor.visit( this, level, false );

    }
    
}
