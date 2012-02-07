package de.codecentric.janus.scaffold

import java.util.zip.ZipFile
import java.util.zip.ZipEntry
import org.apache.commons.io.IOUtils
import groovy.text.SimpleTemplateEngine
import groovy.text.Template

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldExecutor {
    static final PACKAGE_WRAPPER_PLACEHOLDER = '-to_package-'
    static final SOURCE_DIRECTORY = 'sources/'
    static final PACKAGE_REGEX = /^([a-z_]{1}[a-z0-9_]*(\.[a-z_]{1}[a-z0-9_]*)*)$/

    final Scaffold scaffold
    final String pckg
    final File scaffoldFile
    final SimpleTemplateEngine engine

    /**
     * Initialize this scaffold executor using the given parameters. Instances
     * of this class can be reused.
     *
     * @param scaffold The scaffold type
     * @param pckg The package under which the target sources should be located
     */
    ScaffoldExecutor(Scaffold scaffold, String pckg) {
        this(scaffold, pckg, { ScaffoldLoader.load(it) })
    }

    ScaffoldExecutor(Scaffold scaffold, String pckg, Closure scaffoldLoader) {
        assert scaffold != null && pckg != null && scaffoldLoader != null

        this.scaffold = scaffold

        pckg = pckg.trim()
        if (pckg.isEmpty()) {
            this.pckg = ''
        } else if (pckg =~ PACKAGE_REGEX) {
            this.pckg = pckg.replace('.', '/')
        } else {
            throw new ScaffoldingException('Invalid package name.')
        }

        scaffoldFile = scaffoldLoader(scaffold)
        engine = new SimpleTemplateEngine()
    }

    void apply(File targetDirectory, Map context) {
        ZipFile file = new ZipFile(scaffoldFile, ZipFile.OPEN_READ)

        Enumeration entries = file.entries()
        while(entries.hasMoreElements()) {
            unzip(file, entries.nextElement(), targetDirectory, context)
        }

        file.close()
    }

    private void unzip(ZipFile file, ZipEntry entry, File targetDirectory,
                       Map context) {
        if (!entry.name.startsWith(SOURCE_DIRECTORY)) {
            return
        }

        if (entry.directory) {
            unzipDirectory(entry, targetDirectory)
        } else {
            unzipFile(file, entry, targetDirectory, context)
        }
    }

    private void unzipDirectory(ZipEntry entry, File targetDirectory) {
        createDirectory(new File(targetDirectory, updateName(entry.name)))
    }

    private void createDirectory(File dir) {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new ScaffoldingException('Couldn\'t create directory ' +
                    dir.absolutePath)
        }
    }

    private void unzipFile(ZipFile zipFile, ZipEntry entry,
                           File targetDirectory, Map context) {

        File outputFile = new File(targetDirectory, updateName(entry.name))

        // ensure the parent directory exists
        File parent = outputFile.parentFile
        if (!parent.exists()) {
            createDirectory(parent)
        }

        // extract file, apply template rules and write to target
        InputStream input = zipFile.getInputStream(entry)
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
                .replace(SOURCE_DIRECTORY, '')
    }
}
