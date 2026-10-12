import generated.Calc1.Calc1Parser;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ListenerMain {

    public static void main(String[] args) {
        Calc1Parser.RootContext context = Main.parse(Main.SAMPLE_INPUT);
        CalcLab5Listener listener = new CalcLab5Listener();
        ParseTreeWalker.DEFAULT.walk(listener, context);

        for (Calc1Parser.ExprContext expr : context.expr()) {
            System.out.printf("%s = %s%n", expr.getText(), listener.getValue(expr));
        }
    }
}
