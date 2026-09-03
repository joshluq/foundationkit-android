plugins {
    alias(libs.plugins.pluginkit.jvm.library)
    alias(libs.plugins.pluginkit.jvm.publishing)
    alias(libs.plugins.pluginkit.quality)
}

group = providers.gradleProperty("groupId").get()
version = providers.gradleProperty("libraryVersion").get()

jvmPublishing {
    repoName = "GitHubPackages"
    repoUrl = "${providers.gradleProperty("repositoryUrl").get()}/${providers.gradleProperty("artifactId").get()}-android"
    repoUser = System.getenv("GITHUB_ACTOR")
    repoPassword = System.getenv("GITHUB_TOKEN")
    version = "${project.version}${project.findProperty("versionType") ?: ""}"
    groupId = project.group.toString()
    artifactId = "${providers.gradleProperty("artifactId").get()}-core"
}

dependencies {
    implementation(libs.bundles.testing.unit)
}

pluginkitQuality {
    sonarHost = "https://sonarcloud.io"
    sonarProjectKey = "joshluq_foundationkit-core"
    koverExclusions = listOf(
        "**.showcase.*",
        "**.di.*",
        "**.*_di_*",
        "**.BuildConfig",
        "**.R",
        "**.R$*"
    )
}
