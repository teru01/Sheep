package sheep.ast;

import java.util.Iterator;
import sheep.core.*;
public abstract class ASTree implements Iterable<ASTree> {
    public abstract ASTree child(int i);
    public abstract int numChildren();
    public abstract Iterator<ASTree> iterator();
    public abstract String location();
    public abstract Object assign(Object r, Environment e);
}
