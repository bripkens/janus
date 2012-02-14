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

import de.codecentric.janus.scaffold.BuildJob
import de.codecentric.janus.scaffold.BuildJobTask

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class BuildJobTasksDelegate {
    final BuildJob buildJob

    BuildJobTasksDelegate(BuildJob buildJob) {
        this.buildJob = buildJob
    }

    def methodMissing(String name, args) {
        // a map entry always has a single value
        if (args.size() != 1) {
            throw new MissingMethodException(name,
                    BuildJobTasksDelegate.class, args)
        }

        try {
            def type = BuildJobTask.Type.valueOf(name.toUpperCase())
            buildJob.tasks << new BuildJobTask(type: type, options: args[0])
        } catch (IllegalArgumentException ex) {
            throw new MissingMethodException(name,
                    BuildJobTasksDelegate.class, args)
        }
    }
}
