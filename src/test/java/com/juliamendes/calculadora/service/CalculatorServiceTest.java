package com.juliamendes.calculadora.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários da CalculatorService")
class CalculatorServiceTest {

    private CalculatorService service;

    @BeforeEach
    void setUp() {
        service = new CalculatorService();
    }

    @Test
    @DisplayName("Deve realizar a soma de dois números positivos")
    void testSomarPositivos() {
        assertEquals(15.0, service.somar(10.0, 5.0), 0.0001);
    }

    @Test
    @DisplayName("Deve realizar a soma com números negativos")
    void testSomarNegativos() {
        assertEquals(-5.0, service.somar(-10.0, 5.0), 0.0001);
    }

    @Test
    @DisplayName("Deve realizar a subtração de dois números")
    void testSubtrair() {
        assertEquals(5.0, service.subtrair(10.0, 5.0), 0.0001);
    }

    @Test
    @DisplayName("Deve realizar a multiplicação de dois números")
    void testMultiplicar() {
        assertEquals(50.0, service.multiplicar(10.0, 5.0), 0.0001);
    }

    @Test
    @DisplayName("Deve realizar a divisão exata entre dois números")
    void testDividir() {
        assertEquals(2.0, service.dividir(10.0, 5.0), 0.0001);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar dividir por zero")
    void testDividirPorZero() {
        ArithmeticException exception = assertThrows(ArithmeticException.class, () -> service.dividir(10.0, 0));
        assertTrue(exception.getMessage().contains("Divisão por zero"));
    }

    @Test
    @DisplayName("Deve calcular a potenciação corretamente")
    void testPotencia() {
        assertEquals(8.0, service.potencia(2.0, 3.0), 0.0001);
    }

    @Test
    @DisplayName("Deve calcular a raiz quadrada de número positivo")
    void testRaizQuadrada() {
        assertEquals(5.0, service.raizQuadrada(25.0), 0.0001);
    }

    @Test
    @DisplayName("Deve lançar exceção ao calcular raiz quadrada de número negativo")
    void testRaizQuadradaNegativo() {
        assertThrows(IllegalArgumentException.class, () -> service.raizQuadrada(-9.0));
    }

    @Test
    @DisplayName("Deve calcular o módulo (resto da divisão) corretamente")
    void testModulo() {
        assertEquals(1.0, service.modulo(10.0, 3.0), 0.0001);
    }

    @Test
    @DisplayName("Deve calcular a porcentagem corretamente")
    void testPorcentagem() {
        assertEquals(30.0, service.porcentagem(200.0, 15.0), 0.0001);
    }

    @Test
    @DisplayName("Deve calcular o fatorial de números válidos")
    void testFatorial() {
        assertEquals(1, service.fatorial(0));
        assertEquals(1, service.fatorial(1));
        assertEquals(120, service.fatorial(5));
    }

    @Test
    @DisplayName("Deve lançar exceção para fatorial de número negativo")
    void testFatorialNegativo() {
        assertThrows(IllegalArgumentException.class, () -> service.fatorial(-1));
    }
}
