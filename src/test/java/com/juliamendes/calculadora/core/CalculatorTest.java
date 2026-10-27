package com.juliamendes.calculadora.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class CalculatorTest {

  private final Calculator calculator = new Calculator();

  private static BigDecimal n(String value) {
    return new BigDecimal(value);
  }

  private static void assertNumber(String expected, BigDecimal actual) {
    assertEquals(
        0, n(expected).compareTo(actual), () -> "esperado " + expected + ", obtido " + actual);
  }

  @ParameterizedTest(name = "{0} + {1} = {2}")
  @CsvSource({"10, 5, 15", "-10, 5, -5", "0.1, 0.2, 0.3", "1e20, 1, 100000000000000000001"})
  void add(String a, String b, String expected) {
    assertNumber(expected, calculator.add(n(a), n(b)));
  }

  @ParameterizedTest(name = "{0} - {1} = {2}")
  @CsvSource({"10, 5, 5", "5, 10, -5", "0.3, 0.1, 0.2", "-1.5, -1.5, 0"})
  void subtract(String a, String b, String expected) {
    assertNumber(expected, calculator.subtract(n(a), n(b)));
  }

  @ParameterizedTest(name = "{0} * {1} = {2}")
  @CsvSource({"10, 5, 50", "-3, 4, -12", "1.1, 1.1, 1.21", "0, -7, 0"})
  void multiply(String a, String b, String expected) {
    assertNumber(expected, calculator.multiply(n(a), n(b)));
  }

  @ParameterizedTest(name = "{0} / {1} = {2}")
  @CsvSource({
    "10, 5, 2",
    "1, 4, 0.25",
    "-9, 3, -3",
    "1, 3, 0.3333333333333333333333333333333333",
    "2, 3, 0.6666666666666666666666666666666667"
  })
  void divide(String a, String b, String expected) {
    assertNumber(expected, calculator.divide(n(a), n(b)));
  }

  @ParameterizedTest(name = "{0} / 0 lança erro")
  @ValueSource(strings = {"10", "0", "-1", "0.0"})
  void divideByZero(String dividend) {
    ArithmeticError error =
        assertThrows(ArithmeticError.class, () -> calculator.divide(n(dividend), n("0.00")));
    assertTrue(error.getMessage().contains("Divisão por zero"));
  }

  @ParameterizedTest(name = "{0} % {1} = {2}")
  @CsvSource({"10, 3, 1", "-10, 3, -1", "10, -3, 1", "5.5, 2, 1.5", "6, 3, 0"})
  void remainder(String a, String b, String expected) {
    assertNumber(expected, calculator.remainder(n(a), n(b)));
  }

  @Test
  void additionIsRoundedBeyondPrecision() {
    BigDecimal huge = n("1E+100");
    assertNumber("1E+100", calculator.add(huge, BigDecimal.ONE));
  }

  @Test
  void remainderWithHugeMagnitudeGapIsRejected() {
    assertThrows(ArithmeticError.class, () -> calculator.remainder(n("1E+20000"), n("3")));
  }

  @Test
  void remainderOfLargeButReasonableNumbers() {
    assertNumber("1", calculator.remainder(n("1E+100"), n("3")));
  }

  @Test
  void remainderByZero() {
    assertThrows(ArithmeticError.class, () -> calculator.remainder(n("10"), BigDecimal.ZERO));
  }

  @ParameterizedTest(name = "{1}% de {0} = {2}")
  @CsvSource({"200, 15, 30", "50, 50, 25", "80, 12.5, 10", "-40, 10, -4", "0.1, 10, 0.01"})
  void percentOf(String total, String percent, String expected) {
    assertNumber(expected, calculator.percentOf(n(total), n(percent)));
  }

  @ParameterizedTest(name = "{0} ^ {1} = {2}")
  @CsvSource({
    "2, 3, 8",
    "2, 0, 1",
    "0, 0, 1",
    "2, -1, 0.5",
    "-2, 3, -8",
    "-2, 2, 4",
    "1.5, 2, 2.25",
    "4, 0.5, 2",
    "0, 2.5, 0",
    "10, 2.0, 100"
  })
  void power(String base, String exponent, String expected) {
    assertNumber(expected, calculator.power(n(base), n(exponent)));
  }

  @ParameterizedTest(name = "{0} ^ {1} lança erro")
  @CsvSource({"0, -1", "0, -0.5", "-8, 0.5", "2, 10000", "2, -10000", "1e300, 1.5", "1e-300, 2.5"})
  void powerUndefinedOrOutOfRange(String base, String exponent) {
    assertThrows(ArithmeticError.class, () -> calculator.power(n(base), n(exponent)));
  }

  @ParameterizedTest(name = "√{0} = {1}")
  @CsvSource({"25, 5", "0, 0", "2.25, 1.5", "2, 1.414213562373095048801688724209698"})
  void squareRoot(String value, String expected) {
    assertNumber(expected, calculator.squareRoot(n(value)));
  }

  @Test
  void squareRootOfNegative() {
    assertThrows(ArithmeticError.class, () -> calculator.squareRoot(n("-9")));
  }

  @ParameterizedTest(name = "{0}! = {1}")
  @CsvSource({
    "0, 1",
    "1, 1",
    "5, 120",
    "20, 2432902008176640000",
    "21, 51090942171709440000",
    "5.0, 120"
  })
  void factorial(String value, String expected) {
    assertNumber(expected, calculator.factorial(n(value)));
  }

  @ParameterizedTest(name = "{0}! lança erro")
  @ValueSource(strings = {"-1", "4.7", "1001"})
  void factorialInvalid(String value) {
    assertThrows(ArithmeticError.class, () -> calculator.factorial(n(value)));
  }

  @Test
  @DisplayName("1000! é calculado com exatidão (2568 dígitos)")
  void largeFactorial() {
    assertEquals(2568, calculator.factorial(n("1000")).toBigInteger().toString().length());
  }
}
