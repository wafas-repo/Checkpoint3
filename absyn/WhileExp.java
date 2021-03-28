package absyn;

public class WhileExp extends Exp {

    public Exp test;
    public Exp body;

    public WhileExp(int pos, Exp test, Exp body) {

        this.pos = pos;
        this.test = test;
        this.body = body;

    }

    @Override
    public void accept(AbsynVisitor visitor, int level) {
        visitor.visit( this, level );

    }
    
}
