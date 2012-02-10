package de.codecentric.janus

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class GroovyExtensions {
    static apply() {
        def toSingleLinePattern = ~/\s{2,}/
        String.metaClass.toSingleLine = {
            (delegate =~ toSingleLinePattern).replaceAll(' ')
        }
    }
}
