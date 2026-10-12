# Lab 5: Calc1 Visitor and Listener

本实验沿用给定的 `Calc1.g4` 和分支标签 `aren`、`mulDiv`、`addSub`、`goFactor`，实现带括号的加、减、乘、除计算器。输入是整数，计算结果使用 `Double`，所以 `1/2` 得到 `0.5`。

## 运行

在 Linux 虚拟机中，需要 JDK、Make 和 ANTLR 4.13.2。第一次构建时，如果 `libs/antlr-4.13.2-complete.jar` 不存在，Makefile 会用 curl 从 ANTLR 官网下载；也可以手动将现有 JAR 放入该位置。

```bash
cd lab5
make run
make run-listener
make test
```

两个入口程序都应输出：

```text
2+3*(5-4)/2 = 3.5
3-4/2 = 1.0
20/4-3 = 2.0
((1+2)*3-5)/2 = 2.0
```

生成的 Java 文件位于 `build/generated/`，包名是 `generated.Calc1`；`Main.java`、`CalcLab5Visitor.java` 和 Listener 示例位于 `src/main/java/`。

## Visitor 实现

`CalcLab5Visitor` 继承 `Calc1BaseVisitor<Double>`。每个 `visit...` 方法返回当前子树的计算结果：

| 方法 | 处理方式 |
| --- | --- |
| `visitFactor` | 将 `INT` 文本转换为 `Double` |
| `visitGoFactor` | 调用 `visit(ctx.factor())` |
| `visitAren` | 调用 `visit(ctx.expr())`，取得括号内部的值 |
| `visitMulDiv` | 递归求左右操作数，再做乘法或除法 |
| `visitAddSub` | 递归求左右操作数，再做加法或减法 |

文法中乘除的左递归分支排在加减之前，因此乘除优先。两组运算默认左结合，例如 `18/3/2` 为 `(18/3)/2`。括号通过嵌套的 `expr` 子树改变运算顺序。

`Main` 遍历 `root` 的每个直接 `expr` 子节点，分别求值并输出；`getText()` 得到的表达式不含被词法分析器跳过的空白。文法将换行也作为空白，因此没有将“一行”定义为独立的表达式规则。

## Listener 设计

Listener 的回调返回 `void`，因此不能像 Visitor 一样直接返回子树结果。`CalcLab5Listener` 使用 `ParseTreeProperty<Double>` 保存“解析树节点 -> 计算值”。

计算放在 `exitFactor`、`exitGoFactor`、`exitAren`、`exitMulDiv` 和 `exitAddSub` 中。退出父节点时，子节点的退出回调已经执行完毕，可以读取左右子节点的值，计算后存入父节点。若在 `enterMulDiv` 中计算，孩子的值尚未准备好。

```java
CalcLab5Listener listener = new CalcLab5Listener();
ParseTreeWalker.DEFAULT.walk(listener, context);
for (Calc1Parser.ExprContext expr : context.expr()) {
    System.out.printf("%s = %s%n", expr.getText(), listener.getValue(expr));
}
```

Visitor 通过显式 `visit(...)` 决定子节点的访问；Listener 由 `ParseTreeWalker` 自动遍历，计算结果通过节点属性表传递。构建时同时使用 `-visitor -listener` 生成两套接口。

也可以在退出回调中使用数值栈；二元运算必须先弹出右操作数、再弹出左操作数，否则减法和除法会颠倒。当前实现采用节点属性表，使每个结果与对应子树直接关联。

参考：[ANTLR Listener 文档](https://github.com/antlr/antlr4/blob/master/doc/listeners.md)、[ParseTreeProperty 源码](https://github.com/antlr/antlr4/blob/4.13.2/runtime/Java/src/org/antlr/v4/runtime/tree/ParseTreeProperty.java)。

## 测试

`CalcLab5Test` 对 Visitor 和 Listener 分别检查预先指定的期望值，覆盖题目样例、乘除优先、同级运算左结合、括号覆盖优先级、多层括号、小数商、负结果、多位整数、多表达式以及空输入。

`make test` 还将两个入口程序的完整标准输出分别与 `tests/expected.out` 比较，检查输出顺序和格式。生成文件与依赖 JAR 不提交到 Git。
