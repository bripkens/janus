/*
 * Copyright (C) 2012 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    @Test void shouldRetrieveAllProjects() {
        client.getProjects()

        // For now this is an assertion free test. While it does not test much,
        // it ensures that the main logic is executed properly. Assertions
        // can not be used at this point since we do not know if projects exist
        // or not. Further tests can be made as part of the createProject(...)
        // tests.
    }

    @Test void shouldSearchUser() {
        assert client.searchUser(USERNAME).any { it.name == USERNAME }
    }
}
