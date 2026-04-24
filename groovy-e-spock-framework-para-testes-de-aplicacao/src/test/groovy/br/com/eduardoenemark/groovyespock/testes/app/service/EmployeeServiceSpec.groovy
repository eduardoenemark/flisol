package br.com.eduardoenemark.groovyespock.testes.app.service

import br.com.eduardoenemark.groovyespock.testes.app.domain.Employee
import spock.lang.*

@Title('Especificação do EmployeeService')
@Narrative('Testes demonstrando ciclo de vida e hooks do Spock.')
class EmployeeServiceSpec extends Specification {

    @Shared def listaEmpregados = []

    def setupSpec() {
        println "=== setupSpec: cria conexao/com recursos compartilhados ==="
        listaEmpregados = []
    }

    def cleanupSpec() {
        println "=== cleanupSpec: libera recursos compartilhados ==="
        listaEmpregados.clear()
    }

    def setup() {
        println "--- setup: prepara estado para cada teste ---"
    }

    def cleanup() {
        println "--- cleanup: limpa estado apos cada teste ---"
    }

    def "adicionar empregado a lista"() {
        given: "uma lista de empregados"
        def empregado = new Employee('Ana', 'Dev', 5000)

        when: "adiciono um empregado"
        listaEmpregados << empregado

        then: "empregado foi adicionado"
        listaEmpregados.size() == 1
        listaEmpregados[0].nome == 'Ana'
        listaEmpregados[0].cargo == 'Dev'
        listaEmpregados[0].salario == 5000
    }

    def "calcular salario com aumento"() {
        given: "um empregado"
        def empregado = new Employee('João', 'Senior Dev', 8000)

        when: "aplico aumento de 10%"
        def novoSalario = empregado.aplicarAumento(10)

        then: "salario atualizado corretamente"
        novoSalario == 8800
    }

    def "calcular salario com aumento negativo (desconto)"() {
        given: "um empregado"
        def empregado = new Employee('Maria', 'Junior Dev', 3000)

        when: "aplico desconto de 5%"
        def novoSalario = empregado.aplicarAumento(-5)

        then: "salario reduzido corretamente"
        novoSalario == 2850
    }

    def "toString de Employee"() {
        given: "um empregado"
        def empregado = new Employee('Carlos', 'Tech Lead', 12000)

        expect: "toString formatado"
        empregado.toString() == 'Employee(nome:\'Carlos\', cargo:\'Tech Lead\', salario:12000.0)'
    }
}
