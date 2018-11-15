package sheep.chap8;
import java.util.*;
import sheep.*;
import sheep.ast.*;
import javassist.gluonj.*;
import sheep.chap6.*;
import sheep.chap6.BasicEvaluator.ASTreeEx;
import sheep.chap7.*;

@Require(FuncEvaluator.class)
@Reviser
public class NativeEvaluator {
    @Reviser
    public static class NativeArgEx extends FuncEvaluator.ArgumentsEx {
        public NativeArgEx(List<ASTree> c) {
            super(c);
        }

        @Override
        public Object eval(Environment callerEnv, Object value) {
            if (!(value instanceof NativeFunction)){
                return super.eval(callerEnv, value);
            }
            NativeFunction func = (NativeFunction)value;
            int nparams = func.numOfParameters();
            if(size() != nparams) {
                throw new SheepException("bad number of arguments", this);
            }
            Object[] args = new Object[nparams];
            int num = 0;
            for(ASTree a: this) {
                ASTreeEx ae = (ASTreeEx)a;
                args[num++] = ae.eval(callerEnv);
            }
            return func.invoke(args, this);
        }
    }
}
