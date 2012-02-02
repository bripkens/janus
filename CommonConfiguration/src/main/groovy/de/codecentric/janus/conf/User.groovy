package de.codecentric.janus.conf

import org.apache.commons.lang.RandomStringUtils

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class User {
    static final GENERATED_PASSWORD_LENGTH = 8;

    String name, email, notes, publicKey, password

    def setRandomPassword() {
        password = RandomStringUtils.randomAlphanumeric(
                GENERATED_PASSWORD_LENGTH)
    }
}
