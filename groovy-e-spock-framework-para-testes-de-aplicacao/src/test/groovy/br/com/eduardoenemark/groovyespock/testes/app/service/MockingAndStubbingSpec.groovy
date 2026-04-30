package br.com.eduardoenemark.groovyespock.testes.app.service


import spock.lang.Specification

class MockingAndStubbingSpec extends Specification {

    def "mock - verificar quantidade de chamadas"() {
        given: "um productService mockado com retorno"
        def productService = Mock(ProductService)

        when: "chamo o metodo mockado"
        def produtos = productService.getProdutos()

        then: "confirmo que foi invocado exatamente uma vez e retorna a lista"
        1 * productService.getProdutos() >> [[id: '1', nome: 'Notebook', preco: '4500.00'],
                                             [id: '2', nome: 'Mouse', preco: '89.90'],
                                             [id: '3', nome: 'Teclado', preco: '179.90']]

        and: "retorno configurado do mock"
        produtos.size() == 3
    }

    def "mock - verificar argumentos da chamada"() {
        given: "um productService mockado"
        def productService = Mock(ProductService)

        when: "chamo com argumento especifico"
        def produto = productService.getProdutoPorId('1')

        then: "metodo chamado com argumento especifico e retorna o produto"
        1 * productService.getProdutoPorId('1') >> [id: '1', nome: 'Notebook', preco: '4500.00']

        and: "retorno configurado"
        produto.nome == 'Notebook'
    }

    def "mock - qualquer numero de chamadas"() {
        given: "um orderService mockado"
        def orderService = Mock(OrderService)

        when: "chamo o metodo varias vezes"
        orderService.processarCompra('1', 2)
        orderService.processarCompra('2', 1)
        orderService.processarCompra('3', 3)

        then: "foi chamado entre 1 e 3 vezes"
        (1..3) * orderService.processarCompra({ true }, _)
    }

    def "mock - exatamente zero chamadas"() {
        given: "um orderService mockado"
        def orderService = Mock(OrderService)

        when: "nao chamo o metodo"

        then: "o metodo NAO foi invocado"
        0 * orderService.verificarEstoque(_)
    }

    def "stub - retorno pre-definido sem verificar interacoes"() {
        given: "um productService stubbed com retorno configurado"
        def productService = Stub(ProductService) {
            getProdutos() >> [[id: '1', nome: 'Mouse', preco: '89.90']]
        }

        when: "chamo um metodo do stub"
        def produtos = productService.getProdutos()

        then: "retorno configurado e nao nulo"
        produtos != null
        produtos.size() == 1
        produtos[0].nome == 'Mouse'
    }

    def "stub - configuro comportamento customizado"() {
        given: "um orderService stubbed com comportamento customizado"
        def orderService = Stub(OrderService) {
            processarCompra(_, _) >> true
            verificarEstoque('1') >> 'disponivel'
            verificarEstoque('2') >> 'esgotado'
        }

        when: "chamo os metodos stubbed"
        def compra = orderService.processarCompra('1', 2)
        def estoque1 = orderService.verificarEstoque('1')
        def estoque2 = orderService.verificarEstoque('2')

        then: "retornos configurados"
        compra == true
        estoque1 == 'disponivel'
        estoque2 == 'esgotado'
    }

    def "mock vs stub - demonstrar a diferença"() {
        given: "mocks e stubs"
        def productServiceMock = Mock(ProductService)
        def productServiceStub = Stub(ProductService) {
            getProdutos() >> [[id: '1', nome: 'Mouse', preco: '89.90']]
        }

        when: "chamo getProdutos em ambos"
        def produtosMock = productServiceMock.getProdutos()
        def produtosStub = productServiceStub.getProdutos()

        then: "mock precisa de verificacao de interacao e retorna a lista"
        1 * productServiceMock.getProdutos() >> [[id: '1', nome: 'Mouse', preco: '89.90']]

        and: "stub so precisa do retorno"
        produtosStub.size() == 1

        and: "ambos retornam lista"
        produtosMock instanceof List
        produtosStub instanceof List
    }

    def "mock - verificar ordem de chamadas"() {
        given: "um productService e orderService mockados"
        def productService = Mock(ProductService) {
            getProdutos() >> [[id: '1', nome: 'Mouse', preco: '89.90']]
        }
        def orderService = Mock(OrderService)

        when: "chamo metodos em ordem"
        productService.getProdutos()
        orderService.processarCompra('1', 1)

        then: "chamadas na ordem correta"
        1 * productService.getProdutos()
        1 * orderService.processarCompra({ true }, 1)
    }

    def "mock - retornar excecao configurada"() {
        given: "um orderService mockado para lancar excecao"
        def orderService = Mock(OrderService) {
            verificarEstoque('inexistente') >> { throw new IllegalArgumentException('Produto não encontrado') }
        }

        when: "chamo o metodo que lanca excecao"
        orderService.verificarEstoque('inexistente')

        then: "excecao retornada do mock"
        thrown(IllegalArgumentException)
    }
}
