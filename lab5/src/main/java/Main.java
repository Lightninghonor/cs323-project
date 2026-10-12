import generated.Calc1.Calc1Lexer;
import generated.Calc1.Calc1Parser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {

    static final String SAMPLE_INPUT =
            "2 + 3 * (5 - 4) / 2\n 3-4/2\n20 /4 -3\n((1+2)*3-5)/2 ";

    static Calc1Parser.RootContext parse(String source) {
        CharStream input = CharStreams.fromString(source);
        Calc1Lexer lexer = new Calc1Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Calc1Parser parser = new Calc1Parser(tokens);
        return parser.root();
    }

    public static void main(String[] args) {
        Calc1Parser.RootContext context = parse(SAMPLE_INPUT);
        CalcLab5Visitor visitor = new CalcLab5Visitor();
        for (Calc1Parser.ExprContext expr : context.expr()) {
            System.out.printf("%s = %s%n", expr.getText(), visitor.visit(expr));
        }
    }
}
