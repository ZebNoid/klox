plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
    // Project "klox" depends on project "tool". (Project paths are separated with ":", so ":tool" refers to the top-level "tool" project.)
//    implementation(project(":tool"))
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `klox.kt` to a class with FQN `com.example.klox.AppKt`.)
    mainClass = "org.lox.Lox"
}
