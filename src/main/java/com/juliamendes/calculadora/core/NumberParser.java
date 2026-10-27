package com.juliamendes.calculadora.core;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Converte texto digitado pelo usuário em {@link BigDecimal}, independente da localidade.
 *
 * <p>Aceita sinal opcional e ponto ou vírgula como separador decimal ({@code 4.5}, {@code 4,5},
 * {@code -0.25}, {@code .5}). Rejeita separadores de milhar, notação científica, {@code NaN} e
 * {@code Infinity}.
 */
public final class NumberParser {

  /** Tamanho máximo aceito para um número digitado. */
  public static final int MAX_LENGTH = 100;

  private static final Pattern NUMBER = Pattern.compile("[+-]?(\\d+([.,]\\d*)?|[.,]\\d+)");

  private NumberParser() {}

  /**
   * Converte o texto em número.
   *
   * @param text texto digitado (espaços nas pontas são ignorados)
   * @return número correspondente
   * @throws CalculatorException se o texto não for um número válido
   */
  public static BigDecimal parse(String text) {
    String trimmed = text == null ? "" : text.strip();
    if (trimmed.isEmpty()) {
      throw new CalculatorException("Nenhum número informado.");
    }
    if (trimmed.length() > MAX_LENGTH) {
      throw new CalculatorException(
          "Número muito longo (máximo de " + MAX_LENGTH + " caracteres).");
    }
    if (!NUMBER.matcher(trimmed).matches()) {
      throw new CalculatorException(
          "Valor inválido: \"" + trimmed + "\". Use, por exemplo, 10, -3 ou 4.5.");
    }
    return new BigDecimal(trimmed.replace(',', '.'));
  }
}
