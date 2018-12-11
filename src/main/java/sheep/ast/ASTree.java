package sheep.ast;

import java.util.Iterator;
import sheep.core.*;
public interface ASTree extends Iterable<ASTree> {
    public ASTree child(int i);
    public int numChildren();
    public Iterator<ASTree> iterator();
    public String location();
    public Object assign(Object r, Environment e);
}
