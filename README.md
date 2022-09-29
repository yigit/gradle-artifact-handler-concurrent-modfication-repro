### Gradle concurrent modificaton exception bug repro

This project has 2 submodules (`child`, `parent`) and 2 plugins for each one of them.

The `child` plugin creates a task that can build an artifact,
The `parent` plugin creates a task that consumes the artifact (via a configuration).

When the parent task is run, Gradle fails with a concurrent modification exception
while trying to resolve dependencies.


to reproduce:

```
./gradlew parent:myConsumeTask --stacktrace
```

will output

```
...
* What went wrong:
Could not determine the dependencies of task ':parent:myConsumeTask'.
> Could not resolve all dependencies for configuration ':parent:myParentConfiguration'.
   > java.util.ConcurrentModificationException (no error message)
...
* Get more help at https://help.gradle.org

BUILD FAILED in 1s
5 actionable tasks: 5 up-to-date
Configuration cache entry stored.
➜  gradle-artifact-repro git:(main) ✗
```

### Root cause

The issue is caused by having an artifact producer task that has a
```
@get:Classpath
abstract val dokkaCliClasspath: Property<FileCollection>
```
input which is set from a newly created configuration via:

```
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
```

### Workaround
If you add another task that zips the output and make it the artifact producer,
it works fine.