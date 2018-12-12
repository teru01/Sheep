package sheep.operator;

import sheep.SheepException;
import sheep.Token;
import sheep.ast.ASTree;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.core.Environment;
public class ProductOperator extends BinaryOperator {
    public ProductOperator(Token t) {
        super(t);
    }

    @Override
    public Object calc(ASTree left, ASTree right, Environment env) {
        Object leftObj = ((ASTreeEx)left).eval(env);
        Object rightObj = ((ASTreeEx)right).eval(env);

        if(leftObj instanceof Integer && rightObj instanceof Integer) {
            return (Integer)leftObj * (Integer)rightObj;
        } else if(leftObj instanceof String && rightObj instanceof Integer) {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < (Integer)rightObj; i++) {
                sb.append((String)leftObj);
            }
            return sb.toString();
        }
        throw new SheepException("Unsupported operation", this);
    }
}
