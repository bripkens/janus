package de.codecentric.janus.scaffold.dsl

import de.codecentric.janus.scaffold.BuildJob
import de.codecentric.janus.scaffold.BuildJobTask

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class BuildJobTasksDelegate {
    final BuildJob buildJob

    BuildJobTasksDelegate(BuildJob buildJob) {
        this.buildJob = buildJob
    }

    def methodMissing(String name, args) {
        // a map entry always has a single value
        if (args.size() != 1) {
            throw new MissingMethodException(name,
                    BuildJobTasksDelegate.class, args)
        }

        try {
            def type = BuildJobTask.Type.valueOf(name.toUpperCase())
            buildJob.tasks << new BuildJobTask(type: type, options: args[0])
        } catch (IllegalArgumentException ex) {
            throw new MissingMethodException(name,
                    BuildJobTasksDelegate.class, args)
        }
    }
}
