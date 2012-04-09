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

package de.codecentric.janus.scaffold.dsl

import org.junit.Before
import org.junit.Test
import de.codecentric.janus.scaffold.Scaffold
import de.codecentric.janus.scaffold.BuildJobTask
import de.codecentric.janus.Application
import de.codecentric.janus.scaffold.BuildJob

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldConfigParserTest {
    File config

    @Before
    void setup() {
        Application.bootstrap()
        config = new File(this.getClass().getClassLoader()
                .getResource('config.janus').toURI())
    }

    @Test
    void testParse() {
        Scaffold scaffold = ScaffoldConfigParser.parse(config)

        assert scaffold.name == 'RESTful Web application'
        assert scaffold.description == 'Web based project with RESTeasy based web service'

        assert scaffold.requiredContext.size() == 2
        assert scaffold.requiredContext['groupId'] == 'Please see Maven groupId'
        assert scaffold.requiredContext['artifactId'] == 'Plase see Maven artifactId'

        assert scaffold.buildJobs.size() == 2
        assert scaffold.buildJobs[0].name == 'entities'
        assert scaffold.buildJobs[0].disabled
        assert !scaffold.buildJobs[0].concurrentBuild
        assert scaffold.buildJobs[0].tasks.size() == 3
        assertBuildTask(scaffold.buildJobs[0].tasks[0],
                BuildJobTask.Type.MAVEN,
                [targets: 'clean package',
                        pom: 'entities/pom.xml',
                        jvmOptions: '-ea',
                        properties: 'showSplash=true',
                        usePrivateRepository: false])
        assertBuildTask(scaffold.buildJobs[0].tasks[1],
                BuildJobTask.Type.ANT,
                [targets: 'clean build',
                        buildFile: 'build-ci.xml',
                        jvmOptions: '-ea',
                        properties: 'something went wrong.'])
        assert scaffold.buildJobs[0].tasks[2].type == BuildJobTask.Type.FAIL
        assert scaffold.buildJobs[0]
                .downstreamBuilds[BuildJob.Status.SUCCESS]
                .containsAll(['parent', 'deployment'])
        assert scaffold.buildJobs[0]
                .downstreamBuilds[BuildJob.Status.FAIL]
                .containsAll(['cleanup'])

        assert scaffold.buildJobs[1].name == 'parent'
        assert scaffold.buildJobs[1].concurrentBuild
        assert scaffold.buildJobs[1].tasks.size() == 4
        assertBuildTask(scaffold.buildJobs[1].tasks[0],
                BuildJobTask.Type.MAVEN,
                [targets: 'clean install', pom: 'pom.xml'])
        assertBuildTask(scaffold.buildJobs[1].tasks[1],
                BuildJobTask.Type.MAVEN,
                [targets: 'clean', pom: 'pom.xml'])
        assertBuildTask(scaffold.buildJobs[1].tasks[2],
                BuildJobTask.Type.SHELL, [value: 'rm -rf /tmp/*'])
        assertBuildTask(scaffold.buildJobs[1].tasks[3],
                BuildJobTask.Type.BATCH, [value: 'del c:\\tmp'])
    }

    def assertBuildTask(given, expType, expOptions) {
        assert given.type == expType
        assertMap(expOptions, given)
    }

    def assertMap(exp, given) {
        exp.each { key, value ->
            assert given[key] == value
        }
    }
}
