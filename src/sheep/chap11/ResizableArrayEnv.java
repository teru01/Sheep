package sheep.chap11;
import java.util.*;
import sheep.chap6.*;
import sheep.chap11.EnvOptimizer.EnvEx2;

/**
 * 大域変数のための環境。格納できる変数は可変
 */
public class ResizableArrayEnv extends ArrayEnv {
    protected Symbols names;
    public ResizableArrayEnv() {
        super(10, null);
        this.names = new Symbols();
    }

    @Override
    public Symbols symbols() {
        return this.names;
    }

    /**
     * 名前によりgetされる。
     */
    @Override
    public Object get(String name) {
        Integer i = this.names.find(name);
        if(i == null) {
            if(this.outer == null) {
                return null;
            } else {
                return this.outer.get(name);
            }
        } else {
            return this.values[i];
        }
    }

    /**
     * 名前によりputされる。(配列インデックスで場所が決まるわけではない)
     */
    @Override
    public void put(String name, Object value) {
        Environment e = where(name);
        if(e == null) {
            e = this;
        }
        ((EnvEx2)e).putNew(name, value);
    }

    @Override
    public void putNew(String name, Object value) {
        assign(this.names.putNew(name), value);
    }

    @Override
    public Environment where(String name) {
        if(this.names.find(name) != null) {
            return this;
        } else if(this.outer == null) {
            return null;
        } else {
            return ((EnvEx2)this.outer).where(name);
        }
    }

    @Override
    public void put(int nest, int index, Object value) {
        if(nest == 0) {
            assign(index, value);
        } else {
            super.put(nest, index, value);
        }
    }

    protected void assign(int index, Object value) {
        // 現在の配列のサイズよりもインデックスが大きくなれば拡張
        if(index >= this.values.length) {
            int newLen = this.values.length * 2;
            if(index >= newLen) {
                newLen = index + 1;
            }
            this.values = Arrays.copyOf(this.values, newLen);
        }
        this.values[index] = value;
    }
}
