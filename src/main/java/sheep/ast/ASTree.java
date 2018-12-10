package sheep.ast;

import sheep.core.Environment;

public interface ASTree extends Iterable<ASTree> {
    public ASTree child(int i);
    public int numChildren();
    public String location();
    public Object computeAssign(Object right, Environment env);
}
