package br.com.eduardoenemark.groovyespock.testes.app.domain

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


