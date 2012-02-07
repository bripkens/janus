package de.codecentric.janus.scaffold

import org.codehaus.jackson.map.ObjectMapper

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class Catalog {
    List<CatalogEntry> scaffolds

    static Catalog from(File file) {
        ObjectMapper mapper = new ObjectMapper();
        new ObjectMapper().readValue(file, Catalog.class)
    }
}
