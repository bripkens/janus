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

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldConfigParserTest {
    File config

    @Before void setup() {
        Application.bootstrap()
        config = new File(this.getClass().getClassLoader()
                .getResource('config.janus').toURI())
    }

    @Test void testParse() {
        Scaffold scaffold = ScaffoldConfigParser.parse(config)

        assert scaffold.name == 'RESTful Web application'
        assert scaffold.description == 'Web based project with RESTeasy based web service'

        assert scaffold.requiredContext.size() == 2
        assert scaffold.requiredContext['groupId'] == 'Please see Maven groupId'
        assert scaffold.requiredContext['artifactId'] == 'Plase see Maven artifactId'

        assert scaffold.buildJobs.size() == 2
        assert scaffold.buildJobs[0].name == 'entities'
        assert !scaffold.buildJobs[0].concurrentBuild
        assert scaffold.buildJobs[0].tasks.size() == 1
        assert scaffold.buildJobs[0].tasks[0].type == BuildJobTask.Type.MAVEN
        assert scaffold.buildJobs[0].tasks[0].options.size() == 2
        assert scaffold.buildJobs[0].tasks[0]['targets'] == 'clean package'
        assert scaffold.buildJobs[0].tasks[0]['pom'] == 'entities/pom.xml'

        assert scaffold.buildJobs[1].name == 'parent'
        assert scaffold.buildJobs[1].concurrentBuild
        assert scaffold.buildJobs[1].tasks.size() == 2
        assert scaffold.buildJobs[1].tasks[0].type == BuildJobTask.Type.MAVEN
        assert scaffold.buildJobs[1].tasks[0].options.size() == 1
        assert scaffold.buildJobs[1].tasks[0]['targets'] == 'clean install'
        assert scaffold.buildJobs[1].tasks[0]['pom'] == 'pom.xml'
        assert scaffold.buildJobs[1].tasks[1].type == BuildJobTask.Type.MAVEN
        assert scaffold.buildJobs[1].tasks[1].options.size() == 1
        assert scaffold.buildJobs[1].tasks[1]['targets'] == 'clean'
        assert scaffold.buildJobs[1].tasks[1]['pom'] == 'pom.xml'

        assert scaffold.buildJobs[1].tasks[1]['foobar'] == null
    }
}
