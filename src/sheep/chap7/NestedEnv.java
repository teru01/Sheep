package sheep.chap7;
import java.util.HashMap;
import sheep.chap6.Environment;
import sheep.chap7.FuncEvaluator.EnvEx;

public class NestedEnv implements Environment {
    protected HashMap<String, Object> values;
    protected Environment outer;

    public NestedEnv() {
        this(null);
    }

    public NestedEnv(Environment e) {
        this.values = new HashMap<>();
        this.outer = e;
    }

    public void setOuter(Environment e) {
        this.outer = e;
    }

    @Override
    public Object get(String name) {
        Object v = this.values.get(name);
        // グローバル変数にアクセスしたとき。
        // value(local変数)はなくてグローバルに存在
        if(v == null && this.outer != null) {
            return this.outer.get(name);
        } else {
            return v;
        }
    }

    /**
     * 現在の環境に新規追加/上書きを行う。
     */
    public void putNew(String name, Object value) {
        this.values.put(name, value);
    }

    /**
     * 現在の外側の環境まで探索して上書きを行う。見つからなければグローバルスコープに書き込む。
     */
    @Override
    public void put(String name, Object value) {
        Environment e = this.where(name);
        if(e == null) {
            e = getOutermostEnv();
        }
        ((EnvEx)e).putNew(name, value);
    }

    /**
     * 渡された名前がどの環境にあるかを外側に再帰的に検索してそれを返す。
     * @param name
     * @return Environment
     */
    public Environment where(String name) {
        if(this.values.get(name) != null) {
            return this;
        } else if(this.outer != null) {
            return ((EnvEx)this.outer).where(name);
        }
        return null;
    }

    public Environment getOutermostEnv() {
        if(this.outer == null) {
            return this;
        }
        return ((EnvEx)this.outer).getOutermostEnv();
    }
}
