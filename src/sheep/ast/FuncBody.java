package sheep.ast;

import java.util.List;

/**
 * 関数本体のためのクラス。スコープの違いからblockとは区別する
 */
public class FuncBody extends ASTList {
    public FuncBody(List<ASTree> c) {
        super(c);
    }
}
