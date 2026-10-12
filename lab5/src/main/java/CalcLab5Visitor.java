import generated.Calc1.Calc1BaseVisitor;
import generated.Calc1.Calc1Parser;

public class CalcLab5Visitor extends Calc1BaseVisitor<Double> {

    @Override
    public Double visitAren(Calc1Parser.ArenContext ctx) {
        // Parentheses determine the tree structure; forward the inner value.
        return visit(ctx.expr());
    }

    @Override
    public Double visitMulDiv(Calc1Parser.MulDivContext ctx) {
        double left = visit(ctx.expr(0));
        double right = visit(ctx.expr(1));
        return ctx.MUL() != null ? left * right : left / right;
    }

    @Override
    public Double visitAddSub(Calc1Parser.AddSubContext ctx) {
        double left = visit(ctx.expr(0));
        double right = visit(ctx.expr(1));
        return ctx.ADD() != null ? left + right : left - right;
    }

    @Override
    public Double visitGoFactor(Calc1Parser.GoFactorContext ctx) {
        return visit(ctx.factor());
    }

    @Override
    public Double visitFactor(Calc1Parser.FactorContext ctx) {
        return Double.parseDouble(ctx.INT().getText());
    }
}
