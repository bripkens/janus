package de.codecentric.janus.jira

import com.atlassian.jira.rpc.soap.beans.RemoteGroup
import com.atlassian.jira.rpc.soap.beans.RemoteProject
import com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme
import com.atlassian.jira.rpc.soap.beans.RemoteScheme
import com.atlassian.jira.rpc.soap.beans.RemoteEntity
import com.atlassian.jira.rpc.soap.beans.RemotePermission
import com.atlassian.jira.rpc.soap.beans.RemoteProjectRole
import com.atlassian.jira.rpc.soap.beans.RemoteUser
import com.atlassian.jira.rpc.soap.beans.RemoteRoleActors

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface JiraSoapClient {
    void deleteGroup(String groupName)
    RemoteGroup createGroup(String groupName)
    RemoteProject getProject(String key)
    void deletePermissionScheme(String name)
    RemotePermissionScheme[] getPermissionSchemes()
    RemotePermissionScheme getPermissionScheme(String name)
    RemoteScheme[] getNotificationSchemes()
    RemoteScheme getNotificationScheme(String name)
    RemotePermissionScheme createPermissionScheme(String name)
    RemotePermissionScheme addPermissionTo(RemotePermissionScheme scheme,
                                           RemotePermission permission,
                                           RemoteEntity entity)
    void deleteProject(String projectKey)
    RemoteProject createProject(RemoteProject project)
    RemoteProjectRole[] getProjectRoles()
    RemoteProjectRole getProjectRole(String name)
    RemoteUser createUser(String username, String password, String fullName,
                          String email)
    void deleteUser(String username)
    void addUserToGroup(RemoteGroup group, RemoteUser user)
    void addGroupToRole(RemoteProject project, RemoteGroup group,
                        RemoteProjectRole role)
    RemoteRoleActors getDefaultRoleActors(RemoteProjectRole role)
}