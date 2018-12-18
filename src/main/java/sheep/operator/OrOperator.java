package sheep.operator;

import sheep.util.SheepUtil;

import sheep.Token;
import sheep.ast.ASTree;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.core.Environment;
public class OrOperator extends BinaryOperator {
    public OrOperator(Token t) {
        super(t);
    }

    @Override
    public Object calc(ASTree left, ASTree right, Environment env) {
        Object leftObj = ((ASTreeEx)left).eval(env);
        if(SheepUtil.isTrue(leftObj)) {
            return true;
        }
        Object rightObj = ((ASTreeEx)right).eval(env);
        if(SheepUtil.isTrue(rightObj)) {
            return true;
        } else {
            return false;
        }
    }
}
