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
}
