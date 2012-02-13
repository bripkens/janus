package de.codecentric.janus.scaffold

import org.junit.Test

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class BuildJobTaskTest {
    
    @Test void testValid() {
        def task = new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                options: [targets: 'clean install'])
        assert task.valid
    }

    @Test void testNotValidNoType() {
        def task = new BuildJobTask(options: [targets: 'clean install'])
        assert !task.valid
    }
    
    @Test void testNotValidInsufficientOptions() {
        def task = new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                options: [foobar: 'humpty dumpty'])
        assert !task.valid
    }

    @Test void testGet() {
        def targets = 'clean install'
        def task = new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                options: [targets: targets])

        assert task['targets'] == targets
        assert task['pom'] == 'pom.xml'
    }

    @Test void testGetNoType() {
        def task = new BuildJobTask(options: [targets: 'clean install'])
        assert task['pom'] == null
    }
}
