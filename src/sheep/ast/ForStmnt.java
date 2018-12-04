package sheep.ast;
import java.util.*;

public class ForStmnt extends ASTList {
    public ForStmnt(List<ASTree> c) {
        super(c);
    }

    /**
     * for(var i=0; i<10; i=i+1)の括弧を取得
     */
    public ASTree IterExpr() {
        return this.child(0);
    }

    public ASTree IterBody() {
        return this.child(1);
    }

}
