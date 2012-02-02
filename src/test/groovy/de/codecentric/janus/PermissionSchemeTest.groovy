package de.codecentric.janus

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.*

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class PermissionSchemeTest {

    def PermissionScheme ps

    @Before void setup() {
        ps = new PermissionScheme()
    }
    
    @Test void testReadAccess() {
        def read = ['tom@example.com']
        ps.readPermissions = read
        assertThat ps.readPermissions, is(read)

        def copiedRead = read.clone()
        copiedRead << 'jenni@example.com'
        assertThat ps.readPermissions.size(), is(1)
        assertThat copiedRead.size(), is(2)

        ps.readPermissions = copiedRead
        assertThat ps.readPermissions, is(copiedRead)
    }
}
