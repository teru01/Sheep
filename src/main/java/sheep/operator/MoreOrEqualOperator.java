package sheep.operator;

import sheep.SheepException;
import sheep.Token;
import sheep.ast.ASTree;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.core.Environment;
public class MoreOrEqualOperator extends BinaryOperator {
    public MoreOrEqualOperator(Token t) {
        super(t);
    }

    @Override
    public Object calc(ASTree left, ASTree right, Environment env) {
        Object leftObj = ((ASTreeEx)left).eval(env);
        Object rightObj = ((ASTreeEx)right).eval(env);

        if(leftObj instanceof Integer && rightObj instanceof Integer) {
            return (Integer)leftObj >= (Integer)rightObj;
        } else if(leftObj instanceof String && rightObj instanceof String) {
            return !(((String)leftObj).compareTo((String)rightObj) == -1);
        }
        throw new SheepException("Unsupported operation", this);
    }
}
