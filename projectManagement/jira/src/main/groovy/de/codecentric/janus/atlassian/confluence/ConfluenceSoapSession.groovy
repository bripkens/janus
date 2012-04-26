package de.codecentric.janus.atlassian.confluence

import com.atlassian.confluence.rpc.soap.ConfluenceSoapService
import com.atlassian.confluence.rpc.soap.ConfluenceSoapServiceService
import com.atlassian.confluence.rpc.soap.ConfluenceSoapServiceServiceLocator
import groovy.util.logging.Slf4j

import javax.xml.rpc.ServiceException

import de.codecentric.janus.atlassian.Credentials
import de.codecentric.janus.atlassian.AtlassianConnectionException

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Slf4j
class ConfluenceSoapSession {
    static final String ENDPOINT_EXTENSION = '/rpc/soap-axis/confluenceservice-v2'

    final ConfluenceSoapServiceService serviceLocator
    final ConfluenceSoapService service
    final String authToken
    final Credentials credentials
    final URL endpoint

    ConfluenceSoapSession(String baseUrl, Credentials credentials) {
        assert baseUrl != null

        endpoint = new URL(baseUrl + ENDPOINT_EXTENSION)
        serviceLocator = new ConfluenceSoapServiceServiceLocator();
        this.credentials = credentials

        try {
            service = serviceLocator.getConfluenceserviceV2(endpoint)
            log.info("Connected to ${endpoint.toExternalForm()}")
        } catch (ServiceException e) {
            throw new AtlassianConnectionException("ServiceException during " +
                    "SOAPClient contruction", e);
        }

        if (credentials != null) {
            authToken = service.login(credentials.name, credentials.password)
            log.info("Logged into Confluence SOAP service.")
        }
    }

    def close() {
        if (authToken != null) {
            service.logout(authToken)
        }
    }
}
