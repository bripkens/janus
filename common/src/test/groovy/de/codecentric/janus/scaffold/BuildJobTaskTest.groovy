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
class BuildJobTaskTest {
    
    @Test void testValid() {
        def task = new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                options: [targets: 'clean install'])
        assert task.valid
    }

    @Test void testNotValidNoType() {
        def task = new BuildJobTask(options: [targets: 'clean install'])
        assert !task.valid
    }
    
    @Test void testNotValidInsufficientOptions() {
        def task = new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                options: [foobar: 'humpty dumpty'])
        assert !task.valid
    }

    @Test void testGet() {
        def targets = 'clean install'
        def task = new BuildJobTask(type: BuildJobTask.Type.MAVEN,
                options: [targets: targets])

        assert task['targets'] == targets
        assert task['pom'] == 'pom.xml'
    }

    @Test void testGetNoType() {
        def task = new BuildJobTask(options: [targets: 'clean install'])
        assert task['pom'] == null
    }
}
