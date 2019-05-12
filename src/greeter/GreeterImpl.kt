package com.weekando.greeter

class GreeterImpl(val env: String): Greeter {
    override fun hello(name: String): String {
        return "Hello ${name}, my friend on (${env})"
    }
}