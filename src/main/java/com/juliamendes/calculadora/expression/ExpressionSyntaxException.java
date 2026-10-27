package com.juliamendes.calculadora.expression;

import com.juliamendes.calculadora.core.CalculatorException;

/** Expressão mal formada; informa a posição (base 1) em que o problema foi detectado. */
public final class ExpressionSyntaxException extends CalculatorException {

  private static final long serialVersionUID = 1L;

  private final int position;

  /**
   * Cria o erro de sintaxe.
   *
   * @param message descrição do problema
   * @param position posição do problema na expressão, iniciando em 1
   */
  public ExpressionSyntaxException(String message, int position) {
    super(message + " (posição " + position + ")");
    this.position = position;
  }

  /**
   * Posição do problema.
   *
   * @return posição na expressão, iniciando em 1
   */
  public int position() {
    return position;
  }
}
