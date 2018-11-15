package sheep.chap9;
import java.util.*;
import sheep.*;
import javassist.gluonj.*;
import sheep.ast.*;
import sheep.chap6.*;
import sheep.chap7.*;
import sheep.chap7.FuncEvaluator.EnvEx;
import sheep.chap9.*;

@Require(FuncEvaluator.class)
@Reviser
public class ClassEvaluator {
    @Reviser
    public static class ClassStmntEx extends ClassStmnt {
        public ClassStmntEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            ClassInfo ci = new ClassInfo(this, env);
            ((EnvEx)env).put(name(), ci);
            return name();
        }
    }
}
