package com.juliamendes.calculadora.expression;

import java.math.BigDecimal;

/** Árvore sintática de uma expressão. */
sealed interface Node {

  /** Literal numérico. */
  record Literal(BigDecimal value) implements Node {}

  /** Negação ({@code -x}) ou identidade ({@code +x}). */
  record Unary(TokenType operator, Node operand) implements Node {}

  /** Operação binária ({@code + - * / % ^}). */
  record Binary(TokenType operator, Node left, Node right) implements Node {}

  /** Fatorial pós-fixo ({@code x!}). */
  record Factorial(Node operand) implements Node {}

  /** Raiz quadrada ({@code sqrt(x)} ou {@code √x}). */
  record SquareRoot(Node operand) implements Node {}
}
