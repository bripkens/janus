package de.codecentric.janus.scaffold

import org.junit.Before
import org.junit.Test

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class CatalogTest {

    @Test void testDeserialization() {
        Catalog catalog = Catalog.fromHomeDirectory()
        catalog.scaffolds.each {println it.name}
    }
}
