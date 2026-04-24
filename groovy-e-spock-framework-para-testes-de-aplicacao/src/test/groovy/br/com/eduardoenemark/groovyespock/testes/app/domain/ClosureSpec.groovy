package br.com.eduardoenemark.groovyespock.testes.app.domain

import spock.lang.Specification

class ClosureSpec extends Specification {

    def "closure atribuida a variavel com parametro nomeado"() {
        given: "uma closure para saudacao"
        def saudacao = { name -> "Olá, $name!" }

        when: "chamo a closure"
        def resultado = saudacao('Maria')

        then: "resultado correto"
        resultado == 'Olá, Maria!'
    }

    def "closure com parametro implicito 'it'"() {
        given: "uma closure para dobro"
        def dobro = { it * 2 }

        expect: "retorna o dobro do valor"
        dobro(5) == 10
    }

    def "closure sem parametros"() {
        given: "uma closure piada"
        def piada = { -> 'Por que o programador trocou o 0 pelo 1?' }

        expect: "retorna algo não nulo"
        piada() != null
    }

    def "closure captura variavel externa"() {
        given: "uma variável externa e uma closure"
        def mensagem = 'Bem-vindo'
        def saudacaoPersonalizada = { nome -> "$mensagem, $nome!" }

        expect: "closure usa a variável externa"
        saudacaoPersonalizada('João') == 'Bem-vindo, João!'
    }

    def "passar closure como parametro"() {
        given: "uma closure para somar"
        def somar = { x, y -> x + y }

        when: "chamo a closure com dois valores"
        def resultado = somar(3, 4)

        then: "resultado correto"
        resultado == 7
    }

    def "collect - transforma cada elemento"() {
        given: "uma lista de numeros"
        def numeros = [1, 2, 3, 4, 5]
        def duplicados = numeros.collect { it * 2 }

        expect: "lista transformada"
        duplicados == [2, 4, 6, 8, 10]
    }

    def "findAll - filtra elementos"() {
        given: "uma lista de numeros"
        def numeros = [1, 2, 3, 4, 5]
        def pares = numeros.findAll { it % 2 == 0 }

        expect: "apenas elementos pares"
        pares == [2, 4]
    }

    def "each - itera sobre elementos"() {
        given: "uma lista e uma variavel de soma"
        def numeros = [1, 2, 3, 4, 5]
        def soma = 0
        numeros.each { soma += it }

        expect: "soma correta"
        soma == 15
    }

    def "sort - ordena por propriedade"() {
        given: "uma lista de palavras"
        def words = ['java', 'groovy', 'kotlin']
        def sorted = words.sort { it.length() }

        expect: "ordenada por comprimento"
        sorted == ['java', 'groovy', 'kotlin']
    }

    def "groupBy - agrupa elementos"() {
        given: "uma lista de numeros"
        def numeros = [1, 2, 3, 4, 5, 6]
        def grupos = numeros.groupBy { it % 2 }

        expect: "agrupados por paridade"
        grupos == [1: [1, 3, 5], 0: [2, 4, 6]]
    }

    def "currying - pré-definir parâmetros"() {
        given: "uma closure de multiplicacao"
        def multiply = { a, b -> a * b }
        def doubleIt = multiply.curry(2)

        expect: "resultado com parametro curry fixo"
        doubleIt(5) == 10
    }

    def "rcurry - curry a partir da direita"() {
        given: "uma closure com rcurry"
        def rcurryAdd = { a, b -> a + b }.rcurry(10)

        expect: "parametro da direita fixo"
        rcurryAdd(5) == 15
    }

    def "memoization - cache de resultados"() {
        given: "uma closure memoizada"
        def memoized = { n ->
            sleep(10)
            n * 2
        }.memoize()

        when: "chamo com o mesmo argumento"
        def resultado1 = memoized(5)
        def resultado2 = memoized(5)

        then: "segunda chamada retorna do cache"
        resultado1 == 10
        resultado2 == 10
    }

    def "composition - encadear closures"() {
        given: "duas closures para compor"
        def addOne = { it + 1 }
        def doubleIt = { it * 2 }
        def composition = addOne << doubleIt

        expect: "composicao funciona (dobra então soma 1)"
        composition(5) == 11
    }

    def "trampoline - recursao trampolim"() {
        given: "uma funcao recursiva com trampoline"
        def factorial
        factorial = { long n, long acc = 1L ->
            n <= 1L ? acc : factorial.trampoline(n - 1, acc * n)
        }.trampoline()

        expect: "fatorial calculado corretamente"
        factorial(10) == 3628800
    }

    def "find - encontra primeiro elemento"() {
        given: "uma lista de numeros"
        def numeros = [1, 2, 3, 4, 5]

        expect: "primeiro maior que 3"
        numeros.find { it > 3 } == 4
    }

    def "every - todos atendem condição"() {
        given: "uma lista de numeros"
        def numeros = [1, 2, 3, 4, 5]

        expect: "todos menores que 10"
        numeros.every { it < 10 }
    }

    def "any - algum atende condição"() {
        given: "uma lista de numeros"
        def numeros = [1, 2, 3, 4, 5]

        expect: "algum maior que 4"
        numeros.any { it > 4 }
    }

    def "sum - soma dos elementos"() {
        given: "uma lista de numeros"
        def numeros = [1, 2, 3, 4, 5]

        expect: "somatorio"
        numeros.sum() == 15
    }

    def "max e min"() {
        given: "uma lista de numeros"
        def numeros = [3, 1, 5, 2, 4]

        expect: "extremos corretos"
        numeros.max() == 5
        numeros.min() == 1
    }
}
