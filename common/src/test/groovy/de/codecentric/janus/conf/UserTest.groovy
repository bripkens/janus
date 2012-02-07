package de.codecentric.janus.conf

import org.junit.Before
import org.junit.Test

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class UserTest {
    def User user

    @Before void setup() {
        user = new User()
    }
    
    @Test void testRandomPassword() {
        user.setRandomPassword()
        assert user.password != null
    }
}
