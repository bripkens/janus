package de.codecentric.janus.atlassian.confluence

import com.atlassian.confluence.rpc.soap.beans.RemoteSpace

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ConfluenceClient implements ConfluenceSoapClient {
    final ConfluenceSession session

    ConfluenceClient(ConfluenceSession session) {
        this.session = session
    }

    @Override
    RemoteSpace addSpace(RemoteSpace space) {
        return session.getConfluenceSoapClient().addSpace(space)
    }
}
