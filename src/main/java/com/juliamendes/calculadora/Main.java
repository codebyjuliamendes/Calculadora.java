package com.juliamendes.calculadora;

import com.juliamendes.calculadora.service.CalculatorService;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Classe principal responsável pela interface de linha de comando (CLI) da Calculadora.
 */
public class Main {

    public static void main(String[] args) {
        CalculatorService service = new CalculatorService();
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        exibirBoasVindas();

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

                processarOpcao(opcao, service, scanner);

            } catch (InputMismatchException e) {
                System.out.println("\n⚠️ Entrada inválida! Por favor, digite um número inteiro correspondente ao menu.\n");
                scanner.nextLine(); // Limpa o buffer do scanner
            }

            if (executando) {
                System.out.print("\nPressione ENTER para continuar...");
                scanner.nextLine(); // Consome newline restante
                scanner.nextLine(); // Aguarda ENTER do usuário
            }
        }

        scanner.close();
    }

    private static void exibirBoasVindas() {
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

    private static void processarOpcao(int opcao, CalculatorService service, Scanner scanner) {
        try {
            switch (opcao) {
                case 1: {
                    System.out.println("\n--- ➕ ADIÇÃO ---");
                    double a = lerNumero(scanner, "Digite o primeiro número: ");
                    double b = lerNumero(scanner, "Digite o segundo número: ");
                    double res = service.somar(a, b);
                    System.out.printf("Resultado: %.2f + %.2f = %.4f%n", a, b, res);
                    break;
                }
                case 2: {
                    System.out.println("\n--- ➖ SUBTRAÇÃO ---");
                    double a = lerNumero(scanner, "Digite o primeiro número: ");
                    double b = lerNumero(scanner, "Digite o segundo número: ");
                    double res = service.subtrair(a, b);
                    System.out.printf("Resultado: %.2f - %.2f = %.4f%n", a, b, res);
                    break;
                }
                case 3: {
                    System.out.println("\n--- ✖️ MULTIPLICAÇÃO ---");
                    double a = lerNumero(scanner, "Digite o primeiro número: ");
                    double b = lerNumero(scanner, "Digite o segundo número: ");
                    double res = service.multiplicar(a, b);
                    System.out.printf("Resultado: %.2f * %.2f = %.4f%n", a, b, res);
                    break;
                }
                case 4: {
                    System.out.println("\n--- ➗ DIVISÃO ---");
                    double a = lerNumero(scanner, "Digite o dividendo: ");
                    double b = lerNumero(scanner, "Digite o divisor: ");
                    double res = service.dividir(a, b);
                    System.out.printf("Resultado: %.2f / %.2f = %.4f%n", a, b, res);
                    break;
                }
                case 5: {
                    System.out.println("\n--- ⚡ POTENCIAÇÃO ---");
                    double base = lerNumero(scanner, "Digite a base: ");
                    double exp = lerNumero(scanner, "Digite o expoente: ");
                    double res = service.potencia(base, exp);
                    System.out.printf("Resultado: %.2f ^ %.2f = %.4f%n", base, exp, res);
                    break;
                }
                case 6: {
                    System.out.println("\n--- 🧪 RAIZ QUADRADA ---");
                    double val = lerNumero(scanner, "Digite o número: ");
                    double res = service.raizQuadrada(val);
                    System.out.printf("Resultado: √%.2f = %.4f%n", val, res);
                    break;
                }
                case 7: {
                    System.out.println("\n--- 🔄 MÓDULO (RESTO) ---");
                    double a = lerNumero(scanner, "Digite o dividendo: ");
                    double b = lerNumero(scanner, "Digite o divisor: ");
                    double res = service.modulo(a, b);
                    System.out.printf("Resultado: %.2f %% %.2f = %.4f%n", a, b, res);
                    break;
                }
                case 8: {
                    System.out.println("\n--- 📊 PORCENTAGEM ---");
                    double total = lerNumero(scanner, "Digite o valor total: ");
                    double perc = lerNumero(scanner, "Digite a porcentagem desejada (%%): ");
                    double res = service.porcentagem(total, perc);
                    System.out.printf("Resultado: %.2f%% de %.2f = %.4f%n", perc, total, res);
                    break;
                }
                case 9: {
                    System.out.println("\n--- 🔢 FATORIAL ---");
                    int n = (int) lerNumero(scanner, "Digite um número inteiro (0 a 20): ");
                    long res = service.fatorial(n);
                    System.out.printf("Resultado: %d! = %d%n", n, res);
                    break;
                }
                default:
                    System.out.println("\n⚠️ Opção inválida! Escolha um número de 0 a 9.");
            }
        } catch (ArithmeticException | IllegalArgumentException e) {
            System.out.println("\n❌ " + e.getMessage());
        }
    }

    private static double lerNumero(Scanner scanner, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Entrada inválida! Digite um valor numérico válido (ex: 10 ou 4.5).");
                scanner.nextLine(); // Limpa o valor inválido
            }
        }
    }
}
