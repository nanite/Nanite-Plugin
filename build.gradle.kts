import org.gradle.internal.impldep.org.bouncycastle.cms.RecipientId.password

plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("jvm") version "1.9.0"
    id("com.gradle.plugin-publish") version "1.2.0"
}

val isSnapshot = providers.environmentVariable("SNAPSHOT").getOrElse("false").toBoolean()

group = "dev.nanite"
version = "0.1.0${if (isSnapshot) "-SNAPSHOT" else ""}"
description = "Not sure yet"

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    testImplementation(kotlin("test:1.8.10"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.9.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

java {
    withSourcesJar()
}

tasks.withType(JavaCompile::class.java).all {
    options.release = 17
}

gradlePlugin {
    website = "https://github.com/nanite/nanite-plugin"
    vcsUrl = "https://github.com/nanite/nanite-plugin"
//    testSourceSet(sourceSets["test"])

    plugins.create("nanite-plugin") {
        id = "dev.nanite.nanite-plugin"
        implementationClass = "dev.nanite.naniteplugin.NanitePlugin"
        displayName = "Nanite Plugin"
        description = project.description
        version = project.version
        tags = listOf("minecraft", )
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        val token = providers.environmentVariable("NANITE_TOKEN")

        if (token.isPresent()) {
            maven {
                url = uri("https://maven.nanite.dev/releases")
                credentials {
                    username = "nanite"
                    password = token.get()
                }
            }
        }
    }
}
