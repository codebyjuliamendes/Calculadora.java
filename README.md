<div align="center">

# Calculadora em Java

[![CI](https://github.com/codebyjuliamendes/Calculadora.java/actions/workflows/ci.yml/badge.svg)](https://github.com/codebyjuliamendes/Calculadora.java/actions/workflows/ci.yml)
[![Java 21](https://img.shields.io/badge/Java-21-orange.svg?logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Maven](https://img.shields.io/badge/Build-Maven-blue.svg?logo=apache-maven)](https://maven.apache.org/)
[![Licença MIT](https://img.shields.io/badge/Licen%C3%A7a-MIT-yellow.svg)](LICENSE)

Calculadora de linha de comando com aritmética decimal exata e avaliador de expressões com precedência de operadores.

</div>

## Sobre o projeto

Calculadoras feitas com `double` costumam errar contas simples (`0.1 + 0.2 = 0.30000000000000004`) e quebrar com entradas inesperadas. Este projeto mostra como fazer uma calculadora de terminal correta e testável em Java puro:

- a matemática fica em uma camada pura, sem entrada/saída, usando `BigDecimal`;
- um analisador sintático próprio avalia expressões como `(1,5 + 2) ^ 2 - sqrt(16)`;
- a interface (menu interativo e modo de linha de comando) só lê, delega e exibe.

Não há dependências em tempo de execução: o JAR gerado roda só com o JDK.

## Funcionalidades

**Menu interativo** (`java -jar ...` sem argumentos):

| Opção | Operação | Observações |
|---|---|---|
| 1 | Adição | exata até 34 dígitos significativos |
| 2 | Subtração | exata até 34 dígitos significativos |
| 3 | Multiplicação | exata até 34 dígitos significativos |
| 4 | Divisão | 34 dígitos significativos; divisão por zero é recusada |
| 5 | Potenciação | expoente inteiro entre -9999 e 9999, ou fracionário com base ≥ 0 |
| 6 | Raiz quadrada | recusa números negativos |
| 7 | Módulo (resto) | exato; o sinal acompanha o dividendo, como o `%` do Java |
| 8 | Porcentagem | `p% de total`, exata até 34 dígitos significativos |
| 9 | Fatorial | inteiros de 0 a 1000, resultado exato |
| e | Expressão | qualquer expressão da tabela abaixo |
| 0 | Sair | Ctrl+D (Linux/macOS) ou Ctrl+Z + Enter (Windows) também encerram |

**Expressões** (opção `e` do menu ou argumento de linha de comando):

| Elemento | Exemplo | Resultado |
|---|---|---|
| `+ -` | `10 - 4 - 3` | `3` |
| `* / %` (resto) | `2 + 3 * 4` | `14` |
| `^` (potência, associativa à direita) | `2 ^ 3 ^ 2` | `512` |
| `!` (fatorial) | `5!` | `120` |
| `sqrt(x)` ou `√x` | `sqrt(16) + 1` | `5` |
| parênteses | `(2 + 3) * 4` | `20` |
| sinal unário | `-2 ^ 2` / `(-2) ^ 2` | `-4` / `4` |

Precedência, da maior para a menor: `!` → `^` → sinal unário (`-x`) → `* / %` → `+ -`. Isso segue a convenção matemática (`-2² = -4`).

**Entrada de números**: ponto ou vírgula como separador decimal (`4.5` ou `4,5`), independente do idioma do sistema. Separador de milhar, notação científica, `NaN` e `Infinity` são recusados com mensagem clara, e o programa pede o número de novo.

**Exibição**: resultados sem zeros desnecessários (`40`, não `40.0000`). Inteiros com até 34 dígitos aparecem completos; demais valores usam 16 dígitos significativos e, se forem muito grandes ou muito pequenos, notação científica (`1E+40`).

## Como executar

Pré-requisitos: [JDK 21+](https://adoptium.net/) e, para a opção recomendada, [Maven 3.8+](https://maven.apache.org/).

```bash
git clone https://github.com/codebyjuliamendes/Calculadora.java.git
cd Calculadora.java
```

### Com Maven (recomendado)

```bash
mvn package                                    # compila, testa e gera o JAR
java -jar target/calculadora-java-2.0.0.jar    # menu interativo
java -jar target/calculadora-java-2.0.0.jar "2 + 3 * (4 - 1)"   # imprime 11
```

Também é possível rodar sem gerar o JAR: `mvn -q compile exec:java`.

### Sem Maven

```bash
javac -d out $(find src/main/java -name '*.java')
java -cp out com.juliamendes.calculadora.Main
java -cp out com.juliamendes.calculadora.Main "10 / 4"         # imprime 2.5
```

No PowerShell, troque o primeiro comando por:
`javac -d out (Get-ChildItem -Recurse src/main/java -Filter *.java).FullName`.

### Modo de linha de comando

- `java -jar calculadora.jar "<expressão>"`: imprime o resultado e sai com código `0`.
- Em caso de erro (sintaxe ou operação indefinida), a mensagem vai para a saída de erro e o código de saída é `1`.
- Opção desconhecida: código `2`. `--help` mostra o uso.
- Os argumentos são unidos por espaço, então `java -jar calculadora.jar 2 + 3` também funciona. Lembre de pôr a expressão entre aspas quando ela tiver `*`, `(`, `)` ou `!`, que o shell interpretaria. Use `--` antes de uma expressão que comece com `--`.

### Exemplo de sessão

```text
=============================================
            CALCULADORA EM JAVA
        Desenvolvido por Julia Mendes
=============================================

------------- MENU DE OPERAÇÕES -------------
 [ 1 ] Adição (+)
 [ 2 ] Subtração (-)
 [ 3 ] Multiplicação (*)
 [ 4 ] Divisão (/)
 [ 5 ] Potenciação (^)
 [ 6 ] Raiz quadrada (√)
 [ 7 ] Módulo / resto (%)
 [ 8 ] Porcentagem
 [ 9 ] Fatorial (n!)
 [ e ] Avaliar expressão (ex.: 2 + 3 * 4)
 [ 0 ] Sair
---------------------------------------------
Escolha uma opção: 1

--- Adição (+) ---
Primeiro número: 25,5
Segundo número: 14.5
Resultado: 25.5 + 14.5 = 40
```

## Scripts de desenvolvimento

| Comando | O que faz |
|---|---|
| `mvn test` | roda os testes |
| `mvn verify` | testes + formatação (Spotless) + lint (Checkstyle) + cobertura mínima de 85% de linhas (JaCoCo). É o que a CI executa |
| `mvn spotless:apply` | formata o código no padrão google-java-format |
| `mvn checkstyle:check` | só o lint |

O relatório de cobertura fica em `target/site/jacoco/index.html` depois de `mvn verify`.

## Arquitetura

```text
src/main/java/com/juliamendes/calculadora/
├── Main.java                     # ponto de entrada: menu ou modo de linha de comando
├── core/                         # domínio puro, sem E/S
│   ├── Calculator.java           # operações em BigDecimal e suas validações
│   ├── NumberParser.java         # texto do usuário -> BigDecimal (ponto ou vírgula)
│   ├── NumberFormatter.java      # BigDecimal -> texto para exibição
│   ├── CalculatorException.java  # erro esperado, com mensagem para o usuário
│   └── ArithmeticError.java      # operação indefinida (divisão por zero etc.)
├── expression/                   # avaliador de expressões
│   ├── Lexer.java                # texto -> tokens
│   ├── Parser.java               # tokens -> árvore (descendente recursivo)
│   ├── Node.java                 # árvore sintática (sealed interface + records)
│   ├── ExpressionEvaluator.java  # API pública: evaluate(String) -> BigDecimal
│   └── ExpressionSyntaxException.java  # erro de sintaxe com posição
└── cli/
    ├── InteractiveSession.java   # menu; lê de BufferedReader e escreve em PrintStream
    └── Operation.java            # opções 1 a 9 do menu
```

Decisões:

- **`BigDecimal` em vez de `double`**: todas as operações usam `MathContext.DECIMAL128` (34 dígitos significativos). Soma, subtração, multiplicação e porcentagem são exatas enquanto o resultado couber nesses 34 dígitos (`0.1 + 0.2 = 0.3`); módulo e fatorial são sempre exatos; divisão, raiz e potência são arredondadas. Limitar a precisão também impede que algo como `(10^9999)^9999 + 1` crie um número com milhões de dígitos. Só a potência com expoente fracionário passa por `Math.pow`, porque o JDK não tem essa operação em `BigDecimal`; nesse caso a precisão é a de um `double` (cerca de 15 dígitos).
- **Analisador próprio em vez de `ScriptEngine`/bibliotecas**: o JDK 21 não traz motor de script, e um analisador descendente recursivo de menos de 200 linhas deixa precedência e mensagens de erro sob controle, sem dependências.
- **Limites explícitos** para evitar travamentos ou estouro de memória: expressão com até 1000 caracteres e 200 níveis de aninhamento, número digitado com até 100 caracteres, expoente inteiro até 9999 em módulo, fatorial até 1000, e no módulo o dividendo pode ter no máximo 10000 ordens de grandeza a mais que o divisor.
- **E/S injetável**: `InteractiveSession` recebe `BufferedReader` e `PrintStream`, então o menu é testado sem console real. O fim da entrada encerra a sessão em vez de entrar em laço ou lançar exceção.
- **Independente de localidade**: a leitura aceita ponto e vírgula e a saída sempre usa ponto, em qualquer configuração regional.

## Testes

JUnit 5 com testes parametrizados (`@CsvSource`, `@ValueSource`), cerca de 210 casos:

- `CalculatorTest`: cada operação, incluindo casos de borda (divisão e módulo por zero, raiz de negativo, `0 ^ -1`, base negativa com expoente fracionário, fatorial fracionário, negativo e acima do limite, `0.1 + 0.2`).
- `ExpressionEvaluatorTest`: precedência, associatividade, sinal unário, fatorial, raiz, vírgula decimal, expressões mal formadas com posição do erro, aninhamento excessivo.
- `NumberParserTest` e `NumberFormatterTest`: entradas aceitas e recusadas, formato de exibição.
- `InteractiveSessionTest` e `MainTest`: fluxos completos do menu e do modo de linha de comando, incluindo entrada inválida, fim de entrada e códigos de saída.

A CI (GitHub Actions) roda `mvn verify` e um teste rápido do JAR em todo push na `master` e em todo pull request.

## Limitações

- Resultados com mais de 34 dígitos significativos são arredondados (exceto fatorial e módulo).
- A potência com expoente fracionário tem precisão de `double` (cerca de 15 dígitos).
- Não há funções trigonométricas, logaritmos, constantes (`pi`, `e`) nem memória/histórico.
- Em `%`, o resto segue o sinal do dividendo (`-10 % 3 = -1`), e não o módulo matemático.
- Em terminais configurados com codificação não UTF-8, caracteres como `√` e acentos podem aparecer trocados. A conta não é afetada; no Windows, `chcp 65001` resolve.

## Contribuição

1. Faça um fork do projeto.
2. Crie uma branch (`git checkout -b feature/minha-funcionalidade`).
3. Rode `mvn spotless:apply verify` antes de commitar.
4. Abra um pull request.

## Licença

Distribuído sob a licença MIT. Veja [LICENSE](LICENSE).

---

<div align="center">

Desenvolvido por **[Julia Mendes](https://github.com/codebyjuliamendes)**

</div>
