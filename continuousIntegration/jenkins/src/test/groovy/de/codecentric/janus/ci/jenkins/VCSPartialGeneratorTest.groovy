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
import de.codecentric.janus.VersionControlSystem
import de.codecentric.janus.conf.vcs.VCSConfig
import de.codecentric.janus.conf.vcs.MercurialConfig

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class VCSPartialGeneratorTest {
    static final VCS_URL = 'https://code.google.com/p/janus-example-scaffold/'

    Project project
    VCSConfig config

    @Before void setup() {
        project = new Project();
        config = null
    }

    @Test void withBranch() {
        config = new MercurialConfig(url: VCS_URL,
                branch: 'prototype',
                vcs: VersionControlSystem.MERCURIAL)

        String actual = new VCSPartialGenerator(config, project)
                .generatePartial()

        String exp = """\
    <scm class="hudson.plugins.mercurial.MercurialSCM">
        <installation>(Default)</installation>
        <source>https://code.google.com/p/janus-example-scaffold/</source>
        <modules></modules>
        <branch>prototype</branch>
        <clean>false</clean>
        <browser class="hudson.plugins.mercurial.browser.HgWeb">
            <url>https://code.google.com/p/janus-example-scaffold/</url>
        </browser>
    </scm>"""

        assert actual == exp
    }

    @Test void noBranch() {
        config = new MercurialConfig(url: VCS_URL,
                vcs: VersionControlSystem.MERCURIAL)

        String actual = new VCSPartialGenerator(config, project)
                .generatePartial()

        String exp = """\
    <scm class="hudson.plugins.mercurial.MercurialSCM">
        <installation>(Default)</installation>
        <source>https://code.google.com/p/janus-example-scaffold/</source>
        <modules></modules>

        <clean>false</clean>
        <browser class="hudson.plugins.mercurial.browser.HgWeb">
            <url>https://code.google.com/p/janus-example-scaffold/</url>
        </browser>
    </scm>"""

        assert actual == exp
    }
}
