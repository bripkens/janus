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

import de.codecentric.janus.VersionControlSystem
import de.codecentric.janus.conf.vcs.VCSConfig
import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import de.codecentric.janus.conf.Project
import org.apache.commons.lang.StringEscapeUtils

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class VCSPartialGenerator {
    final VCSConfig vcs
    final Project project

    VCSPartialGenerator(VCSConfig vcs, Project project) {
        assert vcs != null && project != null
        
        this.vcs = vcs
        this.project = project
    }

    public String generatePartial() {
        SimpleTemplateEngine engine = new SimpleTemplateEngine()
        Template template = engine.createTemplate(templateReader)
        
        Map context = ['vcs': this.vcs, 'project': project]

        context.encode = { String text ->
            StringEscapeUtils.escapeXml(text)
        }
        
        template.make(context).toString()
    }

    private Reader getTemplateReader() {
        String fileName = "/vcs/${vcs.vcs.name().toLowerCase()}.xml"
        InputStream input = this.getClass().getResourceAsStream(fileName)

        if (!input) {
            throw new JenkinsConfigurationException('A Jenkins ' +
                    'configuration for the given version control ' +
                    "system (${vcs.vcs.name()}) does not exist.")
        }

        input.newReader()
    }
}
