package de.codecentric.janus.jira

import com.atlassian.jira.rpc.soap.beans.RemoteGroup

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface JiraSoapClient {
    void deleteGroup(String groupName)
    RemoteGroup createGroup(String groupName)
}