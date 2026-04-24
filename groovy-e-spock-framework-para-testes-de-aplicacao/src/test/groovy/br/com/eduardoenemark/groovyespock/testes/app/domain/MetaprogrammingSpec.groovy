package br.com.eduardoenemark.groovyespock.testes.app.domain

import spock.lang.Specification

class MetaprogrammingSpec extends Specification {

    def "metodoMissing - DSLite captura metodos inexistentes"() {
        given: "uma instancia de DSLite"
        def dsl = new DSLite()

        when: "chamo metodos que nao existem"
        def map1 = [class: 'container']
        def map2 = [type: 'text', id: 'nome']
        dsl.selecao('div', map1)
        dsl.campo(map2)

        then: "os metodos sao capturados e armazenados"
        dsl.conteudo.size() == 2
        dsl.conteudo[0].contains('selecao')
        dsl.conteudo[0].contains('div')
        dsl.conteudo[1].contains('campo')
    }

    def "AST @ToString - PessoaAST gera toString automatizado"() {
        given: "uma pessoa com nome e idade"
        def pessoa = new PessoaAST(nome: 'Ana', idade: 30)

        expect: "toString gerado automaticamente pelo @ToString"
        pessoa.toString() == 'PessoaAST(nome:Ana, idade:30)'
    }

    def "@CompileStatic - Calculadora funciona com compilacao estatica"() {
        given: "uma calculadora com @CompileStatic"
        def calc = new Calculadora()

        expect: "operacoes basicas funcionam"
        calc.somar(3, 4) == 7
        calc.subtrair(10, 3) == 7
        calc.multiplicar(5, 2) == 10
    }

    def "@CompileStatic - methodMissing esta desativado"() {
        given: "uma calculadora com @CompileStatic"
        def calc = new Calculadora()
        def result = calc.methodMissing('foo', [])

        expect: "methodMissing funciona como metodo normal"
        result == 0
    }

    def "metaprogramacao - adicionar metaclasse em tempo de execucao"() {
        given: "uma metaclasse adicionada ao String"
        String.metaClass.saudar = { -> "Oi, " + delegate + "!" }

        when: "chamo o metodo adicionado"
        def resultado = 'Mundo'.saudar()

        then: "o metodo funciona"
        resultado == 'Oi, Mundo!'
    }

    def "operador elvis - valor alternativo"() {
        given: "uma string vazia"
        def nome = ''

        when: "uso o operador elvis"
        def nomeSeguro = nome ?: 'Anônimo'

        then: "retorna o valor padrao"
        nomeSeguro == 'Anônimo'
    }

    def "safe navigation - sem NullPointerException"() {
        given: "uma referncia nula"
        def pessoa = null

        expect: "safe navigation retorna null sem erro"
        pessoa?.nome == null
        pessoa?.endereco?.cidade == null
    }

    def "spread operator - aplica metodo em todos os elementos"() {
        given: "uma lista de numeros"
        def numeros = [1, 2, 3]

        expect: "toString aplicado a todos"
        numeros*.toString() == ['1', '2', '3']
    }

    def "range - sequencia inclusiva"() {
        given: "um range de 1 a 5"
        def intervalo = 1..5

        expect: "range contem os valores"
        intervalo.contains(3)
        intervalo.contains(1)
        intervalo.contains(5)
    }

    def "tipos numericos avanzados - BigInteger e BigDecimal"() {
        given: "valores com sufixo G"
        def bigInt = 123G
        def bigDec = 123.45G

        expect: "tipos corretos"
        bigInt instanceof java.math.BigInteger
        bigDec instanceof java.math.BigDecimal
        bigInt == new BigInteger('123')
        bigDec == new BigDecimal('123.45')
    }

    def "potencia operator - 2 ** 100"() {
        given: "potencia grande com G"
        def resultado = 2G ** 100

        expect: "resultado com precisao arbitraria"
        resultado instanceof java.math.BigInteger
        resultado > 0
    }
}
