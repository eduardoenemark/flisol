package br.com.eduardoenemark.groovyespock.testes.app.service

import br.com.eduardoenemark.groovyespock.testes.app.domain.Calculator
import br.com.eduardoenemark.groovyespock.testes.app.domain.Transaction
import org.springframework.stereotype.Service

@Service
class FinancialService {
    def calcularJuros(double capital, double taxaJuros, int meses) {
        capital * ((1 + taxaJuros / 100) ** meses)
    }

    def calcularParcela(double valor, int parcelas, double taxaMensal) {
        if (parcelas <= 0) throw new IllegalArgumentException('Parcelas devem ser positivas')
        if (valor <= 0) throw new IllegalArgumentException('Valor deve ser positivo')

        def fator = (taxaMensal * ((1 + taxaMensal) ** parcelas)) / (((1 + taxaMensal) ** parcelas) - 1)
        return valor * fator
    }

    def calcularDesconto(double valor, List<Integer> faixas, List<Double> descontos) {
        if (!faixas || !descontos || faixas.size() != descontos.size()) {
            throw new IllegalArgumentException('Faixas e descontos devem ter mesmo tamanho')
        }

        def melhorDesconto = 0 as BigDecimal
        for (int i = 0; i < faixas.size(); i++) {
            if (valor >= faixas[i]) {
                melhorDesconto = descontos[i] as BigDecimal
            }
        }

        return valor - (valor * melhorDesconto / 100)
    }

    def gerarRelatorioFinanceiro(List<Transaction> transacoes) {
        def totalEntradas = transacoes.findAll { it.type == 'CREDIT' }*.amount.sum() ?: 0
        def totalSaidas = transacoes.findAll { it.type == 'DEBIT' }*.amount.sum() ?: 0
        def totalReembolsos = transacoes.findAll { it.type == 'REFUND' }*.amount.sum() ?: 0

        [
            totalEntradas: totalEntradas,
            totalSaidas: totalSaidas,
            totalReembolsos: totalReembolsos,
            saldo: totalEntradas - totalSaidas + totalReembolsos,
            quantidade: transacoes.size()
        ]
    }
}
