package sheep;
import javassist.gluonj.Require;
import javassist.gluonj.Reviser;
import sheep.ast.*;
import static sheep.Parser.rule;

@Require(VarParser.class)
@Reviser
public class ForParser extends BasicParser {
    Parser iterControl = rule(ForIterExpr.class)
                            .sep("(")
                            .maybe(rule().ast(this.simple)).sep(";")
                            .maybe(rule().ast(this.simple)).sep(";")
                            .maybe(rule().ast(this.simple))
                            .sep(")");
    Parser forBady = rule(NonScopedBlock.class)
                            .sep("{")
                            .option(this.statement0)
                            .repeat(rule().sep(";", Token.EOL).option(this.statement0))
                            .sep("}");
    Parser sheepfor = rule(ForStmnt.class)
                        .sep("for")
                        .ast(this.iterControl)
                        .ast(this.forBady);
    public ForParser() {
        this.statement.insertChoice(this.sheepfor);
    }
}
