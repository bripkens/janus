/*
 * Copyright (C) 2012 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    
    @Test void generateMaven() {
        def buildJob = new BuildJob(name: 'Main', concurrentBuild: false,
                tasks: [
                        new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                                options: [targets: 'clean build'])
                ])
        def generator = new JenkinsConfigGenerator(project, buildJob,
                [] as HashMap, vcsConfig)

        assert generator.generate() == """\
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

    <publishers>
    </publishers>

    <buildWrappers/>
</project>"""
    }

    @Test void testGenerateCombination() {
        def buildJob = new BuildJob(name: 'Main',
                concurrentBuild: true,
                disabled: true,
                tasks: [
                        new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                                options: [targets: 'clean install']),
                        new BuildJobTask(type: BuildJobTask.Type.ANT,
                                options: [targets: 'clean build']),
                        new BuildJobTask(type: BuildJobTask.Type.SHELL,
                                options: [value: 'cd ~ && ls übung']),
                        new BuildJobTask(type: BuildJobTask.Type.BATCH,
                                options: [value: 'cd c:\\']),
                        new BuildJobTask(type: BuildJobTask.Type.FAIL)
                ])
        buildJob.downstreamBuilds[BuildJob.Status.SUCCESS].add('parent')
        def generator = new JenkinsConfigGenerator(project, buildJob,
                [] as HashMap, vcsConfig)

        assert generator.generate() == """\
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
    <disabled>true</disabled>
    <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
    <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
    <triggers class="vector">
        <hudson.triggers.SCMTrigger>
            <spec>* * * * *</spec>
        </hudson.triggers.SCMTrigger>
    </triggers>
    <concurrentBuild>
        true
    </concurrentBuild>

    <builders>
        <hudson.tasks.Maven>
            <targets>clean install</targets>
            <mavenName>(Default)</mavenName>
            <pom>pom.xml</pom>
            <usePrivateRepository>false</usePrivateRepository>
        </hudson.tasks.Maven>
        <hudson.tasks.Ant>
            <targets>clean build</targets>
            <buildFile>build.xml</buildFile>
        </hudson.tasks.Ant>
        <hudson.tasks.Shell>
            <command>cd ~ &amp;&amp; ls &#252;bung</command>
        </hudson.tasks.Shell>
        <hudson.tasks.BatchFile>
            <command>cd c:\\</command>
        </hudson.tasks.BatchFile>
        <org.jvnet.hudson.test.FailureBuilder/>
    </builders>

    <publishers>
        <hudson.tasks.BuildTrigger>
            <childProjects>Janus-parent</childProjects>
            <threshold>
                <name>SUCCESS</name>
                <ordinal>0</ordinal>
                <color>BLUE</color>
            </threshold>
        </hudson.tasks.BuildTrigger>
    </publishers>

    <buildWrappers/>
</project>"""
    }
}
