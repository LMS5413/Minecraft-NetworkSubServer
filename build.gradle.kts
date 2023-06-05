plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "org.subserver"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    testCompileOnly("org.projectlombok:lombok:1.18.28")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.28")
}

tasks.test {
    useJUnitPlatform()
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    jar {
        destinationDirectory
                .set(file("D:\\Codes\\SubServer\\out"))
        manifest {
            attributes["Main-Class"] = "org.subserver.Main"
        }
    }
    compileJava {
        options.encoding = "UTF-8"
    }
    shadowJar {
        archiveVersion.set("1.0")
        archiveBaseName.set("LM-SubServer")
        destinationDirectory.set(
                file("D:\\Codes\\SubServer\\out")
        )
    }

}