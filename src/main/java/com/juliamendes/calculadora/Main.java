package com.juliamendes.calculadora;

import com.juliamendes.calculadora.cli.InteractiveSession;
import com.juliamendes.calculadora.core.CalculatorException;
import com.juliamendes.calculadora.core.NumberFormatter;
import com.juliamendes.calculadora.expression.ExpressionEvaluator;
import java.io.BufferedReader;
import java.io.Console;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Ponto de entrada.
 *
 * <ul>
 *   <li>Sem argumentos: abre o menu interativo.
 *   <li>Com argumentos: avalia a expressão (argumentos unidos por espaço), imprime o resultado e
 *       termina com código 0, ou imprime o erro em stderr e termina com código 1.
 *   <li>{@code --help}: mostra o uso.
 * </ul>
 */
public final class Main {

  /** Código de saída para sucesso. */
  static final int EXIT_OK = 0;

  /** Código de saída para expressão inválida ou erro de cálculo. */
  static final int EXIT_ERROR = 1;

  /** Código de saída para uso incorreto da linha de comando. */
  static final int EXIT_USAGE = 2;

  private Main() {}

  /**
   * Inicia a aplicação.
   *
   * @param args argumentos de linha de comando
   */
  public static void main(String[] args) {
    Charset charset = consoleCharset();
    PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out), true, charset);
    PrintStream err = new PrintStream(new FileOutputStream(FileDescriptor.err), true, charset);
    int code;
    if (args.length == 0) {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in, inputCharset()));
      new InteractiveSession(in, out).run();
      code = EXIT_OK;
    } else {
      code = run(args, out, err);
    }
    out.flush();
    err.flush();
    if (code != EXIT_OK) {
      System.exit(code);
    }
  }

  /**
   * Executa o modo não interativo.
   *
   * @param args argumentos de linha de comando (não vazio)
   * @param out saída padrão
   * @param err saída de erro
   * @return código de saída
   */
  static int run(String[] args, PrintStream out, PrintStream err) {
    if (args.length == 1 && (args[0].equals("--help") || args[0].equals("-h"))) {
      printUsage(out);
      return EXIT_OK;
    }
    String[] expressionArgs = args;
    if (args[0].equals("--")) {
      expressionArgs = Arrays.copyOfRange(args, 1, args.length);
      if (expressionArgs.length == 0) {
        printUsage(err);
        return EXIT_USAGE;
      }
    } else if (args[0].startsWith("--")) {
      err.println("Opção desconhecida: " + args[0]);
      printUsage(err);
      return EXIT_USAGE;
    }
    try {
      out.println(
          NumberFormatter.format(
              new ExpressionEvaluator().evaluate(String.join(" ", expressionArgs))));
      return EXIT_OK;
    } catch (CalculatorException e) {
      err.println("Erro: " + e.getMessage());
      return EXIT_ERROR;
    }
  }

  private static void printUsage(PrintStream stream) {
    stream.println("Uso:");
    stream.println("  java -jar calculadora-java.jar               abre o menu interativo");
    stream.println("  java -jar calculadora-java.jar \"<expressão>\" avalia e imprime o resultado");
    stream.println();
    stream.println("Operadores: + - * / % (resto) ^ (potência) ! (fatorial) sqrt(x) e parênteses.");
    stream.println("Exemplo: java -jar calculadora-java.jar \"(1.5 + 2) ^ 2 - sqrt(16)\"");
  }

  private static Charset inputCharset() {
    Console console = System.console();
    return console == null ? Charset.defaultCharset() : console.charset();
  }

  private static Charset consoleCharset() {
    String encoding = System.getProperty("stdout.encoding");
    try {
      return encoding == null ? Charset.defaultCharset() : Charset.forName(encoding);
    } catch (IllegalArgumentException e) {
      return Charset.defaultCharset();
    }
  }
}
