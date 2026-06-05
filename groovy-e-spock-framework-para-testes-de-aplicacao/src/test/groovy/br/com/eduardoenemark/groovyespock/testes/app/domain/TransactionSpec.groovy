package br.com.eduardoenemark.groovyespock.testes.app.domain

import br.com.eduardoenemark.groovyespock.testes.app.domain.User
import spock.lang.*

import java.time.LocalDateTime

@Title('Especificação de Transaction')
@Narrative('Testes de domain class Transaction com @CompileStatic e @ToString.')
class TransactionSpec extends Specification {

    @Shared
    def user = new User('1', 'Eduardo', 'eduardo@email.com', 'USER')

    def setup() {
        // Setup antes de cada teste
    }

    def cleanup() {
        // Nothing to clean up - @CompileStatic, immutable data
    }

    def "transaction positiva retorna true para isPositive"() {
        given: "uma transacao credito"
        def transaction = new Transaction(
                'TX-001',
                user,
                500.0,
                'CREDIT',
                LocalDateTime.of(2026, 6, 4, 10, 30)
        )

        expect: "valor positivo"
        transaction.isPositive() == true
    }

    def "transaction negativa retorna false para isPositive"() {
        given: "uma transacao debit com valor negativo"
        def transaction = new Transaction(
                'TX-002',
                user,
                -200.0,
                'DEBIT',
                LocalDateTime.of(2026, 6, 4, 11, 0)
        )

        expect: "valor negativo"
        transaction.isPositive() == false
    }

    def "transaction zero retorna false para isPositive"() {
        given: "uma transacao com valor zero"
        def transaction = new Transaction(
                'TX-003',
                user,
                0.0,
                'CREDIT',
                LocalDateTime.of(2026, 6, 4, 12, 0)
        )

        expect: "valor zero nao e positivo"
        transaction.isPositive() == false
    }

    def "transaction tipo REFUND retorna true para isRefund"() {
        given: "uma transacao reembolso"
        def transaction = new Transaction(
                'TX-004',
                user,
                150.0,
                'REFUND',
                LocalDateTime.of(2026, 6, 4, 13, 0)
        )

        expect: "tipo refund"
        transaction.isRefund() == true
    }

    def "transaction tipo CREDIT retorna false para isRefund"() {
        given: "uma transacao credito"
        def transaction = new Transaction(
                'TX-005',
                user,
                1000.0,
                'CREDIT',
                LocalDateTime.of(2026, 6, 4, 14, 0)
        )

        expect: "tipo credit nao e refund"
        transaction.isRefund() == false
    }

    def "transaction tipo DEBIT retorna false para isRefund"() {
        given: "uma transacao debit"
        def transaction = new Transaction(
                'TX-006',
                user,
                300.0,
                'DEBIT',
                LocalDateTime.of(2026, 6, 4, 15, 0)
        )

        expect: "tipo debit nao e refund"
        transaction.isRefund() == false
    }

    @Unroll
    def "formatCurrency formata #amount como #expected"() {
        given: "uma transacao com valor"
        def transaction = new Transaction(
                'TX-FMT',
                user,
                amount as double,
                'CREDIT',
                LocalDateTime.of(2026, 6, 4, 16, 0)
        )

        expect: "formatacao US locale (%,.2f)"
        transaction.formatCurrency() == expected

        where:
        amount     || expected
        100.0      || 'R$ 100.00'
        1500.50    || 'R$ 1,500.50'
        12345.67   || 'R$ 12,345.67'
        0.01       || 'R$ 0.01'
        -500.0     || 'R$ -500.00'
    }

    def "toString gera string formatada com @ToString"() {
        given: "uma transacao completa"
        def transaction = new Transaction(
                'TX-007',
                user,
                2500.0,
                'CREDIT',
                LocalDateTime.of(2026, 6, 4, 17, 0)
        )

        expect: "toString contem id e amount"
        transaction.toString().contains('id:TX-007')
        transaction.toString().contains('amount:2500.0')
        transaction.toString().contains('type:CREDIT')
    }

    def "transaction com user null"() {
        given: "uma transacao sem user"
        def transaction = new Transaction(
                'TX-NULL',
                null,
                100.0,
                'CREDIT',
                LocalDateTime.of(2026, 6, 4, 18, 0)
        )

        expect: "id e amount sao validos mesmo sem user"
        transaction.id == 'TX-NULL'
        transaction.amount == 100.0
        transaction.isPositive() == true
    }

    def "transaction com data futura"() {
        given: "uma transacao com data no futuro"
        def futureDate = LocalDateTime.of(2027, 1, 1, 0, 0)
        def transaction = new Transaction(
                'TX-FUTURE',
                user,
                500.0,
                'CREDIT',
                futureDate
        )

        expect: "data armazenada corretamente"
        transaction.createdAt == futureDate
    }

    def "transaction com data presente"() {
        given: "uma transacao com data atual"
        def now = LocalDateTime.now()
        def transaction = new Transaction(
                'TX-NOW',
                user,
                100.0,
                'DEBIT',
                now
        )

        expect: "data armazenada"
        transaction.createdAt != null
        transaction.createdAt.year == now.year
    }

    def "comparar transacoes credito vs debit (valores)"() {
        given: "duas transacoes opostas"
        def credit = new Transaction('C1', user, 500.0, 'CREDIT', LocalDateTime.of(2026, 6, 4, 10, 0))
        def debit = new Transaction('D1', user, -300.0, 'DEBIT', LocalDateTime.of(2026, 6, 4, 11, 0))

        expect: "credit positivo, debit negativo"
        credit.isPositive() == true
        debit.isPositive() == false
        credit.isRefund() == false
        debit.isRefund() == false
    }

    def "transaction com valor grande"() {
        given: "uma transacao com valor muito grande"
        def transaction = new Transaction(
                'TX-BIG',
                user,
                1_000_000.0 as double,
                'CREDIT',
                LocalDateTime.of(2026, 6, 4, 19, 0)
        )

        expect: "valor grande mantido"
        transaction.isPositive() == true
        transaction.formatCurrency().contains('1,000,000')
    }

    def "transaction com decimal preciso"() {
        given: "uma transacao com decimal preciso"
        def preciseValue = 123.456789 as double
        def transaction = new Transaction(
                'TX-PREC',
                user,
                preciseValue,
                'REFUND',
                LocalDateTime.of(2026, 6, 4, 20, 0)
        )

        expect: "refund com decimal"
        transaction.isRefund() == true
        transaction.isPositive() == true
    }

    def "transaction toString contem user info"() {
        given: "uma transacao com user"
        def transaction = new Transaction(
                'TX-USER',
                user,
                750.0,
                'CREDIT',
                LocalDateTime.of(2026, 6, 4, 21, 0)
        )

        expect: "toString contem user reference"
        transaction.toString().contains('user:')
    }

    def "transaction com tipo invalido"() {
        given: "uma transacao com tipo nao standard"
        def transaction = new Transaction(
                'TX-INVALID',
                user,
                100.0,
                'UNKNOWN',
                LocalDateTime.of(2026, 6, 4, 22, 0)
        )

        expect: "isRefund false, isPositive true"
        transaction.isRefund() == false
        transaction.isPositive() == true
        transaction.type == 'UNKNOWN'
    }

    def "toString contem all fields"() {
        given: "uma transacao completa"
        def transaction = new Transaction(
                'TX-FULL',
                user,
                4200.50,
                'CREDIT',
                LocalDateTime.of(2026, 6, 4, 23, 0)
        )

        expect: "toString contem todos campos"
        transaction.toString().contains('id:TX-FULL')
        transaction.toString().contains('user:')
        transaction.toString().contains('amount:4200.5')
        transaction.toString().contains('type:CREDIT')
        transaction.toString().contains('createdAt:2026-06-04T23')
    }

    def "multiple transactions same user"() {
        given: "multiples transacoes do mesmo user"
        def t1 = new Transaction('M1', user, 100.0, 'CREDIT', LocalDateTime.of(2026, 6, 4, 10, 0))
        def t2 = new Transaction('M2', user, 200.0, 'CREDIT', LocalDateTime.of(2026, 6, 4, 11, 0))
        def t3 = new Transaction('M3', user, -50.0, 'DEBIT', LocalDateTime.of(2026, 6, 4, 12, 0))

        expect: "todos com mesmo user"
        t1.user == user
        t2.user == user
        t3.user == user
    }
}
