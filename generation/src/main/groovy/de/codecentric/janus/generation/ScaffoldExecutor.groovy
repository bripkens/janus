package de.codecentric.janus.generation

import java.util.zip.ZipFile
import java.util.zip.ZipEntry
import org.apache.commons.io.IOUtils
import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import groovy.util.logging.Slf4j

import de.codecentric.janus.scaffold.Descriptor
import de.codecentric.janus.scaffold.Scaffold
import de.codecentric.janus.conf.Project

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Slf4j
class ScaffoldExecutor {
    static final PACKAGE_WRAPPER_PLACEHOLDER = '-to_package-'
    static final SOURCE_DIRECTORY = 'sources/'

    final Scaffold scaffold
    final Project project
    final Map context

    final String pckg
    final SimpleTemplateEngine engine

    ScaffoldExecutor(Scaffold scaffold, Project project, Map context) {
        assert scaffold != null && project != null && context != null

        this.scaffold = scaffold
        this.project = project
        this.context = context
        this.context['project'] = project

        pckg = project.pckg.replace('.', '/')
        engine = new SimpleTemplateEngine()
    }

    void apply(File targetDir) {
        ZipFile zip = new ZipFile(scaffold.file, ZipFile.OPEN_READ)

        Enumeration entries = zip.entries()
        while(entries.hasMoreElements()) {
            unzip(zip, entries.nextElement(), targetDir)
        }

        try {
            zip.close()
        } catch (IOException ex) {
        }
    }

    private void unzip(ZipFile zip, ZipEntry entry, File targetDir) {
        if (!entry.name.startsWith(SOURCE_DIRECTORY)) {
            return
        }

        if (entry.directory) {
            unzipDirectory(entry, targetDir)
        } else {
            unzipFile(zip, entry, targetDir)
        }
    }

    private void unzipDirectory(ZipEntry entry, File targetDir) {
        createDirectory(new File(targetDir, updateName(entry.name)))
    }

    private void createDirectory(File dir) {
        log.debug("Extracting directory ${dir.absolutePath}.")
        if (!dir.exists() && !dir.mkdirs()) {
            throw new ScaffoldingException('Couldn\'t create directory ' +
                    dir.absolutePath)
        }
    }

    private void unzipFile(ZipFile zip, ZipEntry entry, File targetDir) {
        File outputFile = new File(targetDir, updateName(entry.name))
        log.debug("Extracting file ${entry.name} to ${outputFile.absolutePath}.")

        // ensure the parent directory exists
        File parent = outputFile.parentFile
        if (!parent.exists()) {
            createDirectory(parent)
        }

        // extract file, apply template rules and write to target
        InputStream input = zip.getInputStream(entry)
        OutputStream output = new FileOutputStream(outputFile)

        try {
            Template template = engine.createTemplate(input.newReader())
            template.make(context).writeTo(new OutputStreamWriter(output))
        } catch (MissingPropertyException ex) {
            throw new ScaffoldingException(ex)
        } finally {
            IOUtils.closeQuietly(input)
            IOUtils.closeQuietly(output)
        }
    }

    /**
     * Transform the file name as it exists in the ZIP to an appropriate one
     * with package hierarchy. Also, remove the parent directory "sources"
     * which is not extracted.
     *
     * @param originalName Name of the ZIP file entry
     * @return The new name which can be used for extraction
     */
    private String updateName(String originalName) {
        return originalName.replace(PACKAGE_WRAPPER_PLACEHOLDER, pckg)
                .replaceFirst(SOURCE_DIRECTORY, '')
    }
}
