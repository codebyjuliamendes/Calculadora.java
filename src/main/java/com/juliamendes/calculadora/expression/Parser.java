package com.juliamendes.calculadora.expression;

import java.math.BigDecimal;
import java.util.List;

/**
 * Analisador sintático descendente recursivo.
 *
 * <p>Gramática, da menor para a maior precedência:
 *
 * <pre>
 * expression := term (("+" | "-") term)*
 * term       := unary (("*" | "/" | "%") unary)*
 * unary      := ("+" | "-") unary | power
 * power      := postfix ("^" unary)?          (associativa à direita)
 * postfix    := primary "!"*
 * primary    := NUMBER | "(" expression ")" | ("sqrt" | "√") primary
 * </pre>
 *
 * <p>Consequências: {@code 2 + 3 * 4 = 14}, {@code -2 ^ 2 = -4}, {@code 2 ^ 3 ^ 2 = 512} e {@code 2
 * ^ -1 = 0.5}.
 */
final class Parser {

  /** Profundidade máxima de aninhamento, para evitar estouro de pilha. */
  static final int MAX_DEPTH = 200;

  private final List<Token> tokens;
  private int current;
  private int depth;

  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  Node parse() {
    if (peek().type() == TokenType.END) {
      throw new ExpressionSyntaxException("Expressão vazia", peek().position());
    }
    Node node = expression();
    Token next = peek();
    if (next.type() != TokenType.END) {
      throw unexpected(next);
    }
    return node;
  }

  private Node expression() {
    enter();
    Node node = term();
    while (match(TokenType.PLUS, TokenType.MINUS)) {
      TokenType operator = previous().type();
      node = new Node.Binary(operator, node, term());
    }
    leave();
    return node;
  }

  private Node term() {
    Node node = unary();
    while (match(TokenType.STAR, TokenType.SLASH, TokenType.PERCENT)) {
      TokenType operator = previous().type();
      node = new Node.Binary(operator, node, unary());
    }
    return node;
  }

  private Node unary() {
    if (match(TokenType.PLUS, TokenType.MINUS)) {
      TokenType operator = previous().type();
      enter();
      Node operand = unary();
      leave();
      return new Node.Unary(operator, operand);
    }
    return power();
  }

  private Node power() {
    Node base = postfix();
    if (match(TokenType.CARET)) {
      enter();
      Node exponent = unary();
      leave();
      return new Node.Binary(TokenType.CARET, base, exponent);
    }
    return base;
  }

  private Node postfix() {
    Node node = primary();
    while (match(TokenType.BANG)) {
      node = new Node.Factorial(node);
    }
    return node;
  }

  private Node primary() {
    Token token = advance();
    return switch (token.type()) {
      case NUMBER -> new Node.Literal(new BigDecimal(token.text()));
      case LEFT_PAREN -> {
        Node inner = expression();
        Token closing = advance();
        if (closing.type() != TokenType.RIGHT_PAREN) {
          throw new ExpressionSyntaxException(
              "Parêntese aberto na posição " + token.position() + " não foi fechado",
              closing.position());
        }
        yield inner;
      }
      case SQRT -> {
        enter();
        Node operand = primary();
        leave();
        yield new Node.SquareRoot(operand);
      }
      default -> throw unexpected(token);
    };
  }

  private void enter() {
    if (++depth > MAX_DEPTH) {
      throw new ExpressionSyntaxException(
          "Expressão aninhada demais (máximo " + MAX_DEPTH + " níveis)", peek().position());
    }
  }

  private void leave() {
    depth--;
  }

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (peek().type() == type) {
        current++;
        return true;
      }
    }
    return false;
  }

  private Token advance() {
    Token token = peek();
    if (token.type() != TokenType.END) {
      current++;
    }
    return token;
  }

  private Token peek() {
    return tokens.get(current);
  }

  private Token previous() {
    return tokens.get(current - 1);
  }

  private static ExpressionSyntaxException unexpected(Token token) {
    if (token.type() == TokenType.END) {
      return new ExpressionSyntaxException("Expressão incompleta", token.position());
    }
    return new ExpressionSyntaxException(
        "Símbolo inesperado \"" + token.text() + "\"", token.position());
  }
}
