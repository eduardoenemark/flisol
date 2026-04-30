package br.com.eduardoenemark.groovyespock.testes.app.domain

import groovy.transform.CompileStatic
import groovy.transform.ToString

import java.time.LocalDateTime

@CompileStatic
@ToString(includeNames = true, includePackage = false)
class Transaction {

    String id
    User user
    double amount
    String type // DEBIT, CREDIT, REFUND
    LocalDateTime createdAt

    Transaction(String id, User user, double amount, String type, LocalDateTime createdAt) {
        this.id = id
        this.user = user
        this.amount = amount
        this.type = type
        this.createdAt = createdAt
    }

    boolean isPositive() {
        return amount > 0
    }

    boolean isRefund() {
        return type == 'REFUND'
    }

    String formatCurrency() {
        return "R\$ ${String.format('%,.2f', amount)}"
    }
}
