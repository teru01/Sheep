package sheep;
import static sheep.Parser.rule;
import sheep.Parser.Operators;
import java.util.HashSet;

import sheep.ast.*;

public class BasicParser {
    HashSet<String> reserved = new HashSet<>();
    Operators operators = new Operators();
    Parser expr0 = rule();
    Parser primary = rule(PrimaryExpr.class)
            .or(rule().sep("(").ast(expr0).sep(")"),
                rule().number(NumberLiteral.class),
                rule().identifier(Name.class, reserved),
                rule().string(StringLiteral.class)
               );
    
    Parser factor = rule().or(rule(NegativeExpr.class).sep("-").ast(primary));
    
    Parser expr = expr0.expression(BinaryExpr.class, factor, operators);

    Parser statement0 = rule();
    Parser block = rule(BlockStmnt.class)
            .sep("{").option(statement0)
            .repeat(rule().sep(";", Token.EOL).option(statement0))
            .sep("}");
    
    Parser simple = rule(PrimaryExpr.class).ast(expr);
    
    Parser statement = statement0.or(
            rule(IfStmnt.class).sep("if").ast(expr).ast(block)
                               .option(rule().sep("else").ast(block)),
            rule(WhileStmnt.class).sep("while").ast(expr).ast(block),
            simple);
    
            Parser program = rule().or(statement, rule(NullStmnt.class))
                                   .sep(";", Token.EOL);

    


}