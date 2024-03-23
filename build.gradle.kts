import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.16.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
}

group = "com.balsdon"
version = System.getenv("VERSION_NUMBER")

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    /**
     * intellijVersion must be from the build list
     * @see [https://plugins.jetbrains.com/docs/intellij/android-studio-releases-list.html#2023]
     *
     * requires plugins:
     *    - "android" for access to ADB functions
     *
     * WIP: Jewel
     * @see [https://packages.jetbrains.team/maven/p/kpm/public/org/jetbrains/jewel/jewel-ide-laf-bridge-platform-specific/]
     */

    val androidStudioVersion = mapOf(
        "Giraffe" to "223.8836.35.2231.11090377",
        "Hedgehog" to "231.9392.1.2311.11330709",
        "Iguana" to "232.10227.8.2321.11379558",
        "Jellyfish" to "233.14475.28.2331.11543046",
    )
    val intellijVersion = androidStudioVersion["Iguana"] // https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html#configuration
    version.set(intellijVersion)
    type.set("AI") // Target Android Studio IDE Platform
    plugins.set(listOf("android"))
}

dependencies {
    // ----- Production dependencies -----
    val rxJava = "3.1.8"
    // RxJava
    implementation("io.reactivex.rxjava3:rxjava:$rxJava")

    // WIP: Jewel
    // The platform version is a supported major IJP version (e.g., 232 or 233 for 2023.2 and 2023.3 respectively)
    // implementation("org.jetbrains.jewel:jewel-ide-laf-bridge-platform-specific:$jewelVersion-ij-${platformVersion}")

    // ----- Testing -----
    val googleTruth = "1.1.4"
    // Google truth
    testImplementation("com.google.truth:truth:$googleTruth")
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt.yaml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks {
    // Set the JVM compatibility versions
    val jvm = "17"

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
        kotlinOptions.jvmTarget = jvm
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = jvm
    }

    withType<JavaCompile> {
        sourceCompatibility = jvm
        targetCompatibility = jvm
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = jvm
    }

    patchPluginXml {
        sinceBuild.set("222")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

//    runIde {
//        // Absolute path to installed target 3.5 Android Studio to use as
//        // IDE Development Instance (the "Contents" directory is macOS specific):
//        ideDir.set(file("/Applications/Android Studio.app/Contents"))
//    }
}
