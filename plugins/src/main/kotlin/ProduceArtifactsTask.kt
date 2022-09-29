import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class ProduceArtifactsTask : DefaultTask(){
    @get:OutputDirectory
    abstract val output: DirectoryProperty

    /**
     * Classpath for running dokka-cli.
     */
    @get:Classpath
    abstract val dokkaCliClasspath: Property<FileCollection>

    @TaskAction
    fun doIt() {
        output.get().asFile.let {
            it.deleteRecursively()
            it.mkdirs()
            it.resolve("myOutput1-${project.name}.txt").writeText(
                "file1"
            )
            it.resolve("myOutput2-${project.name}.txt").writeText(
                "file1"
            )
        }
    }
}