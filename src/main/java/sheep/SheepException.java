package sheep;
import sheep.ast.ASTree;

@SuppressWarnings("serial")
public class SheepException extends RuntimeException {
    public SheepException(){}
    public SheepException(String m) { super(m); }
    public SheepException(String m, ASTree t) {
        super(m + " " + t.location());
    }
    public SheepException(String m, int n) {
        super(m + " " + "at line " + n);
    }
}
