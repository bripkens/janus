package de.codecentric.janus.scaffold.dsl

import groovy.util.logging.Slf4j
import de.codecentric.janus.scaffold.Scaffold

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Slf4j
class ScaffoldConfigEvaluator {
    final File configFile

    ScaffoldConfigEvaluator(File configFile) {
        this.configFile = configFile
    }

    def evaluate() {
        Script configScript = new GroovyShell().parse(configFile.text)

        Scaffold scaffold = new Scaffold()

        configScript.metaClass = create(Script.class) { ExpandoMetaClass emc ->
            emc.scaffold = { Closure closure ->
                closure.delegate = new ScaffoldDelegate(scaffold)
                closure.resolveStrategy = Closure.DELEGATE_FIRST
                closure()
            }
        }

        configScript.run()

        scaffold
    }

    ExpandoMetaClass create(Class clazz, Closure closure) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        // allow customization of EMC
        closure(emc)
        emc.initialize()
        emc
    }
}