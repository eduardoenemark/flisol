package br.com.eduardoenemark.groovyespock.testes.app.service

import br.com.eduardoenemark.groovyespock.testes.app.domain.User
import spock.lang.Shared
import spock.lang.Specification

class ReportServiceSpec extends Specification {

    @Shared
    def service = new ReportService()

    def setupSpec() {
        println "=== setupSpec: ReportServiceSpec ==="
    }

    def cleanupSpec() {
        println "=== cleanupSpec: ReportServiceSpec ==="
    }

    def setup() {
        // Setup antes de cada teste
    }

    def cleanup() {
        // Nothing to clean up - service is @Shared and stateless
    }

    def "gerar relatario sem criterios retorna relatorio completo"() {
        given: "lista de usuarios"
        def usuarios = [
                new User('1', 'Ana Silva', 'ana@email.com', 'ADMIN'),
                new User('2', 'Carlos Santos', 'carlos@email.com', 'USER'),
                new User('3', 'Maria Oliveira', 'maria@email.com', 'USER')
        ]

        when: "gero relatorio sem criterios"
        def relatorio = service.gerarRelatorioUsuarios(usuarios, null)

        then: "relatorio contem dados"
        relatorio != null
        relatorio.contains('RELATÓRIO DE USUÁRIOS')
        relatorio.contains('Total de usuários: 3')
        relatorio.contains('Ana Silva')
        relatorio.contains('Carlos Santos')
        relatorio.contains('Maria Oliveira')
        relatorio.contains('ADMIN')
        relatorio.contains('USER')
    }

    def "gerar relatorio com criterios filtrados"() {
        given: "usuarios e criterios"
        def usuarios = [
                new User('1', 'Ana Silva', 'ana@email.com', 'ADMIN'),
                new User('2', 'Carlos Santos', 'carlos@email.com', 'USER')
        ]
        def criterios = ['teste': 0]

        when: "gero relatorio com criterios"
        def relatorio = service.gerarRelatorioUsuarios(usuarios, criterios)

        then: "relatorio contem criterios e filtrados"
        relatorio.contains('Total de usuários: 2')
        relatorio.contains('Critérios')
    }

    def "extrair emails retorna lista de emails"() {
        given: "usuarios"
        def usuarios = [
                new User('1', 'Ana', 'ana@email.com', 'ADMIN'),
                new User('2', 'Carlos', 'carlos@email.com', 'USER'),
                new User('3', 'Maria', 'maria@email.com', 'USER')
        ]

        when: "extrai emails"
        def emails = service.extrairEmails(usuarios)

        then: "emails extraidos corretamente"
        emails.size() == 3
        emails == ['ana@email.com', 'carlos@email.com', 'maria@email.com']
    }

    def "extrair emails lista vazia retorna lista vazia"() {
        expect: "lista vazia"
        service.extrairEmails([]).isEmpty()
    }

    def "agrupar por role retorna grupos corretos"() {
        given: "usuarios com diferentes roles"
        def usuarios = [
                new User('1', 'Ana', 'ana@email.com', 'ADMIN'),
                new User('2', 'Carlos', 'carlos@email.com', 'USER'),
                new User('3', 'Maria', 'maria@email.com', 'ADMIN')
        ]

        when: "agrupo por role"
        def grupos = service.agruparPorRole(usuarios)

        then: "grupos corretos"
        grupos.containsKey('ADMIN')
        grupos.containsKey('USER')
        grupos['ADMIN'].size() == 2
        grupos['USER'].size() == 1
        grupos['ADMIN'] == ['Ana', 'Maria']
    }

    def "agrupar por role com usuarios sem role"() {
        given: "usuarios"
        def usuarios = [
                new User('1', 'Ana', 'ana@email.com', 'MODERATOR')
        ]

        when: "agrupo por role"
        def grupos = service.agruparPorRole(usuarios)

        then: "grupo unico"
        grupos.containsKey('MODERATOR')
        grupos['MODERATOR'].size() == 1
    }

    def "relatorio formata numeracao corretamente"() {
        given: "usuarios numerados"
        def usuarios = [
                new User('1', 'Usuario1', 'u1@email.com', 'USER'),
                new User('2', 'Usuario2', 'u2@email.com', 'USER'),
                new User('3', 'Usuario3', 'u3@email.com', 'USER')
        ]

        when: "gero relatorio"
        def relatorio = service.gerarRelatorioUsuarios(usuarios, [:])

        then: "numeracao correta"
        relatorio.contains(' 1. Usuario1')
        relatorio.contains(' 2. Usuario2')
        relatorio.contains(' 3. Usuario3')
    }

    def "relatorio com 1 usuario"() {
        given: "apenas um usuario"
        def usuarios = [
                new User('1', 'SoUm', 'so@email.com', 'ADMIN')
        ]

        when: "gero relatorio"
        def relatorio = service.gerarRelatorioUsuarios(usuarios, null)

        then: "relatorio com 1 usuario"
        relatorio.contains('Total de usuários: 1')
        relatorio.contains('SoUm')
    }

    def "mock report service com retorno simulado"() {
        given: "um service mockado"
        def reportService = Mock(ReportService)

        when: "chamo metodos mockados"
        def relatorio = reportService.gerarRelatorioUsuarios([], [:])

        then: "interacao verificada"
        1 * reportService.gerarRelatorioUsuarios([], [:]) >> 'Relatorio Mockado'

        and: "retorno mockado"
        relatorio == 'Relatorio Mockado'
    }

    def "stub com retorno pre-definido"() {
        given: "um service stubbed"
        def reportService = Stub(ReportService) {
            extrairEmails(_) >> ['stub1@email.com', 'stub2@email.com']
        }

        when: "chamo metodo do stub"
        def emails = reportService.extrairEmails(null)

        then: "retorno pre-definido"
        emails == ['stub1@email.com', 'stub2@email.com']
        emails.size() == 2
    }
}
