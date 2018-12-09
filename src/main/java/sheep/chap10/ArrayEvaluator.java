package sheep.chap10;
import java.util.*;
import javassist.gluonj.*;
import sheep.*;
import sheep.ast.*;
import sheep.chap6.*;
import sheep.chap6.BasicEvaluator.ASTreeEx;
import sheep.chap7.*;
import sheep.chap7.FuncEvaluator.PrimaryEx;

@Require(FuncEvaluator.class)
@Reviser
public class ArrayEvaluator {
    @Reviser
    public static class ArrayLitEx extends ArrayLiteral {
        public ArrayLitEx(List<ASTree> list) {
            super(list);
        }

        public Object eval(Environment env) {
            int s = numChildren();
            Object[] res = new Object[s];
            int i = 0;
            for(ASTree t: this) {
                res[i++] = ((ASTreeEx)t).eval(env);
            }
            return res;
        }
    }

    @Reviser
    public static class ArrayRefEx extends ArrayRef {
        public ArrayRefEx(List<ASTree> c) {
            super(c);
        }
        public Object eval(Environment env, Object value) {
            if(value instanceof Object[]) {
                Object index = ((ASTreeEx)index()).eval(env);
                if(index instanceof Integer) {
                    return ((Object[]) value)[(Integer) index];
                }
            }
            throw new SheepException("bad array access", this);
        }
    }
    @Reviser
    public static class AssignEx extends BasicEvaluator.BinaryEx {
        public AssignEx(List<ASTree> c) {
            super(c);
        }

        @Override
        protected Object computeAssign(Environment env, Object rvalue) {
            ASTree le = left();
            if(!(le instanceof PrimaryExpr)) {
                return super.computeAssign(env, rvalue);
            }
            PrimaryEx p = (PrimaryEx)le;
            if(!(p.hasPostfix(0) && p.postfix(0) instanceof ArrayRef)) {
                return super.computeAssign(env, rvalue);
            }
            Object ary = ((PrimaryEx)le).evalSubExpr(env, 1);
            if(!(ary instanceof Object[])) {
                throw new SheepException("bad array access", this);
            }
            ArrayRef aref = (ArrayRef)p.postfix(0);
            Object index = ((ASTreeEx)aref.index()).eval(env);
            if(!(index instanceof Integer)) {
                throw new SheepException("bad array access", this);
            }
            ((Object[])ary)[(Integer)index] = rvalue;
            return rvalue;
        }
    }
}
