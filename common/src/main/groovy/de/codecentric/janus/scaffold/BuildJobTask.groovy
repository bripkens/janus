/*
 * Copyright (C) 2012 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.codecentric.janus.scaffold

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class BuildJobTask {
    enum Type {
        MAVEN(['targets'], [pom: 'pom.xml', usePrivateRepository: false]),
        ANT(['targets'], [buildFile: 'build.xml']),
        FAIL([], [] as HashMap),
        SHELL(['value'], [] as HashMap),
        BATCH(['value'], [] as HashMap)

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

    def getAt(String key) {
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
