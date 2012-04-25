package de.codecentric.janus.jira

import com.atlassian.jira.rpc.exception.RemoteValidationException
import com.atlassian.jira.rpc.soap.beans.RemoteGroup
import java.rmi.RemoteException
import com.atlassian.jira.rpc.soap.JiraSoapService

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JiraSoapClientImpl implements JiraSoapClient {
    private final JiraSoapService service
    private final String token

    JiraSoapClientImpl(JiraSoapSession session) {
        service = session.getService()
        token = session.getAuthToken()
    }

    @Override
    void deleteGroup(String groupName) {
        try {
            service.deleteGroup(token, groupName, null)
        } catch (RemoteValidationException ex) {
            // gets thrown when the group does not exist.
        } catch (RemoteException ex) {
            throw new JiraConnectionException(ex)
        }
    }

    @Override
    RemoteGroup createGroup(String groupName) {
        // JavaDoc lists third argument as optional
        try {
            return service.createGroup(token, groupName, null)
        } catch (RemoteException ex) {
            throw new JiraConnectionException(ex)
        }
    }
}
