package com.exemplo.domain

import groovy.transform.CompileStatic
import groovy.transform.ToString

// DSLite - example of methodMissing metaprogramming
class DSLite {
    def conteudo = []

    def methodMissing(String name, args) {
        conteudo << "$name(${args.collect { it.toString() }.join(', ')})"
    }
}

// PessoaAST - example of AST Transformations
@ToString(includeNames=true)
class PessoaAST {
    String nome
    int idade
}

// Calculadora - example of @CompileStatic
@CompileStatic
class Calculadora {
    int somar(int a, int b) { a + b }
    int subtrair(int a, int b) { a - b }
    int multiplicar(int a, int b) { a * b }

    def methodMissing(String name, args) {
        // N\u00e3o ser\u00e1 chamado com @CompileStatic
        return 0
    }
}
