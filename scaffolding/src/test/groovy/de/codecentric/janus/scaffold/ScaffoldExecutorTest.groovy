package de.codecentric.janus.scaffold

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

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
        ScaffoldExecutor executor = new ScaffoldExecutor(scaffold, "",
                { testScaffoldLoader(it) })
        File out = getOutputDirectory('noPackage')
        executor.apply(out)
        println "Please verify the test results manually by" +
                " investigating ${out.absolutePath}."
    }

    @Test void testWithPackage() {
        ScaffoldExecutor executor = new ScaffoldExecutor(scaffold,
                "de.codecentric", { testScaffoldLoader(it) })
        File out = getOutputDirectory('withPackage')
        executor.apply(out)
        println "Please verify the test results manually by" +
                " investigating ${out.absolutePath}."
    }

    File testScaffoldLoader(Scaffold scaffold) {
        return new File(this.getClass().getClassLoader()
                .getResource(scaffold.filename).toURI())
    }

    File getOutputDirectory(String subDirectory) {
        new File(new File('.').getAbsolutePath() + File.separator +
                'build' + File.separator + subDirectory)
    }
}
