
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }

    configurations.all {
        resolutionStrategy {
            dependencySubstitution {
                val originalDependency = module("net.sf.proguard:proguard-gradle")
                val substituteDependency = module("com.guardsquare:proguard-gradle:7.1.1")
                substitute(originalDependency).with(substituteDependency)
            }
        }
    }

}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
