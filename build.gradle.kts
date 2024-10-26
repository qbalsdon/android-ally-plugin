import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform
import org.jetbrains.kotlin.incremental.createDirectory

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("org.jetbrains.intellij.platform") version "2.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
}

group = "com.balsdon"
version = System.getenv("VERSION_NUMBER")

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

intellijPlatform {
    pluginConfiguration {
        name = "android_ally"
        group = "com.balsdon.android-ally-plugin"
        ideaVersion.sinceBuild.set(project.property("sinceBuild").toString())
        ideaVersion.untilBuild.set(provider { null })
    }
    buildSearchableOptions.set(false)
    instrumentCode = true

    signing {
        privateKey = System.getenv("PRIVATE_KEY")
        password = System.getenv("PRIVATE_KEY_PASSWORD")
        certificateChain = System.getenv("CERTIFICATE_CHAIN")
    }

    publishing {
        token = System.getenv("PUBLISH_TOKEN")
    }
}

dependencies {
    intellijPlatform {
        bundledPlugin("org.jetbrains.android")
        instrumentationTools()
        testFramework(TestFrameworkType.Platform)
        zipSigner()
        if (project.hasProperty("localIdeOverride")) {
            local(property("localIdeOverride").toString())
        } else {
            androidStudio(property("ideVersion").toString())
        }
    }

    // ----- Production dependencies -----
    val rxJava = "3.1.8"
    // RxJava
    implementation("io.reactivex.rxjava3:rxjava:$rxJava")

    // WIP: Jewel
    // The platform version is a supported major IJP version (e.g., 232 or 233 for 2023.2 and 2023.3 respectively)
    // implementation("org.jetbrains.jewel:jewel-ide-laf-bridge-platform-specific:$jewelVersion-ij-${platformVersion}")

    // ----- Signing -----
    // @see https://github.com/JetBrains/marketplace-zip-signer
    val zipSigner = "0.1.8"
    implementation(dependencyNotation = "org.jetbrains:marketplace-zip-signer:$zipSigner")

    // ----- Testing -----
    val googleTruth = "1.1.4"
    // Google truth
    testImplementation("com.google.truth:truth:$googleTruth")

    testImplementation("junit:junit:4.13.2")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt.yaml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

fun download(url: String, path: String) {
    val destination = File(path)
    ant.invokeMethod("get", mapOf("src" to url, "dest" to destination))
}

fun performanceTestCheck() {
    if (extra["ideVersion"] != "2024.2.1.9") {
        println("performanceTestCheck Fix not applied outside of LadyBug")
        return
    }
    val homeDir = System.getProperty("user.home")
    println("performanceTestCheck running in [${homeDir}]")
    // get the cache lib dir
    val filteredDirs = fileTree("$homeDir/.gradle/caches/transforms-3").filter {
        it.path.contains("/transformed/") && it.path.contains("android-studio-2024.2.1.9")
    }

    filteredDirs.forEach {
        println("        [${it.path}]")
    }

    val targetCacheDir = filteredDirs.map {
        it.path
            .replace("$homeDir/.gradle/caches/transforms-3/", "")
            .substringBefore("/")
    }.toSet()

    if (targetCacheDir.isEmpty() || targetCacheDir.size > 1) {
        println("performanceTestCheck: Not sure what to do in this situation [targetCacheDir.size=${targetCacheDir.size}]")
        return
    }

    val androidStudio = fileTree("$homeDir/.gradle/caches/transforms-3/${targetCacheDir.first()}/transformed")
        .first()
        .path
        .replace("$homeDir/.gradle/caches/transforms-3/${targetCacheDir.first()}/transformed/", "")
        .substringBefore("/")


    val destinationRoot = "$homeDir/.gradle/caches/transforms-3/${targetCacheDir.first()}/transformed/${androidStudio}/plugins/"

    File("${destinationRoot}/performanceTesting").apply {
        if (!exists()) {
            println("performanceTestCheck(): creating directory performanceTesting")
            createDirectory()
        }
    }
    File("${destinationRoot}/performanceTesting/lib").apply {
        if (!exists()) {
            println("performanceTestCheck(): creating directory performanceTesting/lib")
            createDirectory()
        }
    }
    val destinationPath = "${destinationRoot}/performanceTesting/lib/"
    val performanceTestingFile = File("${destinationRoot}/performanceTesting/lib/performance-testing-242.23339.19.jar")
    if (performanceTestingFile.exists()){
        println("performanceTestCheck(): performanceTesting.jar dependency exists")
    } else {
        println("performanceTestCheck(): performanceTesting.jar not dependency found")
        val webURL =
            "https://www.jetbrains.com/intellij-repository/releases/com/jetbrains/intellij/performanceTesting/performance-testing/242.23339.19/performance-testing-242.23339.19.jar"
        download(webURL, destinationPath)
        println("performanceTestCheck(): performanceTesting.jar downloaded")
    }
}

tasks {
    // Set the JVM compatibility versions
    val projectJvmTarget = "17"
    val projectApiVersion = "1.8"

    withType<Detekt>().configureEach {
        reports {
            html.required.set(true) // observe findings in your browser with structure and code snippets
            xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
            txt.required.set(true) // similar to the console output, contains issue signature to manually edit baseline files
            sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
            md.required.set(true) // simple Markdown format
        }
    }

    withType<Detekt>().configureEach {
        jvmTarget = "1.8"
    }
    withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = "1.8"
    }

    buildSearchableOptions {
        enabled = false
    }

    compileKotlin {
        kotlinOptions.jvmTarget = projectJvmTarget
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = projectJvmTarget
    }

    withType<JavaCompile> {
        sourceCompatibility = projectJvmTarget
        targetCompatibility = projectJvmTarget
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = projectJvmTarget
        all {
            kotlinOptions {
                jvmTarget = jvmTarget
                apiVersion = projectApiVersion
            }
        }
    }
    build {
        performanceTestCheck()
    }
    runIde {
        jvmArgs = listOf("-Xmx4096m", "-XX:+UnlockDiagnosticVMOptions")
    }
}