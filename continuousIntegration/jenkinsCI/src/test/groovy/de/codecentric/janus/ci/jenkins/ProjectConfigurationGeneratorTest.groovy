package de.codecentric.janus.ci.jenkins

import de.codecentric.janus.conf.Project

import org.junit.Test
import org.junit.Before

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ProjectConfigurationGeneratorTest {

    Project project
    
    @Before void setup() {
        project = new Project(name: 'Janus',
                description: 'A dummy project')
    }
    
    @Test void testGenerate() {
        println new ProjectConfigurationGenerator().generate(project)
    }
}
