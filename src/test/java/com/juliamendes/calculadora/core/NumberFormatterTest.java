package com.juliamendes.calculadora.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class NumberFormatterTest {

  @ParameterizedTest(name = "{0} -> {1}")
  @CsvSource({
    "0, 0",
    "0.000, 0",
    "15.000, 15",
    "-2.50, -2.5",
    "100, 100",
    "0.3333333333333333333333333333333333, 0.3333333333333333",
    "0.6666666666666666666666666666666667, 0.6666666666666667",
    "1234567890123456, 1234567890123456",
    "12345678901234567, 12345678901234567",
    "1000000000000000000000000000000000, 1000000000000000000000000000000000",
    "10000000000000000000000000000000000, 1E+34",
    "12345678901234567.5, 1.234567890123457E+16",
    "-123.456, -123.456",
    "0.0000000001, 0.0000000001",
    "0.00000000001, 1E-11"
  })
  void formats(String value, String expected) {
    assertEquals(expected, NumberFormatter.format(new BigDecimal(value)));
  }
}
