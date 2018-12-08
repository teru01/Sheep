package sheep.ast;

import java.util.*;

public class ConstExpr extends ASTList {
    public ConstExpr(List<ASTree> c) {
        super(c);
    }

    /**
     * const shep = 10 と言う文のshepを取得する
     */
    public ASTree getConstant() {
        return child(0);
    }

    @Override
    public String toString() {
        return "const " + getConstant();
    }
}
