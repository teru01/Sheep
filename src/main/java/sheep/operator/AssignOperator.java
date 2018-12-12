package sheep.operator;

import sheep.SheepException;
import sheep.Token;
import sheep.ast.ASTLeaf;
import sheep.ast.ASTree;
import sheep.ast.ArrayRef;
import sheep.ast.Name;
import sheep.ast.PrimaryExpr;
import sheep.ast.VarExpr;
import sheep.core.Environment;
import sheep.function.FuncEvaluator.PrimaryEx;
import sheep.core.BasicEvaluator.*;

public class AssignOperator extends ASTLeaf {
    public AssignOperator(Token t) {
        super(t);
    }

    public Object assignObject(ASTree left, Object rightObj, Environment env) {
        //leftがvar, const, hoge., hoge[2], hoge
        return left.assign(rightObj, env);
    }

    public Object assign(ASTree left, ASTree right, Environment env) {
        return assignObject(left, ((ASTreeEx)right).eval(env), env);
        // if(left instanceof PrimaryExpr) {
        //     return assignObject(((PrimaryEx)left), ((ASTreeEx)right).eval(env), env);
        // } else {
            
        // }
    }
}
