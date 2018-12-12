package sheep.function;
import java.util.*;

import sheep.SheepException;
import sheep.core.Environment;
import sheep.function.FuncEvaluator.EnvEx;

public class NestedEnv implements Environment {
    protected HashMap<String, Object> values;
    protected ArrayList<String> constans;
    protected Environment outer;

    public NestedEnv() {
        this(null);
    }

    public NestedEnv(Environment e) {
        this.values = new HashMap<>();
        this.constans = new ArrayList<>();
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
    public void putInCurrentEnv(String name, Object value) throws SheepException{
        if(this.constans.contains(name)) {
            throw new SheepException("cannot assign to a constant");
        }
        this.values.put(name, value);
    }

    /**
     * 定数を格納する。上書きしようとすれば例外を投げる
     */
    public void putConst(String name, Object value) throws SheepException{
        putInCurrentEnv(name, value);
        this.constans.add(name);
    }

    /**
     * 現在の外側の環境まで探索して上書きを行う。見つからなければグローバルスコープに書き込む。
     */
    @Override
    public void put(String name, Object value) {
        Environment e = where(name);
        if(e == null) {
            e = getOutermostEnv();
        }
        ((EnvEx)e).putInCurrentEnv(name, value);
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
