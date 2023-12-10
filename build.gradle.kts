plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.16.0"
}

group = "com.balsdon"
version = "1.0-SNAPSHOT"

/**
 * The most difficult element is getting the versions to work
 * The see the Jewel LAF versions that match with the IDE
 * @see [https://packages.jetbrains.team/maven/p/kpm/public/org/jetbrains/jewel/jewel-ide-laf-bridge-platform-specific/]
 *
 * [intellijVersion] must be from the build list
 * @see [https://plugins.jetbrains.com/docs/intellij/android-studio-releases-list.html#2023]
 */
val intellijVersion = "232.10072.27.2321.11006994"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set(intellijVersion)
    type.set("AI") // Target Android Studio IDE Platform
    plugins.set(listOf(/* Plugin Dependencies */))
}

dependencies {
    val rxJava = "3.1.8"
    // The platform version is a supported major IJP version (e.g., 232 or 233 for 2023.2 and 2023.3 respectively)
//    implementation("org.jetbrains.jewel:jewel-ide-laf-bridge-platform-specific:$jewelVersion-ij-${platformVersion}")

    // RxJava
    implementation("io.reactivex.rxjava3:rxjava:$rxJava")
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("232.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
