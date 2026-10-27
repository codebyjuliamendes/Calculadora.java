package com.juliamendes.calculadora.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class NumberParserTest {

  @ParameterizedTest(name = "\"{0}\" -> {1}")
  @CsvSource(
      delimiter = '|',
      value = {
        "10|10",
        "-3|-3",
        "+7|7",
        "4.5|4.5",
        "4,5|4.5",
        "  2.25  |2.25",
        ".5|0.5",
        ",5|0.5",
        "3.|3"
      })
  void parsesValidNumbers(String input, String expected) {
    assertEquals(0, new BigDecimal(expected).compareTo(NumberParser.parse(input)));
  }

  @ParameterizedTest(name = "\"{0}\" é inválido")
  @NullAndEmptySource
  @ValueSource(
      strings = {
        "   ",
        "abc",
        "1.000,50",
        "1,2,3",
        "NaN",
        "Infinity",
        "1e5",
        "--1",
        "1 2",
        "0x10",
        ".",
        "-"
      })
  void rejectsInvalidInput(String input) {
    assertThrows(CalculatorException.class, () -> NumberParser.parse(input));
  }

  @Test
  void rejectsOverlyLongInput() {
    assertThrows(CalculatorException.class, () -> NumberParser.parse("9".repeat(101)));
  }
}
