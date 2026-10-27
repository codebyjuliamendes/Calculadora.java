package com.juliamendes.calculadora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MainTest {

  private final ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
  private final PrintStream out = new PrintStream(outBuffer, true, StandardCharsets.UTF_8);
  private final PrintStream err = new PrintStream(errBuffer, true, StandardCharsets.UTF_8);

  private String out() {
    return outBuffer.toString(StandardCharsets.UTF_8);
  }

  private String err() {
    return errBuffer.toString(StandardCharsets.UTF_8);
  }

  @ParameterizedTest(name = "{0} = {1}")
  @CsvSource(
      delimiter = '|',
      value = {"2+3*4|14", "(1.5 + 2) ^ 2 - sqrt(16)|8.25", "0.1+0.2|0.3"})
  void evaluatesSingleArgument(String expression, String expected) {
    assertEquals(Main.EXIT_OK, Main.run(new String[] {expression}, out, err));
    assertEquals(expected, out().strip());
    assertEquals("", err());
  }

  @Test
  void joinsMultipleArguments() {
    assertEquals(Main.EXIT_OK, Main.run(new String[] {"2", "+", "3"}, out, err));
    assertEquals("5", out().strip());
  }

  @Test
  void reportsCalculationErrorOnStderr() {
    assertEquals(Main.EXIT_ERROR, Main.run(new String[] {"1/0"}, out, err));
    assertEquals("", out());
    assertTrue(err().contains("Divisão por zero"), err());
  }

  @Test
  void reportsSyntaxErrorOnStderr() {
    assertEquals(Main.EXIT_ERROR, Main.run(new String[] {"2 +"}, out, err));
    assertTrue(err().contains("Expressão incompleta"), err());
  }

  @Test
  void negativeNumberIsNotTreatedAsOption() {
    assertEquals(Main.EXIT_OK, Main.run(new String[] {"-5", "+", "2"}, out, err));
    assertEquals("-3", out().strip());
  }

  @Test
  void printsHelp() {
    assertEquals(Main.EXIT_OK, Main.run(new String[] {"--help"}, out, err));
    assertTrue(out().contains("Uso:"), out());
  }

  @Test
  void rejectsUnknownOption() {
    assertEquals(Main.EXIT_USAGE, Main.run(new String[] {"--foo"}, out, err));
    assertTrue(err().contains("Opção desconhecida: --foo"), err());
  }

  @Test
  void doubleDashEndsOptions() {
    assertEquals(Main.EXIT_OK, Main.run(new String[] {"--", "-2", "*", "3"}, out, err));
    assertEquals("-6", out().strip());
  }

  @Test
  void doubleDashAloneIsUsageError() {
    assertEquals(Main.EXIT_USAGE, Main.run(new String[] {"--"}, out, err));
    assertTrue(err().contains("Uso:"), err());
  }
}
