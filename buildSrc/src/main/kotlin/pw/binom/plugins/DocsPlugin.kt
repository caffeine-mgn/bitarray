package pw.binom.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class DocsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(org.jetbrains.dokka.gradle.DokkaPlugin::class.java)
        val kotlin = target.extensions.getByType(KotlinMultiplatformExtension::class.java)
//        target.tasks.withType(DokkaTask::class.java).configureEach {
//            println("!!!!")
//            // custom output directory
//            it.outputDirectory.set(target.buildDir.resolve("dokka"))
//
//            it.dokkaSourceSets.apply {
// //                named("customNameMain") { // The same name as in Kotlin Multiplatform plugin, so the sources are fetched automatically
// //                    includes.from("packages.md", "extra.md")
// //                    samples.from("samples/basic.kt", "samples/advanced.kt")
// //                }
//
//                register("differentName") { // Different name, so source roots must be passed explicitly
//                    it.displayName.set("JVM")
//                    it.platform.set(org.jetbrains.dokka.Platform.jvm)
//                    it.sourceRoots.from(kotlin.sourceSets.getByName("jvmMain").kotlin.srcDirs)
//                    it.sourceRoots.from(kotlin.sourceSets.getByName("commonMain").kotlin.srcDirs)
//                }
//            }
//        }

        val publishing = target.extensions.findByName("publishing") as PublishingExtension

        publishing.publications {
            it.forEach { publication ->
                println("--->${publication.name}")
                publication as MavenPublication
                val docTask = target.tasks.register("${publication.name}Doc", DokkaTask::class.java) { dokkaTask ->
//                    dokkaTask.plugins.dependencies.add(target.dependencies.create("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.10"))
//                    dokkaTask.plugins.dependencies.add(target.dependencies.create("org.jetbrains.dokka:javadoc-plugin:1.6.10"))
                    dokkaTask.dokkaSourceSets.create(publication.name) {
                        it.includeNonPublic.set(false)
                        it.displayName.set(publication.name)
                        val platform = when (publication.name) {
                            "js" -> org.jetbrains.dokka.Platform.js
                            "jvm" -> org.jetbrains.dokka.Platform.jvm
                            "kotlinMultiplatform" -> org.jetbrains.dokka.Platform.common
                            else -> org.jetbrains.dokka.Platform.native
                        }
                        it.platform.set(platform)
//                        it.sourceRoots.from(kotlin.sourceSets.getByName("jvmMain").kotlin.srcDirs)
                        it.sourceRoots.from(kotlin.sourceSets.getByName("commonMain").kotlin.srcDirs)
                        val source = kotlin.sourceSets.findByName("${publication.name}Main")
                        if (source != null) {
                            it.sourceRoots.from(source.kotlin.srcDirs)
                        }
                    }
                }
                val docJar = target.tasks.register("${docTask.name}Jar", Jar::class.java) {
                    it.dependsOn(docTask)
                    it.archiveClassifier.set("javadoc")
                    it.archiveBaseName.set("${docTask.name}-javadoc")
                    docTask.get().also { docTask2 ->
                        it.from(docTask2.outputs)
                    }
                }
                publication.artifact(docJar)
            }
        }
    }
}
