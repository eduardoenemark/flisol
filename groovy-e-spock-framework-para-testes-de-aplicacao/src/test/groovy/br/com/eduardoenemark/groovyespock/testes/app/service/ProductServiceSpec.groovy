package br.com.eduardoenemark.groovyespock.testes.app.service

import spock.lang.*

@Title('Especificação de ProductService')
@Narrative('Testes de ProductService e OrderService - catalogos, criacao e processamento.')
class ProductServiceSpec extends Specification {

    @Shared
    def productService = new ProductService()

    def setupSpec() {
        println "=== setupSpec: ProductServiceSpec ==="
    }

    def cleanupSpec() {
        println "=== cleanupSpec: ProductServiceSpec ==="
    }

    def setup() {
        // Setup antes de cada teste
    }

    def cleanup() {
        // Nothing to clean up
    }

    def "getProdutos retorna 3 produtos"() {
        expect: "lista com 3 produtos"
        productService.getProdutos().size() == 3
    }

    @Unroll
    def "getProdutos contem #nome com preco #preco"() {
        given: "lista de produtos"
        def produtos = productService.getProdutos()

        expect: "produto encontrado com preco"
        def produto = produtos.find { it.nome == nome }
        produto != null
        produto.preco == preco

        where:
        nome    || preco
        'Notebook' || '4500.00'
        'Mouse'    || '89.90'
        'Teclado'  || '179.90'
    }

    def "getProdutoPorId retorna notebook"() {
        given: "id '1'"

        expect: "notebook retornado"
        def produto = productService.getProdutoPorId('1')
        produto.nome == 'Notebook'
        produto.preco == '4500.00'
    }

    def "getProdutoPorId retorna mouse"() {
        given: "id '2'"

        expect: "mouse retornado"
        def produto = productService.getProdutoPorId('2')
        produto.nome == 'Mouse'
        produto.preco == '89.90'
    }

    def "getProdutoPorId inexistente retorna null"() {
        given: "id inexistente"

        expect: "produto nao encontrado"
        productService.getProdutoPorId('999') == null
    }

    def "criarProduto cria novo produto"() {
        given: "nome e preco validos"

        when: "crio novo produto"
        def produto = productService.criarProduto('Monitor', 1200.0)

        then: "produto criado corretamente"
        produto != null
        produto.nome == 'Monitor'
        produto.preco == '1200.0'
    }

    def "criarProduto gera novo id sequencial"() {
        given: "3 produtos existentes"

        when: "crio novo produto"
        def produto = productService.criarProduto('Webcam', 250.0)

        then: "id sequencial (4)"
        produto.id == '4'
    }

    def "excluirProduto retorna true"() {
        given: "id de produto"

        expect: "exclusao bem sucedida"
        productService.excluirProduto('1') == true
    }

    def "excluirProduto inexistente retorna true"() {
        given: "id inexistente"

        expect: "exclusao retorna true mesmo inexistente"
        productService.excluirProduto('999') == true
    }

    def "mock ProductService mockado"() {
        given: "um service mockado"
        def productServiceMock = Mock(ProductService)

        when: "chamo metodos mockados"
        def produtos = productServiceMock.getProdutos()
        def produto = productServiceMock.getProdutoPorId('1')
        def criado = productServiceMock.criarProduto('Test', 100.0)

        then: "interacoes verificadas"
        1 * productServiceMock.getProdutos() >> [
                [id: '1', nome: 'MockProduct', preco: '99.99']
        ]
        1 * productServiceMock.getProdutoPorId('1') >> [id: '1', nome: 'MockProduct', preco: '99.99']
        1 * productServiceMock.criarProduto('Test', 100.0) >> [id: '5', nome: 'Test', preco: '100.0']

        and: "retornos configurados"
        produtos.size() == 1
        produto.nome == 'MockProduct'
        criado.id == '5'
    }

    def "stub ProductService com retorno pre-definido"() {
        given: "um service stubbed"
        def productServiceStub = Stub(ProductService) {
            getProdutos() >> [
                    [id: '1', nome: 'StubProduct', preco: '50.00']
            ]
            getProdutoPorId(_) >> [id: '1', nome: 'StubProduct', preco: '50.00']
        }

        when: "chamo metodos do stub"
        def produtos = productServiceStub.getProdutos()
        def produto = productServiceStub.getProdutoPorId('999')

        then: "retornos pre-definidos"
        produtos.size() == 1
        produto.nome == 'StubProduct'
    }
}
