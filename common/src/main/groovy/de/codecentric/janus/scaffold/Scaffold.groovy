package de.codecentric.janus.scaffold

import java.util.zip.ZipFile
import java.util.zip.ZipEntry
import org.codehaus.jackson.map.ObjectMapper
import de.codecentric.janus.scaffold.dsl.ScaffoldConfigParser

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class Scaffold {
    static final DESCRIPTOR_FILE_NAME = 'scaffold.groovy'
    
    String name, description
    Map<String, String> requiredContext = [] as HashMap
    List<BuildJob> buildJobs = []
    File file

    static Scaffold from(File file) {
        assert file != null

        ZipFile zip = new ZipFile(file, ZipFile.OPEN_READ)
        ZipEntry descriptor = zip.getEntry(DESCRIPTOR_FILE_NAME)

        if (descriptor == null) {
            throw new ScaffoldLoadingException('File ' + file.absolutePath +
                    ' does not contain a standard scaffold descriptor ' +
                    DESCRIPTOR_FILE_NAME + '.')
        }

        try {
            String config = zip.getInputStream(descriptor).text
            Scaffold s = ScaffoldConfigParser.parse(config)
            s.file = file
            return s
        } finally {
            try {
                if (zip != null) {
                    zip.close()
                }
            } catch (IOException ex) {
            }
        }
    }
}
