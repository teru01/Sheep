package sheep;
import javassist.gluonj.Reviser;
import sheep.ast.*;
import static sheep.Parser.rule;

@Reviser
public class ForParser extends BasicParser {
    Parser iterControl = rule(ForIterExpr.class)
                            .ast(this.factor)
                            .ast(this.factor)
                            .ast(this.factor);
    Parser sheepfor = rule(ForStmnt.class)
                        .sep("for")
                        .ast(this.iterControl)
                        .ast(this.block);
}
