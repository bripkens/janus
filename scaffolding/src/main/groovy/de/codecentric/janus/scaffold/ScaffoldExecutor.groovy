package de.codecentric.janus.scaffold

import java.util.zip.ZipFile
import java.util.zip.ZipEntry
import org.apache.commons.io.IOUtils

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldExecutor {
    final Scaffold scaffold

    ScaffoldExecutor(Scaffold scaffold) {
        this.scaffold = scaffold
    }

    void apply(File targetDirectory) {
        String home = System.getProperty('user.home')
        String uri = home + File.separator + Catalog.SCAFFOLD_DIR +
                File.separator + scaffold.filename
        ZipFile file = new ZipFile(new File(uri), ZipFile.OPEN_READ)

        Enumeration entries = file.entries()
        while(entries.hasMoreElements()) {
            unzip(file, entries.nextElement(), targetDirectory)
        }

        file.close()
    }

    private void unzip(ZipFile file, ZipEntry entry, File targetDirectory) {
        if (entry.directory) {
            unzipDirectory(entry, targetDirectory)
        } else {
            unzipFile(file, entry, targetDirectory)
        }
    }

    private void unzipDirectory(ZipEntry entry, File targetDirectory) {
        unzipDirectory(new File(targetDirectory, entry.name))
    }

    private void unzipDirectory(File dir) {
        if (!dir.mkdirs()) {
            throw new ScaffoldingException("Couldn't create directory ${dir.absolutePath}")
        }
    }

    private void unzipFile(ZipFile zipFile, ZipEntry entry,
                           File targetDirectory) {
        File outputFile = new File(targetDirectory, entry.getName())

        // ensure the parent directory exists
        File parent = outputFile.getParentFile()
        if (!parent.exists()) {
            unzipDirectory(parent)
        }

        // extract file
        InputStream input = zipFile.getInputStream(entry)
        OutputStream output = new FileOutputStream(outputFile)

        try {
            IOUtils.copy(input, output)
        } finally {
            IOUtils.closeQuietly(input)
            IOUtils.closeQuietly(output)
        }
    }
}
