package sheep.operator;

import sheep.SheepException;
import sheep.Token;
import sheep.ast.ASTree;
import static sheep.core.BasicEvaluator.*;
import static sheep.util.SheepUtil.*;
import sheep.core.Environment;
public class AndOperator extends BinaryOperator {
    public AndOperator(Token t) {
        super(t);
    }

    @Override
    public Object calc(ASTree left, ASTree right, Environment env) {
        Object leftObj = ((ASTreeEx)left).eval(env);
        if(!isTrue(leftObj)) {
            return FALSE;
        }
        Object rightObj = ((ASTreeEx)right).eval(env);
        if(isTrue(rightObj)) {
            return TRUE;
        } else {
            return FALSE;
        }
    }
}
