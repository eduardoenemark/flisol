package br.com.eduardoenemark.groovyespock.testes.app.domain

import spock.lang.Shared
import spock.lang.Unroll
import spock.lang.Specification

class AccountSpec extends Specification {

    @Shared
    def acc = new Account('001', 'Joao Silva', 1000.0, 1.5)

    def setupSpec() {
        println "=== setupSpec: AccountSpec ==="
    }

    def cleanupSpec() {
        println "=== cleanupSpec: AccountSpec ==="
    }

    def setup() {
        acc = new Account('001', 'Joao Silva', 1000.0, 1.5)
    }

    def cleanup() {
        acc = null
    }

    def "conta e criada corretamente"() {
        expect: "propriedades iniciais corretas"
        acc.numero == '001'
        acc.titular == 'Joao Silva'
        acc.saldo == 1000.0
        acc.taxaJuros == 1.5
        acc.extrato.size() == 1
        acc.extrato[0].contains('Conta criada')
        acc.temSaldo() == true
        acc.isAtiva() == true
    }

    def "deposito adiciona ao saldo e ao extrato"() {
        given: "uma conta com saldo inicial"

        when: "faço um deposito"
        acc.depositar(500.0)

        then: "saldo e extrato sao atualizados"
        acc.saldo == 1500.0
        acc.extrato.size() == 2
        acc.extrato.last().contains('Deposito')
    }

    def "deposito com valor negativo lanza excecao"() {
        when: "tento depositar valor negativo"
        acc.depositar(-100.0)

        then: "excecao e lançada"
        thrown(IllegalArgumentException)
    }

    @Unroll
    def "deposicao de #valor resulta em saldo #resultado"() {
        given: "uma conta com saldo inicial"

        when: "faço o deposito"
        acc.depositar(valor)

        then: "saldo e atualizado"
        acc.saldo == resultado

        where:
        valor | resultado
        100   | 1100.0
        500   | 1500.0
        1000  | 2000.0
        0.01  | 1000.01
    }

    @Unroll
    def "saque de #valor resulta em saldo #resultado"() {
        given: "uma conta com saldo inicial"

        when: "faço o saque"
        acc.sacar(valor)

        then: "saldo e atualizado"
        acc.saldo == resultado

        where:
        valor | resultado
        200   | 800.0
        500   | 500.0
        1000  | 0.0
        100.5 | 899.5
    }

    def "saque com valor negativo lanza excecao"() {
        when: "tento sacar valor negativo"
        acc.sacar(-100.0)

        then: "excecao e lançada"
        thrown(IllegalArgumentException)
    }

    def "saque maior que o saldo lanza excecao"() {
        when: "tento sacar mais que o saldo"
        acc.sacar(2000.0)

        then: "excecao e lançada"
        thrown(IllegalArgumentException)
    }

    def "juros sao calculados corretamente"() {
        given: "uma conta com saldo e taxa de juros"

        when: "calculei os juros"
        def resultado = acc.calcularJuros()

        then: "valor dos juros e correto"
        resultado == 15.0
    }

    def "juros sao aplicados ao saldo"() {
        when: "aplico os juros"
        acc.aplicarJuros()

        then: "saldo e atualizado com juros"
        acc.saldo == 1015.0
        acc.extrato.last().contains('Juros')
    }

    def "balanco retorna o saldo atual"() {
        given: "uma conta com saldo"
        acc.depositar(500.0)

        expect: "balanco e igual ao saldo"
        acc.balanco == 1500.0
    }

    @Unroll
    def "temSaldo verifica corretamente para #saldo"() {
        given: "uma conta com saldo"
        acc.saldo = saldo

        expect: "verificacao de saldo"
        acc.temSaldo() == esperado

        where:
        saldo || esperado
        1000  || true
        0     || false
        -100  || false
        0.01  || true
    }

    def "metadados retorna mapa vazio"() {
        expect: "metadados e um mapa vazios"
        acc.metadados == [:]
    }
}
