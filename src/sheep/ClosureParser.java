package sheep;
import static sheep.Parser.rule;
import sheep.ast.Fun;

public class ClosureParser extends FuncParser {
    public ClosureParser() {
        primary.insertChoice(rule(Fun.class).sep("fun").ast(paramList).ast(this.NonScopedBlock));
    }
}
