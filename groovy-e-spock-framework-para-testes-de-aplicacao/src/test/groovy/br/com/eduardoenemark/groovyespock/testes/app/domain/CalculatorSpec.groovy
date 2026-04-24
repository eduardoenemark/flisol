package br.com.eduardoenemark.groovyespock.testes.app.domain

import spock.lang.Specification
import spock.lang.Shared
import spock.lang.Unroll

class CalculatorSpec extends Specification {

    @Shared calc = new Calculator()

    def setupSpec() {
        println "=== setupSpec: Executado uma vez antes de todos os tests ==="
    }

    def cleanupSpec() {
        println "=== cleanupSpec: Executado uma vez depois de todos os tests ==="
    }

    def setup() {
        println "--- setup: Executado antes de cada teste ---"
    }

    def cleanup() {
        println "--- cleanup: Executado depois de cada teste ---"
    }

    def "soma funciona corretamente"() {
        given: "uma calculadora e dois numeros"
        def a = 5
        def b = 3

        when: "realizo a soma"
        def resultado = calc.calcular(a, b, '+')

        then: "o resultado e a soma dos numeros"
        resultado == 8

        expect: "alternativa mais concisa"
        calc.calcular(a, b, '+') == 8
    }

    @Unroll
    def "todas operacoes basicas - #a #op #b = #esperado"() {
        given: "calculadora inicializada"

        expect: "operacoes corretas"
        calc.calcular(a, b, op) == esperado

        where:
        a | b | op | esperado
        1 | 2 | '+' | 3
        5 | 3 | '-' | 2
        4 | 2 | '*' | 8
        10| 2 | '/' | 5
    }

    def "divisao por zero lanza excecao"() {
        given: "uma calculadora"

        when: "tento dividir por zero"
        calc.calcular(10, 0, '/')

        then: "excecao e lanzamiento"
        thrown(IllegalArgumentException)
    }

    def "operacao invalida lanza excecao"() {
        when: "tento usar operacao invalida"
        calc.calcular(10, 5, '%')

        then: "excecao e lançamento"
        thrown(IllegalArgumentException)
    }

    def 'isPar verifica corretamente numeros pares'() {
        expect: "o numero eh par ou impar"
        calc.isPar(numero) == esperado

        where:
        numero || esperado
        0      || true
        2      || true
        4      || true
        1      || false
        3      || false
        7      || false
    }

    def "numeracao com #a + #b"() {
        expect: "somatorio correto"
        calc.calcular(a, b, '+') == a + b

        where:
        a | b
        1 | 1
        100 | 200
        -5 | 5
    }
}
