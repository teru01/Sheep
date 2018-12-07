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
        return child(0);
    }

    /**
     * for条件式
     */
    public ASTree conditionExpr() {
        return child(1);
    }

    /**
     * for更新式
     */
    public ASTree updateExpr() {
        return child(2);
    }

    @Override
    public String toString() {
        return "(" + initExpr() + "," + conditionExpr() + "," + updateExpr() + ")";
    }
}
