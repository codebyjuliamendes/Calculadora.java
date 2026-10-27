package com.juliamendes.calculadora.expression;

/** Categorias de símbolos reconhecidos pelo {@link Lexer}. */
enum TokenType {
  NUMBER,
  PLUS,
  MINUS,
  STAR,
  SLASH,
  PERCENT,
  CARET,
  BANG,
  SQRT,
  LEFT_PAREN,
  RIGHT_PAREN,
  END
}
