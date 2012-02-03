package de.codecentric.janus.ci.jenkins

import groovy.text.Template
import groovy.text.SimpleTemplateEngine

import de.codecentric.janus.conf.Project

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ProjectConfigurationGenerator {
    static final CONFIG_TEMPLATE = '/templates/projectConfig.xml'

    final Template template

    ProjectConfigurationGenerator() {
        String template = getClass().getResourceAsStream(CONFIG_TEMPLATE).text
        this.template = new SimpleTemplateEngine().createTemplate(template)
    }

    String generate(Project project) {
        assert project != null
        template.make([project: project]).toString()
    }
}
