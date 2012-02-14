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

package de.codecentric.janus.conf

import org.junit.Before
import org.junit.Test

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class PermissionSchemeTest {

    def PermissionScheme ps

    @Before void setup() {
        ps = new PermissionScheme()
    }
    
    @Test void testReadAccess() {
        def read = [new User(email:'tom@example.com')]
        ps.readPermissions = read
        assert ps.readPermissions.is(read)

        def copiedRead = read.clone()
        copiedRead << new User(email:'jenni@example.com')
        assert ps.readPermissions.size() == 1
        assert copiedRead.size() == 2

        ps.readPermissions = copiedRead
        assert ps.readPermissions.is(copiedRead)
    }
}
