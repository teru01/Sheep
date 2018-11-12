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

    public void putNew(String name, Object value) {
        this.values.put(name, value);
    }

    @Override
    public void put(String name, Object value) {
        Environment e = this.where(name);
        if(e == null) {
            e = this;
        }
        ((EnvEx)e).putNew(name, value);
    }

    /**
     * 渡された名前がどの環境にあるかを再帰的に検索してそれを返す。
     * @param name
     * @return
     */
    public Environment where(String name) {
        if(this.values.get(name) != null) {
            return this;
        } else if(this.outer == null) {
            return null;
        } else {
            return ((EnvEx)this.outer).where(name);
        }
    }

}
