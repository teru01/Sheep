package sheep.ast;
import java.util.*;

public class ForIterExpr extends ASTList{
    public ForIterExpr(List<ASTree> c) {
        super(c);
    }

    /**
     * forの初期化式
     */
    public ASTree initExpr() {
        return this.child(0);
    }

    /**
     * for条件式
     * @return
     */
    public ASTree conditionExpr() {
        return this.child(1);
    }

    /**
     * for更新式
     * @return
     */
    public ASTree updateExpr() {
        return this.child(2);
    }

    @Override
    public String toString() {
        return "(" + initExpr() + "," + conditionExpr() + "," + updateExpr() + ")";
    }
}
