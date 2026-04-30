package br.com.eduardoenemark.groovyespock.testes.app.domain

import groovy.transform.ToString

@ToString(includeNames = true, includePackage = false)
class Invoice {

    String numero
    String cliente
    double valorBruto
    double desconto
    Status status
    List<String> historico

    enum Status {
        OPEN("Aberta"),
        PENDING("Pendente"),
        PAID("Paga"),
        CANCELLED("Cancelada")

        final String descricao

        Status(String descricao) {
            this.descricao = descricao
        }
    }

    Invoice(String numero, String cliente, double valorBruto) {
        this.numero = numero
        this.cliente = cliente
        this.valorBruto = valorBruto
        this.desconto = 0.0
        this.status = Status.OPEN
        this.historico = []
        this.historico.add('Fatura criada - Status: ' + status.descricao + ', Valor: R$ ' + formatarMoeda(valorBruto))
    }

    boolean definirDesconto(double percentual) {
        if (percentual < 0 || percentual > 100) {
            throw new IllegalArgumentException('Percentual de desconto deve estar entre 0 e 100')
        }
        boolean statusPermitido
        switch (status) {
            case Status.OPEN:
            case Status.PENDING:
                statusPermitido = true
                break
            default:
                historico.add('Rejeicao de desconto - Fatura ' + status.descricao)
                return false
        }

        this.desconto = percentual
        switch (status) {
            case Status.OPEN:
                status = Status.PENDING
                break
        }
        historico.add('Desconto de ' + percentual + '% definido - Valor liquido: R$ ' + formatarMoeda(calcularTotal()))
        return true
    }

    double calcularDesconto() {
        return valorBruto * (desconto / 100)
    }

    double calcularTotal() {
        return valorBruto - calcularDesconto()
    }

    boolean pagar() {
        if (status != Status.PENDING) {
            historico.add('Pagamento rejeitado - Status atual: ' + status.descricao)
            return false
        }
        status = Status.PAID
        historico.add('Fatura paga - Status: ' + status.descricao + ', Total: R$ ' + formatarMoeda(calcularTotal()))
        return true
    }

    boolean cancelar() {
        if (status == Status.PAID) {
            historico.add('Cancelamento rejeitado - Fatura paga')
            return false
        }
        if (status == Status.CANCELLED) {
            return false
        }
        def antigoStatus = status
        status = Status.CANCELLED
        historico.add('Fatura cancelada - Status anterior: ' + antigoStatus.descricao)
        return true
    }

    boolean isReembolsavel() {
        return status == Status.PAID
    }

    boolean isReembolsado() {
        return false
    }

    String getFormatarDescricao() {
        switch (status) {
            case Status.OPEN: return numero + ' - ' + cliente + ' - Aberta'
            case Status.PENDING: return numero + ' - ' + cliente + ' - Pendente (' + desconto.intValue() + '% desc.)'
            case Status.PAID: return numero + ' - ' + cliente + ' - Paga'
            case Status.CANCELLED: return numero + ' - ' + cliente + ' - Cancelada'
            default: return numero + ' - ' + cliente + ' - Desconhecida'
        }
    }

    private static String formatarMoeda(double valor) {
        String.format('%,.2f', valor)
    }
}
