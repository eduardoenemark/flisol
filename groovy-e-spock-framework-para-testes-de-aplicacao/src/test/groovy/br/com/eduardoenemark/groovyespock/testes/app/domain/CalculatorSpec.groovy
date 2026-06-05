package br.com.eduardoenemark.groovyespock.testes.app.domain

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class CalculatorSpec extends Specification {

    @Shared
    def calc = new Calculator()

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
        a  | b | op  | esperado
        1  | 2 | '+' | 3
        5  | 3 | '-' | 2
        4  | 2 | '*' | 8
        10 | 2 | '/' | 5
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
        a   | b
        1   | 1
        100 | 200
        -5  | 5
    }

    @Unroll
    def "operacao com numeros negativos #a #op #b = #resultado"() {
        given: "calculadora inicializada"

        expect: "operacoes com negativos funcionam"
        calc.calcular(a, b, op) == resultado

        where:
        a  | b  | op  || resultado
        -5 | 3  | '+' || -2
        -5 | -3 | '+' || -8
        5  | -3 | '-' || 8
        -4 | -2 | '*' || 8
        -10| 2  | '/' || -5
        10 | -2 | '/' || -5
    }

    def "isPar com zero retorna true (zero e par)"() {
        expect: "zero e par"
        calc.isPar(0) == true
    }

    def "isPar com numero negativo par"() {
        expect: "negativo par"
        calc.isPar(-4) == true
    }

    def "isPar com numero negativo impar"() {
        expect: "negativo impar"
        calc.isPar(-3) == false
    }

    def "calcularPi retorna valor preciso de pi"() {
        given: "valor de pi"

        expect: "pi como double com precision"
        def pi = calc.calcularPi()
        pi > 0
        pi.toString().contains('3.141592653')
    }

    @Unroll
    def "multiplicacao grande #a * #b = #resultado"() {
        given: "calculadora inicializada"

        expect: "multiplicacao funciona"
        calc.calcular(a, b, '*') == resultado

        where:
        a      | b       || resultado
        100    | 100     || 10000
        1000   | 1000    || 1000000
        999    | 999     || 998001
    }

    def "subtracao com zero"() {
        expect: "x - 0 = x"
        calc.calcular(5, 0, '-') == 5
    }

    def "soma com zero"() {
        expect: "x + 0 = x"
        calc.calcular(5, 0, '+') == 5
    }

    def "divisao exata"() {
        expect: "divisao exata"
        calc.calcular(100, 4, '/') == 25
    }

    def "divisao com resto (truncado)"() {
        expect: "divisao inteira (resto truncado)"
        calc.calcular(10, 3, '/') == 3
    }

    def "operacao com string vazia lanza excecao"() {
        when: "tento operacao com string vazia"
        calc.calcular(10, 5, '')

        then: "excecao e lancada"
        thrown(IllegalArgumentException)
    }
}
