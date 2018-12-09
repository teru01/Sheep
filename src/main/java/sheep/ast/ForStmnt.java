package sheep.ast;
import java.util.*;

import sheep.function.NestedEnv;
import sheep.core.Environment;

public class ForStmnt extends ASTList {
    public ForStmnt(List<ASTree> c) {
        super(c);
    }

    /**
     * for(var i=0; i<10; i=i+1)の括弧を取得
     */
    public ASTree iterExpr() {
        return child(0);
    }

    public ASTree iterBody() {
        return child(1);
    }

    public Environment makeEnv(Environment env) {
        return new NestedEnv(env);
    }

    @Override
    public String toString() {
        return "(for" + iterExpr() + iterBody() + ")";
    }

}
