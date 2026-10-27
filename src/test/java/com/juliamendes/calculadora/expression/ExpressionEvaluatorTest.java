package com.juliamendes.calculadora.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import com.juliamendes.calculadora.core.ArithmeticError;
import com.juliamendes.calculadora.core.CalculatorException;
import java.math.BigDecimal;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ExpressionEvaluatorTest {

  private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

  private void assertEvaluates(String expression, String expected) {
    BigDecimal actual = evaluator.evaluate(expression);
    assertEquals(
        0,
        new BigDecimal(expected).compareTo(actual),
        () -> expression + ": esperado " + expected + ", obtido " + actual);
  }

  @ParameterizedTest(name = "{0} = {1}")
  @CsvSource(
      delimiter = '|',
      value = {
        "42|42",
        "2 + 3|5",
        "2 + 3 * 4|14",
        "(2 + 3) * 4|20",
        "10 - 4 - 3|3",
        "100 / 10 / 2|5",
        "10 % 3|1",
        "2 * 3 % 4|2",
        "0.1 + 0.2|0.3",
        "1,5 + 2,5|4",
        "2 ^ 3 ^ 2|512",
        "(2 ^ 3) ^ 2|64",
        "-2 ^ 2|-4",
        "(-2) ^ 2|4",
        "2 ^ -1|0.5",
        "--3|3",
        "-+3|-3",
        "3 - -2|5",
        "5!|120",
        "3!!|720",
        "-3!|-6",
        "2 ^ 3!|64",
        "sqrt(16)|4",
        "SQRT(2.25)|1.5",
        "√9 + 1|4",
        "√(3 * 3) * 2|6",
        "sqrt 16|4",
        "2 × 3 ÷ 4 − 1|0.5",
        "  ( ( 1 ) )  |1",
        "1 / 3 * 3|0.9999999999999999999999999999999999"
      })
  void evaluatesWithCorrectPrecedence(String expression, String expected) {
    assertEvaluates(expression, expected);
  }

  @ParameterizedTest(name = "\"{0}\" é sintaxe inválida")
  @NullAndEmptySource
  @ValueSource(
      strings = {
        "   ", "2 +", "* 2", "(2 + 3", "2 + 3)", "()", "2 3", "2 (3)", "1..2", "1.2.3", ".", "abc",
        "sin(1)", "2 & 3", "!3", "sqrt", "2 ^"
      })
  void rejectsMalformedExpressions(String expression) {
    assertThrows(ExpressionSyntaxException.class, () -> evaluator.evaluate(expression));
  }

  @ParameterizedTest(name = "\"{0}\" é operação indefinida")
  @ValueSource(
      strings = {
        "1 / 0",
        "1 / (2 - 2)",
        "5 % 0",
        "sqrt(-1)",
        "0 ^ -1",
        "(-8) ^ 0.5",
        "(-1)!",
        "2.5!",
        "1001!",
        "2 ^ 10000",
        "((9 ^ 9999) ^ 9999) ^ 9999",
        "(10 ^ 9999) ^ 9999 % 3"
      })
  void reportsArithmeticErrors(String expression) {
    assertThrows(ArithmeticError.class, () -> evaluator.evaluate(expression));
  }

  @Test
  void hugeIntermediateResultsDoNotExplode() {
    assertTimeoutPreemptively(
        Duration.ofSeconds(2), () -> evaluator.evaluate("(10 ^ 9999) ^ 9999 + 10 ^ -9999"));
  }

  @Test
  void reportsPositionOfSyntaxError() {
    ExpressionSyntaxException error =
        assertThrows(ExpressionSyntaxException.class, () -> evaluator.evaluate("2 + * 3"));
    assertEquals(5, error.position());
  }

  @Test
  void reportsUnclosedParenthesis() {
    ExpressionSyntaxException error =
        assertThrows(ExpressionSyntaxException.class, () -> evaluator.evaluate("(1 + 2"));
    assertEquals(7, error.position());
  }

  @Test
  void rejectsOverlyLongExpression() {
    String expression = "1+".repeat(500) + "1";
    assertThrows(CalculatorException.class, () -> evaluator.evaluate(expression));
  }

  @Test
  void rejectsDeepNestingWithoutStackOverflow() {
    String expression = "(".repeat(400) + "1" + ")".repeat(400);
    assertThrows(ExpressionSyntaxException.class, () -> evaluator.evaluate(expression));
  }

  @Test
  void acceptsModerateNesting() {
    assertEvaluates("(".repeat(100) + "1" + ")".repeat(100), "1");
  }

  @Test
  void rejectsLongChainOfUnaryOperators() {
    assertThrows(ExpressionSyntaxException.class, () -> evaluator.evaluate("-".repeat(500) + "1"));
  }
}
