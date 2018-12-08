package sheep.ast;
import java.util.*;

// elifの(条件,実行文)のリスト
public class ElifStmnt extends ASTList {
    public ElifStmnt(List<ASTree> c) {
        super(c);
    }

    public ASTree getElifCondition(int i) {
        return child(i).child(0);
    }

    public ASTree getElifBlock(int i) {
        return child(i).child(1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < numChildren(); i++) {
            sb.append("(elif " + getElifCondition(i) + " " + getElifBlock(i) + ")");
        }
        return sb.toString();
    }
}
