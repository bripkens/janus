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

import com.atlassian.jira.rpc.soap.beans.RemoteGroup
import com.atlassian.jira.rpc.soap.beans.RemoteProject
import com.atlassian.jira.rpc.soap.beans.RemotePermissionScheme
import com.atlassian.jira.rpc.soap.beans.RemoteScheme
import com.atlassian.jira.rpc.soap.beans.RemoteEntity
import com.atlassian.jira.rpc.soap.beans.RemotePermission
import com.atlassian.jira.rpc.soap.beans.RemoteProjectRole
import com.atlassian.jira.rpc.soap.beans.RemoteUser
import com.atlassian.jira.rpc.soap.beans.RemoteRoleActors
import com.atlassian.jira.rpc.soap.beans.RemoteProjectRoleActors

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface JiraSoapClient {
    RemoteGroup getGroup(String groupName)
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
    RemoteProjectRoleActors getProjectRoleActors(RemoteProject project,
                                                 RemoteProjectRole role)
}