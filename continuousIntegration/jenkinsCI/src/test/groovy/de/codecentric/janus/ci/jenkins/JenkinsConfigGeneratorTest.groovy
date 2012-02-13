package de.codecentric.janus.ci.jenkins

import org.junit.Test
import org.junit.Before
import de.codecentric.janus.conf.Project
import de.codecentric.janus.conf.vcs.VCSConfig
import de.codecentric.janus.conf.vcs.MercurialConfig
import de.codecentric.janus.VersionControlSystem
import de.codecentric.janus.scaffold.BuildJob
import de.codecentric.janus.scaffold.BuildJobTask

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JenkinsConfigGeneratorTest {

    static final VCS_URL = 'https://code.google.com/p/janus-example-scaffold/'

    Project project
    VCSConfig vcsConfig

    @Before void setup() {
        project = new Project([name: 'Janus',
                description: 'Simple project creation'])
        vcsConfig = new MercurialConfig(url: VCS_URL,
                vcs: VersionControlSystem.MERCURIAL)
    }
    
    @Test void generate() {
        def buildJob = new BuildJob(name: 'Main', concurrentBuild: false,
                tasks: [
                        new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                                options: [targets: 'clean build'])
                ])
        def generator = new JenkinsConfigGenerator(project, buildJob,
                [] as HashMap, vcsConfig)

        String exp = """\
<?xml version='1.0' encoding='UTF-8'?>
<project>
    <actions/>
    <description>Simple project creation</description>
    <logRotator>
        <daysToKeep>-1</daysToKeep>
        <numToKeep>10</numToKeep>
        <artifactDaysToKeep>-1</artifactDaysToKeep>
        <artifactNumToKeep>10</artifactNumToKeep>
    </logRotator>
    <keepDependencies>false</keepDependencies>
    <properties/>

    <scm class="hudson.plugins.mercurial.MercurialSCM">
        <installation>(Default)</installation>
        <source>https://code.google.com/p/janus-example-scaffold/</source>
        <modules></modules>

        <clean>false</clean>
        <browser class="hudson.plugins.mercurial.browser.HgWeb">
            <url>https://code.google.com/p/janus-example-scaffold/</url>
        </browser>
    </scm>

    <canRoam>true</canRoam>
    <disabled>false</disabled>
    <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
    <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
    <triggers class="vector">
        <hudson.triggers.SCMTrigger>
            <spec>* * * * *</spec>
        </hudson.triggers.SCMTrigger>
    </triggers>
    <concurrentBuild>
        false
    </concurrentBuild>

    <builders>
        <hudson.tasks.Maven>
            <targets>clean build</targets>
            <mavenName>(Default)</mavenName>
            <pom>pom.xml</pom>
            <usePrivateRepository>false</usePrivateRepository>
        </hudson.tasks.Maven>
    </builders>

    <publishers/>
    <buildWrappers/>
</project>"""
        
        assert generator.generate() == exp
    }
}
