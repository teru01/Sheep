package sheep.chap12;
import sheep.*;
import sheep.chap11.*;

public class SymbolThis extends Symbols {
    //thisは局所変数と解釈される
    public static final String NAME = "this";

    public SymbolThis(Symbols outer) {
        super(outer);
        add(NAME);
    }

    @Override
    public int putNew(String key) {
        throw new SheepException("fatal error. cannot use String for putNew()");
    }

    @Override
    public Location put(String key) {
        Location loc = this.outer.put(key);
        // if(loc.nest >= 0) {
        //     loc.nest++;
        // }
        return loc;
    }
}
