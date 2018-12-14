package sheep.ast;
import java.util.*;
public class ReturnStmnt extends ASTList{
    public ReturnStmnt(List<ASTree> c) { super(c); }

    public ASTree returnValue() {
        return this.child(0);
    }
}
