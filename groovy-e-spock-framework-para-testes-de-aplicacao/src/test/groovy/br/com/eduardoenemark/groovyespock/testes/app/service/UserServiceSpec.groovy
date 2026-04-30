package br.com.eduardoenemark.groovyespock.testes.app.service

import br.com.eduardoenemark.groovyespock.testes.app.domain.User
import spock.lang.Shared
import spock.lang.Specification

class UserServiceSpec extends Specification {
    @Shared
            service = new UserService()

    def setupSpec() {
        println "=== setupSpec: UserServiceSpec ==="
    }

    def cleanupSpec() {
        println "=== cleanupSpec: UserServiceSpec ==="
        // Limpar banco de dados estático
        UserService.database.clear()
    }

    def setup() {
        // Resetar o banco de dados antes de cada teste
        def db = UserService.database
        db.clear()
        db['1'] = new User('1', 'Ana Silva', 'ana@email.com', 'ADMIN')
        db['2'] = new User('2', 'Carlos Santos', 'carlos@email.com', 'USER')
        db['3'] = new User('3', 'Maria Oliveira', 'maria@email.com', 'USER')
    }

    def "buscar todos usuarios retorna lista com 3 usuarios"() {
        expect: "busca retorna todos usuarios"
        service.buscarTodos().size() == 3
    }

    def "buscar admin retorna apenas admins"() {
        expect: "somente um ADMIN"
        service.buscarAdmins().size() == 1
        service.buscarAdmins()[0].role == 'ADMIN'
    }

    def "buscar por id valida"() {
        given: "id existente"

        expect: "usuario encontrado"
        service.buscarPorId('1').orElse(null).nome == 'Ana Silva'
        service.buscarPorId('2').orElse(null).nome == 'Carlos Santos'
    }

    def "buscar por id inexistente retorna empty"() {
        expect: "usuario nao encontrado"
        service.buscarPorId('999').isEmpty()
    }

    def "buscar por email valida"() {
        expect: "encontra usuario por email"
        service.buscarPorEmail('ana@email.com').orElse(null).nome == 'Ana Silva'
        service.buscarPorEmail('carlos@email.com').orElse(null).nome == 'Carlos Santos'
    }

    def "buscar por email inexistente retorna empty"() {
        expect: "email nao encontrado"
        service.buscarPorEmail('inexistente@email.com').isEmpty()
    }

    def "criar usuario com sucesso"() {
        given: "dados validos"

        when: "crio novo usuario"
        def usuario = service.criarUsuario('Joao Pereira', 'joao@email.com', 'USER')

        then: "usuario criado corretamente"
        usuario != null
        usuario.nome == 'Joao Pereira'
        usuario.email == 'joao@email.com'
        usuario.role == 'USER'

        and: "usuario esta no banco"
        service.buscarPorId(usuario.id).isPresent()
    }

    def "criar usuario com nome vazio lança excecao"() {
        when: "tento criar usuario sem nome"
        service.criarUsuario('', 'user@email.com', 'USER')

        then: "excecao e lançada"
        thrown(IllegalArgumentException)
    }

    def "criar usuario com nome vazio spaces lança excecao"() {
        when: "tento criar usuario com nome so spaces"
        service.criarUsuario('   ', 'user@email.com', 'USER')

        then: "excecao e lançada"
        thrown(IllegalArgumentException)
    }

    def "criar usuario com email vazio lança excecao"() {
        when: "tento criar usuario sem email"
        service.criarUsuario('Joao', '', 'USER')

        then: "excecao e lancada"
        thrown(IllegalArgumentException)
    }

    def "atualizar usuario com sucesso"() {
        given: "usuario existente"

        when: "atualizo o usuario"
        def updated = service.atualizarUsuario('1', ['Nome': 'Ana Silva Lima', 'Email': 'ana.silva@email.com'])

        then: "usuario atualizado"
        updated.nome == 'Ana Silva Lima'
        updated.email == 'ana.silva@email.com'
        updated.role == 'ADMIN'
    }

    def "atualizar usuario mantendo valores nao fornecidos"() {
        given: "usuario existente"

        when: "atualizo so o nome"
        def updated = service.atualizarUsuario('1', ['Nome': 'Ana Atualizada'])

        then: "emails e role sao mantidos"
        updated.nome == 'Ana Atualizada'
        updated.email == 'ana@email.com'
        updated.role == 'ADMIN'
    }

    def "atualizar usuario com novo role"() {
        given: "usuario USER"

        when: "mudo para ADMIN"
        def updated = service.atualizarUsuario('2', ['Role': 'ADMIN'])

        then: "role atualizado"
        updated.role == 'ADMIN'
        updated.nome == 'Carlos Santos'
    }

    def "atualizar usuario inexistente lança excecao"() {
        when: "tento atualizar usuario inexistente"
        service.atualizarUsuario('999', ['Nome': 'Teste'])

        then: "excecao e lançada"
        thrown(NoSuchElementException)
    }

    def "remover usuario com sucesso"() {
        when: "removo usuario"
        def resultado = service.removerUsuario('3')

        then: "remocao bem sucedida"
        resultado == true

        and: "usuario nao existe mais"
        service.buscarPorId('3').isEmpty()
    }

    def "remover usuario inexistente retorna falso"() {
        expect: "remocao falha"
        service.removerUsuario('999') == false
    }

    def "usuarios para json retorna json valido"() {
        when: "converto para json"
        def json = service.usuariosParaJson()

        then: "json e valido e contem dados"
        json != null
        json.contains('Ana Silva')
        json.contains('Carlos Santos')
        json.contains('Maria Oliveira')
    }

    def "usuario de json cria usuario corretamente"() {
        given: "string json de usuario"
        def json = '{"Nome":"Teste User","Email":"teste@email.com","Role":"USER"}'

        when: "crio usuario do json"
        def usuario = service.usuarioDeJson(json)

        then: "usuario criado corretamente"
        usuario.nome == 'Teste User'
        usuario.email == 'teste@email.com'
        usuario.role == 'USER'
    }

    def "usuario de json com role default"() {
        given: "json sem role"
        def json = '{"Nome":"Usuario Default","Email":"default@email.com"}'

        when: "crio usuario do json sem role"
        def usuario = service.usuarioDeJson(json)

        then: "role default e USER"
        usuario.role == 'USER'
    }

    def "mock com UserService mockado"() {
        given: "um service mockado"
        def userService = Mock(UserService)

        when: "chamo metodos mockados"
        def usuarios = userService.buscarTodos()
        def admin = userService.buscarAdmins()

        then: "interacoes verificadas"
        1 * userService.buscarTodos() >> [new User('1', 'Teste', 'test@email.com', 'USER')]
        1 * userService.buscarAdmins() >> []

        and: "retornos configurados"
        usuarios.size() == 1
        admin.size() == 0
    }

    def "stub com comportamento pre-definido"() {
        given: "um service stubbed"
        def userService = Stub(UserService) {
            buscarTodos() >> [
                    new User('1', 'Stub1', 's1@email.com', 'USER'),
                    new User('2', 'Stub2', 's2@email.com', 'ADMIN')
            ]
            buscarAdmins() >> [new User('2', 'Stub2', 's2@email.com', 'ADMIN')]
        }

        when: "chamo metodos do stub"
        def todos = userService.buscarTodos()
        def admins = userService.buscarAdmins()

        then: "retornos configurados"
        todos.size() == 2
        admins.size() == 1
    }
}
