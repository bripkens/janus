package de.codecentric.janus.jira;

import de.codecentric.janus.jira.model.RemoteGroupSummary
import de.codecentric.janus.jira.model.RemoteProjectSummary;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public interface JiraRestClient {
    RemoteGroupSummary[] getGroups()
    RemoteProjectSummary[] getProjects()
}
