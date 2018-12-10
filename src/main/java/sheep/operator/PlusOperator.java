package sheep.operator;

import sheep.Token;
import sheep.ast.ASTree;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.core.Environment;
public class PlusOperator extends Operator {
    public PlusOperator(Token t) {
        super(t);
    }
    public Object calc(ASTree left, ASTree right, Environment env) {
        Object leftObj = ((ASTreeEx)left).eval(env);
        Object rightObj = ((ASTreeEx)right).eval(env);

        if(leftObj instanceof Integer && rightObj instanceof Integer) {
            return (Integer)leftObj + (Integer)rightObj;
        }
        return String.valueOf(leftObj) + String.valueOf(rightObj);
    }
}
