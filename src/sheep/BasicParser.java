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
            .or(rule().sep("(").ast(this.expr0).sep(")"),
                rule().number(NumberLiteral.class),
                rule().identifier(Name.class, reserved),
                rule().string(StringLiteral.class)
               );

    Parser factor = rule().or(rule(NegativeExpr.class).sep("-").ast(this.primary), this.primary);

    Parser expr = this.expr0.expression(BinaryExpr.class, this.factor, this.operators);

    Parser statement0 = rule();

    Parser block = rule(BlockStmnt.class)
            .sep("{").option(this.statement0)
            .repeat(rule().sep(";", Token.EOL).option(this.statement0))
            .sep("}");

    Parser simple = rule(PrimaryExpr.class).ast(this.expr);

    Parser statement = this.statement0
            .or(rule(IfStmnt.class).sep("if").ast(this.expr).ast(this.block)
                                   .option(rule().sep("else").ast(this.block)),
                rule(WhileStmnt.class).sep("while").ast(this.expr).ast(this.block),
                this.simple
               );

    Parser program = rule().or(this.statement, rule(NullStmnt.class))
                           .sep(";", Token.EOL);

    public BasicParser() {
        this.reserved.add(";");
        this.reserved.add("}");
        this.reserved.add(Token.EOL);

        this.operators.add("=", 1, Operators.RIGHT);
        this.operators.add("==", 2, Operators.LEFT);
        this.operators.add(">", 2, Operators.LEFT);
        this.operators.add("<", 2, Operators.LEFT);
        this.operators.add("+", 3, Operators.LEFT);
        this.operators.add("-", 3, Operators.LEFT);
        this.operators.add("*", 4, Operators.LEFT);
        this.operators.add("/", 4, Operators.LEFT);
        this.operators.add("%", 4, Operators.LEFT);
    }

    public ASTree parse(Lexer lexer) throws ParseException {
        return this.program.parse(lexer);
    }
}
