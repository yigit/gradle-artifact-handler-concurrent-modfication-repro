import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Attribute

val ARTIFACT_TYPE = Attribute.of("artifactType", String::class.java)
val ATTRIBUTE_NAME = "myArtifacts"
open class MyParentPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val config = target.configurations.register("myParentConfiguration") {
            it.configureAttributes()
        }
        target.tasks.register(
            "myConsumeTask", ConsumeArtifactsTask::class.java
        ) {
            it.dependencyArtifacts.set(
                config.map {
                    it.incoming.artifactView {  }.files
                }
            )
        }
    }
}

open class MyChildPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val produceTask = target.tasks.register(
            "myProduceTask", ProduceArtifactsTask::class.java
        ) {
            it.output.set(
                target.layout.buildDirectory.dir("my-output")
            )
            it.dokkaCliClasspath.set(
                // REMOVE THIS AND IT WORKS
                createPluginsConfiguration(target)
            )

        }
        target.configurations.create("myChildConfiguration") { config ->
            config.configureAttributes()
            config.isCanBeResolved = false
            config.isCanBeConsumed = true
        }
        target.artifacts.add("myChildConfiguration", produceTask.map {
            it.output
        })
    }

    fun createPluginsConfiguration(
        project: Project
    ): NamedDomainObjectProvider<Configuration> {
        return project.configurations.register("dokkaCliPlugins") { config ->
            config.dependencies.add(
                project.dependencies.create(
                    "org.jetbrains.dokka:dokka-cli:1.7.10"
                )
            )
        }
    }
}

private fun Configuration.configureAttributes() {
    this.attributes {
        it.attribute(
            ARTIFACT_TYPE,
            ATTRIBUTE_NAME
        )
    }
}