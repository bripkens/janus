package de.codecentric.janus.jira

import org.junit.Test
import org.junit.Before
import com.atlassian.jira.rpc.soap.beans.RemoteGroup

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JiraClientTest {

    static final String BASE_URL = 'http://localhost:47623',
            USERNAME = 'janustestadmin',
            PASSWORD = 'janusRocks'

    Session session
    JiraClient client

    @Before
    void setup() {
        session = new Session(BASE_URL, USERNAME, PASSWORD)
        client = new JiraClient(session)
    }

    @Test
    void testGroupCreation() {
        def name = "biographer"

        client.deleteGroup(name)
        RemoteGroup group = client.createGroup(name)

        assert group.name == name
    }
}
