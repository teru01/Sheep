package sheep.ast;

import java.util.Iterator;

public interface ASTree extends Iterable<ASTree> {
    public abstract ASTree child(int i);
    public abstract int numChildren();
    public abstract Iterator<ASTree> iterator();
    public abstract String location();
}
