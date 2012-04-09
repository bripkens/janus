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

import de.codecentric.janus.conf.Project
import de.codecentric.janus.scaffold.BuildJob
import de.codecentric.janus.conf.vcs.VCSConfig
import de.codecentric.janus.scaffold.BuildJobTask
import groovy.text.SimpleTemplateEngine
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringEscapeUtils

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class JenkinsConfigGenerator {
    final Project project
    final BuildJob buildJob
    final Map context
    final VCSConfig vcs
    final SimpleTemplateEngine engine

    JenkinsConfigGenerator(Project project, BuildJob buildJob, Map context,
                           VCSConfig vcs) {
        this.project = project
        this.buildJob = buildJob
        this.context = context
        this.vcs = vcs

        engine = new SimpleTemplateEngine()
    }

    String generate() {
        def vcsPartial = new VCSPartialGenerator(vcs, project)
                .generatePartial()
        
        def taskPartial = buildJob.tasks.collect { generateTaskPartial(it) }
                .join('\n')

        return runThoughEngine(project: project, job: buildJob,
                vcsConfig: vcsPartial, tasks: taskPartial, '/base.xml')
    }
    
    private String generateTaskPartial(BuildJobTask task) {
        assert task.valid

        def path = "/task/${task.type.name().toLowerCase()}.xml"
        return runThoughEngine([job: task], path)
    }

    private String runThoughEngine(Map context, String path) {
        def input = this.getClass().getResourceAsStream(path)

        if (!input) {
            throw new JenkinsConfigurationException('A Jenkins ' +
                    "configuration could not be located at ${path}.")
        }

        def template = engine.createTemplate(input.newReader('UTF-8'))

        context.encode = { String text ->
            StringEscapeUtils.escapeXml(text)
        }
        
        try {
            return template.make(context).toString()
        } finally {
            IOUtils.closeQuietly(input)
        }
    }
}