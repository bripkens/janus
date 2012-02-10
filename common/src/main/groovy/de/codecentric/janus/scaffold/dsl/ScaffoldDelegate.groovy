package de.codecentric.janus.scaffold.dsl

import de.codecentric.janus.conf.Project
import de.codecentric.janus.scaffold.Scaffold

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
}
