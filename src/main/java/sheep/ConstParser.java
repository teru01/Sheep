package sheep;

import sheep.BasicParser;
import javassist.gluonj.*;
import static sheep.Parser.rule;
import sheep.ast.*;

@Require(ForParser.class)
@Reviser
public class ConstParser extends BasicParser {
    Parser constant = rule(ConstExpr.class)
                        .sep("const")
                        .identifier(Name.class, this.reserved);

    public ConstParser() {
        this.factor.insertChoice(this.constant);
    }
}
