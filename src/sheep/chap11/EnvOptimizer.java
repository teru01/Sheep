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
 * lookupメソッドは構文木の根から葉に向かって降りていく。
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

    /**
     * 関数宣言のlookupはdefがある環境のシンボルにその関数を書き込んで、あとはFunに丸投げ
     */
    @Reviser
    public static class DefStmntEx extends DefStmnt {
        protected int index, size;

        public DefStmntEx(List<ASTree> c) {
            super(c);
        }

        // グローバルな関数の情報（関数名、evalで書き込まれる配列のインデックス）がシンボルテーブルに書き込まれる。
        public void lookup(Symbols syms) {
            this.index = syms.putNew(name());
            this.size = FunEx.lookup(syms, parameters(), body());
        }

        public Object eval(Environment env) {
            ((EnvEx2) env).put(0, this.index, new OptFunction(parameters(), body(), env, this.size));
            return name();
        }
    }

    /**
     * クロージャと関数。新たなSymbolをスコープとして作成
     */
    @Reviser
    public static class FunEx extends Fun {
        protected int size = -1;

        public FunEx(List<ASTree> c) {
            super(c);
        }

        public void lookup(Symbols syms) {
            this.size = lookup(syms, parameters(), body());
        }

        /**
         * 実行時には
         */
        public Object eval(Environment env) {
            return new OptFunction(parameters(), body(), env, size);
        }

        /**
         * 新たなSymbolを作成して関数内部のシンボル数を返す。
         */
        public static int lookup(Symbols syms, ParameterList params, NonScopedBlock body) {
            Symbols newSyms = new Symbols(syms);
            ((ParamsEx)params).lookup(newSyms);
            ((ASTreeOptEx)revise(body)).lookup(newSyms);
            return newSyms.size();
        }
    }

    @Reviser
    public static class ParamsEx extends ParameterList {
        // 環境中の変数配列のインデックスを格納する。
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

        /**
         * 仮引数が環境に書き込まれる。環境における位置などはすでにlookup()でSymbolにより決定されている。
         * @param env
         * @param index
         * @param value
         */
        public void eval(Environment env, int index, Object value) {
            ((EnvEx2)env).put(0, this.offsets[index], value);
        }
    }

    /**
     * 変数が利用される部分。構文木の末端に当たる。シンボルに値を追加したり取り出したりしている
     */
    @Reviser
    public static class NameEx extends Name {
        protected static final int UNKNOWN = -1;
        protected int nest, index;

        public NameEx(Token t) {
            super(t);
            this.index = UNKNOWN;
        }

        /**
         *
         * 変数のNameオブジェクトにSymbolから取り出した位置情報を記録する。
         * @param syms
         */
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

    /**
     * 基本Nameよりも根寄りに位置している。
     */
    @Reviser
    public static class BinaryEx2 extends BasicEvaluator.BinaryEx {
        public BinaryEx2(List<ASTree> c) {
            super(c);
        }
        public void lookup(Symbols syms) {
            ASTree left = left();
            // 代入文の時は左辺の値をシンボルに追加する。
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

