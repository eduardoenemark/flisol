package br.com.eduardoenemark.groovyespock.testes.app.service

import br.com.eduardoenemark.groovyespock.testes.app.domain.User
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.stereotype.Service

@Service
class UserService {
    private static final Map<String, User> usersDatabase = [
        '1': new User('1', 'Ana Silva', 'ana@email.com', 'ADMIN'),
        '2': new User('2', 'Carlos Santos', 'carlos@email.com', 'USER'),
        '3': new User('3', 'Maria Oliveira', 'maria@email.com', 'USER')
    ]

    static Map<String, User> getDatabase() {
        return usersDatabase
    }

    User criarUsuario(String nome, String email, String role) {
        if (!nome || !nome.trim()) {
            throw new IllegalArgumentException('Nome é obrigatório')
        }
        if (!email || !email.trim()) {
            throw new IllegalArgumentException('Email é obrigatório')
        }

        def usuario = new User(
            (usersDatabase.size() + 1).toString(),
            nome,
            email,
            role
        )
        usersDatabase[usuario.id] = usuario
        return usuario
    }

    Optional<User> buscarPorId(String id) {
        return Optional.ofNullable(usersDatabase[id])
    }

    Optional<User> buscarPorEmail(String email) {
        def user = usersDatabase.values().findResult { it.email == email ? it : null }
        return Optional.ofNullable(user)
    }

    List<User> buscarTodos() {
        return usersDatabase.values() as List<User>
    }

    List<User> buscarAdmins() {
        return usersDatabase.values().findAll { it.role == 'ADMIN' } as List<User>
    }

    User atualizarUsuario(String id, Map<String, String> updates) {
        def usuario = usersDatabase[id]
        if (!usuario) {
            throw new NoSuchElementException("Usuário com id ${id} não encontrado")
        }

        usuario.nome = updates.get('Nome', usuario.nome)
        usuario.email = updates.get('Email', usuario.email)
        if (updates['Role']) {
            usuario.role = updates.get('Role')
        }

        return usuario
    }

    boolean removerUsuario(String id) {
        return usersDatabase.remove(id) != null
    }

    String usuariosParaJson() {
        JsonOutput.prettyPrint(JsonOutput.toJson(usersDatabase.values()))
    }

    User usuarioDeJson(String jsonStr) {
        def map = new JsonSlurper().parseText(jsonStr) as Map<String, String>
        return new User(
            map.get('id', (usersDatabase.size() + 1).toString()),
            map.get('Nome'),
            map.get('Email'),
            map.get('Role', 'USER')
        )
    }
}
