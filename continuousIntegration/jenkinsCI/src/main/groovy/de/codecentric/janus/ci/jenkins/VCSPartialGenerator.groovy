package de.codecentric.janus.ci.jenkins

import de.codecentric.janus.VersionControlSystem
import de.codecentric.janus.conf.vcs.VCSConfig
import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import de.codecentric.janus.conf.Project

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
        
        template.make(['vcs': this.vcs, 'project': project]).toString()
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
