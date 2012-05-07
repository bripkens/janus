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

package de.codecentric.janus.atlassian.confluence

import com.atlassian.confluence.rpc.soap.beans.RemoteSpace
import com.atlassian.confluence.rpc.soap.beans.RemoteUser

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
interface ConfluenceSoapClient {
    RemoteSpace addSpace(RemoteSpace space)
    void deleteSpace(String key)
    void createUser(RemoteUser user, String password)
    RemoteUser getUser(String username)
    void deleteUser(String username)
    RemoteSpace getSpace(String key)
    void removeAnonymousPermissionFromSpace(SpacePermission permission,
                                            String spaceKey)
    void removeAllAnonymousPermissionsFromSpace(String spaceKey)
    Collection<String> getPermissions(String spaceKey)
    void addPermissionToSpace(SpacePermission permission, String entityName,
                              String spaceKey)
    void addGroup(String name)
    void deleteGroup(String groupName)
    boolean hasGroup(String groupName)
}
