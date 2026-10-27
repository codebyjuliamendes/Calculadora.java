package com.juliamendes.calculadora.cli;

import com.juliamendes.calculadora.core.Calculator;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/** Opções numéricas do menu interativo. */
enum Operation {
  ADD("1", "Adição (+)", List.of("Primeiro número", "Segundo número"), "%s + %s") {
    @Override
    BigDecimal apply(Calculator c, List<BigDecimal> args) {
      return binary(c::add, args);
    }
  },
  SUBTRACT("2", "Subtração (-)", List.of("Primeiro número", "Segundo número"), "%s - %s") {
    @Override
    BigDecimal apply(Calculator c, List<BigDecimal> args) {
      return binary(c::subtract, args);
    }
  },
  MULTIPLY("3", "Multiplicação (*)", List.of("Primeiro número", "Segundo número"), "%s * %s") {
    @Override
    BigDecimal apply(Calculator c, List<BigDecimal> args) {
      return binary(c::multiply, args);
    }
  },
  DIVIDE("4", "Divisão (/)", List.of("Dividendo", "Divisor"), "%s / %s") {
    @Override
    BigDecimal apply(Calculator c, List<BigDecimal> args) {
      return binary(c::divide, args);
    }
  },
  POWER("5", "Potenciação (^)", List.of("Base", "Expoente"), "%s ^ %s") {
    @Override
    BigDecimal apply(Calculator c, List<BigDecimal> args) {
      return binary(c::power, args);
    }
  },
  SQUARE_ROOT("6", "Raiz quadrada (√)", List.of("Número"), "√%s") {
    @Override
    BigDecimal apply(Calculator c, List<BigDecimal> args) {
      return unary(c::squareRoot, args);
    }
  },
  REMAINDER("7", "Módulo / resto (%)", List.of("Dividendo", "Divisor"), "%s %% %s") {
    @Override
    BigDecimal apply(Calculator c, List<BigDecimal> args) {
      return binary(c::remainder, args);
    }
  },
  PERCENT("8", "Porcentagem", List.of("Valor total", "Porcentagem"), "%2$s%% de %1$s") {
    @Override
    BigDecimal apply(Calculator c, List<BigDecimal> args) {
      return binary(c::percentOf, args);
    }
  },
  FACTORIAL(
      "9",
      "Fatorial (n!)",
      List.of("Número inteiro (0 a " + Calculator.MAX_FACTORIAL + ")"),
      "%s!") {
    @Override
    BigDecimal apply(Calculator c, List<BigDecimal> args) {
      return unary(c::factorial, args);
    }
  };

  private final String key;
  private final String label;
  private final List<String> prompts;
  private final String template;

  Operation(String key, String label, List<String> prompts, String template) {
    this.key = key;
    this.label = label;
    this.prompts = prompts;
    this.template = template;
  }

  String key() {
    return key;
  }

  String label() {
    return label;
  }

  List<String> prompts() {
    return prompts;
  }

  /** Descreve a conta já com os operandos formatados, ex.: {@code "2 + 3"}. */
  String describe(List<String> formattedArgs) {
    return String.format(template, formattedArgs.toArray());
  }

  abstract BigDecimal apply(Calculator calculator, List<BigDecimal> args);

  static Operation fromKey(String key) {
    for (Operation operation : values()) {
      if (operation.key.equals(key)) {
        return operation;
      }
    }
    return null;
  }

  private static BigDecimal binary(
      BiFunction<BigDecimal, BigDecimal, BigDecimal> fn, List<BigDecimal> args) {
    return fn.apply(args.get(0), args.get(1));
  }

  private static BigDecimal unary(Function<BigDecimal, BigDecimal> fn, List<BigDecimal> args) {
    return fn.apply(args.get(0));
  }
}
