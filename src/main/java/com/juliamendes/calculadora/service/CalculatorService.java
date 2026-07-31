package com.juliamendes.calculadora.service;

/**
 * Serviço responsável por realizar as operações matemáticas da calculadora.
 * Fornece métodos para operações básicas e avançadas com validações e tratamento de erros.
 */
public class CalculatorService {

    /**
     * Realiza a soma de dois números.
     */
    public double somar(double a, double b) {
        return a + b;
    }

    /**
     * Realiza a subtração de dois números.
     */
    public double subtrair(double a, double b) {
        return a - b;
    }

    /**
     * Realiza a multiplicação de dois números.
     */
    public double multiplicar(double a, double b) {
        return a * b;
    }

    /**
     * Realiza a divisão de dois números.
     * @throws ArithmeticException se o divisor for zero.
     */
    public double dividir(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Erro: Divisão por zero não é permitida.");
        }
        return a / b;
    }

    /**
     * Realiza a potenciação (base elevada ao expoente).
     */
    public double potencia(double base, double expoente) {
        return Math.pow(base, expoente);
    }

    /**
     * Calcula a raiz quadrada de um número.
     * @throws IllegalArgumentException se o número for negativo.
     */
    public double raizQuadrada(double a) {
        if (a < 0) {
            throw new IllegalArgumentException("Erro: Não é possível calcular a raiz quadrada de um número negativo no conjunto dos reais.");
        }
        return Math.sqrt(a);
    }

    /**
     * Calcula o resto da divisão (módulo) entre dois números.
     * @throws ArithmeticException se o divisor foi zero.
     */
    public double modulo(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Erro: Operação de módulo por zero não é permitida.");
        }
        return a % b;
    }

    /**
     * Calcula a porcentagem de um valor.
     * Exemplo: porcentagem(200, 15) retorna 30 (15% de 200).
     */
    public double porcentagem(double total, double percentual) {
        return (total * percentual) / 100.0;
    }

    /**
     * Calcula o fatorial de um número inteiro não-negativo.
     * @throws IllegalArgumentException se n for negativo.
     */
    public long fatorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Erro: Fatorial não é definido para números negativos.");
        }
        if (n > 20) {
            throw new IllegalArgumentException("Erro: Fatorial suportado até 20 para evitar estouro de memória (overflow).");
        }
        long resultado = 1;
        for (int i = 2; i <= n; i++) {
            resultado *= i;
        }
        return resultado;
    }
}
