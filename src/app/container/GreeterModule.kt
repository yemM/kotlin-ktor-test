package com.weekando.app.container

import com.weekando.greeter.Greeter
import com.weekando.greeter.GreeterImpl
import org.koin.dsl.module

val GreeterModule = module {
    single<Greeter> { GreeterImpl(getProperty("env")) }
}