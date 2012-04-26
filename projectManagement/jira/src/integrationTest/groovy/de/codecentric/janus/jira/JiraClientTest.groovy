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

import com.atlassian.jira.rpc.soap.beans.RemoteGroup
import com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme
import com.atlassian.jira.rpc.soap.beans.RemoteUser
import com.atlassian.jira.rpc.soap.beans.RemoteProject

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JiraClientTest {

    static final String BASE_URL = 'http://localhost:47623',
                        USERNAME = 'janustestadmin',
                        PASSWORD = 'janusRocks',
                        TEST_GROUP_NAME = 'janus-users',
                        TEST_USER_USERNAME = 'tom',
                        TEST_USER_PASSWORD = '12345',
                        TEST_USER_FULL_NAME = 'Tom Tomson',
                        TEST_USER_EMAIL = 'tom@example.com',
                        TEST_PROJECT_NAME = 'janus',
                        TEST_PROJECT_KEY = 'JAN'

    Session session
    JiraClient client

    @Before
    void setup() {
        session = new Session(BASE_URL, USERNAME, PASSWORD)
        client = new JiraClient(session)
    }

    def createTestGroup() {
        // for idempotent test
        client.deleteGroup(TEST_GROUP_NAME)
        return client.createGroup(TEST_GROUP_NAME)
    }

    def createTestUser() {
        // for idempotent test
        client.deleteUser(TEST_USER_USERNAME)
        return client.createUser(TEST_USER_USERNAME, TEST_USER_PASSWORD,
                TEST_USER_FULL_NAME, TEST_USER_EMAIL)
    }

    def getUserValidator() {
        return {
            it.name == TEST_USER_USERNAME && it.email == TEST_USER_EMAIL &&
                    it.fullname == TEST_USER_FULL_NAME
        }
    }

    def createTestProject() {
        // for idempotent test
        client.deleteProject(TEST_PROJECT_KEY)

        RemoteProject project = new RemoteProject();
        project.setName(TEST_PROJECT_NAME);
        project.setKey(TEST_PROJECT_KEY);
        project.setLead(USERNAME);
        project.setPermissionScheme(client
                .getPermissionScheme("Default Permission Scheme"));
        project.setNotificationScheme(client
                .getNotificationScheme("Default Notification Scheme"));

        return client.createProject(project)
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

    @Test void shouldCreateGroup() {
        RemoteGroup group = createTestGroup()

        assert group.name == TEST_GROUP_NAME
        assert client.getGroup(TEST_GROUP_NAME).name == TEST_GROUP_NAME
        assert client.getGroups().any { it.name == TEST_GROUP_NAME }
    }

    @Test void shouldCreateUser() {
        RemoteUser user = createTestUser()

        assert userValidator(user)
        assert client.searchUser(TEST_USER_USERNAME).any(userValidator)
    }

    @Test void shouldAddUsersToGroup() {
        RemoteUser user = createTestUser()
        RemoteGroup group = createTestGroup()

        client.addUserToGroup(group, user)

        group = client.getGroup(group.name)
        assert group.users.any { it.name == user.name }
        assert group.users.length == 1
    }

    @Test void shouldCreateProject() {
        RemoteProject project = createTestProject()

        assert project.name == TEST_PROJECT_NAME
    }
}
