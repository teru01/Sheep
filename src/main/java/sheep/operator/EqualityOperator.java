package sheep.operator;

import sheep.Token;
import sheep.ast.ASTree;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.core.Environment;
public class EqualityOperator extends BinaryOperator {
    public EqualityOperator(Token t) {
        super(t);
    }

    @Override
    public Object calc(ASTree left, ASTree right, Environment env) {
        Object leftObj = ((ASTreeEx)left).eval(env);
        Object rightObj = ((ASTreeEx)right).eval(env);
        if(leftObj == null) {
            return rightObj == null;
        } else {
            return leftObj.equals(rightObj);
        }
    }
}
