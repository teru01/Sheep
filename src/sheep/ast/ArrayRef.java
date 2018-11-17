package sheep.ast;
import java.util.List;

public class ArrayRef extends Postfix {
    public ArrayRef(List<ASTree> c) { super(c); }
    // 配列のインデックスを表す添字 ary[1]の1のこと
    public ASTree index() {
        return child(0);
    }
    @Override
    public String toString() {
        return "[" + index() + "]";
    }
}
