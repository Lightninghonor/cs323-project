import generated.Calc1.Calc1BaseListener;
import generated.Calc1.Calc1Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class CalcLab5Listener extends Calc1BaseListener {

    // Listener callbacks return void, so save each node's value explicitly.
    private final ParseTreeProperty<Double> values = new ParseTreeProperty<>();

    public double getValue(ParseTree node) {
        return values.get(node);
    }

    @Override
    public void exitFactor(Calc1Parser.FactorContext ctx) {
        values.put(ctx, Double.parseDouble(ctx.INT().getText()));
    }

    @Override
    public void exitGoFactor(Calc1Parser.GoFactorContext ctx) {
        values.put(ctx, values.get(ctx.factor()));
    }

    @Override
    public void exitAren(Calc1Parser.ArenContext ctx) {
        values.put(ctx, values.get(ctx.expr()));
    }

    @Override
    public void exitMulDiv(Calc1Parser.MulDivContext ctx) {
        // exit callbacks run after both children have been visited.
        double left = values.get(ctx.expr(0));
        double right = values.get(ctx.expr(1));
        values.put(ctx, ctx.MUL() != null ? left * right : left / right);
    }

    @Override
    public void exitAddSub(Calc1Parser.AddSubContext ctx) {
        double left = values.get(ctx.expr(0));
        double right = values.get(ctx.expr(1));
        values.put(ctx, ctx.ADD() != null ? left + right : left - right);
    }
}
