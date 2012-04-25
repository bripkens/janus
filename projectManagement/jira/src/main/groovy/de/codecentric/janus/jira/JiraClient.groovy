package de.codecentric.janus.jira

import com.atlassian.jira.rpc.soap.beans.RemoteGroup

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JiraClient implements JiraSoapClient {

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
}
