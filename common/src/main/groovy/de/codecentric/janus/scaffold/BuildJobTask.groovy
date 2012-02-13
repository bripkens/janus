package de.codecentric.janus.scaffold

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class BuildJobTask {
    enum Type {
        MAVEN(['targets'], [pom: 'pom.xml'])

        final List<String> requiredOptions
        final Map<String, String> defaultOptions

        private Type(List<String> requiredOptions,
                     Map<String, String> defaultOptions) {
            this.requiredOptions = requiredOptions
            this.defaultOptions = defaultOptions
        }
    }

    Type type
    Map<String, String> options

    String getAt(String key) {
        def value = options[key]

        if (value != null) {
            return value
        } else if (type != null) {
            return type.defaultOptions[key]
        } else {
            return null
        }
    }

    boolean isValid() {
        if (type == null) {
            return false
        }

        def valid = true

        type.requiredOptions.each { option ->
            if (valid && !options.containsKey(option)) {
                valid = false
            }
        }

        return valid
    }
}
