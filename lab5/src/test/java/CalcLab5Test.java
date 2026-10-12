import generated.Calc1.Calc1Lexer;
import generated.Calc1.Calc1Parser;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class CalcLab5Test {

    private static int programs;
    private static int expressions;

    private static final BaseErrorListener FAIL_ON_ERROR = new BaseErrorListener() {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                int line, int column, String message,
                                RecognitionException exception) {
            throw new AssertionError("Unexpected syntax error at " + line + ":" + column
                    + ": " + message);
        }
    };

    private static void check(String source, double... expected) {
        Calc1Lexer lexer = new Calc1Lexer(CharStreams.fromString(source));
        lexer.removeErrorListeners();
        lexer.addErrorListener(FAIL_ON_ERROR);
        Calc1Parser parser = new Calc1Parser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(FAIL_ON_ERROR);
        Calc1Parser.RootContext root = parser.root();

        if (root.expr().size() != expected.length) {
            throw new AssertionError("Wrong expression count for: " + source);
        }

        CalcLab5Visitor visitor = new CalcLab5Visitor();
        CalcLab5Listener listener = new CalcLab5Listener();
        ParseTreeWalker.DEFAULT.walk(listener, root);
        for (int i = 0; i < expected.length; i++) {
            Calc1Parser.ExprContext expression = root.expr(i);
            assertValue("Visitor", expression.getText(), visitor.visit(expression), expected[i]);
            assertValue("Listener", expression.getText(), listener.getValue(expression), expected[i]);
            expressions++;
        }
        programs++;
    }

    private static void assertValue(String mode, String expression, double actual, double expected) {
        if (!Double.isFinite(actual) || Math.abs(actual - expected) > 1e-12) {
            throw new AssertionError(mode + ": " + expression
                    + " expected " + expected + " but got " + actual);
        }
    }

    public static void main(String[] args) {
        check(Main.SAMPLE_INPUT, 3.5, 1.0, 2.0, 2.0);
        check("2+3*4", 14.0);
        check("(2+3)*4", 20.0);
        check("18/3/2", 3.0);
        check("18/(3/2)", 12.0);
        check("20/4*2", 10.0);
        check("20/(4*2)", 2.5);
        check("10-3-2", 5.0);
        check("10-(3-2)", 9.0);
        check("1/2", 0.5);
        check("5/2", 2.5);
        check("3-5", -2.0);
        check("0", 0.0);
        check("0/5", 0.0);
        check("123+45", 168.0);
        check("(((((7)))))", 7.0);
        check("2*(3+(4*5))", 46.0);
        check("8/(3-1)*(2+1)", 12.0);
        check("(7-1)/(2+1)", 2.0);
        check("1-7/2", -2.5);
        check("  12\t+\r\n6 / 4 ", 13.5);
        check("1/3", 1.0 / 3.0);
        check("2+2 3*3 (8-2)/3", 4.0, 9.0, 2.0);
        check("");
        check(" \t\r\n ");
        System.out.printf("[PASS] Visitor and Listener: %d programs, %d expressions%n",
                programs, expressions);
    }
}
