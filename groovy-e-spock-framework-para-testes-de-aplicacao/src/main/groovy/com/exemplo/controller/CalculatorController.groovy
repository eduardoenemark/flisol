package com.exemplo.controller

import com.exemplo.domain.Calculator
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping('/api/calculadora')
class CalculatorController {
    private final Calculator calculator = new Calculator()

    @GetMapping('/soma/{a}/{b}')
    String somar(@PathVariable int a, @PathVariable int b) {
        "Resultado: ${calculator.calcular(a, b, '+')}"
    }

    @GetMapping('/subtracao/{a}/{b}')
    String subtrair(@PathVariable int a, @PathVariable int b) {
        "Resultado: ${calculator.calcular(a, b, '-')}"
    }

    @GetMapping('/multiplicacao/{a}/{b}')
    String multiplicar(@PathVariable int a, @PathVariable int b) {
        "Resultado: ${calculator.calcular(a, b, '*')}"
    }

    @GetMapping('/divisao/{a}/{b}')
    String dividir(@PathVariable int a, @PathVariable int b) {
        "Resultado: ${calculator.calcular(a, b, '/')}"
    }

    @GetMapping('/par/{numero}')
    String ehPar(@PathVariable int numero) {
        "${numero} eh ${calculator.ehPar(numero) ? 'par' : 'impar'}"
    }
}
