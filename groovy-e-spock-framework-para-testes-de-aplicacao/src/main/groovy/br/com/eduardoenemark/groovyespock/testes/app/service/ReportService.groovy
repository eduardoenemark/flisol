package br.com.eduardoenemark.groovyespock.testes.app.service

import br.com.eduardoenemark.groovyespock.testes.app.domain.User
import org.springframework.stereotype.Service

@Service
class ReportService {
    String gerarRelatorioUsuarios(List<User> usuarios, Map<String, Integer> criterios) {
        def cabecalho = "=" * 60
        def cabecalho2 = "-" * 60

        StringBuilder relatorio = new StringBuilder()
        relatorio << "\n${cabecalho}\n"
        relatorio << "RELATÓRIO DE USUÁRIOS\n"
        relatorio << "Total de usuários: ${usuarios.size()}\n"

        if (criterios) {
            relatorio << "Critérios: ${criterios.collect { k, v -> "${k} > $v" }.join(', ')}\n"
            def filtrados = usuarios.findAll { usuario ->
                criterios.every { key, minValor ->
                    getValorPropriedade(usuario, key) > minValor
                }
            }
            relatorio << "Usuários filtrados: ${filtrados.size()}\n"
        }

        relatorio << "${cabecalho2}\n"

        usuarios.eachWithIndex { usuario, indice ->
            relatorio << String.format("%2d. %-20s %-30s [${usuario.role}]\n",
                indice + 1, usuario.nome, usuario.email)
        }

        relatorio << "${cabecalho}\n"
        return relatorio.toString()
    }

    private String getValorPropriedade(User usuario, String propriedade) {
        if (propriedade == 'Salario') {
            return '0'
        }
        return '0'
    }

    List<String> extrairEmails(List<User> usuarios) {
        return usuarios*.email
    }

    Map<String, List<String>> agruparPorRole(List<User> usuarios) {
        return usuarios.groupBy { it.role }.collectEntries { k, v -> [(k): v*.nome] }
    }
}
