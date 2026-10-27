package com.juliamendes.calculadora.expression;

import com.juliamendes.calculadora.core.ArithmeticError;
import com.juliamendes.calculadora.core.Calculator;
import com.juliamendes.calculadora.core.CalculatorException;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Avalia expressões aritméticas em texto, como {@code "2 + 3 * (4 - 1)"}.
 *
 * <p>Operadores suportados: {@code + - * / % ^ !}, parênteses e raiz quadrada ({@code sqrt(x)} ou
 * {@code √x}). Ponto ou vírgula são aceitos como separador decimal. Veja {@link Parser} para a
 * precedência exata.
 *
 * <p>A classe é imutável e segura para uso concorrente.
 */
public final class ExpressionEvaluator {

  /** Tamanho máximo aceito para uma expressão. */
  public static final int MAX_LENGTH = 1_000;

  private final Calculator calculator;

  /** Cria um avaliador com um {@link Calculator} padrão. */
  public ExpressionEvaluator() {
    this(new Calculator());
  }

  /**
   * Cria um avaliador que delega as operações ao {@code calculator} informado.
   *
   * @param calculator implementação das operações
   */
  public ExpressionEvaluator(Calculator calculator) {
    this.calculator = Objects.requireNonNull(calculator, "calculator");
  }

  /**
   * Avalia a expressão.
   *
   * @param expression texto da expressão
   * @return resultado
   * @throws ExpressionSyntaxException se a expressão for mal formada
   * @throws ArithmeticError se uma operação for indefinida (ex.: divisão por zero)
   */
  public BigDecimal evaluate(String expression) {
    if (expression == null || expression.isBlank()) {
      throw new ExpressionSyntaxException("Expressão vazia", 1);
    }
    if (expression.length() > MAX_LENGTH) {
      throw new CalculatorException(
          "Expressão muito longa (máximo de " + MAX_LENGTH + " caracteres).");
    }
    Node tree = new Parser(new Lexer(expression).tokenize()).parse();
    try {
      return evaluate(tree);
    } catch (ArithmeticException e) {
      // Estouro de escala do BigDecimal em resultados intermediários gigantescos.
      throw new ArithmeticError("Resultado fora do intervalo suportado.");
    }
  }

  private BigDecimal evaluate(Node node) {
    return switch (node) {
      case Node.Literal literal -> literal.value();
      case Node.Unary unary ->
          unary.operator() == TokenType.MINUS
              ? evaluate(unary.operand()).negate()
              : evaluate(unary.operand());
      case Node.Factorial factorial -> calculator.factorial(evaluate(factorial.operand()));
      case Node.SquareRoot root -> calculator.squareRoot(evaluate(root.operand()));
      case Node.Binary binary -> binary(binary);
    };
  }

  private BigDecimal binary(Node.Binary node) {
    BigDecimal left = evaluate(node.left());
    BigDecimal right = evaluate(node.right());
    return switch (node.operator()) {
      case PLUS -> calculator.add(left, right);
      case MINUS -> calculator.subtract(left, right);
      case STAR -> calculator.multiply(left, right);
      case SLASH -> calculator.divide(left, right);
      case PERCENT -> calculator.remainder(left, right);
      case CARET -> calculator.power(left, right);
      default -> throw new IllegalStateException("Operador binário inesperado: " + node.operator());
    };
  }
}
