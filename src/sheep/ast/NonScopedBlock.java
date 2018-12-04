package sheep.ast;

import java.util.List;

/**
 * スコープを持たないブロックスコープの違いからblockとは区別する
 */
public class NonScopedBlock extends ASTList {
    public NonScopedBlock(List<ASTree> c) {
        super(c);
    }
}
