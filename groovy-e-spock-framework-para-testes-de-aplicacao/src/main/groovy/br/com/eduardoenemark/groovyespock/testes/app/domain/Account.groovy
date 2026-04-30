package br.com.eduardoenemark.groovyespock.testes.app.domain

import groovy.transform.ToString

@ToString(includeNames = true, includePackage = false)
class Account {

    String numero
    String titular
    double saldo
    double taxaJuros
    List<String> extrato

    Account(String numero, String titular, double saldo, double taxaJuros) {
        this.numero = numero
        this.titular = titular
        this.saldo = saldo
        this.taxaJuros = taxaJuros
        this.extrato = []
        this.extrato.add('Conta criada - Saldo inicial: R$ ' + formatarMoeda(saldo))
    }

    void depositar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException('Valor do deposito deve ser positivo')
        }
        saldo += valor
        extrato.add('Deposito: R$ ' + formatarMoeda(valor) + ' - Saldo: R$ ' + formatarMoeda(saldo))
    }

    void sacar(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException('Valor do saque deve ser positivo')
        }
        if (valor > saldo) {
            throw new IllegalArgumentException('Saldo insuficiente para saque de R$ ' + formatarMoeda(valor))
        }
        saldo -= valor
        extrato.add('Saque: R$ ' + formatarMoeda(valor) + ' - Saldo: R$ ' + formatarMoeda(saldo))
    }

    double calcularJuros() {
        return saldo * (taxaJuros / 100)
    }

    void aplicarJuros() {
        double juros = calcularJuros()
        saldo += juros
        extrato.add('Juros aplicados: R$ ' + formatarMoeda(juros) + ' - Saldo: R$ ' + formatarMoeda(saldo))
    }

    double getBalanco() {
        return saldo
    }

    boolean temSaldo() {
        return saldo > 0
    }

    boolean isAtiva() {
        return numero && !numero.isEmpty()
    }

    Map<String, Object> getMetadados() {
        return [:]
    }

    private static String formatarMoeda(double valor) {
        String.format('%,.2f', valor)
    }
}
