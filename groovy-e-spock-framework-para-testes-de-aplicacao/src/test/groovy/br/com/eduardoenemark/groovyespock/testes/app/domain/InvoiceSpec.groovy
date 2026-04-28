package br.com.eduardoenemark.groovyespock.testes.app.domain

import spock.lang.Specification
import spock.lang.Shared
import spock.lang.Unroll

class InvoiceSpec extends Specification {

    @Shared List<Invoice> faturasCriadas = []

    def setupSpec() {
        println "=== setupSpec: inicializacao de recursos compartilhados ==="
        faturasCriadas = []
    }

    def cleanupSpec() {
        println "=== cleanupSpec: liberacao de recursos compartilhados ==="
        faturasCriadas.clear()
    }

    def setup() {
        println "--- setup: prepara estado para cada teste ---"
    }

    def cleanup() {
        println "--- cleanup: limpa estado apos cada teste ---"
        faturasCriadas.clear()
    }

    def "criar fatura inicial com status OPEN"() {
        given: "dados de uma nova fatura"
        def fatura = new Invoice("FAT-001", "Empresa ABC", 1500.0)

        expect: "fatura e criada corretamente"
        fatura.numero == "FAT-001"
        fatura.cliente == "Empresa ABC"
        fatura.valorBruto == 1500.0
        fatura.desconto == 0.0
        fatura.status == Invoice.Status.OPEN
        fatura.historico.size() == 1
        fatura.historico[0].contains("Fatura criada")
        fatura.calcularDesconto() == 0.0
        fatura.calcularTotal() == 1500.0
    }

    def "toString gera representacao formatada"() {
        given: "uma fatura criada"
        def fatura = new Invoice("INV-002", "ClienteXYZ", 500.0)

        expect: "toString inclui os dados da fatura"
        fatura.toString().contains("Invoice")
        fatura.toString().contains("numero:")
        fatura.toString().contains("INV-002")
        fatura.toString().contains("cliente:")
        fatura.toString().contains("ClienteXYZ")
    }

    @Unroll
    def "definir desconto #percentual percentual resulta em total #totalEsperado"() {
        given: "uma fatura com valor bruto fixo"
        def fatura = new Invoice("DESC-01", "Loja X", 100.0)

        expect: "validacao inicial"
        fatura.status == Invoice.Status.OPEN
        fatura.desconto == 0.0

        when: "defino o desconto"
        def resultado = fatura.definirDesconto(percentual)

        then: "desconto e aplicado corretamente"
        resultado == true
        fatura.desconto == percentual
        fatura.calcularTotal() == totalEsperado
        fatura.status == Invoice.Status.PENDING
        fatura.historico.size() == 2

        where:
        percentual || totalEsperado
        0          || 100.0
        10         || 90.0
        25         || 75.0
        50         || 50.0
        100        || 0.0
    }

    @Unroll
    def "calcularDesconto com percentual #percentual percentual"() {
        given: "uma fatura"
        def valorBase = valorBaseParam
        def descontoPerc = percentual

        expect: "crio a fatura"
        def fatura = new Invoice("CALC-01", "Teste", valorBaseParam)

        when: "aplico o desconto"
        def descontoCalculado = fatura.calcularDesconto()

        then: "sem desconto aplicado"
        descontoCalculado == 0.0

        when: "defino o desconto"
        fatura.definirDesconto(descontoPerc)
        def totalDepois = fatura.calcularDesconto()

        then: "desconto calculado"
        totalDepois == descontoEsperado

        where:
        valorBaseParam | percentual || descontoEsperado
        100.0          | 10         || 10.0
        500.0          | 20         || 100.0
        1000.0         | 5          || 50.0
        200.0          | 50         || 100.0
        10000.0        | 30         || 3000.0
    }

    @Unroll
    def "calcularTotal com desconto #descontoPerc% sobre #valorBaseParam"() {
        given: "uma fatura"
        def fatura = new Invoice("TOTAL-01", "Teste", valorBaseParam)

        expect: "crio a fatura com total igual ao bruto"
        fatura.calcularTotal() == valorBaseParam

        when: "defino o desconto"
        fatura.definirDesconto(descontoPerc)
        def totalCalculado = fatura.calcularTotal()

        then: "total calculado"
        totalCalculado == totalEsperado

        where:
        valorBaseParam | descontoPerc || totalEsperado
        100.0          | 10           | 90.0
        500.0          | 20           | 400.0
        1000.0         | 0            | 1000.0
        200.0          | 50           | 100.0
        100.0          | 100          | 0.0
    }

    def "definir desconto sobre fatura PENDING atualiza porem mantém status"() {
        given: "uma fatura com status PENDING e desconto aplicado"
        def fatura = new Invoice("PEND-01", "Cliente P", 800.0)
        fatura.definirDesconto(10)

        expect: "desconto inicial aplicado"
        fatura.desconto == 10.0
        fatura.status == Invoice.Status.PENDING

        when: "altero o desconto"
        fatura.definirDesconto(20)

        then: "desconto atualizado mantendo PENDING"
        fatura.status == Invoice.Status.PENDING
        fatura.desconto == 20.0
        fatura.historico.size() == 3
    }

    def "definir desconto em fatura PAGA e CANCELADA é negado"() {
        given: "uma fatura com status que nao permite desconto"
        def faturaPaga = new Invoice("PAGA-01", "Paga", 100.0)
        faturaPaga.definirDesconto(5)
        faturaPaga.pagar()
        def faturaCancelada = new Invoice("CANC-01", "Cancelada", 200.0)
        faturaCancelada.cancelar()

        expect: "fatura1 PENDING -> PAGA, fatura2 CANCELADA"
        faturaPaga.desconto == 5.0
        faturaPaga.status == Invoice.Status.PAID
        faturaCancelada.status == Invoice.Status.CANCELLED

        when: "tento aplicar desconto nas faturas pagas/canceladas"
        def resultadoPaga = faturaPaga.definirDesconto(10)
        def resultadoCancelada = faturaCancelada.definirDesconto(10)

        then: "descontos sao rejeitados"
        resultadoPaga == false
        resultadoCancelada == false
        faturaPaga.desconto == 5.0
        faturaPaga.status == Invoice.Status.PAID
        faturaCancelada.desconto == 0.0
        faturaCancelada.status == Invoice.Status.CANCELLED
    }

    def "definir desconto com percentual negativo lanza excecao"() {
        given: "uma fatura aberta"
        def fatura = new Invoice("NEG-01", "Teste", 100.0)

        when: "tento aplicar desconto negativo"
        fatura.definirDesconto(-5.0)

        then: "excecao e lancada"
        thrown(IllegalArgumentException)
    }

    def "definir desconto com percentual acima de 100 lanza excecao"() {
        given: "uma fatura aberta"
        def fatura = new Invoice("ACIMA-01", "Teste", 100.0)

        when: "tento aplicar desconto acima de 100%"
        fatura.definirDesconto(101.0)

        then: "excecao e lancada"
        thrown(IllegalArgumentException)
    }

    def "pagar fatura com status PENDING tem sucesso"() {
        given: "uma fatura pendente com desconto"
        def fatura = new Invoice("PAG-01", "Pagador", 500.0)
        fatura.definirDesconto(10)

        expect: "fatura no status PENDING"
        fatura.status == Invoice.Status.PENDING
        fatura.desconto == 10.0

        when: "paro a fatura"
        def resultado = fatura.pagar()

        then: "pagamento com sucesso"
        resultado == true
        fatura.status == Invoice.Status.PAID
        fatura.historico.size() == 3
    }

    def "pagar fatura com status OPEN nao permite pagamento"() {
        given: "uma fatura ainda aberta sem desconto"
        def fatura = new Invoice("PAG-OPEN", "Cliente", 500.0)

        expect: "fatura no status OPEN"
        fatura.status == Invoice.Status.OPEN

        when: "tento pagar"
        def resultado = fatura.pagar()

        then: "pagamento negado"
        resultado == false
        fatura.status == Invoice.Status.OPEN
    }

    def "pagar fatura com status PAIDA nao permite pagamento duplicado"() {
        given: "uma fatura ja paga"
        def fatura = new Invoice("PAG-02", "Cliente", 300.0)
        fatura.definirDesconto(0)
        fatura.pagar()

        expect: "fatura ja no status PAGA"
        fatura.status == Invoice.Status.PAID

        when: "tento pagar novamente"
        def resultado = fatura.pagar()

        then: "pagamento negado"
        resultado == false
        fatura.status == Invoice.Status.PAID
    }

    def "pagar fatura com status CANCELADA nao permite pagamento"() {
        given: "uma fatura cancelada"
        def fatura = new Invoice("PAG-CANC", "Cliente", 400.0)
        fatura.cancelar()

        expect: "fatura no status CANCELADA"
        fatura.status == Invoice.Status.CANCELLED

        when: "tento pagar"
        def resultado = fatura.pagar()

        then: "pagamento negado"
        resultado == false
        fatura.status == Invoice.Status.CANCELLED
    }

    def "pagar fatura PENDING gera registro no historico"() {
        given: "uma fatura pendente com desconto"
        def fatura = new Invoice("HIST-PAG", "Historico", 1000.0)
        fatura.definirDesconto(15)

        expect: "status PENDING"
        fatura.status == Invoice.Status.PENDING

        when: "paro a fatura"
        fatura.pagar()

        then: "historico tem registro de pagamento"
        fatura.status == Invoice.Status.PAID
        fatura.historico.size() == 3
        fatura.historico[2].contains("Fatura paga")
        fatura.historico[2].contains("Total")
    }

    def "cancelar fatura com status OPEN tem sucesso"() {
        given: "uma fatura aberta"
        def fatura = new Invoice("CANC-01", "Cancelador", 300.0)

        expect: "fatura no status OPEN"
        fatura.status == Invoice.Status.OPEN

        when: "cancelo a fatura"
        def resultado = fatura.cancelar()

        then: "cancelamento com sucesso"
        resultado == true
        fatura.status == Invoice.Status.CANCELLED
    }

    def "cancelar fatura com status PENDING tem sucesso"() {
        given: "uma fatura pendente"
        def fatura = new Invoice("CANC-02", "Cancelador", 500.0)
        fatura.definirDesconto(5)

        expect: "fatura no status PENDING"
        fatura.status == Invoice.Status.PENDING

        when: "cancelo a fatura"
        def resultado = fatura.cancelar()

        then: "cancelamento com sucesso"
        resultado == true
        fatura.status == Invoice.Status.CANCELLED
    }

    def "cancelar fatura PAIDA é rejeitado"() {
        given: "uma fatura paga"
        def fatura = new Invoice("NÃO-CANC", "Teste", 500.0)
        fatura.definirDesconto(0)
        fatura.pagar()

        expect: "fatura no status PAGA"
        fatura.status == Invoice.Status.PAID

        when: "tento cancelar"
        def resultado = fatura.cancelar()

        then: "cancelamento rejeitado"
        resultado == false
        fatura.status == Invoice.Status.PAID
        fatura.historico.size() >= 3
        fatura.isReembolsavel() == true
    }

    def "cancelar fatura CANCELADA nao permite cancelamento duplicado"() {
        given: "uma fatura ja cancelada"
        def fatura = new Invoice("CANC-03", "Cliente", 200.0)
        fatura.cancelar()

        expect: "fatura no status CANCELADA"
        fatura.status == Invoice.Status.CANCELLED
        fatura.historico.size() == 2

        when: "tento cancelar novamente"
        def resultado = fatura.cancelar()

        then: "cancelamento rejeitado"
        resultado == false
        fatura.status == Invoice.Status.CANCELLED
    }

    @Unroll
    def "isReembolsavel para status #statusNome retorna #esperado"() {
        given: "uma fatura"
        def fatura = new Invoice("REEMB-01", "Reembolso", 100.0)
        configurarStatus(fatura, statusNome)

        expect: "isReembolsavel retorna valor correto"
        fatura.isReembolsavel() == esperado

        where:
        statusNome  || esperado
        "OPEN"      || false
        "PENDING"   || false
        "PAID"      || true
        "CANCELLED" || false
    }

    def configurarStatus(Invoice invoice, String statusNome) {
        switch (statusNome) {
            case "PENDING":
                invoice.definirDesconto(0)
                break
            case "PAID":
                invoice.definirDesconto(0)
                invoice.pagar()
                break
            case "CANCELLED":
                invoice.cancelar()
                break
            default:
                invoice.status = Invoice.Status.OPEN
                break
        }
    }

    def "getFormatarDescricao para status OPEN"() {
        given: "uma fatura aberta"
        def fatura = new Invoice("DESCR-01", "Cliente Demo", 300.0)

        expect: "descricao formatada"
        fatura.getFormatarDescricao().contains("DESCR-01")
        fatura.getFormatarDescricao().contains("Cliente Demo")
        fatura.getFormatarDescricao().contains("Aberta")
    }

    def "getFormatarDescricao para status PENDING"() {
        given: "uma fatura pendente"
        def fatura = new Invoice("DESCR-02", "Cliente Demo", 300.0)
        fatura.definirDesconto(10)

        expect: "descricao formatada com desconto"
        fatura.getFormatarDescricao().contains("Pendente")
    }

    def "getFormatarDescricao para status PAID"() {
        given: "uma fatura paga"
        def fatura = new Invoice("DESCR-03", "Cliente Demo", 300.0)
        fatura.definirDesconto(0)
        fatura.pagar()

        expect: "descricao formatada como Paga"
        fatura.getFormatarDescricao().contains("Paga")
    }

    def "getFormatarDescricao para status CANCELLED"() {
        given: "uma fatura cancelada"
        def fatura = new Invoice("DESCR-04", "Cliente Demo", 300.0)
        fatura.cancelar()

        expect: "descricao formatada como Cancelada"
        fatura.getFormatarDescricao().contains("Cancelada")
    }

    def "fluxo completo: criar -> aplicar desconto -> pagar"() {
        given: "uma fatura nova"
        def fatura = new Invoice("FLUXO-01", "Fluxo Cliente", 1000.0)

        expect: "status inicial"
        fatura.status == Invoice.Status.OPEN
        fatura.calcularTotal() == 1000.0

        when: "aplico desconto de 10%"
        fatura.definirDesconto(10)

        then: "status muda para PENDING"
        fatura.status == Invoice.Status.PENDING
        fatura.desconto == 10.0

        when: "paro a fatura"
        def resultado = fatura.pagar()

        then: "fatura finalizada"
        resultado == true
        fatura.status == Invoice.Status.PAID
        fatura.calcularTotal() == 900.0
        fatura.historico.size() == 3
        fatura.isReembolsavel() == true
    }

    def "fluxo completo: criar -> cancelar"() {
        given: "uma fatura nova"
        def fatura = new Invoice("CANCEL-F", "Cliente Cancel", 500.0)

        expect: "status inicial"
        fatura.status == Invoice.Status.OPEN

        when: "cancelo a fatura"
        def resultado = fatura.cancelar()

        then: "fatura cancelada"
        resultado == true
        fatura.status == Invoice.Status.CANCELLED
        fatura.desconto == 0.0
        fatura.calcularTotal() == 500.0
        fatura.isReembolsavel() == false
    }

    def "fluxo completo: criar -> desconto -> PENDING -> sem pagar depois cancelar"() {
        given: "uma fatura com desconto"
        def fatura = new Invoice("FLUXO-03", "Cliente F", 2000.0)
        fatura.definirDesconto(15)

        expect: "fatura pendente"
        fatura.status == Invoice.Status.PENDING
        fatura.desconto == 15.0

        when: "cancelo sem pagar"
        def resultado = fatura.cancelar()

        then: "cancelamento permitido"
        resultado == true
        fatura.status == Invoice.Status.CANCELLED
    }

    def "mock e stubbing com InvoiceService mockado"() {
        given: "um servico de faturas mockado"
        def servicoFaturas = Mock(InvoiceService)

        when: "busco faturas via servico mockado"
        def faturas = servicoFaturas.buscarFaturasPorCliente("Cliente X")

        then: "interacao verificada e retorno configurado"
        1 * servicoFaturas.buscarFaturasPorCliente("Cliente X") >> [
            new Invoice("INV-100", "Cliente X", 200.0),
            new Invoice("INV-101", "Cliente X", 300.0)
        ]

        and: "retorno configurado"
        faturas.size() == 2
        faturas[0].cliente == "Cliente X"
    }

    def "stub com comportamento pre-definido para InvoiceService"() {
        given: "um servico de faturas stubbed"
        def servicoFaturas = Stub(InvoiceService) {
            getTaxaAdmin() >> 2.5
            getDiasVencimento() >> 30
        }

        when: "chamo metodos do Stub"
        def taxa = servicoFaturas.getTaxaAdmin()
        def dias = servicoFaturas.getDiasVencimento()

        then: "retornos configurados"
        taxa == 2.5
        dias == 30
    }

    def "mock verificar zero chamadas"() {
        given: "um servico de faturas mockado"
        def servicoFaturas = Mock(InvoiceService)

        when: "nao chamo nenhum metodo"

        then: "nenhuma interacao ocorreu"
        0 * servicoFaturas.cancelarFatura(_)
    }

    def "multiplos invoices com @Shared shared list"() {
        given: "invoices para adicionar a lista compartilhada"
        def f1 = new Invoice("SH-01", "Shared1", 100.0)
        def f2 = new Invoice("SH-02", "Shared2", 200.0)
        def f3 = new Invoice("SH-03", "Shared3", 300.0)

        when: "adiciono a lista compartilhada"
        faturasCriadas << f1 << f2 << f3

        then: "lista tem todas as faturas"
        faturasCriadas.size() == 3
        faturasCriadas*.numero == ["SH-01", "SH-02", "SH-03"]
    }

    def "múltiplos invoices e verificacoes de status"() {
        given: "multiplos invoices"
        def f1 = new Invoice("MULTI-01", "Cliente A", 100.0)
        def f2 = new Invoice("MULTI-02", "Cliente B", 200.0)
        def f3 = new Invoice("MULTI-03", "Cliente C", 300.0)
        f2.definirDesconto(10)
        f2.pagar()

        expect: "f1 aberta, f2 paga, f3 aberta"
        f1.status == Invoice.Status.OPEN
        f2.status == Invoice.Status.PAID
        f3.status == Invoice.Status.OPEN

        and: "descontos corretos"
        f1.desconto == 0.0
        f2.desconto == 10.0
        f3.desconto == 0.0
    }
}

class InvoiceService {
    List<Invoice> buscarFaturasPorCliente(String cliente) { return [] }
    double getTaxaAdmin() { return 0.0 }
    int getDiasVencimento() { return 0 }
    boolean cancelarFatura(String numero) { return false }
}
