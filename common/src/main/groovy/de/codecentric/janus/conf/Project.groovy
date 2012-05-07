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

package de.codecentric.janus.conf

import java.util.regex.Pattern

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class Project {
    static final Pattern PACKAGE_REGEX = ~/(?i)^([a-z_]{1}[a-z0-9_]*(\.[a-z_]{1}[a-z0-9_]*)*)$/

    String name, description, pckg

    static boolean isValidName(String name) {
        name != null && name.length() > 2
    }

    static boolean isValidPackage(String pckg) {
        pckg != null && (pckg.isEmpty() || pckg =~ PACKAGE_REGEX)
    }

    static boolean isValidDescription(String description) {
        // placeholder for later
        true
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pckg='" + pckg + '\'' +
                '}';
    }


}
