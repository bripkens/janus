package de.codecentric.janus.atlassian.confluence

import de.codecentric.janus.atlassian.jira.JiraSession
import de.codecentric.janus.atlassian.jira.JiraClient
import org.junit.Before
import org.junit.Test
import com.atlassian.confluence.rpc.soap.beans.RemoteSpace
import com.atlassian.confluence.rpc.soap.beans.RemoteUser

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ConfluenceClientTest {
    static final String BASE_URL = 'http://localhost:47624',
                        USERNAME = 'janustestadmin',
                        PASSWORD = 'janusRocks',
                        TEST_GROUP_NAME = 'janus-users',
                        TEST_USER_USERNAME = 'tom',
                        TEST_USER_PASSWORD = '12345',
                        TEST_USER_FULL_NAME = 'Tom Tomson',
                        TEST_USER_EMAIL = 'tom@example.com',
                        TEST_SPACE_NAME = 'janus',
                        TEST_SPACE_KEY = 'JAN'

    ConfluenceSession session
    ConfluenceClient client

    @Before
    void setup() {
        session = new ConfluenceSession(BASE_URL, USERNAME, PASSWORD)
        client = new ConfluenceClient(session)
    }

    def createTestUser() {
        // for idempotent test
        client.deleteUser(TEST_USER_USERNAME)

        RemoteUser user = new RemoteUser()
        user.name = TEST_USER_USERNAME
        user.email = TEST_USER_EMAIL
        user.fullname = TEST_USER_FULL_NAME

        client.createUser(user, TEST_USER_PASSWORD)

        return client.getUser(TEST_USER_USERNAME)
    }

    def createTestSpace() {
        // for idempotent test
        client.deleteSpace(TEST_SPACE_KEY)

        RemoteSpace space = new RemoteSpace();
        space.name = TEST_SPACE_NAME
        space.key = TEST_SPACE_KEY
        client.addSpace(space)

        return client.getSpace(TEST_SPACE_KEY)
    }

    @Test
    void shouldNotThrowAnExceptionWhenAlreadyDeleted() {
        client.deleteSpace(TEST_SPACE_KEY)
        client.deleteSpace(TEST_SPACE_KEY)

        client.deleteUser(TEST_USER_USERNAME)
        client.deleteUser(TEST_USER_USERNAME)
    }

    @Test void shouldAddSpace() {
        RemoteSpace space = createTestSpace()
        assert space.name == TEST_SPACE_NAME
        assert space.key == TEST_SPACE_KEY
    }

    @Test void shouldCreateUser() {
        RemoteUser user = createTestUser()
        assert user.name == TEST_USER_USERNAME
        assert user.email == TEST_USER_EMAIL
        assert user.fullname == TEST_USER_FULL_NAME
    }

    @Test void shouldRemoveAllAnonymousPermissions() {
        RemoteSpace space = createTestSpace()
        client.removeAllAnonymousPermissionsFromSpace(space.key)

        // The current version of the API does not enable verification
        // whether all the permissions were actually revoked
        // (although possible, it is very time consuming).
        // The last manual verification was done on 2012-04-30.
    }
}
