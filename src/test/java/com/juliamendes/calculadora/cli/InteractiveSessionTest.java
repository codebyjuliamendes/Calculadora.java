package com.juliamendes.calculadora.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class InteractiveSessionTest {

  private static String run(String input) {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
    assertTimeoutPreemptively(
        Duration.ofSeconds(5),
        () -> new InteractiveSession(new BufferedReader(new StringReader(input)), out).run());
    return buffer.toString(StandardCharsets.UTF_8);
  }

  @ParameterizedTest(name = "opção {0}: {1}")
  @CsvSource(
      delimiter = '|',
      value = {
        "1\\n25.5\\n14.5|Resultado: 25.5 + 14.5 = 40",
        "2\\n0.3\\n0.1|Resultado: 0.3 - 0.1 = 0.2",
        "3\\n1,1\\n1,1|Resultado: 1.1 * 1.1 = 1.21",
        "4\\n1\\n3|Resultado: 1 / 3 = 0.3333333333333333",
        "5\\n2\\n10|Resultado: 2 ^ 10 = 1024",
        "6\\n2.25|Resultado: √2.25 = 1.5",
        "7\\n10\\n3|Resultado: 10 % 3 = 1",
        "8\\n200\\n15|Resultado: 15% de 200 = 30",
        "9\\n20|Resultado: 20! = 2432902008176640000",
        "e\\n2 + 3 * 4|Resultado: 14",
        "E\\n(1,5 + 2) ^ 2|Resultado: 12.25"
      })
  void performsOperations(String input, String expected) {
    String output = run(input.replace("\\n", "\n") + "\n0\n");
    assertTrue(output.contains(expected), output);
  }

  @ParameterizedTest(name = "opção {0}: {1}")
  @CsvSource(
      delimiter = '|',
      value = {
        "4\\n10\\n0|Erro: Divisão por zero não é permitida.",
        "7\\n10\\n0|Erro: Módulo por zero não é permitido.",
        "6\\n-9|Erro: Não existe raiz quadrada real de número negativo.",
        "9\\n4.7|Erro: Fatorial só é definido para números inteiros.",
        "9\\n-1|Erro: Fatorial não é definido para números negativos.",
        "e\\n1 / 0|Erro: Divisão por zero não é permitida.",
        "e\\n2 +|Erro: Expressão incompleta (posição 4)"
      })
  void reportsErrorsAndKeepsRunning(String input, String expected) {
    String output = run(input.replace("\\n", "\n") + "\n1\n1\n1\n0\n");
    assertTrue(output.contains(expected), output);
    assertTrue(output.contains("Resultado: 1 + 1 = 2"), "sessão deve continuar após o erro");
  }

  @Test
  void repromptsUntilNumberIsValid() {
    String output = run("1\nabc\nNaN\n2\n3\n0\n");
    assertTrue(output.contains("Erro: Valor inválido: \"abc\""), output);
    assertTrue(output.contains("Erro: Valor inválido: \"NaN\""), output);
    assertTrue(output.contains("Resultado: 2 + 3 = 5"), output);
  }

  @Test
  void rejectsUnknownMenuOption() {
    String output = run("42\nx\n\n0\n");
    assertTrue(output.contains("Opção inválida"), output);
    assertTrue(output.contains("Obrigado por usar a Calculadora Java"), output);
  }

  @Test
  void endOfInputAtMenuExitsCleanly() {
    String output = run("");
    assertTrue(output.contains("Obrigado por usar a Calculadora Java"), output);
  }

  @Test
  void endOfInputDuringOperationExitsCleanly() {
    String output = run("1\n5\n");
    assertTrue(output.contains("Obrigado por usar a Calculadora Java"), output);
    assertFalse(output.contains("Resultado"), output);
  }

  @Test
  void endOfInputDuringExpressionExitsCleanly() {
    String output = run("e\n");
    assertTrue(output.contains("Obrigado por usar a Calculadora Java"), output);
  }

  @Test
  void menuListsAllOptions() {
    String output = run("0\n");
    for (Operation operation : Operation.values()) {
      assertTrue(output.contains("[ " + operation.key() + " ] " + operation.label()), output);
    }
    assertTrue(output.contains("[ e ] Avaliar expressão"), output);
  }
}
