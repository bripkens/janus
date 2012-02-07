package de.codecentric.janus.conf

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class Project {
    static final PACKAGE_REGEX = /^([a-z_]{1}[a-z0-9_]*(\.[a-z_]{1}[a-z0-9_]*)*)$/

    String name, description, pckg
}
