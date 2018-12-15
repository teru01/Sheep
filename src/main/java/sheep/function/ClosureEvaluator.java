package sheep.function;
import java.util.List;
import javassist.gluonj.*;
import sheep.ast.*;
import sheep.core.Environment;

@Require(FuncEvaluator.class)
@Reviser
public class ClosureEvaluator {
    @Reviser
    public static class FunEx extends Fun {
        public FunEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            // このクロージャが定義されている環境を渡す
            // BlockStmnt b = body();
            return new Function(parameters(), body(), env);
        }
    }
}

