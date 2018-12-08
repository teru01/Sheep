package sheep.ast;
import java.util.List;

import sheep.SheepException;

public class IfStmnt extends ASTList {
    public IfStmnt(List<ASTree> c) {
        super(c);
    }

    public ASTree condition() { return child(0); }

    public ASTree thenBlock() { return child(1); }

    public int getElseIfNum() {
        if(!(child(2) instanceof ElifStmnt)) {
            throw new SheepException("fatal error", this);
        }
        return ((ElifStmnt)child(2)).numChildren();
    }

    public ElifStmnt elifStmnt() { return (ElifStmnt)child(2); }

    public ASTree elseBlock() {
        return numChildren() > 3 ? child(3) : null;
    }

    public String toString() {
        return "(if " + condition() + " " + thenBlock() + " " + elifStmnt() + " else " + elseBlock() + ")";
    }
}
