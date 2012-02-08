package de.codecentric.janus.ci.jenkins

import de.codecentric.janus.ci.jenkins.conf.ServiceConfig
import de.codecentric.janus.conf.Project
import org.junit.Before
import org.junit.Test
import org.junit.Ignore

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JenkinsProjectCreatorTest {
    Project project
    ServiceConfig config
    
    @Before void setup() {
        project = new Project([name: 'Janus',
                description: 'Simple project creation'])
        config = new ServiceConfig([username: 'test',
                apiToken: '1f75782b174cec612e1c28dc8f8cbf82',
                uri: 'http://localhost:8080'])
    }
    
    @Test void createJob() {
        //assert new JenkinsProjectCreator(config).apply(project)
    }
}
