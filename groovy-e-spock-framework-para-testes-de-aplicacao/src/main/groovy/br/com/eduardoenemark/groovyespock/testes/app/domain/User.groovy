package br.com.eduardoenemark.groovyespock.testes.app.domain

import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString(includeNames = true, includePackage = false)
@CompileStatic
class User {

    String id
    String nome
    String email
    String role

    User(String id, String nome, String email, String role) {
        this.id = id
        this.nome = nome
        this.email = email
        this.role = role
    }

    boolean isAdmin() {
        return role == 'ADMIN'
    }

    boolean hasValidEmail() {
        return email ==~ /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/
    }
}
