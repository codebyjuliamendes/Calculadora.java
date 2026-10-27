package com.juliamendes.calculadora.core;

/** Operação matematicamente indefinida ou fora dos limites suportados (ex.: divisão por zero). */
public final class ArithmeticError extends CalculatorException {

  private static final long serialVersionUID = 1L;

  /**
   * Cria o erro com uma mensagem destinada ao usuário.
   *
   * @param message descrição do problema
   */
  public ArithmeticError(String message) {
    super(message);
  }
}
