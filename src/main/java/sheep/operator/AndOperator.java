package sheep.operator;

import sheep.Token;
import sheep.ast.ASTree;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.core.Environment;
import sheep.util.SheepUtil;

public class AndOperator extends BinaryOperator {
    public AndOperator(Token t) {
        super(t);
    }

    @Override
    public Object calc(ASTree left, ASTree right, Environment env) {
        Object leftObj = ((ASTreeEx)left).eval(env);
        if(!SheepUtil.isTrue(leftObj)) {
            return false;
        }
        Object rightObj = ((ASTreeEx)right).eval(env);
        if(SheepUtil.isTrue(rightObj)) {
            return true;
        } else {
            return false;
        }
    }
}
