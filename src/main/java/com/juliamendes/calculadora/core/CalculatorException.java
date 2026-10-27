package com.juliamendes.calculadora.core;

/**
 * Base de todos os erros esperados da calculadora (entrada inválida, operação indefinida etc.).
 *
 * <p>As mensagens são escritas para o usuário final e podem ser exibidas diretamente na interface.
 */
public class CalculatorException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Cria a exceção com uma mensagem destinada ao usuário.
   *
   * @param message descrição do problema
   */
  public CalculatorException(String message) {
    super(message);
  }

  /**
   * Cria a exceção preservando a causa original.
   *
   * @param message descrição do problema
   * @param cause causa original
   */
  public CalculatorException(String message, Throwable cause) {
    super(message, cause);
  }
}
