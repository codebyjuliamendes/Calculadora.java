package com.juliamendes.calculadora.cli;

import com.juliamendes.calculadora.core.Calculator;
import com.juliamendes.calculadora.core.CalculatorException;
import com.juliamendes.calculadora.core.NumberFormatter;
import com.juliamendes.calculadora.core.NumberParser;
import com.juliamendes.calculadora.expression.ExpressionEvaluator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Menu interativo de linha de comando.
 *
 * <p>Lê linhas de um {@link BufferedReader} e escreve em um {@link PrintStream}, o que permite
 * testá-lo sem console real. O fim da entrada (Ctrl+D / Ctrl+Z ou pipe encerrado) encerra a sessão
 * de forma limpa.
 */
public final class InteractiveSession {

  static final String EXPRESSION_KEY = "e";
  static final String EXIT_KEY = "0";

  private final BufferedReader in;
  private final PrintStream out;
  private final Calculator calculator;
  private final ExpressionEvaluator evaluator;

  /**
   * Cria a sessão.
   *
   * @param in entrada do usuário
   * @param out saída para o usuário
   */
  public InteractiveSession(BufferedReader in, PrintStream out) {
    this.in = Objects.requireNonNull(in, "in");
    this.out = Objects.requireNonNull(out, "out");
    this.calculator = new Calculator();
    this.evaluator = new ExpressionEvaluator(calculator);
  }

  /** Executa o laço do menu até o usuário sair ou a entrada terminar. */
  public void run() {
    printBanner();
    while (true) {
      printMenu();
      Optional<String> choice = prompt("Escolha uma opção");
      if (choice.isEmpty()) {
        break;
      }
      String key = choice.get().strip().toLowerCase(Locale.ROOT);
      if (key.equals(EXIT_KEY)) {
        break;
      }
      if (!handle(key)) {
        break;
      }
    }
    out.println();
    out.println("Obrigado por usar a Calculadora Java. Até a próxima!");
  }

  /**
   * Trata uma opção do menu.
   *
   * @return {@code false} se a entrada terminou durante a leitura dos operandos
   */
  private boolean handle(String key) {
    if (key.isEmpty()) {
      return true;
    }
    if (key.equals(EXPRESSION_KEY)) {
      return evaluateExpression();
    }
    Operation operation = Operation.fromKey(key);
    if (operation == null) {
      out.println("Opção inválida. Escolha um número de 0 a 9 ou \"e\".");
      return true;
    }
    return runOperation(operation);
  }

  private boolean runOperation(Operation operation) {
    out.println();
    out.println("--- " + operation.label() + " ---");
    List<BigDecimal> args = new ArrayList<>();
    for (String label : operation.prompts()) {
      Optional<BigDecimal> value = readNumber(label);
      if (value.isEmpty()) {
        return false;
      }
      args.add(value.get());
    }
    try {
      BigDecimal result = operation.apply(calculator, args);
      List<String> formatted = args.stream().map(NumberFormatter::format).toList();
      out.println(
          "Resultado: " + operation.describe(formatted) + " = " + NumberFormatter.format(result));
    } catch (CalculatorException e) {
      out.println("Erro: " + e.getMessage());
    } catch (ArithmeticException e) {
      out.println("Erro: resultado fora do intervalo suportado.");
    }
    return true;
  }

  private boolean evaluateExpression() {
    out.println();
    out.println("--- Expressão ---");
    out.println("Exemplos: 2 + 3 * 4   (1,5 + 2) ^ 2   sqrt(16) + 5!   10 % 3");
    Optional<String> line = prompt("Expressão");
    if (line.isEmpty()) {
      return false;
    }
    try {
      out.println("Resultado: " + NumberFormatter.format(evaluator.evaluate(line.get())));
    } catch (CalculatorException e) {
      out.println("Erro: " + e.getMessage());
    }
    return true;
  }

  private Optional<BigDecimal> readNumber(String label) {
    while (true) {
      Optional<String> line = prompt(label);
      if (line.isEmpty()) {
        return Optional.empty();
      }
      try {
        return Optional.of(NumberParser.parse(line.get()));
      } catch (CalculatorException e) {
        out.println("Erro: " + e.getMessage());
      }
    }
  }

  private Optional<String> prompt(String label) {
    out.print(label + ": ");
    out.flush();
    try {
      return Optional.ofNullable(in.readLine());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void printBanner() {
    out.println("=============================================");
    out.println("            CALCULADORA EM JAVA");
    out.println("        Desenvolvido por Julia Mendes");
    out.println("=============================================");
  }

  private void printMenu() {
    out.println();
    out.println("------------- MENU DE OPERAÇÕES -------------");
    for (Operation operation : Operation.values()) {
      out.println(" [ " + operation.key() + " ] " + operation.label());
    }
    out.println(" [ e ] Avaliar expressão (ex.: 2 + 3 * 4)");
    out.println(" [ 0 ] Sair");
    out.println("---------------------------------------------");
  }
}
