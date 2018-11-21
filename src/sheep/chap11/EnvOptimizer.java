package sheep.chap11;
import static javassist.gluonj.GluonJ.revise;

import javassist.compiler.ast.Symbol;
import javassist.gluonj.*;
import java.util.List;
import sheep.*;
import sheep.ast.*;
import sheep.chap11.Symbols.Location;
import sheep.chap6.Environment;
import sheep.chap6.BasicEvaluator;
import sheep.chap7.ClosureEvaluator;

/**
 * 変数の値を配列で管理するためのクラス。ここにはハッシュテーブルは出てこない
 */
@Require(ClosureEvaluator.class)
@Reviser
public class EnvOptimizer {
    // interfaceに対してextendsして宣言をする。実装はArrayEnvで行われている。
    @Reviser
    public static interface EnvEx2 extends Environment {
        Symbols symbols();

        void put(int nest, int index, Object value);

        Object get(int nest, int index);

        void putNew(String name, Object value);

        Environment where(String name);
    }

    @Reviser
    public static abstract class ASTreeOptEx extends ASTree {
        public void lookup(Symbols syms) {}
    }

    @Reviser
    public static class ASTListEx extends ASTList {
        public ASTListEx(List<ASTree> c) {
            super(c);
        }

        public void lookup(Symbols syms) {
            for (ASTree t : this)
                ((ASTreeOptEx) t).lookup(syms);
        }
    }

    @Reviser
    public static class DefStmntEx extends DefStmnt {
        protected int index, size;

        public DefStmntEx(List<ASTree> c) {
            super(c);
        }

        public void lookup(Symbols syms) {
            this.index = syms.putNew(name());
            this.size = FunEx.lookup(syms, parameters(), body());
        }

        public Object eval(Environment env) {
            ((EnvEx2) env).put(0, this.index, new OptFunction(parameters(), body(), env, this.size));
            return name();
        }
    }

    @Reviser
    public static class FunEx extends Fun {
        protected int size = -1;

        public FunEx(List<ASTree> c) {
            super(c);
        }

        public void lookup(Symbols syms) {
            this.size = this.lookup(syms, parameters(), body());
        }

        public Object eval(Environment env) {
            return new OptFunction(parameters(), body(), env, size);
        }

        public static int lookup(Symbols syms, ParameterList params, BlockStmnt body) {
            Symbols newSyms = new Symbols(syms);
            ((ParamsEx)params).lookup(newSyms);
            ((ASTreeOptEx)revise(body)).lookup(newSyms);
            return newSyms.size();
        }
    }

    @Reviser
    public static class ParamsEx extends ParameterList {
        protected int[] offsets = null;

        public ParamsEx(List<ASTree> c) {
            super(c);
        }

        public void lookup(Symbols syms) {
            int s = size();
            this.offsets = new int[s];
            for(int i=0; i<s; i++) {
                this.offsets[i] = syms.putNew(name(i));
            }
        }

        public void eval(Environment env, int index, Object value) {
            ((EnvEx2)env).put(0, this.offsets[index], value);
        }
    }

    /**
     * シンボルに値を追加したり取り出したりしている
     */
    @Reviser
    public static class NameEx extends Name {
        protected static final int UNKNOWN = -1;
        protected int nest, index;

        public NameEx(Token t) {
            super(t);
            this.index = UNKNOWN;
        }

        public void lookup(Symbols syms) {
            Location loc = syms.get(name());
            if(loc == null) {
                throw new SheepException("undefined name: " + name(), this);
            } else {
                this.nest = loc.nest;
                this.index = loc.index;
            }
        }

        public void lookupForAssign(Symbols syms) {
            Location loc = syms.put(name());
            this.nest = loc.nest;
            this.index = loc.index;
        }

        public Object eval(Environment env) {
            if (this.index == UNKNOWN){
                return env.get(name());
            } else {
                return ((EnvEx2) env).get(nest, index);
            }
        }

        public void evalForAssign(Environment env, Object value) {
            if(index == UNKNOWN) {
                env.put(name(), value);
            } else {
                ((EnvEx2)env).put(nest, index, value);
            }
        }
    }

    @Reviser
    public static class BinaryEx2 extends BasicEvaluator.BinaryEx {
        public BinaryEx2(List<ASTree> c) {
            super(c);
        }
        public void lookup(Symbols syms) {
            ASTree left = left();
            if("=".equals(operator()) && left instanceof Name) {
                ((NameEx)left).lookupForAssign(syms);
                ((ASTreeOptEx)right()).lookup(syms);
                return;
            }
            ((ASTreeOptEx)left).lookup(syms);
            ((ASTreeOptEx)right()).lookup(syms);
        }

        @Override
        protected Object computeAssign(Environment env, Object rvalue) {
            ASTree l = left();
            if (l instanceof Name) {
                ((NameEx) l).evalForAssign(env, rvalue);
                return rvalue;
            } else{
                return super.computeAssign(env, rvalue);
            }
        }
    }
}

