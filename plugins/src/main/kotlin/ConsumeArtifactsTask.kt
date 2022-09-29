import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class ConsumeArtifactsTask : DefaultTask(){
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val dependencyArtifacts: Property<FileCollection>

    @TaskAction
    fun doIt() {
        println("begin partial docs")
        dependencyArtifacts.get().files.forEach {
            println("partial doc: $it")
        }
        println("done partial docs")
    }
}