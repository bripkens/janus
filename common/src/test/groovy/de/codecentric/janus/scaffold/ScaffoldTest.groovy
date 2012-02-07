package de.codecentric.janus.scaffold

import org.junit.Test
import org.junit.Before

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldTest {
    File zip

    @Before void setup() {
        zip = new File(this.getClass().getClassLoader()
                .getResource('quickstart.zip').toURI())
    }
    
    @Test void testFrom() {
        Scaffold scaffold = Scaffold.from(zip)
        assert scaffold.name == 'Quickstart'
        assert scaffold.description == 'An empty Gradle project.'
        assert scaffold.file == zip
        assert scaffold.requiredContext['artifactId'] == 'A Maven like artifact id'
        assert scaffold.requiredContext['message'] == 'The initial message...'
    }
}
