package de.codecentric.janus.atlassian.confluence

import de.codecentric.janus.atlassian.jira.JiraSession
import de.codecentric.janus.atlassian.jira.JiraClient
import org.junit.Before

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


}
