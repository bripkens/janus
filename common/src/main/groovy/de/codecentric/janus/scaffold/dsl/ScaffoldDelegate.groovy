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

package de.codecentric.janus.scaffold.dsl

import de.codecentric.janus.conf.Project
import de.codecentric.janus.scaffold.Scaffold
import de.codecentric.janus.scaffold.BuildJob

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldDelegate {
    Scaffold scaffold

    ScaffoldDelegate(Scaffold scaffold) {
        this.scaffold = scaffold
    }

    void name(String name) {
        scaffold.name = name
    }

    void description(String description) {
        scaffold.description = description
    }

    void requiredContext(Closure closure) {
        closure.delegate = new RequiredContextDelegate(scaffold)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }

    void buildJob(Closure closure) {
        def buildJob = new BuildJob()
        scaffold.buildJobs << buildJob
        closure.delegate = new BuildJobDelegate(buildJob)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }
}
