package de.codecentric.janus.scaffold.dsl

import de.codecentric.janus.conf.Project
import de.codecentric.janus.scaffold.Scaffold
import de.codecentric.janus.scaffold.BuildJob

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldDelegate {
    Scaffold scaffold

    ScaffoldDelegate(Scaffold scaffold) {
        this.scaffold = scaffold
    }

    void name(String name) {
        scaffold.name = name
    }

    void description(String description) {
        scaffold.description = description
    }

    void requiredContext(Closure closure) {
        closure.delegate = new RequiredContextDelegate(scaffold)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }

    void buildJob(Closure closure) {
        def buildJob = new BuildJob()
        scaffold.buildJobs << buildJob
        closure.delegate = new BuildJobDelegate(buildJob)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }
}
