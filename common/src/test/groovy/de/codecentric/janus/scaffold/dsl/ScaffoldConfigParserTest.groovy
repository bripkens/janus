package de.codecentric.janus.scaffold.dsl

import org.junit.Before
import org.junit.Test
import de.codecentric.janus.scaffold.Scaffold
import de.codecentric.janus.scaffold.BuildJobTask
import de.codecentric.janus.Application

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldConfigParserTest {
    File config

    @Before void setup() {
        Application.bootstrap()
        config = new File(this.getClass().getClassLoader()
                .getResource('config.janus').toURI())
    }

    @Test void testParse() {
        Scaffold scaffold = ScaffoldConfigParser.parse(config)

        assert scaffold.name == 'RESTful Web application'
        assert scaffold.description == 'Web based project with RESTeasy based web service'

        assert scaffold.requiredContext.size() == 2
        assert scaffold.requiredContext['groupId'] == 'Please see Maven groupId'
        assert scaffold.requiredContext['artifactId'] == 'Plase see Maven artifactId'

        assert scaffold.buildJobs.size() == 2
        assert scaffold.buildJobs[0].name == 'entities'
        assert !scaffold.buildJobs[0].concurrentBuild
        assert scaffold.buildJobs[0].tasks.size() == 1
        assert scaffold.buildJobs[0].tasks[0].type == BuildJobTask.Type.MAVEN
        assert scaffold.buildJobs[0].tasks[0].options.size() == 2
        assert scaffold.buildJobs[0].tasks[0]['targets'] == 'clean package'
        assert scaffold.buildJobs[0].tasks[0]['pom'] == 'entities/pom.xml'

        assert scaffold.buildJobs[1].name == 'parent'
        assert scaffold.buildJobs[1].concurrentBuild
        assert scaffold.buildJobs[1].tasks.size() == 2
        assert scaffold.buildJobs[1].tasks[0].type == BuildJobTask.Type.MAVEN
        assert scaffold.buildJobs[1].tasks[0].options.size() == 1
        assert scaffold.buildJobs[1].tasks[0]['targets'] == 'clean install'
        assert scaffold.buildJobs[1].tasks[0]['pom'] == 'pom.xml'
        assert scaffold.buildJobs[1].tasks[1].type == BuildJobTask.Type.MAVEN
        assert scaffold.buildJobs[1].tasks[1].options.size() == 1
        assert scaffold.buildJobs[1].tasks[1]['targets'] == 'clean'
        assert scaffold.buildJobs[1].tasks[1]['pom'] == 'pom.xml'

        assert scaffold.buildJobs[1].tasks[1]['foobar'] == null
    }
}
