plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
//    // Apply Kotlin Serialization plugin from `gradle/libs.versions.toml`.
//    alias(libs.plugins.kotlinPluginSerialization)

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
//    // Apply the kotlinx bundle of dependencies from the version catalog (`gradle/libs.versions.toml`).
//    implementation(libs.bundles.kotlinxEcosystem)
//    testImplementation(kotlin("test"))
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `klox.kt` to a class with FQN `com.example.klox.AppKt`.)
    mainClass = "org.lox.tool.GenerateAst"
}
