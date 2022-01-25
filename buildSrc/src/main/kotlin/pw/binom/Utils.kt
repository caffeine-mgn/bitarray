package pw.binom

import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile

fun TaskContainer.eachKotlinTest(func: (Task) -> Unit) {
    this.mapNotNull { it as? org.jetbrains.kotlin.gradle.tasks.KotlinTest }
        .forEach(func)
    this.mapNotNull { it as? org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest }
        .forEach(func)
}

fun TaskContainer.eachKotlinCompile(func: (Task) -> Unit) {
    this.mapNotNull { it as? AbstractKotlinCompile<*> }
        .forEach(func)
    eachKotlinNativeCompile(func)
}

fun TaskContainer.eachKotlinNativeCompile(func: (AbstractKotlinNativeCompile<*, *>) -> Unit) {
    this
//        .mapNotNull { it as? AbstractKotlinNativeCompile<*, *> }
        .mapNotNull { it as? KotlinNativeCompile }
        .filter { "Test" !in it.name }
        .forEach(func)
}
