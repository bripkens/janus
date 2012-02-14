/*
 * Copyright (C) 2012 codecentric AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
