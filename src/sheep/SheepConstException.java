package sheep;

import sheep.ast.*;

public class SheepConstException extends SheepException {
    public SheepConstException(){}
    public SheepConstException(String m) {
        super(m);
    }
    public SheepConstException(String m, ASTree t) {
        super(m, t);
    }
}
