package de.codecentric.janus.scaffold

import java.util.zip.ZipFile
import java.util.zip.ZipEntry
import org.codehaus.jackson.map.ObjectMapper

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class Scaffold {
    static final DESCRIPTOR_FILE_NAME = 'scaffold.json'
    
    String name, description
    Map<String, String> requiredContext
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
            InputStream input = zip.getInputStream(descriptor)
            ObjectMapper mapper = new ObjectMapper();
            Scaffold s = new ObjectMapper().readValue(input, Scaffold.class)
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
