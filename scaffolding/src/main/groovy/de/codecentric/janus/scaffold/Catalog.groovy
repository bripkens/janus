package de.codecentric.janus.scaffold

import org.codehaus.jackson.map.ObjectMapper
import static de.codecentric.janus.settings.FileSystem.CONFIG_DIR

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class Catalog {
    static final SCAFFOLD_DIR = CONFIG_DIR +
            File.separator + 'scaffolds'
    static final CATALOG_FILE = SCAFFOLD_DIR +
            File.separator + 'catalog.json'
    
    List<Scaffold> scaffolds

    static Catalog from(File file) {
        ObjectMapper mapper = new ObjectMapper();
        new ObjectMapper().readValue(file, Catalog.class)
    }

    static Catalog fromHomeDirectory() {
        String home = System.getProperty('user.home')
        String uri = home + File.separator + CATALOG_FILE

        from(new File(uri))
    }
}
