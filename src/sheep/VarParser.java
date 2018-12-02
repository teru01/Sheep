package sheep;
import javassist.gluonj.Reviser;
import sheep.FuncParser;
import static sheep.Parser.rule;
import sheep.*;
import sheep.ast.*;

@Reviser
public class VarParser extends BasicParser {
    Parser var = rule(VarExpr.class)
                        .sep("var")
                        .identifier(Name.class, this.reserved);

    public VarParser() {
        this.factor.insertChoice(this.var);
    }
}
