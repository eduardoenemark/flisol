package br.com.eduardoenemark.groovyespock.testes.app.domain

class Calculator {

    int calcular(int a, int b, String op) {
        switch (op) {
            case '+': return a + b
            case '-': return a - b
            case '*': return a * b
            case '/':
                if (b == 0) throw new IllegalArgumentException('Divis\u00e3o por zero')
                return a / b
            default:
                throw new IllegalArgumentException("Operacao inv\u00e1lida: $op")
        }
    }

    boolean isPar(int numero) { numero % 2 == 0 }

    double calcularPi() { 3.141592653 as BigDecimal }
}



