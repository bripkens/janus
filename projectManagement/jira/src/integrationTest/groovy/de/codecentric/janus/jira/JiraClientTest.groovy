package de.codecentric.janus.jira

import org.junit.Test
import org.junit.Before
import static org.junit.Assert.assertThat
import static org.hamcrest.CoreMatchers.*
import static org.hamcrest.Matcher.*
import com.atlassian.jira.rpc.soap.beans.RemoteGroup
import com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JiraClientTest {

    static final String BASE_URL = 'http://localhost:47623',
            USERNAME = 'janustestadmin',
            PASSWORD = 'janusRocks'

    Session session
    JiraClient client

    @Before void setup() {
        session = new Session(BASE_URL, USERNAME, PASSWORD)
        client = new JiraClient(session)
    }

    @Test void shouldDeleteAndCreateGroups() {
        def name = 'biographer'

        client.deleteGroup(name)
        RemoteGroup group = client.createGroup(name)

        assert group.name == name
    }

    @Test void shouldRetrieveDefaultPermissionScheme() {
        def name = 'Default Permission Scheme'

        RemotePermissionScheme scheme = client.getPermissionScheme(name)

        assert scheme.name == name
    }

    @Test void shouldRetrieveAllGroups() {
        def groups = client.getGroups()

        assert groups.any { it.name == 'jira-users' }
        assert groups.any { it.name == 'jira-administrators' }
        assert groups.any { it.name == 'jira-developers' }
    }
}
