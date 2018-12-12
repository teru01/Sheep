package sheep.operator;

import sheep.Token;
import sheep.ast.*;
import sheep.core.Environment;
import sheep.core.BasicEvaluator.*;

public class AssignOperator extends ASTLeaf {
    public AssignOperator(Token t) {
        super(t);
    }

    public Object assignObject(ASTree left, Object rightObj, Environment env) {
        return left.assign(rightObj, env);
    }

    public Object assign(ASTree left, ASTree right, Environment env) {
        return assignObject(left, ((ASTreeEx)right).eval(env), env);
    }
}
