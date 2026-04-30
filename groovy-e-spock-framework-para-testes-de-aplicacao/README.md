# Groovy & Spock Framework para Testes de Aplicação <font size="2">versão 1.2 - Flisol 2026</font>

![alt Flisol](resources/imgs/flisol.png "FliSol")

- [Groovy \& Spock Framework para Testes de Aplicação versão 1.1 - Flisol 2026](#groovy--spock-framework-para-testes-de-aplicação-versão-11---flisol-2026)
  - [Apresentação Pessoal](#apresentação-pessoal)
  - [Introdução ao Groovy](#introdução-ao-groovy)
    - [Versões Java suportadas](#versões-java-suportadas)
  - [Sintaxe e Destaques da Linguagem Groovy](#sintaxe-e-destaques-da-linguagem-groovy)
    - [Comentários](#comentários)
    - [Identificadores](#identificadores)
    - [Strings e Interpolação](#strings-e-interpolação)
  - [Operadores e Funções Booleanas](#operadores-e-funções-booleanas)
    - [Operadores Especiais do Groovy](#operadores-especiais-do-groovy)
    - [Verdade Booleana no Groovy](#verdade-booleana-no-groovy)
  - [Coleções e Mapas em Groovy](#coleções-e-mapas-em-groovy)
    - [Listas Ricas](#listas-ricas)
    - [Mapas e Operações](#mapas-e-operações)
    - [Tipos Primitivos e Derivados](#tipos-primitivos-e-derivados)
    - [Tipos numéricos avançados (Java vs Groovy)](#tipos-numéricos-avançados-java-vs-groovy)
  - [Impactos do uso de @CompileStatic](#impactos-do-uso-de-compilestatic)
    - [Closures](#closures)
    - [Currying](#currying)
    - [Memoization](#memoization)
    - [Composition](#composition)
  - [Metaprogramação e DSLs](#metaprogramação-e-dsls)
    - [Métodos Mágicos: methodMissing](#métodos-mágicos-methodmissing)
    - [AST Transformations](#ast-transformations)
  - [O que é Spock Framework?](#o-que-é-spock-framework)
  - [Por que usar Groovy com Spock?](#por-que-usar-groovy-com-spock)
  - [Estrutura do Spock Framework](#estrutura-do-spock-framework)
    - [Anotações úteis do Spock](#anotações-úteis-do-spock)
  - [Relatórios de Testes com Spock](#relatórios-de-testes-com-spock)
    - [Relatório JUnit XML (padrão)](#relatório-junit-xml-padrão)
    - [spock-reports (HTML detalhado)](#spock-reports-html-detalhado)
    - [Integração com CI (ex. Jenkins)](#integração-com-ci-ex-jenkins)
  - [Data-Driven Testing com Spock](#data-driven-testing-com-spock)
  - [Mocking e Stubbing no Spock](#mocking-e-stubbing-no-spock)
  - [Ciclo de Vida e Hooks do Spock](#ciclo-de-vida-e-hooks-do-spock)
  - [Exemplo Prático Integrado](#exemplo-prático-integrado)
    - [Código Principal (`src/main/groovy/Calculator.groovy`)](#código-principal-srcmaingroovycalculatorgroovy)
    - [Especificação de Testes (`src/test/groovy/CalculatorSpec.groovy`)](#especificação-de-testes-srctestgroovycalculatorspecgroovy)
  - [Exemplos Práticos do Projeto](#exemplos-práticos-do-projeto)
    - [1. Domain Classes com Groovy (@CompileStatic, @ToString)](#1-domain-classes-com-groovy-compilestatic-tostring)
      - [`Account` – Conta bancária](#account--conta-bancária)
      - [`Invoice` – Fatura com status transitions](#invoice--fatura-com-status-transitions)
    - [2. Validation e Business Rules](#2-validation-e-business-rules)
      - [`User` – Validação de email](#user--validação-de-email)
    - [3. Service Layer e JSON](#3-service-layer-e-json)
      - [`UserService` – CRUD com JSON](#userservice--crud-com-json)
      - [`ReportService` – Relatórios com closures](#reportservice--relatórios-com-closures)
    - [4. FinancialService – Cálculos Financeiros](#4-financialservice--cálculos-financeiros)
    - [5. Spring Boot REST Controller](#5-spring-boot-rest-controller)
    - [6. Testes de Integração](#6-testes-de-integração)
      - [`AccountSpec` – Testes de conta bancária](#accountspec--testes-de-conta-bancária)
      - [`InvoiceSpec` – Testes de fatura](#invoicespec--testes-de-fatura)
      - [`UserSpec` – Testes de validação de email](#userspec--testes-de-validação-de-email)
      - [`UserServiceSpec` – Testes de CRUD e JSON](#userservicespec--testes-de-crud-e-json)
      - [`ReportServiceSpec` – Testes de relatórios](#reportservicespec--testes-de-relatórios)
  - [Conclusões](#conclusões)
    - [Pontos Principais](#pontos-principais)
    - [Recomendações Práticas](#recomendações-práticas)
    - [Referências](#referências)

---

## Apresentação Pessoal

Sou o Eduardo Vieira, Analista Programador. Considero-me +1 curioso da Computação desde 2004. Trabalhei no desenvolvimento de vários sistemas em âmbito distrital, federal e privado usando Java, Groovy, PHP, Javascript, PL/SQL, Shell Script, AWK <i>et al</i>, além de banco de dados como MySQL, Oracle <i>et al</i>. Ex-docente do IFB. Atualmente trabalho na TI do sistema bancário entre os vários projetos estou, também, no PIX. Precisei(o) escovar muitos bits para resolver problemas de performance e de baixo nível.

**Telegram/X: @eduardoenemark**

---

## Introdução ao Groovy

**O que é Groovy?** É uma linguagem de programção dinâmica construída para a plataforma Java (JVM). Criada por James Strachan em 2003, o Groovy permite escrever código mais conciso do que Java enquanto mantém compatibilidade total com todas as bibliotecas Java existentes.

**Características principais:**

- Tipagem opcional (você pode usar `def` ou declarar tipos explicitamente)
- Sintaxe enxuta remove a necessidade de "boilerplate" code
- Closures nativas que facilitam programação funcional
- Integração perfeita com ecossistema Java

> Groovy é como um "Java mais flexível" — você aproveita tudo que conhece no Java, mas escreve menos código para fazer coisas complexas.

A linguagem permite criar scripts simples em poucos minutos ou sistemas enterprise robustos. É amplamente usada no desenvolvimento de DSLs (Domain Specific Languages), automação com Gradle, e aplicações Apache Grails.

### Versões Java suportadas

- Java 8 (Groovy 2.x)
- Java 11 (Groovy 3.x)
- Java 17 (Groovy 4.x)
- Java 21 (Groovy 5.x)

---

## Sintaxe e Destaques da Linguagem Groovy

### Comentários

Comentários em Groovy seguem padrões similares ao Java:

```groovy
// Comentário de linha única
/* Comentário de várias linhas */
/** 
 * Comentário Groovydoc usado para documentação 
 * de classes, métodos e propriedades
 */
```

### Identificadores

Identificadores podem começar com letras, `$` ou `_`. Contém letras, dígitos, `$` ou `_`:

```groovy
def nomeVar = 10
def $_variavelPrivada = 20
def variable_with_underscores = 30
```

### Strings e Interpolação
Groovy suporta múltiplos tipos de strings:

- **Single quotes `'texto'`** — String literal simples, sem interpolação
- **Double quotes `"texto ${expr}"`** — GString com interpolação de variáveis
- **Triple quotes `'''texto'''`** — Strings multiline simples
- **Slashy `/regex/`** — Útil para regex (evita escaping de `\\`)
- **Dollar-slashy `$'/texto/$'`** — Permite `$` e `/` sem escape

```groovy
def name = 'Ana'
println "Olá $name!" // → Olá Ana!
println "Valor: ${2+3}" // → Valor: 5

// Multiline com interpolação
def city = 'São Paulo'
def message = """
Bem-vindo a ${city}!
Hoje faz sol e temperatura é de 28°C.
"""

// Regex sem excesso de backslashes
def pattern = ~/[0-9]+\.[0-9]+/
assert '123.456' ==~ pattern
```

---

## Operadores e Funções Booleanas

### Operadores Especiais do Groovy

Groovy adiciona vários operadores úteis à sintaxe Java:

- **Elvis operator `a ?: b`** — Retorna `a` se não for falsy, senão retorna `b`
- **Safe navigation `a?.b`** — Evita NullPointerException acessando propriedades
- **Spread operator `list*.method()`** — Chama method em cada elemento da lista
- **Ranges `1..5`** — Intervalo inclusivo
- **Power `**`** — Cálculo de potência com inferência inteligente de tipo

```groovy
// Elvis operator - valor alternativo quando vazio
def nome = ''
def nomeSeguro = nome ?: 'Anônimo'
assert nomeSeguro == 'Anônimo'

// Safe navigation - não lança NPE
def pessoa = null
assert pessoa?.nome == null

// Spread operator - transforma lista inteira
def numeros = [1, 2, 3]
assert numeros*.toString() == ['1', '2', '3']

// Ranges
assert (1..5).contains(3)
assert (5 > 3) && (2 < 4)
```

### Verdade Booleana no Groovy

No Groovy, qualquer objeto pode ser avaliado em contexto booleano:

| Valor | Resultado Booleano |
|-------|--------------------|
| `null` | `false` |
| `false` | `false` |
| `0` | `false` |
| `''` (string vazia) | `false` |
| `[]` (lista vazia) | `false` |
| `[:]` (mapa vazio) | `false` |
| Qualquer outro valor | `true` |

```groovy
assert !null
assert !false
assert !0
assert !''
assert ![]
assert ![:]
assert 'groovy' as boolean
assert [1, 2, 3] as boolean
assert -1 as boolean
```

---

## Coleções e Mapas em Groovy

### Listas Ricas

Listas no Groovy são muito mais do que Arrays Java:

```groovy
def numeros = [1, 2, 3, 4, 5]

// Adicionar elemento
numeros << 6  // [1, 2, 3, 4, 5, 6]

// Operações poderosas
assert numeros.collect { it * 2 } == [2, 4, 6, 8, 10, 12]
assert numeros.findAll { it > 2 } == [3, 4, 5, 6]

def grupos = numeros.groupBy { it % 2 }
assert grupos == [0: [2, 4, 6], 1: [1, 3, 5]]

// Métodos utilitários
assert numeros.find { it > 3 } == 4
assert numeros.every { it < 10 }
assert numeros.any { it > 4 }
assert numeros.sum() == 21
assert numeros.max() == 6
assert numeros.min() == 1

// Criação com ranges
def pares = (0..10).step(2)    // [0, 2, 4, 6, 8, 10]
assert pares == [0, 2, 4, 6, 8, 10]

def reverse = (10..1) // [10, 9, ..., 1]
assert reverse == [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
```

### Mapas e Operações

Mapas no Groovy oferecem sintaxe flexível:

```groovy
// Criação de mapas
def cores = [red: '#FF0000', green: '#00FF00']
assert cores.red == '#FF0000'
assert cores['green'] == '#00FF00'

// Adicionar novo entry
cores.yellow = '#FFFF00'
assert cores.yellow == '#FFFF00'

// Operações avançadas
def pessoas = [
        ana: [idade: 30, cidade: 'SP'],
        joao: [idade: 25, cidade: 'RJ']
]

def jovens = pessoas.findAll { _, pessoa -> pessoa.idade < 30 }
assert jovens == [joao: [idade: 25, cidade: 'RJ']]
```

---

### Tipos Primitivos e Derivados

**Tipos exclusivos do Groovy (não presentes no Java):**

- **GString** – strings interpoladas que avaliam expressões Groovy (`"Olá $nome"`).
- **Range** – sequência de valores usando `..` ou `..<` (`0..10`).
- **List** – lista literal com colchetes (`[1,2,3]`).
- **Map** – mapa literal com chaves e valores (`[nome:'Ana', idade:30]`).
- **Closure** – bloco de código reutilizável (`{ it * 2 }`).
- **ExpandoMetaClass** – permite adicionar métodos ou propriedades a classes existentes em tempo de execução.

```groovy
def nome = 'Ana'
def saudacao = "Olá $nome!"           // GString
def intervalo = 1..5                  // Range
def lista = [1, 2, 3]                 // List
def mapa = [cidade: 'SP', estado: 'São Paulo'] // Map
def dobrar = { int x -> x * 2 }       // Closure
String.metaClass.sayHello = { -> "Hi!" } // ExpandoMetaClass
assert saudacao == 'Olá Ana!'
assert intervalo.contains(3)
assert lista.sum() == 6
assert mapa.cidade == 'SP'
assert dobrar(4) == 8
assert 'test'.sayHello() == 'Hi!'
```

```groovy
def populacao = 1_500_000_000
def pi = 3.141_592_653

def hexadecimal = 0x1F
def binario = 0b1010

assert 10 as BigInteger == new BigInteger('10')
assert '100' as int == 100
assert 3.14 as BigDecimal == new BigDecimal('3.14')
```

### Tipos numéricos avançados (Java vs Groovy)

Groovy introduz literais para **BigInteger** e **BigDecimal**, que não possuem sintaxe direta em Java.

- **BigInteger** usa o sufixo `G` (ex.: `123G`).
- **BigDecimal** pode ser escrito como número decimal comum ou com sufixo `G` (`123.45G`).

Esses tipos permitem trabalhar com valores de precisão arbitrária sem precisar criar explicitamente objetos.

```groovy
def populacao = 1_500_000_000       // int literal
def pi = 3.141_592_653               // double literal
def bigInt = 123G                    // BigInteger
def bigDec = 123.45G                 // BigDecimal
def hexadecimal = 0x1F
def binario = 0b1010

assert bigInt instanceof java.math.BigInteger
assert bigDec instanceof java.math.BigDecimal
assert bigInt == new BigInteger('123')
assert bigDec == new BigDecimal('123.45')
assert 10 / 2 == new BigDecimal('5')
assert 10.0 / 2 == 5.0
assert 2 ** 3 == 8
assert 2.0 ** 3 == 8.0
//assert (2G ** 100) == (2 ^ 100G)
```

---

## Impactos do uso de @CompileStatic

A anotação `@CompileStatic` instrui o compilador Groovy a aplicar compilação estática ao código, trazendo alguns benefícios, mas também alguns custos:

| Benefício | Custo/Perca |
|-----------|-------------|
| **Performance**: o bytecode gerado costuma ser mais rápido, comparável ao Java puro. | **Perda de Dinamicidade**: recursos dinâmicos como `methodMissing`, `propertyMissing`, e chamadas dinâmicas são desativados ou geram erros de compilação.
| **Detecção precoce de erros**: tipos incompatíveis ou chamadas inexistentes são identificados em tempo de compilação. | **Reflexão limitada**: acesso a propriedades/métodos via GPath ou metaprogramação pode falhar ou exigir código adicional.
| **Integração com IDEs**: melhor autocomplete e navegação. | **Tempo de compilação maior**: o compilador analisa tipos e gera código estático, aumentando o tempo de build.
| **Compatibilidade com APIs Java**: chamadas a APIs Java são verificadas em tempo de compilação, reduzindo chamadas errôneas. | **Restrição de tipos genéricos**: pode exigir cast explícitos e reduzir a flexibilidade de tipos inferidos.

**Quando usar:**

- Código de produção crítico onde performance é essencial.
- Bibliotecas estáveis onde a API não muda frequentemente.
- Quando se deseja maior segurança de tipo.

**Quando evitar:**

- Scripts, protótipos ou código que depende fortemente de metaprogramação, DSLs ou extensões dinâmicas.
- Projetos que precisam de flexibilidade para evoluir rapidamente.

**Exemplo:**

```groovy
import groovy.transform.CompileStatic

@CompileStatic
class Calculadora {
  int somar(int a, int b) { a + b }
  // método dinâmico desaparece em compilação estática
  def methodMissing(String name, args) { /* não será chamado */ }

  static void main(String[] args) {
    def calc = new Calculadora()
    println calc.somar(3, 4) // Imprime 7
    println calc.methodMissing('foo', []) // Imprime null, não é chamado
  }
}
```

Nesse caso, a tentativa de chamar um método inexistente resultará em erro de compilação, ao contrário do modo dinâmico onde o `methodMissing` poderia ser usado.

---

### Closures

**Explicação:** São blocos de código anônimos que podem ser atribuídos a variáveis, passados como parâmetros e executados posteriormente. Elas capturam o escopo onde foram criadas, permitindo acesso a variáveis externas (lexical scope). Em Groovy, closures são objetos da classe `groovy.lang.Closure` e suportam sintaxe curta, parâmetros implícitos (`it`) e delegação.

```groovy
// Atribuição a variável
def saudacao = { name -> "Olá, $name!" }
assert saudacao('Maria') == 'Olá, Maria!'

// Parâmetro implícito 'it'
def dobro = { it * 2 }
assert dobro(5) == 10

//Closure sem parâmetros
def piada = { -> 'Por que o programador trocou o 0 pelo 1? Porque ele estava no modo debugger.' }
assert piada() != null

// Closure capturando variável externa (lexical scope)
def mensagem = 'Bem-vindo'
def saudacaoPersonalizada = { nome -> "$mensagem, $nome!" }
assert saudacaoPersonalizada('João') == 'Bem-vindo, João!'

// passando closure como parâmetro
def processar(int a, int b, Closure op) {
    op(a, b)
}
def somar = { x, y -> x + y }
assert processar(3, 4, somar) == 7
```

**Uso comuns em coleções:**

```groovy
def numeros = [1, 2, 3, 4, 5]

// collect: transforma cada elemento
def duplicados = numeros.collect { it * 2 }
assert duplicados == [2, 4, 6, 8, 10]

// findAll: filtra elementos
def pares = numeros.findAll { it % 2 == 0 }
assert pares == [2, 4]

// each: itera sobre elementos
def soma = 0
numeros.each { soma += it }
assert soma == 15

// sort: ordena por propriedade
def words = ['kotlin', 'java', 'groovy', 'scala']
assert words.sort { it.length() } == ['java', 'scala', 'kotlin', 'groovy']
```

---

### Currying

**Explicação:** *Currying* permite *pré‑definir* um ou mais parâmetros de uma closure, devolvendo uma nova closure que aceita menos argumentos. Isso facilita a criação de funções mais específicas a partir de funções genéricas.

```groovy
def multiply = { a, b -> a * b }
def doubleIt = multiply.curry(2)
assert doubleIt(5) == 10

def rcurryAdd = { a, b -> a + b }.rcurry(10)
assert rcurryAdd(5) == 15
```

---

### Memoization

**Explicação:** *Memoization* guarda o resultado de chamadas de uma closure em cache, evitando recomputação quando os mesmos argumentos são usados novamente – útil para funções custosas ou recursivas.

```groovy
def slowCalc = { n -> 
    sleep(100)
    n * 2 
}.memoize()
assert slowCalc(5) == 10
assert slowCalc(5) == 10 // cached
```

---

### Composition

**Explicação:** *Composition* combina duas closures encadeando‑as (ex.: `addOne << doubleIt`) para produzir uma nova função:

```groovy
def addOne = { it + 1 }
def doubleIt = { it * 2 }
def composition = addOne << doubleIt
assert composition(5) == 11
```

---

## Metaprogramação e DSLs

**Metaprogramação** permite que o código Groovy altere ou estenda seu próprio comportamento em tempo de compilação (via **AST Transformations**) ou em tempo de execução (via **methodMissing**, **propertyMissing**, **ExpandoMetaClass**, etc.). Essa capacidade é a base para construir **DSLs (Domain Specific Languages)** – APIs fluentes e específicas de domínio que usam closures, delegação e transformações de AST para oferecer sintaxe quase natural. DSLs facilitam a expressão de regras de negócio, configurações ou scripts de forma legível, enquanto a metaprogramação cuida da geração de código boilerplate.

### Métodos Mágicos: methodMissing

```groovy
class DSLite {
  def conteudo = []
  def methodMissing(String name, args) {
    conteudo << "$name(${args.collect { it.toString() }.join(', ')})"
  }
}
def dsl = new DSLite()
dsl.selecao('div', class: 'container')
dsl.campo(type: 'text', id: 'nome')
assert dsl.conteudo == ["selecao([class:container], div)", "campo([type:text, id:nome])"]
```

### AST Transformations

**Explicação:** *AST Transformations* são anotações que modificam o código‑fonte durante a compilação, permitindo gerar automaticamente métodos como `toString`, `equals`, `hashCode`, ou tornar classes imutáveis. Elas reduzem boilerplate e melhoram a legibilidade.

```groovy
import groovy.transform.ToString

@ToString(includeNames=true)
class PessoaAST {
  String nome
  int idade
}
assert new PessoaAST(nome: 'Ana', idade: 30).toString() == 'PessoaAST(nome:Ana, idade:30)'
```

---

## O que é Spock Framework?

**O que é Spock?** É um framework de testes baseado em Groovy projetado para criar testes automatizados legíveis. Inspirado nos frameworks BDD (Behavior Driven Development) como RSpec e Cucumber, o Spock transforma testes em especificações executáveis que servem como documentação viva do sistema.

**Principais vantagens:**

- Sintaxe GHerkin natural (given/when/then) que descreve comportamento
- Integração nativa com Groovy para código mais expressivo
- Suporte a data-driven testing (testar múltiplos cenários em um único teste)
- Sistema poderoso de mocks e stubs para isolar dependências

**Spock Infraestrutura:**

- Spock 1.x: implementa um runner que estende o org.junit.runner.Runner do JUnit 4. Assim, os testes Spock são reconhecidos e executados pelo engine do JUnit 4 (e podem ser usados em IDEs, Maven/Gradle etc. que já suportam JUnit.  
- Spock 2.x: migra para o JUnit 5 Jupiter e fornece a extensão org.spockframework.runtime.extension.junit5.SpockExtension. O Spock se registra como um TestEngine no JUnit 5, permitindo que os mesmos relatórios, filtros e recursos do JUnit 5 (como testes parametrizados, tags, etc.) sejam aplicados.  

Portanto, embora o Spock tenha sua própria DSL e metaprogramação, ele depende do runtime do JUnit para discovery, execução e integração com ferramentas de build e IDEs.

---

## Por que usar Groovy com Spock?

A combinação Groovy + Spock oferece os seguintes benefícios:

1. **Produtividade:** Closures e sintaxe concisa de Groovy aceleram a escrita de testes
2. **Clareza:** O DSL do Spock torna especificações fáceis de entender
3. **Manutenção:** Testes bem escritos documentam o comportamento esperado
4. **Integração:** Compatibilidade total com ecossistema Java existente
5. **Flexibilidade:** Metaprogramação do Groovy permite criar extensions personalizadas

---

## Estrutura do Spock Framework

```groovy
import spock.lang.Specification

class Calculadora {
  int somar(int a, int b) { a + b }
}

class CalculatoraSpec extends Specification {
  def "soma deve funcionar corretamente"() {
    given: "uma calculadora e dois numeros"
    def calc = new Calculadora()
    def a = 5
    def b = 3

    when: "realizo a soma"
    def resultado = calc.somar(a, b)

    then: "o resultado eh a soma dos numeros"
    resultado == 8

    expect: "alternativa mais concisa (sem blocos separate)"
    calc.somar(a, b) == 8
  }
}

def spec = new CalculatoraSpec()
assert spec."soma deve funcionar corretamente"().booleanValue()
```

**Blocos explicados:**

- `given:` — Prepara o estado inicial e fixtures
- `when:` — Executa a ação/operacao sendo testada
- `then:` — Verifica resultados (pode incluir verificações de excecao)
- `expect:` — Versão compacta: só contém assert
- `where:` — Tabelas de dados para testes data‑driven

### Anotações úteis do Spock

Spock oferece várias anotações que facilitam a escrita e organização de testes:

- `@Shared` – compartilha uma variável entre todos os testes da especificação.
- `@Unroll` – gera um teste separado para cada linha da tabela `where:`.
- `@Ignore` – desabilita temporariamente um teste.
- `@Timeout` – falha o teste se exceder o tempo especificado.
- `@Stepwise` – executa os métodos de teste sequencialmente, parando na primeira falha.
- `@Title` e `@Narrative` – definem título e descrição da especificação (usado principalmente com o relatório Spock). 

```groovy
import spock.lang.*

@Title('Calculadora Spec')
@Narrative('Testes da classe Calculator usando Spock')
@Stepwise
class CalculatorSpec extends Specification {
    @Shared Calculator calc = new Calculator()

    @Unroll
    def "#a + #b = #c"() {
        expect:
        calc.calcular(a, b, '+') == c

        where:
        a | b | c
        1 | 2 | 3
        4 | 5 | 9
    }

    @Ignore('Ainda não implementado')
    def "subtrair ainda não testado"() {
        expect:
        calc.calcular(5, 3, '-') == 2
    }

    @Timeout(1)
    def "teste de performance"() {
        when:
        // código que deve terminar rapidamente
        true
        then:
        noExceptionThrown()
    }
}
```

These annotations improve readability, control execution flow, and provide useful metadata for test reports.

## Relatórios de Testes com Spock

Spock gera relatórios JUnit XML por padrão, que podem ser consumidos por ferramentas CI (Jenkins, GitLab CI, etc.) e plugins de IDE. Para relatórios legíveis e detalhados, pode‑se usar o plugin **spock-reports** ou integrar ao **Gradle TestKit**.

### Relatório JUnit XML (padrão)

```groovy
// build.gradle
plugins {
    id 'groovy'
    id 'java'
}

test {
    useJUnitPlatform() // para Spock 2.x (JUnit 5)
    testLogging {
        events 'passed', 'failed', 'skipped'
    }
}
```

Esse arquivo `build/test-results/test/TEST-*.xml` contém informações de cada teste (nome, tempo, status, falhas).

### spock-reports (HTML detalhado)

```groovy

// build.gradle
plugins { id 'groovy' }

dependencies {
    testImplementation 'org.spockframework:spock-core:2.0-groovy-3.0'
    testImplementation 'org.spockframework:spock-reports:2.0.0-groovy-3.0' // versão compatível
}

test {
    systemProperty 'spock.reporting.enabled', 'true'
    systemProperty 'spock.reporting.outputDir', "$buildDir/spock-reports"
}
```

Após a execução, abra `build/spock-reports/index.html` para visualizar um relatório HTML com:

- Sumário de resultados (pass/fail/skip).
- Detalhes de cada feature, blocos `given/when/then`, e valores das variáveis `where:`.
- Traces de exceções e screenshots (se integrados).

### Integração com CI (ex. Jenkins)

```groovy
pipeline {
    stages {
        stage('Test') {
            steps {
                sh './gradlew test'
                junit '**/build/test-results/**/*.xml' // importa relatórios JUnit
                // opcional: publicar relatório HTML
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'build/spock-reports',
                    reportFiles: 'index.html',
                    reportName: 'Spock Report']
                )
            }
        }
    }
}
```

Esse pipeline coleta os XMLs de testes e publica o relatório HTML gerado pelo *spock‑reports*.

Com essas opções, você tem relatórios compactos (JUnit) para integração automática e relatórios detalhados (HTML) para análise humana.

---

## Data-Driven Testing com Spock

**Explicação:** O **Data‑Driven Testing** (ou testes baseados em tabelas) permite executar o mesmo cenário de teste várias vezes com diferentes conjuntos de dados, definidos em uma tabela `where:`. Cada linha da tabela gera uma nova execução do método de teste, facilitando a cobertura de múltiplos casos sem duplicar código. O Spock interpreta a tabela e injeta os valores nas variáveis declaradas, tornando o teste mais legível e conciso.  

```groovy

def "operacoes basicas da calculadora"() {
    given: "tenho uma calculadora"
    def calc = new Calculator()
    
    expect: "cada operacao retorna o resultado correto"
    calc.operar(a, b, op) == resultado
    
    where:
    a  | b  | op    || resultado
    10 | 5  | '+'   || 15
    10 | 5  | '-'   || 5
10 | 5  | '*'  || 50
    // ... mais linhas
}
```

```groovy
def "operacoes basicas da calculadora"() {
    given: "tenho uma calculadora"
    def calc = new Calculator()
    
    expect: "cada operacao retorna o resultado correto"
    calc.operar(a, b, op) == resultado
    
    where:
    a  | b  | op   || resultado
    10 | 5  | '+'  || 15
    10 | 5  | '-'  || 5
    10 | 5  | '*'  || 50
    10 | 5  | '/'  || 2
    -5 | -3 | '+'  || -8
     0 | 7  | '*'  || 0
}
```

---

## Mocking e Stubbing no Spock

**Diferença:**  

- **Mock**: simula um colaborador e permite verificar interações (quantas vezes um método foi chamado, ordem, argumentos). Usa‑se quando o teste precisa afirmar o comportamento do código sob teste.  
- **Stub**: fornece respostas pré‑definidas para chamadas de método sem registrar interações. É útil quando o foco é apenas o valor retornado, não como o método foi usado.  

No Spock, `Mock(MyService)` cria um mock, enquanto `Stub(MyService)` cria um stub.


```groovy
def "testar com mock"() {
    given: "um service mocked"
    def service = Mock(MyService)
    
    when: "chamo o metodo mocked"
    service.getData()
    
    then: "confirmo que foi invocado exatamente uma vez"
    1 * service.getData()
}

def "testar com stub"() {
    given: "um service stubbed"
    def service = Stub(MyService)
    
    when: "chamo o metodo"
    def dado = service.getData()
    
    then: "o resultado e o esperado"
    dado == 'mocked'
}
```

---

## Ciclo de Vida e Hooks do Spock

**Explicação:**

- `setupSpec()` é executado **uma única vez antes** de todos os testes da especificação (classe) – ideal para criar recursos compartilhados.
- `cleanupSpec()` é executado **uma única vez depois** de todos os testes – libera recursos criados em `setupSpec()`.
- `setup()` é executado **antes de cada método de teste** (feature method) – prepara o estado específico de cada teste.
- `cleanup()` é executado **depois de cada método de teste** – limpa alterações feitas durante o teste.

```groovy
class DatabaseSpec extends Specification {
    @Shared def conn = criarConexao()
    
    setupSpec() { println "Setup inicial" }
    cleanupSpec() { println "Cleanup final" }
    
    setup() { println "Setup antes de cada teste" }
    cleanup() { println "Limpeza apos cada teste" }
}
```

---

## Exemplo Prático Integrado

### Código Principal (`src/main/groovy/Calculator.groovy`)

```groovy
class Calculator {
    int calcular(int a, int b, String op) {
        switch (op) {
            case '+': return a + b
            case '-': return a - b
            case '*': return a * b
            case '/':
                if (b == 0) throw new IllegalArgumentException('Division by zero')
                return a / b
            default:
                throw new IllegalArgumentException("Invalid operation: $op")
        }
    }
    
    boolean isPar(int numero) { numero % 2 == 0 }
}
```

### Especificação de Testes (`src/test/groovy/CalculatorSpec.groovy`)

```groovy
import spock.lang.Specification
import spock.lang.Shared

class CalculatorSpec extends Specification {
    @Shared def calculadora = new Calculator()
    
    def "todas operacoes basicas funcionam"() {
        given: "calculadora inicializada"
        expect: "operacoes corretas"
        calculadora.calcular(a, b, op) == esperado
        where:
        a | b | op  || esperado
        1 | 2 | '+' || 3
        5 | 3 | '-' || 2
        4 | 2 | '*' || 8
        10| 2 | '/' || 5
    }
}
```

---

## Exemplos Práticos do Projeto

O projeto inclui múltiplos exemplos práticos que demonstram o uso de Groovy e Spock em cenários reais. Todos os código estão disponíveis no repositório e passam nos testes automatizados.

### 1. Domain Classes com Groovy (`@CompileStatic`, `@ToString`)

#### `Account` – Conta bancária

Demonstra entidades com comportamento completo, usando Groovy para simplificar operações de banco:

```groovy
@ToString(includeNames = true, includePackage = false)
class Account {
    String numero
    String titular
    double saldo
    double taxaJuros
    List<String> extrato

    Account(String numero, String titular, double saldo, double taxaJuros) {
        this.numero = numero
        this.titular = titular
        this.saldo = saldo
        this.taxaJuros = taxaJuros
        this.extrato = []
        this.extrato.add('Conta criada - Saldo inicial: R$ ' + formatarMoeda(saldo))
    }

    void depositar(double valor) {
        if (valor <= 0) throw new IllegalArgumentException('Valor do deposito deve ser positivo')
        saldo += valor
        extrato.add('Deposito: R$ ' + formatarMoeda(valor) + ' - Saldo: R$ ' + formatarMoeda(saldo))
    }

    void sacar(double valor) {
        if (valor <= 0) throw new IllegalArgumentException('Valor do saque deve ser positivo')
        if (valor > saldo) throw new IllegalArgumentException('Saldo insuficiente')
        saldo -= valor
        extrato.add('Saque: R$ ' + formatarMoeda(valor) + ' - Saldo: R$ ' + formatarMoeda(saldo))
    }

    double calcularJuros() { saldo * (taxaJuros / 100) }
    void aplicarJuros() { saldo += calcularJuros() }
}
```

#### `Invoice` – Fatura com status transitions

Exemplo de domínio com enum de status e transições de estado válidas:

```groovy
@ToString(includeNames = true, includePackage = false)
class Invoice {
    String numero
    String cliente
    double valorBruto
    double desconto
    Status status
    List<String> historico

    enum Status { OPEN("Aberta"), PENDING("Pendente"), PAID("Paga"), CANCELLED("Cancelada") }

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
        if (status != Status.PENDING) return false
        status = Status.PAID
        return true
    }

    boolean cancelar() {
        if (status == Status.PAID) return false
        if (status == Status.CANCELLED) return false  // Previne cancelamento duplicado
        status = Status.CANCELLED
        return true
    }
}
```

### 2. Validação e Business Rules

#### `User` – Validação de email

Exemplo de validaão com regex Groovy e `@CompileStatic`:

```groovy
@CompileStatic
@ToString(includeNames = true, includePackage = false)
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
// Email válido
def user = new User('1', 'Ana', 'ana@email.com', 'USER')
assert user.hasValidEmail() == true  // Regex Groovy funciona com @CompileStatic no Groovy 4.x
// Email inválido
def userInvalido = new User('2', 'Teste', 'invalido', 'USER')
assert userInvalido.hasValidEmail() == false
```

### 3. Service Layer com Groovy

#### `UserService` – CRUD com JSON

Demonstra uso de Groovy para JSON (JsonOutput, JsonSlurper) e Optional:

```groovy
@Service
class UserService {
    private static final Map<String, User> usersDatabase = [
        '1': new User('1', 'Ana Silva', 'ana@email.com', 'ADMIN'),
        '2': new User('2', 'Carlos Santos', 'carlos@email.com', 'USER'),
        '3': new User('3', 'Maria Oliveira', 'maria@email.com', 'USER')
    ]

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
```

#### `ReportService` – Relatórios com closures

Exemplo de closures e map/collect para gerar relatórios:

```groovy
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

    List<String> extrairEmails(List<User> usuarios) {
        return usuarios*.email
    }

    Map<String, List<String>> agruparPorRole(List<User> usuarios) {
        return usuarios.groupBy { it.role }.collectEntries { k, v -> [(k): v*.nome] }
    }
}
// Groovy spread operator
def emails = users*.email  // ['ana@email.com', 'carlos@email.com', ...]
```

### 4. `FinancialService` – Cálculos Financeiros

Demonstra closures para cálculos financeiros com Groovy:

```groovy
class FinancialService {
    def calcularJuros(double capital, double taxaJuros, int meses) {
        capital * ((1 + taxaJuros / 100) ** meses)  // Groovy power operator!
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
}
// Groovy power operator para exponencial
assert (1.05 ** 12) > 1.6  // Juros compostos: ~1.6x
```

### 5. Spring Boot REST Controller

API REST usando Groovy com Spring Boot:

```groovy
@RestController
@RequestMapping('/api/calculadora')
class CalculatorController {
    private final Calculator calculator = new Calculator()

    @GetMapping('/soma/{a}/{b}')
    String somar(@PathVariable int a, @PathVariable int b) {
        "Resultado: ${calculator.calcular(a, b, '+')}"
    }

    @GetMapping('/par/{numero}')
    String ehPar(@PathVariable int numero) {
        "${numero} eh ${calculator.isPar(numero) ? 'par' : 'impar'}"
    }
}
```

### 6. Testes de Integração

#### `AccountSpec` – Testes de conta bancária

```groovy
import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared

class AccountSpec extends Specification {
    @Shared def acc = new Account('001', 'Joao Silva', 1000.0, 1.5)

    @Unroll
    def "deposicao de #valor resulta em saldo #resultado"() {
        given: "uma conta com saldo inicial"

        when: "faço o deposito"
        acc.depositar(valor)

        then: "saldo e atualizado"
        acc.saldo == resultado

        where:
        valor | resultado
        100   | 1100.0
        500   | 1500.0
        1000  | 2000.0
        0.01  | 1000.01
    }

    def "deposito com valor negativo lanza excecao"() {
        when: "tento depositar valor negativo"
        acc.depositar(-100.0)

        then: "excecao e lancada"
        thrown(IllegalArgumentException)
    }

    def "deposito com valor zero lana excecao"() {
        when: "tento depositar valor zero"
        acc.depositar(0.0)

        then: "excecao e lancada"
        thrown(IllegalArgumentException)
    }
}
```

#### `InvoiceSpec` – Testes de fatura

```groovy
import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared

class InvoiceSpec extends Specification {
    @Shared List<Invoice> faturasCriadas = []

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

        where:
        percentual || totalEsperado
        0          || 100.0
        10         || 90.0
        25         || 75.0
        50         || 50.0
        100        || 0.0
    }

    def "pagar fatura com status PENDING tem sucesso"() {
        given: "uma fatura pendente com desconto"
        def fatura = new Invoice("PAG-01", "Pagador", 500.0)
        fatura.definirDesconto(10)

        expect: "fatura no status PENDING"
        fatura.status == Invoice.Status.PENDING

        when: "paro a fatura"
        def resultado = fatura.pagar()

        then: "pagamento com sucesso"
        resultado == true
        fatura.status == Invoice.Status.PAID
        fatura.historico.size() == 3
    }
}
```

#### `UserSpec` – Testes de validação de email

```groovy
import spock.lang.Specification
import spock.lang.Shared
import spock.lang.Unroll

class UserSpec extends Specification {
    @Shared user = new User('1', 'Ana Silva', 'ana@email.com', 'ADMIN')

    @Unroll
    def "email valido #email retorna #valido"() {
        given: "usuario com email"
        def usuario = new User('1', 'Teste', email, 'USER')

        expect: "validacao de email"
        usuario.hasValidEmail() == valido

        where:
        email              || valido
        'ana@email.com'    || true
        'joao.123@dominio.org' || true
        'maria@empresa.co.br'  || true
        'emailinvalido'    || false
        '@missing.com'     || false
        'user@'            || false
        ''                 || false
        'sem arroba'       || false
        'user@@duplo.com'  || false
        'user@.com'        || false
    }
}
```

#### `UserServiceSpec` – Testes de CRUD e JSON

```groovy
import spock.lang.Specification
import spock.lang.Shared

class UserServiceSpec extends Specification {
    @Shared service = new UserService()

    def "buscar todos usuarios retorna lista com 3 usuarios"() {
        expect: "busca retorna todos usuarios"
        service.buscarTodos().size() == 3
    }

    def "criar usuario com sucesso"() {
        given: "dados validos"

        when: "crio novo usuario"
        def usuario = service.criarUsuario('Joao Pereira', 'joao@email.com', 'USER')

        then: "usuario criado corretamente"
        usuario != null
        usuario.nome == 'Joao Pereira'
        usuario.email == 'joao@email.com'
        usuario.role == 'USER'

        and: "usuario esta no banco"
        service.buscarPorId(usuario.id).isPresent()
    }

    def "buscar por email valida"() {
        expect: "encontra usuario por email"
        service.buscarPorEmail('ana@email.com').orElse(null).nome == 'Ana Silva'
    }

    def "criar usuario com nome vazio lança excecao"() {
        when: "tento criar usuario sem nome"
        service.criarUsuario('', 'user@email.com', 'USER')

        then: "excecao e lancada"
        thrown(IllegalArgumentException)
    }

    def "mock com UserService mockado"() {
        given: "um service mockado"
        def userService = Mock(UserService)

        when: "chamo metodos mockados"
        def usuarios = userService.buscarTodos()
        def admin = userService.buscarAdmins()

        then: "interacoes verificadas"
        1 * userService.buscarTodos() >> [new User('1', 'Teste', 'test@email.com', 'USER')]
        1 * userService.buscarAdmins() >> []

        and: "retornos configurados"
        usuarios.size() == 1
        admin.size() == 0
    }
}
```

#### `ReportServiceSpec` – Testes de relatórios

```groovy
import spock.lang.Specification
import spock.lang.Shared

class ReportServiceSpec extends Specification {
    @Shared service = new ReportService()

    def "extrair emails retorna lista de emails"() {
        given: "usuarios"
        def usuarios = [
            new User('1', 'Ana', 'ana@email.com', 'ADMIN'),
            new User('2', 'Carlos', 'carlos@email.com', 'USER'),
            new User('3', 'Maria', 'maria@email.com', 'USER')
        ]

        when: "extrai emails"
        def emails = service.extrairEmails(usuarios)

        then: "emails extraidos corretamente"
        emails.size() == 3
        emails == ['ana@email.com', 'carlos@email.com', 'maria@email.com']
    }

    def "agrupar por role retorna grupos corretos"() {
        given: "usuarios com diferentes roles"
        def usuarios = [
            new User('1', 'Ana', 'ana@email.com', 'ADMIN'),
            new User('2', 'Carlos', 'carlos@email.com', 'USER'),
            new User('3', 'Maria', 'maria@email.com', 'ADMIN')
        ]

        when: "agrupo por role"
        def grupos = service.agruparPorRole(usuarios)

        then: "grupos corretos"
        grupos.containsKey('ADMIN')
        grupos.containsKey('USER')
        grupos['ADMIN'].size() == 2
        grupos['USER'].size() == 1
        grupos['ADMIN'] == ['Ana', 'Maria']
    }

    def "gerar relatorio sem criterios retorna relatorio completo"() {
        given: "lista de usuarios"
        def usuarios = [
            new User('1', 'Ana Silva', 'ana@email.com', 'ADMIN'),
            new User('2', 'Carlos Santos', 'carlos@email.com', 'USER'),
            new User('3', 'Maria Oliveira', 'maria@email.com', 'USER')
        ]

        when: "gero relatorio sem criterios"
        def relatorio = service.gerarRelatorioUsuarios(usuarios, null)

        then: "relatorio contem dados"
        relatorio.contains('RELATÓRIO DE USUÁRIOS')
        relatorio.contains('Total de usuários: 3')
    }
}
```

### Rodar os Exemplos

```bash
# Compilar e rodar todos os testes
mvn clean test

# Compilar sem rodar testes
mvn clean package -DskipTests

# Rodar apenas testes de um spec específico
mvn test -Dtest=AccountSpec

# Verificar build completo
mvn clean verify
```

**Resultados dos testes:**
- **CalculatorSpec** — 19 testes (aritmética básica + lifecycle)
- **ClosureSpec** — 20 testes (todas as features de closure)
- **AccountSpec** — 24 testes (conta bancária completa)
- **InvoiceSpec** — 50 testes (fatura com transições de estado)
- **UserSpec** — 17 testes (validação de email e roles)
- **UserServiceSpec** — 21 testes (CRUD + JSON + mock)
- **ReportServiceSpec** — 10 testes (relatórios + agrupamento)
- **EmployeeServiceSpec** — 4 testes (funcionários)
- **MetaprogrammingSpec** — 11 testes (DSL + AST)
- **MockingAndStubbingSpec** — 9 testes (mocks + stubs avançados)

**Total: 185 testes passando!**

---

## Conclusões

### Pontos Principais

1. **Sintaxe concisa do Groovy** — Acelera o desenvolvimento na JVM enquanto mantém compatibilidade total com Java
2. **DSL expressivo do Spock** — Faz os testes lerem como documentação executável do comportamento
3. **Metaprogramação poderosa** — Permite criar DSLs e reduzir boilerplate via AST transformations
4. **Coleções e closures integradas** — Facilitam transformação e análise de dados

### Recomendações Práticas

- Use **@CompileStatic** para performance crítica
- Aproveite **tabelas data-driven** para cobrir multiplos cenários rapidamente
- Documente comportamento com **Spock BDD** — testes como especificação viva
- Utilize **ExpandoMetaClass** para estender APIs sem modificar código fonte

---

<font size="2">Este documento foi construído utilizando Markdown e segue padrões de documentação técnica.</font>

**Apresentação Groovy & Spock v1.0.**

### Referências

- Groovy Documentation: https://groovy-lang.org/documentation.html
- Spock Framework Documentation: https://spockframework.org/
