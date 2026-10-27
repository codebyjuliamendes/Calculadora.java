package com.juliamendes.calculadora.core;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Converte resultados em texto legível, independente da localidade do sistema.
 *
 * <p>Usa ponto como separador decimal e remove zeros à direita. Inteiros com até {@link
 * #EXACT_INTEGER_DIGITS} dígitos são exibidos por completo (ex.: {@code 20! =
 * 2432902008176640000}); os demais valores são limitados a {@link #DISPLAY_DIGITS} dígitos
 * significativos, e valores muito grandes ou muito pequenos são exibidos em notação científica
 * (ex.: {@code 1.5E+30}).
 */
public final class NumberFormatter {

  /** Quantidade máxima de dígitos significativos exibidos. */
  public static final int DISPLAY_DIGITS = 16;

  /** Inteiros com até esta quantidade de dígitos são exibidos sem arredondamento. */
  public static final int EXACT_INTEGER_DIGITS = 34;

  private static final MathContext DISPLAY =
      new MathContext(DISPLAY_DIGITS, RoundingMode.HALF_EVEN);
  private static final BigDecimal PLAIN_UPPER = BigDecimal.TEN.pow(DISPLAY_DIGITS);
  private static final BigDecimal PLAIN_LOWER = new BigDecimal("1E-10");

  private NumberFormatter() {}

  /**
   * Formata um número para exibição.
   *
   * @param value número a formatar
   * @return representação textual
   */
  public static String format(BigDecimal value) {
    if (value.signum() == 0) {
      return "0";
    }
    BigDecimal stripped = value.stripTrailingZeros();
    if (stripped.scale() <= 0 && stripped.precision() - stripped.scale() <= EXACT_INTEGER_DIGITS) {
      return stripped.toPlainString();
    }
    BigDecimal rounded = stripped.round(DISPLAY).stripTrailingZeros();
    BigDecimal abs = rounded.abs();
    if (abs.compareTo(PLAIN_UPPER) >= 0 || abs.compareTo(PLAIN_LOWER) < 0) {
      return rounded.toString();
    }
    return rounded.toPlainString();
  }
}
