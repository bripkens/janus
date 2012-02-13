package de.codecentric.janus.ci.jenkins

import de.codecentric.janus.ci.jenkins.conf.ServiceConfig
import de.codecentric.janus.conf.Project
import org.junit.Before
import org.junit.Test
import de.codecentric.janus.conf.vcs.VCSConfig
import de.codecentric.janus.conf.vcs.MercurialConfig
import de.codecentric.janus.VersionControlSystem
import de.codecentric.janus.scaffold.Scaffold
import de.codecentric.janus.scaffold.BuildJob
import de.codecentric.janus.scaffold.BuildJobTask

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JenkinsProjectCreatorTest {
    static final VCS_URL = 'https://code.google.com/p/janus-example-scaffold/'

    Project project
    ServiceConfig config
    VCSConfig vcsConfig
    Scaffold scaffold
    
    @Before void setup() {
        project = new Project([name: 'Janus',
                description: 'Simple project creation'])
        config = new ServiceConfig([username: 'test',
                apiToken: '1f75782b174cec612e1c28dc8f8cbf82',
                uri: 'http://localhost:8080'])
        vcsConfig = new MercurialConfig(url: VCS_URL,
                vcs: VersionControlSystem.MERCURIAL)
        scaffold = new Scaffold()
        scaffold.buildJobs = [new BuildJob(name: 'Entities', concurrentBuild: false,
                tasks: [
                        new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                                options: [targets: 'clean install']),
                        new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                                options: [targets: 'clean'])
                ]), new BuildJob(name: 'Main', concurrentBuild: false,
                tasks: [
                        new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                                options: [targets: 'clean install']),
                        new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                                options: [targets: 'clean'])
                ])]
    }

    @Test void create() {
        new JenkinsProjectCreator(config, project, vcsConfig, scaffold,
                [] as HashMap).create()
    }
}
