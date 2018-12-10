package sheep;

import static sheep.Parser.rule;

import java.util.HashSet;
import java.util.stream.IntStream;

import sheep.Parser.Operators;
import sheep.ast.*;

public class BasicParser {
    private HashSet<String> reserved = new HashSet<>();
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
                                   .maybe(rule(ElifStmnt.class).repeat(rule().sep("elif").ast(this.expr).ast(this.block)))
                                   .option(rule().sep("else").ast(this.block)),
                rule(WhileStmnt.class).sep("while").ast(this.expr).ast(this.block),
                this.simple
               );

    Parser program = rule().or(this.statement, rule(NullStmnt.class))
                           .sep(";", Token.EOL);

    Parser postfix;
    Parser def;

    private void functionParser() {
        Parser param = rule().identifier(this.reserved);
        Parser params = rule(ParameterList.class)
                            .ast(param)
                            .repeat(rule().sep(",").ast(param));
        Parser paramList = rule().sep("(").maybe(params).sep(")");
        Parser funcBody = rule(NonScopedBlock.class)
                            .sep("{")
                            .option(this.statement0)
                            .repeat(rule().sep(";", Token.EOL).option(this.statement0))
                            .sep("}");
        this.def = rule(DefStmnt.class)
                        .sep("def").identifier(this.reserved)
                        .ast(paramList)
                        .ast(funcBody);
        Parser args = rule(Arguments.class).ast(expr).repeat(rule().sep(",").ast(expr));
        this.postfix = rule().sep("(").maybe(args).sep(")");
        Parser closure = rule(Fun.class).sep("fun").ast(paramList).ast(funcBody);

        this.reserved.add(")");
        this.primary.repeat(postfix);
        this.simple.option(args);
        this.program.insertChoice(def);
        this.primary.insertChoice(closure);
    }

    private void forParser() {
        Parser iterControl = rule(ForIterExpr.class)
                                .sep("(")
                                .maybe(rule().ast(this.simple.repeat(rule().sep(",").option(this.simple)))).sep(";")
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
                                .ast(iterControl)
                                .ast(forBady);
        this.statement.insertChoice(sheepfor);
        this.reserved.add(",");
    }

    private void constParser() {
        Parser constant = rule(ConstExpr.class)
                            .sep("const")
                            .identifier(Name.class, this.reserved);

        this.factor.insertChoice(constant);
    }

    private void varParser() {
        Parser var = rule(VarExpr.class)
                        .sep("var")
                        .identifier(Name.class, this.reserved);
        this.factor.insertChoice(var);
    }

    private void arrayParser() {
        Parser elements = rule(ArrayLiteral.class)
                            .ast(this.expr)
                            .repeat(rule().sep(",").ast(this.expr));
        reserved.add("]");
        primary.insertChoice(rule().sep("[").maybe(elements).sep("]"));
        postfix.insertChoice(rule(ArrayRef.class).sep("[").ast(this.expr).sep("]"));
    }

    private void classParser() {
        Parser member = rule().or(this.def, simple);
        Parser class_body = rule(ClassBody.class)
                                .sep("{")
                                .option(member)
                                .repeat(rule().sep(";", Token.EOL).option(member))
                                .sep("}");
        Parser defclass = rule(ClassStmnt.class)
                                .sep("class")
                                .identifier(reserved)
                                .option(rule().sep("extends").identifier(reserved))
                                .ast(class_body);
        postfix.insertChoice(rule(Dot.class).sep(".").identifier(reserved));
        program.insertChoice(defclass);
    }

    public void defineOperators(Operator[] operators, int priority) {
        for(Operator op: operators) {
            this.operators.add(op.token, priority, op.bind);
        }
    }

    public class Operator {
        private String token;
        private boolean bind;
        Operator(String token, boolean bind) {
            this.token = token;
            this.bind = bind;
        }
    }

    public BasicParser() {
        this.reserved.add(";");
        this.reserved.add("}");
        this.reserved.add(Token.EOL);
        Operator[][] operators = {
                                    {   new Operator("=", Operators.RIGHT),
                                        new Operator("+=", Operators.RIGHT),
                                        new Operator("-=", Operators.RIGHT),
                                        new Operator("*=", Operators.RIGHT),
                                        new Operator("/=", Operators.RIGHT),
                                    },
                                    {
                                        new Operator("||", Operators.LEFT)
                                    },
                                    {
                                        new Operator("&&", Operators.LEFT)
                                    },
                                    {
                                        new Operator("==", Operators.LEFT),
                                        new Operator("!=", Operators.LEFT),
                                    },
                                    {
                                        new Operator(">", Operators.LEFT),
                                        new Operator("<", Operators.LEFT),
                                        // new Operator(">=", Operators.LEFT),
                                        // new Operator("<=", Operators.LEFT),
                                    },
                                    {
                                        new Operator("+", Operators.LEFT),
                                        new Operator("-", Operators.LEFT),
                                    },
                                    {
                                        new Operator("*", Operators.LEFT),
                                        new Operator("/", Operators.LEFT),
                                        new Operator("%", Operators.LEFT),
                                    }
                                 };
        int i = 1;
        for(Operator[] op: operators) {
            defineOperators(op, i);
            i++;
        }

        functionParser();
        classParser();
        arrayParser();
        varParser();
        forParser();
        constParser();
    }

    public ASTree parse(Lexer lexer) throws ParseException {
        return this.program.parse(lexer);
    }
}
