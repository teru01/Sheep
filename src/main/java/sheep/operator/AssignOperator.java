package sheep.operator;

import sheep.*;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.core.BasicEvaluator.BinaryEx;
import sheep.core.*;
import sheep.ast.*;

public class AssignOperator extends ASTLeaf {
    public AssignOperator(Token t) {
        super(t);
    }
    public Object calc(ASTree left, ASTree right, Environment env) {
        return left.computeAssign(((ASTreeEx)right).eval(env), env);
    }
}
