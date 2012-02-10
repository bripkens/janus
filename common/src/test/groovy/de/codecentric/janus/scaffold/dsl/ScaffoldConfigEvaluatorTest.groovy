package de.codecentric.janus.scaffold.dsl

import org.junit.Before
import org.junit.Test
import de.codecentric.janus.scaffold.Scaffold

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldConfigEvaluatorTest {
    File config

    @Before void setup() {
        config = new File(this.getClass().getClassLoader()
                .getResource('config.janus').toURI())
    }

    @Test void run() {
        Scaffold scaffold = new ScaffoldConfigEvaluator(config).evaluate()

        assert scaffold.name == 'RESTful Web application'
        assert scaffold.description == 'Web based project with RESTeasy based web service'

        assert scaffold.requiredContext.size() == 2
        assert scaffold.requiredContext['groupId'] == 'Please see Maven groupId'
        assert scaffold.requiredContext['artifactId'] == 'Plase see Maven artifactId'
    }
}
