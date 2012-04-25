package de.codecentric.janus.jira

import com.atlassian.jira.rpc.soap.beans.RemoteGroup
import com.atlassian.jira.rpc.soap.beans.RemoteProject
import com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme
import com.atlassian.jira.rpc.soap.beans.RemoteScheme
import com.atlassian.jira.rpc.soap.beans.RemotePermission
import com.atlassian.jira.rpc.soap.beans.RemoteEntity
import com.atlassian.jira.rpc.soap.beans.RemoteProjectRole
import com.atlassian.jira.rpc.soap.beans.RemoteUser
import com.atlassian.jira.rpc.soap.beans.RemoteRoleActors
import de.codecentric.janus.jira.model.RemoteGroupSummary
import de.codecentric.janus.jira.model.RemoteProjectSummary

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JiraClient implements JiraSoapClient, JiraRestClient {

    final Session session

    JiraClient(Session session) {
        this.session = session
    }

    @Override
    void deleteGroup(String groupName) {
        session.getJiraSoapClient().deleteGroup(groupName)
    }

    @Override
    RemoteGroup createGroup(String groupName) {
        return session.getJiraSoapClient().createGroup(groupName)
    }

    @Override
    RemoteProject getProject(String key) {
        return session.getJiraSoapClient().getProject(key)
    }

    @Override
    void deletePermissionScheme(String name) {
        session.getJiraSoapClient().deletePermissionScheme(name)
    }

    @Override
    RemotePermissionScheme[] getPermissionSchemes() {
        return session.getJiraSoapClient().getPermissionSchemes()
    }

    @Override
    RemotePermissionScheme getPermissionScheme(String name) {
        return session.getJiraSoapClient().getPermissionScheme(name);
    }

    @Override
    RemoteScheme[] getNotificationSchemes() {
        return session.getJiraSoapClient().getNotificationSchemes()
    }

    @Override
    RemoteScheme getNotificationScheme(String name) {
        return session.getJiraSoapClient().getNotificationScheme(name)
    }

    @Override
    RemotePermissionScheme createPermissionScheme(String name) {
        return session.getJiraSoapClient().createPermissionScheme(name)
    }

    @Override
    RemotePermissionScheme addPermissionTo(RemotePermissionScheme scheme,
                                           RemotePermission permission,
                                           RemoteEntity entity) {
        return session.getJiraSoapClient().addPermissionTo(scheme, permission,
                entity)
    }

    @Override
    void deleteProject(String projectKey) {
        session.getJiraSoapClient().deleteProject(projectKey)
    }

    @Override
    RemoteProject createProject(RemoteProject project) {
        return session.getJiraSoapClient().createProject(project)
    }

    @Override
    RemoteProjectRole[] getProjectRoles() {
        return session.getJiraSoapClient().getProjectRoles()
    }

    @Override
    RemoteProjectRole getProjectRole(String name) {
        return session.getJiraSoapClient().getProjectRole(name)
    }

    @Override
    RemoteUser createUser(String username, String password, String fullName, String email) {
        return session.getJiraSoapClient().createUser(username, password,
                fullName, email)
    }

    @Override
    void deleteUser(String username) {
        session.getJiraSoapClient().deleteUser(username)
    }

    @Override
    void addUserToGroup(RemoteGroup group, RemoteUser user) {
        session.getJiraSoapClient().addUserToGroup(group, user)
    }

    @Override
    void addGroupToRole(RemoteProject project, RemoteGroup group, RemoteProjectRole role) {
        session.getJiraSoapClient().addGroupToRole(project, group, role)
    }

    @Override
    RemoteRoleActors getDefaultRoleActors(RemoteProjectRole role) {
        return session.getJiraSoapClient().getDefaultRoleActors(role)
    }

    @Override
    RemoteGroupSummary[] getGroups() {
        return session.getJiraRestClient().getGroups()
    }

    @Override
    RemoteProjectSummary[] getProjects() {
        return session.getJiraRestClient().getProjects()
    }
}
