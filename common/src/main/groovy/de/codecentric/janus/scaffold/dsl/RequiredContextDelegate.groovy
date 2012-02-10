package de.codecentric.janus.scaffold.dsl

import de.codecentric.janus.scaffold.Scaffold

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class RequiredContextDelegate {
    Scaffold scaffold

    RequiredContextDelegate(Scaffold scaffold) {
        this.scaffold = scaffold
        
        if (scaffold.requiredContext == null) {
            scaffold.requiredContext = [] as HashMap
        }
    }

    def methodMissing(String name, args) {
        // a map entry always has a single value
        if (args.size() != 1) {
            throw new MissingMethodException(name, delegate, args)
        }
        scaffold.requiredContext[name] = args[0]
    }
}
