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
    final VersionControlSystem vcs
    final VCSConfig vcsConfig
    final Project project

    VCSPartialGenerator(VersionControlSystem vcs, VCSConfig vcsConfig,
                        Project project) {
        assert vcs != null && vcsConfig != null && project != null
        
        this.vcs = vcs
        this.vcsConfig = vcsConfig
        this.project = project
    }

    public String generatePartial() {
        SimpleTemplateEngine engine = new SimpleTemplateEngine()
        Template template = engine.createTemplate(templateReader)
        
        template.make(['vcs': vcsConfig, 'project': project]).toString()
    }

    private Reader getTemplateReader() {
        String fileName = "/vcs/${vcs.name().toLowerCase()}.xml"
        InputStream input = this.getClass().getResourceAsStream(fileName)

        if (!input) {
            throw new JenkinsConfigurationException("""A Jenkins \
            configuration for the given version control system \
            (${vcs.name()}) does not exist.""".stripIndent())
        }

        input.newReader()
    }
}
