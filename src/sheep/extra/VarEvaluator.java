package sheep.extra;
import java.util.List;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.SheepException;
import sheep.VarParser;
import sheep.ast.ASTree;
import sheep.ast.Name;
import sheep.ast.PrimaryExpr;
import sheep.ast.VarStmnt;
import sheep.chap6.BasicEvaluator.ASTreeEx;
import sheep.chap6.BasicEvaluator.BinaryEx;
import sheep.chap6.Environment;
import sheep.chap7.FuncEvaluator;
import sheep.chap7.FuncEvaluator.EnvEx;

@Require({FuncEvaluator.class, VarParser.class})
@Reviser
public class VarEvaluator {
    @Reviser
    public static class VarStmntEx extends VarStmnt {
        public VarStmntEx(List<ASTree> c) {
            super(c);
        }

        // public Object eval(Environment env) {
        //     return ((ASTreeEx)this).eval(env);
        // }
    }

    @Reviser
    public static class AssignExForBlockScope extends BinaryEx {
        public AssignExForBlockScope(List<ASTree> c) {
            super(c);
        }

        // @Override
        // protected Object computeAssign(Environment env, Object rvalue) {
        //     ASTree left = left();
        //     if(!(left instanceof PrimaryExpr)) {
        //         return super.computeAssign(env, rvalue);
        //     }
        //     PrimaryExpr p = (PrimaryExpr)left;
        //     if(!(p.hasVar())) {
        //         return super.computeAssign(env, rvalue);
        //     }
        //     if(left instanceof Name) {
        //         ((EnvEx)env).putNew(((Name) l).name(), rvalue);
        //         return rvalue;
        //     } else {
        //         throw new SheepException("bad assignment", this);
        //     }
        // }
    }
}
