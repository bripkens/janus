package de.codecentric.janus.scaffold

import org.junit.Before
import org.junit.Test

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldExecutorTest {

    @Test void testApply() {
        ScaffoldExecutor executor = new ScaffoldExecutor(new Scaffold([filename: 'quickstart.zip']))
        executor.apply(new File('/home/ben/tmp'))
    }
}
