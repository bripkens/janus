package de.codecentric.janus.atlassian.confluence

import com.atlassian.confluence.rpc.soap.ConfluenceSoapService
import com.atlassian.confluence.rpc.soap.beans.RemoteSpace
import java.rmi.RemoteException
import de.codecentric.janus.atlassian.AtlassianException

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ConfluenceSoapClientImpl implements ConfluenceSoapClient {
    private final ConfluenceSoapService service
    private final String token

    ConfluenceSoapClientImpl(ConfluenceSoapSession session) {
        service = session.getService()
        token = session.getAuthToken()
    }

    @Override
    RemoteSpace addSpace(RemoteSpace space) {
        return maskRemoteException {
            return service.addSpace(token, space)
        }
    }

    private maskRemoteException(Closure closure) {
        try {
            return closure()
        } catch (RemoteException ex) {
            throw new AtlassianException(ex)
        }
    }
}
