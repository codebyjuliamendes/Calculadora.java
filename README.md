<div align="center">

# 🧮 Calculadora em Java

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg?style=for-the-badge&logo=openjdk)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-Build-blue.svg?style=for-the-badge&logo=apache-maven)](https://maven.apache.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5-green.svg?style=for-the-badge&logo=junit5)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](LICENSE)

*Uma aplicação de calculadora interativa, modular e orientada a objetos desenvolvida em Java com cobertura de testes unitários.*

---

</div>

## 📌 Sobre o Projeto

A **Calculadora em Java** é um projeto desenvolvido para demonstrar boas práticas de programação em Java, incluindo a separação de responsabilidades (Arquitetura em Camadas / Service-CLI), tratamento rigoroso de exceções e testes unitários automatizados com JUnit 5.

O projeto oferece suporte tanto para compilação rápida em arquivo único quanto para execução modularizada utilizando **Apache Maven**.

---

## ✨ Funcionalidades

- [x] **Adição** (`+`) e **Subtração** (`-`)
- [x] **Multiplicação** (`*`) e **Divisão** (`/`) com tratamento contra divisão por zero
- [x] **Potenciação** (`^`) e **Raiz Quadrada** (`√`)
- [x] **Módulo / Resto da Divisão** (`%`)
- [x] **Cálculo de Porcentagem** (`%`)
- [x] **Cálculo de Fatorial** (`n!`)
- [x] **Interface CLI Interativa** com menu intuitivo e limpo
- [x] **Tratamento de Exceções**: Prevenção contra erros de entrada de dados, divisão por zero e números negativos em raiz quadrada.
- [x] **Testes Automatizados**: Cobertura de testes unitários para a camada de serviço.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java (JDK 17 ou superior)
- **Gerenciador de Dependências e Build**: Apache Maven
- **Framework de Testes**: JUnit 5 (Jupiter)

---

## 📁 Estrutura do Projeto

```text
Calculadora.java/
├── Calculadora.java                         # Versão standalone (arquivo único para execução direta)
├── pom.xml                                  # Configurações do Maven e dependências (JUnit 5)
├── README.md                                # Documentação oficial do repositório
├── .gitignore                               # Regras de exclusão do Git
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── juliamendes/
    │               └── calculadora/
    │                   ├── Main.java        # Interface de linha de comando (CLI)
    │                   └── service/
    │                       └── CalculatorService.java  # Lógica matemática e validações
    └── test/
        └── java/
            └── com/
                └── juliamendes/
                    └── calculadora/
                        └── service/
                            └── CalculatorServiceTest.java  # Testes unitários
```

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

Antes de começar, certifique-se de ter instalado em sua máquina:
- [Java Development Kit (JDK 17+)](https://www.oracle.com/java/technologies/downloads/)
- [Git](https://git-scm.com/)
- [Apache Maven](https://maven.apache.org/) *(opcional para uso via Maven)*

---

### Opção 1: Executando via Maven (Recomendado)

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/codebyjuliamendes/Calculadora.java.git
   cd Calculadora.java
   ```

2. **Compile a aplicação:**
   ```bash
   mvn clean compile
   ```

3. **Execute o projeto:**
   ```bash
   mvn exec:java
   ```

---

### Opção 2: Execução Simples (Sem Maven / Standalone)

Caso deseje executar rapidamente apenas o arquivo `.java` único sem instalar o Maven:

```bash
git clone https://github.com/codebyjuliamendes/Calculadora.java.git
cd Calculadora.java

# Compilar
javac Calculadora.java

# Executar
java Calculadora
```

---

## 🧪 Como Executar os Testes Unitários

Para garantir a estabilidade das operações matemáticas, o projeto conta com suíte de testes unitários com JUnit 5.

Execute o comando abaixo:

```bash
mvn test
```

---

## 💻 Exemplo de Uso na Linha de Comando (CLI)

```text
=============================================
         🧮 CALCULADORA EM JAVA              
       Desenvolvido por Julia Mendes         
=============================================

------------- MENU DE OPERAÇÕES -------------
 [ 1 ] ➕ Adição (+)
 [ 2 ] ➖ Subtração (-)
 [ 3 ] ✖️ Multiplicação (*)
 [ 4 ] ➗ Divisão (/)
 [ 5 ] ⚡ Potenciação (^)
 [ 6 ] 🧪 Raiz Quadrada (√)
 [ 7 ] 🔄 Módulo / Resto (%)
 [ 8 ] 📊 Porcentagem (%)
 [ 9 ] 🔢 Fatorial (n!)
 [ 0 ] ❌ Sair
---------------------------------------------
Escolha uma opção (0-9): 1

--- ➕ ADIÇÃO ---
Digite o primeiro número: 25.5
Digite o segundo número: 14.5
Resultado: 25.50 + 14.50 = 40.0000
```

---

## 🤝 Contribuição

Contribuições são super bem-vindas! Sinta-se à vontade para abrir uma *Issue* ou enviar um *Pull Request*:

1. Faça um **Fork** do projeto
2. Crie uma **Branch** para a sua funcionalidade (`git checkout -b feature/NovaFuncionalidade`)
3. Faça **Commit** das suas alterações (`git commit -m 'Add: Nova funcionalidade de cálculo'`)
4. Faça **Push** para a sua Branch (`git push origin feature/NovaFuncionalidade`)
5. Abra um **Pull Request**

---

## 📄 Licença

Este projeto está sob a licença **MIT** - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

<div align="center">

Desenvolvido com ❤️ por **[Julia Mendes](https://github.com/codebyjuliamendes)**

</div>
