@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.loom)
    `maven-publish`
}

val ver = stonecutter.current.version
val modId = project.property("mod.id").toString()
val modName = project.property("mod.name").toString()
val modVer = project.property("mod.version").toString()

version = "$modVer+$ver"
base.archivesName = modId

repositories {
    fun strictMaven(url: String, vararg groups: String) = maven(url) { content { groups.forEach(::includeGroupAndSubgroups) } }

    strictMaven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1", "me.djtheredstoner")
    strictMaven("https://api.modrinth.com/maven", "maven.modrinth")
    maven("https://maven.starred.foo/releases")
}

dependencies {
    minecraft("com.mojang:minecraft:$ver")

    localRuntime("devauth".global)

    compileOnly("caxton".versioned)

    implementation("fabric-api".versioned)
    implementation("fabric-loader".global)
    implementation("fabric-language-kotlin".global)

    implementation("kommand".global)
}

loom {
    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json")

    runConfigs.named("client") {
        generateRunConfig = true
        jvmArguments.addAll(
            "-Ddevauth.enabled=true",
            "-Ddevauth.account=main",
            "-XX:+AllowEnhancedClassRedefinition",
            "-XX:+IgnoreUnrecognizedVMOptions",
        )
    }

    runConfigs.named("server") {
        generateRunConfig = false
    }
}

publishing {
    repositories {
        val a = if (Regex("-b[0-9]*$") in modVer) "snapshots" else "releases"
        maven("https://maven.starred.foo/$a") {
            name = "starred"
            credentials {
                username = (project.findProperty("MAVEN_USER") as? String) ?: System.getenv("MAVEN_USER") ?: ""
                password = (project.findProperty("MAVEN_PASS") as? String) ?: System.getenv("MAVEN_PASS") ?: ""
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = "foo.starred"
            artifactId = modId
            version = "$modVer+$ver"
            from(components["java"])
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
    withSourcesJar()
}

kotlin {
    jvmToolchain(25)

    compilerOptions {
        jvmTarget.set(JvmTarget.valueOf("JVM_25"))
    }
}

tasks {
    processResources {
        val r = mapOf("id" to modId, "name" to modName, "version" to modVer, "minecraft" to project.property("mod.mc_dep"))

        inputs.properties(r)
        filesMatching("fabric.mod.json") { expand(r) }
    }

    register<Copy>("buildAndCollect") {
        description = "Builds and collects mod jars."
        group = "build"
        from(jar, kotlinSourcesJar)
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

val String.global: Provider<MinimalExternalModuleDependency>
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs").findLibrary(this).get()

val String.versioned: Provider<MinimalExternalModuleDependency>
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs").findLibrary("$this-${ver.replace(".", "_")}").get()
