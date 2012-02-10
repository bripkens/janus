package de.codecentric.janus.scaffold

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class BuildJobTask {
    enum Type {
        MAVEN
    }

    Type type
    Map<String, String> options
}
