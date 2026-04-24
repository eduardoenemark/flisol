package com.exemplo.dto

class ProdutoDTO {
    String id
    String nome
    String preco

    ProdutoDTO(String id, String nome, String preco) {
        this.id = id
        this.nome = nome
        this.preco = preco
    }
}

class ResultadoDTO {
    int operandoA
    int operandoB
    String operacao
    int resultado

    ResultadoDTO(int operandoA, int operandoB, String operacao, int resultado) {
        this.operandoA = operandoA
        this.operandoB = operandoB
        this.operacao = operacao
        this.resultado = resultado
    }
}
