package sheep.array;
import java.util.*;
import javassist.gluonj.*;
import sheep.*;
import sheep.ast.*;
import sheep.core.*;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.function.*;
import sheep.function.FuncEvaluator.PrimaryEx;

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
}
