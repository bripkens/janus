package de.codecentric.janus.generation

import org.junit.Test
import de.codecentric.janus.scaffold.Descriptor
import de.codecentric.janus.scaffold.Scaffold
import java.util.zip.ZipFile
import de.codecentric.janus.conf.Project

/**
 * Please manually verify the generated output.
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldExecutorTest {
    Scaffold scaffold
    Map context

    ScaffoldExecutorTest() {
        File zip = new File(this.getClass().getClassLoader()
                .getResource('quickstart.zip').toURI())
        scaffold = Scaffold.from(zip)

        context = [
                'artifactId': 'Janus',
                'message': 'Hello World!'
        ] as HashMap
    }

    @Test void testNoPackage() {
        File out = getOutputDirectory('noPackage')
        new ScaffoldExecutor(scaffold, createProject(''), context).apply(out)
        println "Please verify the test results manually by" +
                " investigating ${out.absolutePath}."
    }

    private Project createProject(String pckg) {
        new Project(['name': 'Janus',
                'description': 'Just a small test...',
                'pckg': pckg])
    }

    File getOutputDirectory(String subDirectory) {
        new File(new File('.').getAbsolutePath() + File.separator +
                'build' + File.separator + 'generated' + File.separator +
                subDirectory)
    }

    @Test void testWithPackage() {
        File out = getOutputDirectory('withPackage')
        new ScaffoldExecutor(scaffold, createProject('de.codecentric'),
                context).apply(out)
        println "Please verify the test results manually by" +
                " investigating ${out.absolutePath}."
    }

    @Test(expected = ScaffoldingException.class) void testWrongContext() {
        File out = getOutputDirectory('invalidContext')
        new ScaffoldExecutor(scaffold, createProject('de.codecentric'),
                [] as HashMap).apply(out)
        println "Please verify the test results manually by" +
                " investigating ${out.absolutePath}."
    }
}
