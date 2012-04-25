package de.codecentric.janus.jira

import com.atlassian.jira.rpc.exception.RemoteValidationException
import com.atlassian.jira.rpc.soap.beans.RemoteGroup
import java.rmi.RemoteException
import com.atlassian.jira.rpc.soap.JiraSoapService
import com.atlassian.jira.rpc.soap.beans.RemoteProject
import com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme
import com.atlassian.jira.rpc.soap.beans.RemoteScheme
import com.atlassian.jira.rpc.soap.beans.RemotePermission
import com.atlassian.jira.rpc.soap.beans.RemoteEntity
import com.atlassian.jira.rpc.soap.beans.RemoteProjectRole
import com.atlassian.jira.rpc.soap.beans.RemoteUser
import com.atlassian.jira.rpc.soap.beans.RemoteRoleActors

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JiraSoapClientImpl implements JiraSoapClient {
    static final JIRA_GROUP_ROLE_ACTOR_IDENTIFIER = 'atlassian-group-role-actor'

    private final JiraSoapService service
    private final String token

    JiraSoapClientImpl(JiraSoapSession session) {
        service = session.getService()
        token = session.getAuthToken()
    }

    @Override
    void deleteGroup(String groupName) {
        maskRemoteException {
            try {
                service.deleteGroup(token, groupName, null)
            } catch (RemoteValidationException ex) {
                // gets thrown when the group does not exist.
            }
        }
    }

    @Override
    RemoteGroup createGroup(String groupName) {
        return maskRemoteException {
            // JavaDoc lists third argument as optional
            return service.createGroup(token, groupName, null)
        }
    }

    @Override
    RemoteProject getProject(String key) {
        return maskRemoteException {
            return service.getProjectByKey(token, key)
        }
    }

    @Override
    void deletePermissionScheme(String name) {
        maskRemoteException {
            service.deletePermissionScheme(token, name)
        }
    }

    @Override
    RemotePermissionScheme[] getPermissionSchemes() {
        return maskRemoteException {
            return service.getPermissionSchemes(token)
        }
    }

    @Override
    RemotePermissionScheme getPermissionScheme(String name) {
        return getPermissionSchemes().find { it.name == name }
    }

    @Override
    RemoteScheme[] getNotificationSchemes() {
        return maskRemoteException {
            return service.getNotificationSchemes(token)
        }
    }

    @Override
    RemoteScheme getNotificationScheme(String name) {
        return getNotificationSchemes().find { it.name == name }
    }

    @Override
    RemotePermissionScheme createPermissionScheme(String name) {
        return maskRemoteException {
            return service.createPermissionScheme(token, name, null)
        }
    }

    @Override
    RemotePermissionScheme addPermissionTo(RemotePermissionScheme scheme,
                                           RemotePermission permission,
                                           RemoteEntity entity) {
        return maskRemoteException {
            return service.addPermissionTo(token, scheme, permission, entity)
        }
    }

    @Override
    void deleteProject(String projectKey) {
        maskRemoteException {
            service.deleteProject(token, projectKey)
        }
    }

    @Override
    RemoteProject createProject(RemoteProject project) {
        return maskRemoteException {
            return service.createProjectFromObject(token, project)
        }
    }

    @Override
    RemoteProjectRole[] getProjectRoles() {
        return maskRemoteException {
            return service.getProjectRoles(token)
        }
    }

    @Override
    RemoteProjectRole getProjectRole(String name) {
        return getProjectRoles().find { it.name == name }
    }

    @Override
    RemoteUser createUser(String username, String password, String fullName,
                          String email) {
        return maskRemoteException {
            return service.createUser(token, username.toLowerCase(),
                    password, fullName, email)
        }
    }

    @Override
    void deleteUser(String username) {
        maskRemoteException {
            service.deleteUser(token, username)
        }
    }

    @Override
    void addUserToGroup(RemoteGroup group, RemoteUser user) {
        maskRemoteException {
            service.addUserToGroup(token, group, user)
        }
    }

    @Override
    void addGroupToRole(RemoteProject project, RemoteGroup group,
                        RemoteProjectRole role) {
        maskRemoteException {
            service.addActorsToProjectRole(token, [group.getName()] as String[],
                    role, project, JIRA_GROUP_ROLE_ACTOR_IDENTIFIER)
        }
    }

    @Override
    RemoteRoleActors getDefaultRoleActors(RemoteProjectRole role) {
        return maskRemoteException {
            return service.getDefaultRoleActors(token, role)
        }
    }

    private maskRemoteException(Closure closure) {
        try {
            return closure()
        } catch (RemoteException ex) {
            throw new JiraClientException(ex)
        }
    }
}
