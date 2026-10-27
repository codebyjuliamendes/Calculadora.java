package com.juliamendes.calculadora.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Quebra uma expressão em {@link Token}s. */
final class Lexer {

  private final String source;
  private int index;

  Lexer(String source) {
    this.source = source;
  }

  List<Token> tokenize() {
    List<Token> tokens = new ArrayList<>();
    while (index < source.length()) {
      char c = source.charAt(index);
      if (Character.isWhitespace(c)) {
        index++;
      } else if (isDigit(c) || isDecimalSeparator(c)) {
        tokens.add(number());
      } else if (Character.isLetter(c)) {
        tokens.add(word());
      } else {
        tokens.add(symbol(c));
      }
    }
    tokens.add(new Token(TokenType.END, "", source.length() + 1));
    return tokens;
  }

  private Token number() {
    int start = index;
    boolean separatorSeen = false;
    while (index < source.length()) {
      char c = source.charAt(index);
      if (isDigit(c)) {
        index++;
      } else if (isDecimalSeparator(c) && !separatorSeen) {
        separatorSeen = true;
        index++;
      } else {
        break;
      }
    }
    String text = source.substring(start, index);
    if (text.length() == 1 && separatorSeen) {
      throw new ExpressionSyntaxException("Número inválido \"" + text + "\"", start + 1);
    }
    if (index < source.length() && isDecimalSeparator(source.charAt(index))) {
      throw new ExpressionSyntaxException("Número com mais de um separador decimal", index + 1);
    }
    return new Token(TokenType.NUMBER, text.replace(',', '.'), start + 1);
  }

  private Token word() {
    int start = index;
    while (index < source.length() && Character.isLetter(source.charAt(index))) {
      index++;
    }
    String text = source.substring(start, index);
    if (text.toLowerCase(Locale.ROOT).equals("sqrt")) {
      return new Token(TokenType.SQRT, text, start + 1);
    }
    throw new ExpressionSyntaxException("Função ou nome desconhecido \"" + text + "\"", start + 1);
  }

  private Token symbol(char c) {
    int position = index + 1;
    TokenType type =
        switch (c) {
          case '+' -> TokenType.PLUS;
          case '-', '−' -> TokenType.MINUS;
          case '*', '×' -> TokenType.STAR;
          case '/', '÷' -> TokenType.SLASH;
          case '%' -> TokenType.PERCENT;
          case '^' -> TokenType.CARET;
          case '!' -> TokenType.BANG;
          case '√' -> TokenType.SQRT;
          case '(' -> TokenType.LEFT_PAREN;
          case ')' -> TokenType.RIGHT_PAREN;
          default ->
              throw new ExpressionSyntaxException("Caractere inválido '" + c + "'", position);
        };
    index++;
    return new Token(type, String.valueOf(c), position);
  }

  private static boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private static boolean isDecimalSeparator(char c) {
    return c == '.' || c == ',';
  }
}
