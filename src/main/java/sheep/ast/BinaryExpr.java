package sheep.ast;

import java.util.List;

//2項演算子のクラス
public class BinaryExpr extends ASTList {
    public BinaryExpr(List<ASTree> c) { super(c); }

    public ASTree left() { return child(0); }

    public ASTLeaf operator() {
        return (ASTLeaf)child(1);
    }

    public ASTree right() { return child(2); }
}

