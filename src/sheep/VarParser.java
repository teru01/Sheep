package sheep;
import javassist.gluonj.Reviser;
import sheep.FuncParser;
import static sheep.Parser.rule;
import sheep.*;
import sheep.ast.*;

@Reviser
public class VarParser extends BasicParser {
    Parser var = rule(VarStmnt.class)
                        .sep("var")
                        .ast(this.expr);

    public VarParser() {
        this.simple.insertChoice(this.var);
    }
}
