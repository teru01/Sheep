package sheep.ast;
import java.util.*;

public class VarExpr extends ASTList {
    public VarExpr(List<ASTree> c){ super(c); }

    /**
     * var shep = 10　と言う文のshepを取得する
     */
    public ASTree getAssignExpr() {
        return child(0);
    }

    @Override
    public String toString() {
        return "var " + getAssignExpr();
    }
}
