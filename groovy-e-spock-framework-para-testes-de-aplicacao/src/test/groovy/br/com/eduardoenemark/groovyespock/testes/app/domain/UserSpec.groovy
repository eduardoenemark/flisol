package br.com.eduardoenemark.groovyespock.testes.app.domain

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class UserSpec extends Specification {
    @Shared user = new User('1', 'Ana Silva', 'ana@email.com', 'ADMIN')

    def setupSpec() {
        println "=== setupSpec: UserSpec ==="
    }

    def cleanupSpec() {
        println "=== cleanupSpec: UserSpec ==="
    }

    def setup() {
        user = new User('1', 'Ana Silva', 'ana@email.com', 'ADMIN')
    }

    def cleanup() {
        user = null
    }

    def "usuario e criado corretamente"() {
        expect: "propriedades iniciais"
        user.id == '1'
        user.nome == 'Ana Silva'
        user.email == 'ana@email.com'
        user.role == 'ADMIN'
    }

    def "isAdmin verifica papel corretamente"() {
        given: "usuario ADMIN"

        expect: "verifica se e admin"
        user.isAdmin() == true
    }

    def "isAdmin retorna falso para USER"() {
        given: "um usuario comum"
        def usuario = new User('2', 'Joao', 'joao@email.com', 'USER')

        expect: "usuario nao e admin"
        usuario.isAdmin() == false
    }

    @Unroll
    def "email valido #email retorna #valido"() {
        given: "usuario com email"
        def usuario = new User('1', 'Teste', email, 'USER')

        expect: "validacao de email"
        usuario.hasValidEmail() == valido

        where:
        email              || valido
        'ana@email.com'    || true
        'joao.123@dominio.org' || true
        'maria@empresa.co.br'  || true
        'emailinvalido'    || false
        '@missing.com'     || false
        'user@'            || false
        ''                 || false
        'sem arroba'       || false
        'user@@duplo.com'  || false
        'user@.com'        || false
    }

    def "toString gera representacao formatada"() {
        expect: "toString inclui dados do usuario"
        user.toString().contains('User')
        user.toString().contains('id:')
        user.toString().contains('Ana Silva')
        user.toString().contains('ana@email.com')
        user.toString().contains('role:')
        user.toString().contains('ADMIN')
    }

    def "construtor com diferentes papeis"() {
        expect: "criacao de usuarios com papeis diferentes"
        new User('1', 'A', 'a@x.com', 'ADMIN').isAdmin() == true
        new User('2', 'B', 'b@x.com', 'USER').isAdmin() == false
        new User('3', 'C', 'c@x.com', 'MODERATOR').isAdmin() == false
    }

    def "propriedades do usuario"() {
        given: "usuario criado"
        def u = new User('42', 'Maria Souza', 'maria@email.com', 'ADMIN')

        expect: "acesso as propriedades"
        u.id == '42'
        u.nome == 'Maria Souza'
        u.email == 'maria@email.com'
        u.role == 'ADMIN'
        u.isAdmin() == true
        u.hasValidEmail() == true
    }
}
