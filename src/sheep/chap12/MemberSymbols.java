package sheep.chap12;
import sheep.chap11.*;

/**
 * lookupの時classブロック内部にあるフィールド、メソッドの位置を決めるためのシンボルテーブル
 */
public class MemberSymbols extends Symbols {
    public static int METHOD = -1;
    public static int FIELD = -2;

    protected int type;
    public MemberSymbols(Symbols outer, int type) {
        super(outer);
        this.type = type;
    }

    @Override
    public Location get(String key, int nest) {
        Integer index = this.table.get(key);
        if(index != null) {
            return new Location(type, index.intValue());
        }
        if(this.outer != null) {
            return this.outer.get(key, nest);
        }
        return null;
    }

    @Override
    public Location put(String key) {
        Location loc = this.get(key, 0);
        if(loc == null) {
            return new Location(this.type, this.add(key));
        } else {
            return loc;
        }
    }
}
