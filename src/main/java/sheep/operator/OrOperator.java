package sheep.operator;

import sheep.SheepException;
import sheep.Token;
import sheep.ast.ASTree;
import static sheep.core.BasicEvaluator.*;
import static sheep.util.SheepUtil.*;
import sheep.core.Environment;
public class OrOperator extends BinaryOperator {
    public OrOperator(Token t) {
        super(t);
    }

    @Override
    public Object calc(ASTree left, ASTree right, Environment env) {
        Object leftObj = ((ASTreeEx)left).eval(env);
        if(isTrue(leftObj)) {
            return true;
        }
        Object rightObj = ((ASTreeEx)right).eval(env);
        if(isTrue(rightObj)) {
            return true;
        } else {
            return false;
        }
    }
}
