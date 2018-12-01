package sheep.ast;
import java.util.*;

public class VarStmnt extends ASTList {
    public VarStmnt(List<ASTree> c){ super(c); }

    public ASTree getAssignExpr() {
        return child(0);
    }
}
