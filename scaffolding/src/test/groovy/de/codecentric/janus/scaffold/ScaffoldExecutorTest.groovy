package de.codecentric.janus.scaffold

import org.junit.Before
import org.junit.Test

/**
 * Please manually verify the generated output.
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldExecutorTest {

    Scaffold scaffold

    ScaffoldExecutorTest() {
        this.scaffold = new Scaffold([filename: 'quickstart.zip'])
    }

    @Test void testNoPackage() {
        ScaffoldExecutor executor = new ScaffoldExecutor(scaffold, "")
        executor.apply(new File('/home/ben/tmp/noPackage'))
    }

    @Test void testWithPackage() {
        ScaffoldExecutor executor = new ScaffoldExecutor(scaffold, "de.codecentric")
        executor.apply(new File('/home/ben/tmp/withPackage'))
    }
}
