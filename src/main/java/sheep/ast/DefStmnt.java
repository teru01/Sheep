package sheep.ast;
import java.util.List;

public class DefStmnt extends ASTList {
    public DefStmnt(List<ASTree> c) { super(c); }

    public String name() {
        return ((ASTLeaf)child(0)).getToken().getText();
    }

    public ParameterList parameters() {
        return (ParameterList)child(1);
    }

    public NonScopedBlock body() {
        return (NonScopedBlock)child(2);
    }

    public String toString() {
        return "(def " + name() + " " + parameters() + " " + body() + ")";
    }
}
