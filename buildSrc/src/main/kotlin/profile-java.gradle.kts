plugins {
    `java-library`
}

group = "dev.piotrulla.ohmc"
version = "1.0.1"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.compileJava {
    options.compilerArgs = listOf("-Xlint:deprecation", "-parameters")
    options.encoding = "UTF-8"
    options.release = 25
}