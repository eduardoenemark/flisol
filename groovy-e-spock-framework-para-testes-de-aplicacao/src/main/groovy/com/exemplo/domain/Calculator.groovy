package com.exemplo.domain

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

    boolean ehPar(int numero) { numero % 2 == 0 }

    double calcularPi() { 3.141592653 as BigDecimal }
}

class Employee {
    String nome
    String cargo
    double salario

    Employee(String nome, String cargo, double salario) {
        this.nome = nome
        this.cargo = cargo
        this.salario = salario
    }

    double aplicarAumento(double percentual) {
        return salario * (1 + percentual / 100)
    }

    @Override
    String toString() {
        return "Employee(nome:'$nome', cargo:'$cargo', salario:$salario)"
    }
}


