plugins {
    id("java")
    application
}

group = "org.main"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
application {
    mainClass.set("org.main.Main")
}
dependencies {
    implementation("com.mysql:mysql-connector-j:9.6.0")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.main.Main"
    }
    from({
        configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}