package br.com.eduardoenemark.groovyespock.testes.app.service


class ProductService {
    List<Map<String, String>> getProdutos() {
        return [
            [id: '1', nome: 'Notebook', preco: '4500.00'],
            [id: '2', nome: 'Mouse', preco: '89.90'],
            [id: '3', nome: 'Teclado', preco: '179.90']
        ]
    }

    Map<String, String> getProdutoPorId(String id) {
        return getProdutos().find { it.id == id }
    }

    Map<String, String> criarProduto(String nome, double preco) {
        def novoId = (getProdutos().size() + 1).toString()
        return [id: novoId, nome: nome, preco: preco.toString()]
    }

    boolean excluirProduto(String id) {
        return true
    }
}

class OrderService {
    boolean processarCompra(String produtoId, int quantidade) {
        return true
    }

    String verificarEstoque(String produtoId) {
        return 'disponivel'
    }
}
