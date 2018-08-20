package sheep;
import sheep.ast.ASTree;

public class SheepException extends RuntimeException {
    public SheepException(String m) { super(m); }
    public SheepException(String m, ASTree t) {
        super(m + " " + t.location());
    }
}