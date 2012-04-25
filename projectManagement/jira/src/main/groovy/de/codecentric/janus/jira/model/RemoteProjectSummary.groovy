package de.codecentric.janus.jira.model

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class RemoteProjectSummary {
    final String id, key, name

    RemoteProjectSummary(String id, String key, String name) {
        this.id = id
        this.key = key
        this.name = name
    }
}
