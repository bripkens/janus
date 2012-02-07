package de.codecentric.janus.scaffold

import org.junit.Test

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class CatalogTest {

    @Test void testDeserialization() {
        File file = new File(this.getClass().getClassLoader()
                .getResource('catalog.json').toURI())
        
        Catalog catalog = Catalog.from(file)

        assert catalog.scaffolds.size() == 1

        CatalogEntry entry = catalog.scaffolds[0]
        assert entry.name == 'Quickstart'
        assert entry.filename == 'quickstart.zip'
    }
}
