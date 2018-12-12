package sheep.operator;

import sheep.SheepException;
import sheep.Token;
import sheep.ast.ASTree;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.core.Environment;
public class MinusOperator extends BinaryOperator {
    public MinusOperator(Token t) {
        super(t);
    }

    @Override
    public Object calc(ASTree left, ASTree right, Environment env) {
        Object leftObj = ((ASTreeEx)left).eval(env);
        Object rightObj = ((ASTreeEx)right).eval(env);

        if(leftObj instanceof Integer && rightObj instanceof Integer) {
            return (Integer)leftObj - (Integer)rightObj;
        }
        throw new SheepException("Unsupported operation", this);
    }
}
