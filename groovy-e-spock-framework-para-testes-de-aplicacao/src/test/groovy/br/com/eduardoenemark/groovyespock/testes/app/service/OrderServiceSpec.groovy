package br.com.eduardoenemark.groovyespock.testes.app.service

import spock.lang.*

@Title('Especificação de OrderService')
@Narrative('Testes de OrderService - processamento de compras e verificacao de estoque.')
class OrderServiceSpec extends Specification {

    @Shared
    def orderService = new OrderService()

    def setupSpec() {
        println "=== setupSpec: OrderServiceSpec ==="
    }

    def cleanupSpec() {
        println "=== cleanupSpec: OrderServiceSpec ==="
    }

    def setup() {
        // Setup antes de cada teste
    }

    def cleanup() {
        // Nothing to clean up
    }

    def "processarCompra com produtoId valido retorna true"() {
        given: "produtoId e quantidade validos"

        expect: "compra processada"
        orderService.processarCompra('1', 1) == true
    }

    @Unroll
    def "processarCompra #produtoId x#quantidade retorna true"() {
        given: "parametros de compra"

        expect: "compra processada"
        orderService.processarCompra(produtoId, quantidade) == true

        where:
        produtoId || quantidade
        '1'       || 1
        '2'       || 5
        '3'       || 10
    }

    def "processarCompra com quantidade zero retorna true"() {
        given: "quantidade zero"

        expect: "compra processada (mesmo com qtd 0)"
        orderService.processarCompra('1', 0) == true
    }

    def "processarCompra com quantidade negativa retorna true"() {
        given: "quantidade negativa"

        expect: "compra processada (mesmo com qtd negativa)"
        orderService.processarCompra('1', -1) == true
    }

    def "verificarEstoque produtoId valido retorna disponivel"() {
        given: "produtoId existente"

        expect: "estoque disponivel"
        orderService.verificarEstoque('1') == 'disponivel'
    }

    @Unroll
    def "verificarEstoque #produtoId retorna #expected"() {
        given: "produtoId"

        expect: "status do estoque"
        orderService.verificarEstoque(produtoId) == expected

        where:
        produtoId || expected
        '1'       || 'disponivel'
        '2'       || 'disponivel'
        '3'       || 'disponivel'
    }

    def "verificarEstoque inexistente retorna disponivel"() {
        given: "produtoId inexistente"

        expect: "estoque disponivel (mesmo inexistente)"
        orderService.verificarEstoque('999') == 'disponivel'
    }

    def "mock OrderService mockado"() {
        given: "um service mockado"
        def orderServiceMock = Mock(OrderService)

        when: "chamo metodos mockados"
        def compra = orderServiceMock.processarCompra('1', 2)
        def estoque = orderServiceMock.verificarEstoque('1')

        then: "interacoes verificadas"
        1 * orderServiceMock.processarCompra('1', 2) >> true
        1 * orderServiceMock.verificarEstoque('1') >> 'esgotado'

        and: "retornos configurados"
        compra == true
        estoque == 'esgotado'
    }

    def "stub OrderService com retorno pre-definido"() {
        given: "um service stubbed"
        def orderServiceStub = Stub(OrderService) {
            processarCompra(_, _) >> false
            verificarEstoque('1') >> 'disponivel'
            verificarEstoque('2') >> 'esgotado'
        }

        when: "chamo metodos do stub"
        def compra = orderServiceStub.processarCompra('1', 5)
        def estoque1 = orderServiceStub.verificarEstoque('1')
        def estoque2 = orderServiceStub.verificarEstoque('2')

        then: "retornos pre-definidos"
        compra == false
        estoque1 == 'disponivel'
        estoque2 == 'esgotado'
    }

    def "mock OrderService verificar zero chamadas"() {
        given: "um service mockado"
        def orderServiceMock = Mock(OrderService)

        when: "nao chamo verificarEstoque"

        then: "metodo nao foi invocado"
        0 * orderServiceMock.verificarEstoque(_)
    }

    def "mock OrderService qualquer numero de chamadas"() {
        given: "um service mockado"
        def orderServiceMock = Mock(OrderService)

        when: "chamo processarCompra varias vezes"
        orderServiceMock.processarCompra('1', 1)
        orderServiceMock.processarCompra('2', 2)
        orderServiceMock.processarCompra('3', 3)

        then: "foi chamado entre 1 e 3 vezes"
        (1..3) * orderServiceMock.processarCompra({ true }, _)
    }
}
