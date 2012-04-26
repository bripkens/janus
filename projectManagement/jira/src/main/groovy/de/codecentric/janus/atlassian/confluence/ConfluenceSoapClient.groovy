package de.codecentric.janus.atlassian.confluence

import com.atlassian.confluence.rpc.soap.beans.RemoteSpace

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
interface ConfluenceSoapClient {
    RemoteSpace addSpace(RemoteSpace space)
}
