package sheep.object;

import java.util.List;

import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.SheepException;
import sheep.ast.ASTree;
import sheep.ast.ClassBody;
import sheep.ast.ClassStmnt;
import sheep.ast.Dot;
import sheep.core.BasicEvaluator.ASTreeEx;
import sheep.core.Environment;
import sheep.core.ReturnObject;
import sheep.function.FuncEvaluator;
import sheep.function.FuncEvaluator.EnvEx;
import sheep.function.NestedEnv;
import sheep.object.SheepObject.AccessException;
import sheep.util.Statements;

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

    @Reviser
    public static class ClassBodyEx extends ClassBody {
        public ClassBodyEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env) {
            for (ASTree t : this)
                ((ASTreeEx)t).eval(env);
            return null;
        }
    }

    @Reviser
    public static class DotEx extends Dot {
        public DotEx(List<ASTree> c) { super(c); }
        public Object eval(Environment env, Object value) {
            String member = name();
            // MyClass.newとか
            if (value instanceof ClassInfo) {
                if ("new".equals(member)) {
                    ClassInfo ci = (ClassInfo) value;
                    // クラス定義された環境の中にクラスの環境を作成
                    NestedEnv e = new NestedEnv(ci.environment());
                    SheepObject so = new SheepObject(e);
                    e.putInCurrentEnv("this", so);
                    initObject(ci, e);
                    return so;
                }
            }
            // myInstance.hoge();など
            else if (value instanceof SheepObject) {
                try {
                    return ((SheepObject) value).read(member);
                } catch (AccessException e) {}
            }
            throw new SheepException("bad member access: " + member, this);
        }

        /**
         * ciの親クラスの本体を頂点からevalする.フィールド、メソッドの登録など。overrideも問題なくされる
         * TODO: 親クラスのフィールドに触れるよう、superの実装
         */
        protected void initObject(ClassInfo ci, Environment env) {
            if (ci.superClass() != null) {
                initObject(ci.superClass(), env);
            }
            ((ClassBodyEx) ci.body()).eval(env);
        }
    }
}
