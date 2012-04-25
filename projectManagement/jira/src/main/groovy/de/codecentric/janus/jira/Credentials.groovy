package de.codecentric.janus.jira

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class Credentials {
    final String name, password

    Credentials(String name, String password) {
        this.name = name
        this.password = password
    }
}
