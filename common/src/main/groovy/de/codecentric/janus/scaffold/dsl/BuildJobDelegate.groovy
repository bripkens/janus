package de.codecentric.janus.scaffold.dsl

import de.codecentric.janus.scaffold.BuildJob

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class BuildJobDelegate {
    final BuildJob buildJob

    BuildJobDelegate(BuildJob buildJob) {
        this.buildJob = buildJob
    }

    void concurrentBuild(boolean concurrentBuild) {
        buildJob.concurrentBuild = concurrentBuild
    }

    void name(String name) {
        buildJob.name = name
    }

    void tasks(Closure closure) {
        closure.delegate = new BuildJobTasksDelegate(buildJob)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }
}
