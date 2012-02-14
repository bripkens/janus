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
import org.junit.Before

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldTest {
    File zip

    @Before void setup() {
        zip = new File(this.getClass().getClassLoader()
                .getResource('quickstart.zip').toURI())
    }
    
    @Test void testFrom() {
        Scaffold scaffold = Scaffold.from(zip)
        assert scaffold.name == 'Quickstart'
        assert scaffold.description == 'An empty Gradle project.'
        assert scaffold.file == zip
        assert scaffold.requiredContext['artifactId'] == 'A Maven like artifact id'
        assert scaffold.requiredContext['message'] == 'The initial message...'
    }
}
