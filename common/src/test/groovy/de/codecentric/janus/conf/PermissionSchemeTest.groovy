package de.codecentric.janus.conf

import org.junit.Before
import org.junit.Test

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class PermissionSchemeTest {

    def PermissionScheme ps

    @Before void setup() {
        ps = new PermissionScheme()
    }
    
    @Test void testReadAccess() {
        def read = [new User(email:'tom@example.com')]
        ps.readPermissions = read
        assert ps.readPermissions.is(read)

        def copiedRead = read.clone()
        copiedRead << new User(email:'jenni@example.com')
        assert ps.readPermissions.size() == 1
        assert copiedRead.size() == 2

        ps.readPermissions = copiedRead
        assert ps.readPermissions.is(copiedRead)
    }
}
