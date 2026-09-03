plugins {
    alias(libs.plugins.pluginkit.jvm.library)
    `maven-publish`
}

group = providers.gradleProperty("groupId").get()
version = providers.gradleProperty("libraryVersion").get()

// Configuración de publicación para GitHub Packages
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = "${providers.gradleProperty("artifactId").get()}-core" // -> foundationkit-core
            version = "${project.version}${project.findProperty("versionType") ?: ""}"
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("${providers.gradleProperty("repositoryUrl").get()}/${providers.gradleProperty("artifactId").get()}-android")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: providers.gradleProperty("gpr.user").orNull
                password = System.getenv("GITHUB_TOKEN") ?: providers.gradleProperty("gpr.key").orNull
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies{
    implementation(libs.bundles.testing.unit)
}