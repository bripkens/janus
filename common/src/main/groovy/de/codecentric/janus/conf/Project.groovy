package de.codecentric.janus.conf

import java.util.regex.Pattern

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class Project {
    static final Pattern PACKAGE_REGEX = ~/^([a-z_]{1}[a-z0-9_]*(\.[a-z_]{1}[a-z0-9_]*)*)$/

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
