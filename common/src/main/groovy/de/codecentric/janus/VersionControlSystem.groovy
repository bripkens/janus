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

package de.codecentric.janus

import de.codecentric.janus.conf.vcs.MercurialConfig
import de.codecentric.janus.conf.vcs.VCSConfig

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
enum VersionControlSystem {
    MERCURIAL(MercurialConfig.class)

    private final Class<? extends VCSConfig> clazz;

    private VersionControlSystem(Class<? extends VCSConfig> clazz) {
        this.clazz = clazz
    }

    public <T extends VCSConfig> T newConfig() {
        T config = clazz.newInstance()
        config.vcs = this
        return config
    }
}