package com.juliamendes.calculadora.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Objects;

/**
 * Operações aritméticas puras, sem dependência de entrada/saída.
 *
 * <p>Todos os cálculos usam {@link BigDecimal} com {@link #PRECISION} (34 dígitos significativos).
 * Soma, subtração, multiplicação e porcentagem são exatas sempre que o resultado cabe nesses 34
 * dígitos (por exemplo, {@code 0.1 + 0.2 = 0.3}); o módulo e o fatorial são sempre exatos. Divisão,
 * raiz quadrada e potências são arredondadas para {@link #PRECISION}. O limite de precisão também
 * impede que expressões como {@code (10^9999)^9999 + 1} gerem números com milhões de dígitos.
 *
 * <p>A classe é imutável e segura para uso concorrente.
 */
public final class Calculator {

  /** Contexto de precisão usado em operações que não têm resultado exato finito. */
  public static final MathContext PRECISION = MathContext.DECIMAL128;

  /** Maior valor absoluto aceito como expoente inteiro em {@link #power}. */
  public static final int MAX_INTEGER_EXPONENT = 9_999;

  /**
   * Maior diferença de ordem de grandeza aceita entre dividendo e divisor em {@link #remainder}.
   */
  public static final int MAX_REMAINDER_MAGNITUDE_GAP = 10_000;

  /** Maior argumento aceito em {@link #factorial}. */
  public static final int MAX_FACTORIAL = 1_000;

  private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

  /**
   * Soma dois números.
   *
   * @param a primeira parcela
   * @param b segunda parcela
   * @return {@code a + b}, arredondado para {@link #PRECISION}
   */
  public BigDecimal add(BigDecimal a, BigDecimal b) {
    return requireValue(a).add(requireValue(b), PRECISION);
  }

  /**
   * Subtrai dois números.
   *
   * @param a minuendo
   * @param b subtraendo
   * @return {@code a - b}, arredondado para {@link #PRECISION}
   */
  public BigDecimal subtract(BigDecimal a, BigDecimal b) {
    return requireValue(a).subtract(requireValue(b), PRECISION);
  }

  /**
   * Multiplica dois números.
   *
   * @param a primeiro fator
   * @param b segundo fator
   * @return {@code a * b}, arredondado para {@link #PRECISION}
   */
  public BigDecimal multiply(BigDecimal a, BigDecimal b) {
    return requireValue(a).multiply(requireValue(b), PRECISION);
  }

  /**
   * Divide dois números.
   *
   * @param dividend dividendo
   * @param divisor divisor
   * @return {@code dividend / divisor}, arredondado para {@link #PRECISION}
   * @throws ArithmeticError se o divisor for zero
   */
  public BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
    requireValue(dividend);
    if (requireValue(divisor).signum() == 0) {
      throw new ArithmeticError("Divisão por zero não é permitida.");
    }
    return dividend.divide(divisor, PRECISION);
  }

  /**
   * Resto da divisão, com o mesmo sinal do dividendo (semântica do operador {@code %} do Java).
   *
   * @param dividend dividendo
   * @param divisor divisor
   * @return resto de {@code dividend / divisor}, exato
   * @throws ArithmeticError se o divisor for zero ou muitas ordens de grandeza menor que o
   *     dividendo
   */
  public BigDecimal remainder(BigDecimal dividend, BigDecimal divisor) {
    requireValue(dividend);
    if (requireValue(divisor).signum() == 0) {
      throw new ArithmeticError("Módulo por zero não é permitido.");
    }
    if (dividend.signum() != 0
        && magnitude(dividend) - magnitude(divisor) > MAX_REMAINDER_MAGNITUDE_GAP) {
      throw new ArithmeticError("Dividendo grande demais em relação ao divisor para o módulo.");
    }
    return dividend.remainder(divisor);
  }

  /**
   * Calcula {@code percent}% de {@code total}.
   *
   * @param total valor de referência
   * @param percent percentual (ex.: {@code 15} para 15%)
   * @return {@code total * percent / 100}, arredondado para {@link #PRECISION}
   */
  public BigDecimal percentOf(BigDecimal total, BigDecimal percent) {
    return multiply(total, percent).divide(ONE_HUNDRED, PRECISION);
  }

  /**
   * Eleva {@code base} a {@code exponent}.
   *
   * <p>Expoentes inteiros (até {@link #MAX_INTEGER_EXPONENT} em módulo) são calculados em {@link
   * BigDecimal}. Expoentes fracionários exigem base não negativa e são calculados em {@code
   * double}, portanto têm cerca de 15 dígitos significativos de precisão.
   *
   * @param base base
   * @param exponent expoente
   * @return {@code base ^ exponent}
   * @throws ArithmeticError se o resultado for indefinido (ex.: {@code 0 ^ -1}) ou não real
   */
  public BigDecimal power(BigDecimal base, BigDecimal exponent) {
    requireValue(base);
    requireValue(exponent);
    if (isInteger(exponent)) {
      return integerPower(base, exponent);
    }
    if (base.signum() < 0) {
      throw new ArithmeticError(
          "Potência de base negativa com expoente fracionário não é um número real.");
    }
    if (base.signum() == 0) {
      if (exponent.signum() < 0) {
        throw new ArithmeticError("Zero elevado a expoente negativo é indefinido.");
      }
      return BigDecimal.ZERO;
    }
    double result = Math.pow(base.doubleValue(), exponent.doubleValue());
    if (Double.isNaN(result) || Double.isInfinite(result) || result == 0.0) {
      throw new ArithmeticError("Resultado da potência está fora do intervalo suportado.");
    }
    return new BigDecimal(result, MathContext.DECIMAL64);
  }

  /**
   * Raiz quadrada.
   *
   * @param value radicando
   * @return raiz quadrada, arredondada para {@link #PRECISION}
   * @throws ArithmeticError se o valor for negativo
   */
  public BigDecimal squareRoot(BigDecimal value) {
    if (requireValue(value).signum() < 0) {
      throw new ArithmeticError("Não existe raiz quadrada real de número negativo.");
    }
    return value.sqrt(PRECISION);
  }

  /**
   * Fatorial de um inteiro não negativo.
   *
   * @param value inteiro entre 0 e {@link #MAX_FACTORIAL}
   * @return {@code value!}, exato
   * @throws ArithmeticError se o valor for negativo, fracionário ou maior que o limite
   */
  public BigDecimal factorial(BigDecimal value) {
    requireValue(value);
    if (!isInteger(value)) {
      throw new ArithmeticError("Fatorial só é definido para números inteiros.");
    }
    if (value.signum() < 0) {
      throw new ArithmeticError("Fatorial não é definido para números negativos.");
    }
    if (value.compareTo(BigDecimal.valueOf(MAX_FACTORIAL)) > 0) {
      throw new ArithmeticError("Fatorial suportado apenas até " + MAX_FACTORIAL + ".");
    }
    int n = value.intValueExact();
    BigInteger result = BigInteger.ONE;
    for (int i = 2; i <= n; i++) {
      result = result.multiply(BigInteger.valueOf(i));
    }
    return new BigDecimal(result);
  }

  private static BigDecimal integerPower(BigDecimal base, BigDecimal exponent) {
    if (exponent.abs().compareTo(BigDecimal.valueOf(MAX_INTEGER_EXPONENT)) > 0) {
      throw new ArithmeticError(
          "Expoente inteiro suportado apenas entre -"
              + MAX_INTEGER_EXPONENT
              + " e "
              + MAX_INTEGER_EXPONENT
              + ".");
    }
    int n = exponent.intValueExact();
    if (base.signum() == 0 && n < 0) {
      throw new ArithmeticError("Zero elevado a expoente negativo é indefinido.");
    }
    try {
      if (n >= 0) {
        return base.pow(n, PRECISION);
      }
      return BigDecimal.ONE.divide(base.pow(-n, PRECISION), PRECISION);
    } catch (ArithmeticException e) {
      throw new ArithmeticError("Resultado da potência está fora do intervalo suportado.");
    }
  }

  /** Ordem de grandeza aproximada (quantidade de dígitos da parte inteira). */
  private static long magnitude(BigDecimal value) {
    return (long) value.precision() - value.scale();
  }

  private static boolean isInteger(BigDecimal value) {
    return value.signum() == 0 || value.stripTrailingZeros().scale() <= 0;
  }

  private static BigDecimal requireValue(BigDecimal value) {
    return Objects.requireNonNull(value, "value");
  }
}
