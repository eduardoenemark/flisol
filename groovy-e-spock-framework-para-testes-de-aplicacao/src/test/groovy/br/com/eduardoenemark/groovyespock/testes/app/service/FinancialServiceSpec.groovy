package br.com.eduardoenemark.groovyespock.testes.app.service

import br.com.eduardoenemark.groovyespock.testes.app.domain.Transaction
import spock.lang.*

import java.time.LocalDateTime

@Title('Especificação de FinancialService')
@Narrative('Testes de calculos financeiros: juros compostos, parcelas, descontos em faixas e relatorio.')
class FinancialServiceSpec extends Specification {

    @Shared
    def service = new FinancialService()

    def setupSpec() {
        println "=== setupSpec: FinancialServiceSpec ==="
    }

    def cleanupSpec() {
        println "=== cleanupSpec: FinancialServiceSpec ==="
    }

    def setup() {
        // Setup antes de cada teste
    }

    def cleanup() {
        // Nothing to clean up - service is stateless
    }

    @Unroll
    def "calcularJuros: capital #capital taxa #taxa% meses #meses resulta em ~#expected"() {
        given: "parametros de juros compostos"
        def resultado = service.calcularJuros(capital, taxa, meses)

        expect: "juros compostos calculados corretamente (approx)"
        Math.abs(resultado - expected) < 0.01

        where:
        capital | taxa || meses || expected
        1000    | 1    || 1     || 1010.0
        1000    | 1    || 12    || 1126.825
        500     | 2    || 6     || 563.081
        10000   | 0.5  || 24    || 11271.598
        1       | 100  || 1     || 2.0
        100     | 0    || 12    || 100.0
    }

    def "calcularJuros: taxa zero retorna capital original"() {
        given: "taxa de juros zero"

        expect: "capital nao cresce"
        service.calcularJuros(1000, 0, 36) == 1000.0
    }

    def "calcularJuros: meses zero retorna capital original"() {
        given: "zero meses"

        expect: "capital nao cresce"
        service.calcularJuros(5000, 5, 0) == 5000.0
    }

    @Unroll
    def "calcularParcela: valor #valor parcelas #parcelas taxa #taxa resulta em ~#expected"() {
        given: "parametros de parcelamento (taxa como decimal: 0.015 = 1.5%)"
        def resultado = service.calcularParcela(valor, parcelas, taxa)

        expect: "parcela calculada corretamente (approx)"
        Math.abs(resultado - expected) < 5

        where:
        valor  | parcelas || taxa    || expected
        1000   | 12       || 0.015   || 91.68
        5000   | 24       || 0.01    || 235.37
        100    | 3        || 0.02    || 34.67
        10000  | 60       || 0.008   || 210.51
    }

    def "calcularParcela: parcelas zero lanza excecao"() {
        when: "tentar parcelar com 0 parcelas"
        service.calcularParcela(1000, 0, 0.015)

        then: "excecao lancada"
        thrown(IllegalArgumentException)
    }

    def "calcularParcela: valor zero lanza excecao"() {
        when: "tentar parcelar com valor zero"
        service.calcularParcela(0, 12, 0.015)

        then: "excecao lancada"
        thrown(IllegalArgumentException)
    }

    def "calcularParcela: valor negativo lanza excecao"() {
        when: "tentar parcelar com valor negativo"
        service.calcularParcela(-1000, 12, 0.015)

        then: "excecao lancada"
        thrown(IllegalArgumentException)
    }

    def "calcularParcela: parcelas negativa lanza excecao"() {
        when: "tentar parcelar com parcelas negativas"
        service.calcularParcela(1000, -5, 0.015)

        then: "excecao lancada"
        thrown(IllegalArgumentException)
    }

    @Unroll
    def "calcularDesconto: valor #valor faixas [#faixas] descontos [#descontos] resulta em #expected"() {
        given: "faixas de desconto progressivo"

        expect: "melhor desconto aplicavel"
        service.calcularDesconto(valor, faixas, descontos) == expected

        where:
        valor  || faixas         || descontos       || expected
        50     || [100, 200, 500] || [5, 10, 15]      || 50.0      // nenhuma faixa atendidas
        150    || [100, 200, 500] || [5, 10, 15]      || 142.5     // faixa 100 -> desconto 5%
        250    || [100, 200, 500] || [5, 10, 15]      || 225.0     // faixa 200 -> desconto 10%
        600    || [100, 200, 500] || [5, 10, 15]      || 510.0     // faixa 500 -> desconto 15%
        1000   || [100, 500, 1000] || [5, 10, 20]     || 800.0     // faixa 1000 -> desconto 20%
    }

    def "calcularDesconto: faixas vazias lanza excecao"() {
        when: "tentar calcular desconto com faixas vazias"
        service.calcularDesconto(100, [], [5])

        then: "excecao lancada"
        thrown(IllegalArgumentException)
    }

    def "calcularDesconto: tamanhos diferentes lanza excecao"() {
        when: "tentar calcular desconto com faixas e descontos de tamanho diferente"
        service.calcularDesconto(100, [100, 200], [5])

        then: "excecao lancada"
        thrown(IllegalArgumentException)
    }

    def "calcularDesconto: sem faixas lanza excecao"() {
        when: "tentar calcular desconto sem faixas"
        service.calcularDesconto(100, null, [5])

        then: "excecao lancada"
        thrown(IllegalArgumentException)
    }

    def "gerarRelatorioFinanceiro: transacoes credit apenas"() {
        given: "apenas transacoes de credito"
        def transacoes = [
                new Transaction('C1', null, 500.0, 'CREDIT', LocalDateTime.of(2026, 6, 4, 10, 0)),
                new Transaction('C2', null, 300.0, 'CREDIT', LocalDateTime.of(2026, 6, 4, 11, 0)),
                new Transaction('C3', null, 200.0, 'CREDIT', LocalDateTime.of(2026, 6, 4, 12, 0))
        ]

        when: "gero relatorio"
        def relatorio = service.gerarRelatorioFinanceiro(transacoes)

        then: "relatorio correto"
        relatorio.totalEntradas == 1000.0
        relatorio.totalSaidas == 0
        relatorio.totalReembolsos == 0
        relatorio.saldo == 1000.0
        relatorio.quantidade == 3
    }

    def "gerarRelatorioFinanceiro: transacoes debit apenas"() {
        given: "apenas transacoes de debit"
        def transacoes = [
                new Transaction('D1', null, 200.0, 'DEBIT', LocalDateTime.of(2026, 6, 4, 10, 0)),
                new Transaction('D2', null, 150.0, 'DEBIT', LocalDateTime.of(2026, 6, 4, 11, 0))
        ]

        when: "gero relatorio"
        def relatorio = service.gerarRelatorioFinanceiro(transacoes)

        then: "relatorio correto"
        relatorio.totalEntradas == 0
        relatorio.totalSaidas == 350.0
        relatorio.totalReembolsos == 0
        relatorio.saldo == -350.0
        relatorio.quantidade == 2
    }

    def "gerarRelatorioFinanceiro: transacoes mixadas"() {
        given: "transacoes credit, debit e refund"
        def transacoes = [
                new Transaction('C1', null, 1000.0, 'CREDIT', LocalDateTime.of(2026, 6, 4, 10, 0)),
                new Transaction('D1', null, 300.0, 'DEBIT', LocalDateTime.of(2026, 6, 4, 11, 0)),
                new Transaction('R1', null, 50.0, 'REFUND', LocalDateTime.of(2026, 6, 4, 12, 0)),
                new Transaction('D2', null, 200.0, 'DEBIT', LocalDateTime.of(2026, 6, 4, 13, 0)),
                new Transaction('C2', null, 500.0, 'CREDIT', LocalDateTime.of(2026, 6, 4, 14, 0))
        ]

        when: "gero relatorio"
        def relatorio = service.gerarRelatorioFinanceiro(transacoes)

        then: "relatorio correto"
        relatorio.totalEntradas == 1500.0
        relatorio.totalSaidas == 500.0
        relatorio.totalReembolsos == 50.0
        relatorio.saldo == 1050.0
        relatorio.quantidade == 5
    }

    def "gerarRelatorioFinanceiro: lista vazia retorna zeros"() {
        given: "lista vazia de transacoes"

        when: "gero relatorio"
        def relatorio = service.gerarRelatorioFinanceiro([])

        then: "todos valores zero"
        relatorio.totalEntradas == 0
        relatorio.totalSaidas == 0
        relatorio.totalReembolsos == 0
        relatorio.saldo == 0
        relatorio.quantidade == 0
    }

    def "gerarRelatorioFinanceiro: refund aumenta saldo"() {
        given: "debit com refund"
        def transacoes = [
                new Transaction('D1', null, 500.0, 'DEBIT', LocalDateTime.of(2026, 6, 4, 10, 0)),
                new Transaction('R1', null, 100.0, 'REFUND', LocalDateTime.of(2026, 6, 4, 11, 0))
        ]

        when: "gero relatorio"
        def relatorio = service.gerarRelatorioFinanceiro(transacoes)

        then: "refund compensa parte do debit"
        relatorio.saldo == -400.0
    }

    def "juros vs parcela: comparacao"() {
        given: "mesmo capital e taxa decimal"
        def capital = 10000.0
        def taxa = 0.015

        when: "calculo juros 12 meses vs parcela 12 meses"
        def juros = service.calcularJuros(capital, taxa, 12)
        def parcela = service.calcularParcela(capital, 12, taxa)

        then: "juros e parcela ambos positivos"
        juros > 0
        parcela > 0
    }

    def "calcularDesconto: valor exatamente na faixa limite"() {
        given: "valor exatamente igual a limite de faixa"
        def faixas = [100, 500, 1000]
        def descontos = [5, 10, 20]

        expect: "valor igual ao limite usa desconto da faixa"
        service.calcularDesconto(100, faixas, descontos) == 95.0
        service.calcularDesconto(500, faixas, descontos) == 450.0
        service.calcularDesconto(1000, faixas, descontos) == 800.0
    }

    def "calcularParcela: taxa zero retorna NaN (divisao por zero na formula)"() {
        given: "taxa de juros zero para parcelamento"
        def valor = 1200.0
        def parcelas = 12

        when: "calculo parcela com taxa zero"
        def parcela = service.calcularParcela(valor, parcelas, 0)

        then: "formula divide por zero -> NaN (limitacao da formula atual)"
        parcela.isNaN()
    }

    def "calcularJuros: meses negativo"() {
        given: "meses negativos"
        def capital = 1000.0
        def taxa = 5.0
        def meses = -6

        when: "calculo juros com meses negativos"
        def resultado = service.calcularJuros(capital, taxa, meses)

        then: "resultado decresce (exponencial negativo)"
        resultado < capital
    }

    def "mock FinancialService com mockado"() {
        given: "um service mockado"
        def financialService = Mock(FinancialService)

        when: "chamo metodos mockados"
        def juros = financialService.calcularJuros(1000, 5, 12)
        def parcela = financialService.calcularParcela(5000, 24, 0.015)
        def relatorio = financialService.gerarRelatorioFinanceiro([])

        then: "interacoes verificadas"
        1 * financialService.calcularJuros(1000, 5, 12) >> 1795.86
        1 * financialService.calcularParcela(5000, 24, 0.015) >> 248.85
        1 * financialService.gerarRelatorioFinanceiro([]) >> [totalEntradas: 0, totalSaidas: 0, totalReembolsos: 0, saldo: 0, quantidade: 0]

        and: "retornos configurados"
        juros == 1795.86
        parcela == 248.85
        relatorio.totalEntradas == 0
    }

    def "stub FinancialService com retorno pre-definido"() {
        given: "um service stubbed"
        def financialService = Stub(FinancialService) {
            calcularJuros(_, _, _) >> 1500.0
            calcularParcela(_, _, _) >> 100.0
        }

        when: "chamo metodos do stub"
        def juros = financialService.calcularJuros(2000, 10, 6)
        def parcela = financialService.calcularParcela(3000, 18, 2.0)

        then: "retornos pre-definidos"
        juros == 1500.0
        parcela == 100.0
    }
}
