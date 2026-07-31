import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Calculadora em Java - Versão Standalone (Arquivo Único)
 * 
 * Permite execução direta via:
 *   javac Calculadora.java
 *   java Calculadora
 *
 * @author Julia Mendes
 */
public class Calculadora {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        exibirCabecalho();

        while (executando) {
            exibirMenu();
            System.out.print("Escolha uma opção (0-9): ");

            try {
                int opcao = scanner.nextInt();

                if (opcao == 0) {
                    executando = false;
                    System.out.println("\n=======================================");
                    System.out.println("  Obrigado por usar a Calculadora Java!");
                    System.out.println("            Até a próxima! 👋           ");
                    System.out.println("=======================================\n");
                    break;
                }

                processarOpcao(opcao, scanner);

            } catch (InputMismatchException e) {
                System.out.println("\n⚠️ Entrada inválida! Digite um número correspondente ao menu.\n");
                scanner.nextLine();
            }

            if (executando) {
                System.out.print("\nPressione ENTER para continuar...");
                scanner.nextLine();
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void exibirCabecalho() {
        System.out.println("=============================================");
        System.out.println("         🧮 CALCULADORA EM JAVA              ");
        System.out.println("       Desenvolvido por Julia Mendes         ");
        System.out.println("=============================================");
    }

    private static void exibirMenu() {
        System.out.println("\n------------- MENU DE OPERAÇÕES -------------");
        System.out.println(" [ 1 ] ➕ Adição (+)");
        System.out.println(" [ 2 ] ➖ Subtração (-)");
        System.out.println(" [ 3 ] ✖️ Multiplicação (*)");
        System.out.println(" [ 4 ] ➗ Divisão (/)");
        System.out.println(" [ 5 ] ⚡ Potenciação (^)");
        System.out.println(" [ 6 ] 🧪 Raiz Quadrada (√)");
        System.out.println(" [ 7 ] 🔄 Módulo / Resto (%)");
        System.out.println(" [ 8 ] 📊 Porcentagem (%)");
        System.out.println(" [ 9 ] 🔢 Fatorial (n!)");
        System.out.println(" [ 0 ] ❌ Sair");
        System.out.println("---------------------------------------------");
    }

    private static void processarOpcao(int opcao, Scanner scanner) {
        switch (opcao) {
            case 1: {
                System.out.println("\n--- ➕ ADIÇÃO ---");
                double a = lerNumero(scanner, "Digite o primeiro número: ");
                double b = lerNumero(scanner, "Digite o segundo número: ");
                System.out.printf("Resultado: %.2f + %.2f = %.4f%n", a, b, a + b);
                break;
            }
            case 2: {
                System.out.println("\n--- ➖ SUBTRAÇÃO ---");
                double a = lerNumero(scanner, "Digite o primeiro número: ");
                double b = lerNumero(scanner, "Digite o segundo número: ");
                System.out.printf("Resultado: %.2f - %.2f = %.4f%n", a, b, a - b);
                break;
            }
            case 3: {
                System.out.println("\n--- ✖️ MULTIPLICAÇÃO ---");
                double a = lerNumero(scanner, "Digite o primeiro número: ");
                double b = lerNumero(scanner, "Digite o segundo número: ");
                System.out.printf("Resultado: %.2f * %.2f = %.4f%n", a, b, a * b);
                break;
            }
            case 4: {
                System.out.println("\n--- ➗ DIVISÃO ---");
                double a = lerNumero(scanner, "Digite o dividendo: ");
                double b = lerNumero(scanner, "Digite o divisor: ");
                if (b == 0) {
                    System.out.println("❌ Erro: Divisão por zero não é permitida!");
                } else {
                    System.out.printf("Resultado: %.2f / %.2f = %.4f%n", a, b, a / b);
                }
                break;
            }
            case 5: {
                System.out.println("\n--- ⚡ POTENCIAÇÃO ---");
                double base = lerNumero(scanner, "Digite a base: ");
                double exp = lerNumero(scanner, "Digite o expoente: ");
                System.out.printf("Resultado: %.2f ^ %.2f = %.4f%n", base, exp, Math.pow(base, exp));
                break;
            }
            case 6: {
                System.out.println("\n--- 🧪 RAIZ QUADRADA ---");
                double val = lerNumero(scanner, "Digite o número: ");
                if (val < 0) {
                    System.out.println("❌ Erro: Raiz quadrada de número negativo não é permitida no conjunto dos reais!");
                } else {
                    System.out.printf("Resultado: √%.2f = %.4f%n", val, Math.sqrt(val));
                }
                break;
            }
            case 7: {
                System.out.println("\n--- 🔄 MÓDULO (RESTO) ---");
                double a = lerNumero(scanner, "Digite o dividendo: ");
                double b = lerNumero(scanner, "Digite o divisor: ");
                if (b == 0) {
                    System.out.println("❌ Erro: Módulo por zero não é permitido!");
                } else {
                    System.out.printf("Resultado: %.2f %% %.2f = %.4f%n", a, b, a % b);
                }
                break;
            }
            case 8: {
                System.out.println("\n--- 📊 PORCENTAGEM ---");
                double total = lerNumero(scanner, "Digite o valor total: ");
                double perc = lerNumero(scanner, "Digite a porcentagem (%%): ");
                System.out.printf("Resultado: %.2f%% de %.2f = %.4f%n", perc, total, (total * perc) / 100.0);
                break;
            }
            case 9: {
                System.out.println("\n--- 🔢 FATORIAL ---");
                int n = (int) lerNumero(scanner, "Digite um número inteiro (0 a 20): ");
                if (n < 0) {
                    System.out.println("❌ Erro: Fatorial não é definido para números negativos!");
                } else if (n > 20) {
                    System.out.println("❌ Erro: Fatorial máximo suportado nesta versão é 20!");
                } else {
                    long fat = 1;
                    for (int i = 2; i <= n; i++) fat *= i;
                    System.out.printf("Resultado: %d! = %d%n", n, fat);
                }
                break;
            }
            default:
                System.out.println("\n⚠️ Opção inválida! Escolha um número de 0 a 9.");
        }
    }

    private static double lerNumero(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Entrada inválida! Digite um valor numérico válido.");
                scanner.nextLine();
            }
        }
    }
}
