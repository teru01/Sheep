package sheep.optimizer;
import javassist.compiler.ast.Symbol;
import sheep.SheepException;
import sheep.optimizer.EnvOptimizer.EnvEx2;
import sheep.core.Environment;

/**
 * 配列で変数を管理するための環境。外側のスコープに対する参照を持つ(Envと同じ)
 */
public class ArrayEnv implements Environment {
    protected Object[] values;
    protected Environment outer;

    public ArrayEnv(int size, Environment out) {
        this.values = new Object[size];
        this.outer = out;
    }

    public Symbols symbols() {
        throw new SheepException("no symbols");
    }

    /**
     * 外側の環境がもつ配列のあるインデックスを返す
     */
    public Object get(int nest, int index) {
        if(nest == 0) {
            return values[index];
        } else if(this.outer == null) {
            return null;
        } else {
            return ((EnvEx2)this.outer).get(nest - 1, index);
        }
    }

    public void put(int nest, int index, Object value) {
        if(nest == 0) {
            this.values[index] = value;
        } else if(this.outer == null) {
            throw new SheepException("not outer environment");
        } else {
            ((EnvEx2)this.outer).put(nest - 1, index, value);
        }
    }

    public void setOuter(Environment e) {
        this.outer = e;
    }

    public Object get(String name) {
        error(name);
        return null;
    }

    public void put(String name, Object value) {
        error(name);
    }

    public void putNew(String name, Object value) {
        error(name);
    }

    public Environment where(String name) {
        error(name);
        return null;
    }

    private void error(String name) {
        throw new SheepException("cannot access by name: " + name);
    }
}
