package com.juliamendes.calculadora.expression;

/**
 * Símbolo léxico.
 *
 * @param type categoria
 * @param text texto original
 * @param position posição na expressão, iniciando em 1
 */
record Token(TokenType type, String text, int position) {}
