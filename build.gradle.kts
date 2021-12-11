import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

plugins {
    java
    id("net.kyori.blossom") version "1.2.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenLocal()
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
    maven("https://repo.fvdh.dev/releases")
}

dependencies {
    shadow("net.kyori:adventure-text-minimessage:4.10.0-SNAPSHOT"){
        exclude("net.kyori", "adventure-api")
    }
    shadow("org.spongepowered:configurate-hocon:4.1.2")
    shadow("org.jetbrains:annotations:23.0.0")
    compileOnly("net.frankheijden.serverutils:ServerUtils:3.3.1")

    compileOnly("com.velocitypowered:velocity-api:3.1.0")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.0")

    testImplementation("org.slf4j:slf4j-api:1.7.32")
    testImplementation("org.spongepowered:configurate-hocon:4.1.2")
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
	testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:4.1.0")
    testImplementation("com.velocitypowered:velocity-api:3.1.0")
    testImplementation("net.kyori:adventure-text-minimessage:4.10.0-SNAPSHOT")

}

group = "me.dreamerzero.chatregulator"
version = "2.1.0"
description = "A Chat regulator for you Velocity network"
java.sourceCompatibility = JavaVersion.VERSION_11

blossom{
	val constants = "src/main/java/net/dreamerzero/chatregulator/utils/Constants.java"
	replaceToken("{name}", rootProject.name, constants)
	replaceToken("{version}", version, constants)
	replaceToken("{description}", description, constants)
    replaceToken("{url}", "https://forums.velocitypowered.com/t/chatregulator-a-global-chat-regulator-for-velocity/962", constants)
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        dependsOn(getByName("relocateShadowJar") as ConfigureShadowRelocation)
        minimize()
        archiveFileName.set("ChatRegulator.jar")
        configurations = listOf(project.configurations.shadow.get())
    }

    create<ConfigureShadowRelocation>("relocateShadowJar") {
        target = shadowJar.get()
        prefix = "me.dreamerzero.chatregulator.libs"
    }
    test {
        useJUnitPlatform()
        testLogging {
		    events("passed", "skipped", "failed")
	    }
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
