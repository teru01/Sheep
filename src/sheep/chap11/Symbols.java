package sheep.chap11;
import java.util.*;

public class Symbols {
    public static class Location {
        public int nest;
        public int index;
        public Location(int nest, int index) {
            this.nest = nest;
            this.index = index;
        }
    }
    protected Symbols outer;
    protected HashMap<String, Integer> table;
    public Symbols() { this(null); }
    public Symbols(Symbols outer) {
        this.outer = outer;
        this.table = new HashMap<>();
    }
    public int size() {
        return this.table.size();
    }

    public void append(Symbols s) {
        this.table.putAll(s.table);
    }

    public Integer find(String key) {
        return this.table.get(key);
    }

    public Location get(String key) {
        return get(key, 0);
    }

    /**
     * 環境のネストと配列の位置を記録したオブジェクトを作成して返す
     * @param key
     * @param nest
     * @return
     */
    public Location get(String key, int nest) {
        Integer index = this.table.get(key);
        if(index != null) {
            return new Location(nest, index.intValue());
        }
        if(outer == null) {
            return null;
        } else {
            return outer.get(key, nest + 1);
        }
    }

    /**
     * オブジェクトが存在しなければ作成してインデックスを返す
     */
    public int putNew(String key) {
        Integer i = this.find(key);
        if(i == null) {
            return this.add(key);
        } else {
            return i;
        }
    }

    /**
     * オブジェクトが存在しなければ作成して位置オブジェクトを返す
     */
    public Location put(String key) {
        Location loc = this.get(key, 0);
        if(loc == null) {
            return new Location(0, add(key));
        } else {
            return loc;
        }
    }

    /**
     * 変数を格納して場所を返す
     * @param key
     * @return int i 格納場所
     */
    public int add(String key) {
        int i = this.table.size();
        this.table.put(key, i);
        return i;
    }
}
