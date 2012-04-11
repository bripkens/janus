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

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class BuildJobDelegate {
    final BuildJob buildJob
    final List<String> downstreamBuilds

    BuildJobDelegate(BuildJob buildJob) {
        this.buildJob = buildJob
        downstreamBuilds = []
    }

    void concurrentBuild(boolean concurrentBuild) {
        buildJob.concurrentBuild = concurrentBuild
    }

    void name(String name) {
        buildJob.name = name
    }

    void tasks(Closure closure) {
        closure.delegate = new BuildJobTasksDelegate(buildJob)
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }

    Object no(String what) {
        buildJob.vcsTrigger = false

        // in order for the DSL to work, a property trigger must be accessible
        // on the returned object. Since we don't need to track this
        // property access, we just return a dummy object.
        return new Object() {
            def trigger = null;
        };
    }

    def propertyMissing(String name, value) {
        switch (name) {
            case 'disabled':
                buildJob.disabled = true
                break
            case 'concurrentBuild':
                buildJob.concurrentBuild = true
                break
            case 'success':
                return BuildJob.Status.SUCCESS
            case 'failure':
                return BuildJob.Status.FAIL
            case 'vcs':
                return 'vcs'
            default:
                throw new MissingPropertyException("No such " +
                        "configuration option ${name}")
        }
    }

    def methodMissing(String name, args) {
        if (name == 'trigger') {
            downstreamBuilds.addAll(args)
            return this
        } else if (name == 'on' && args.size() == 1) {
            buildJob.downstreamBuilds[args[0]].addAll(downstreamBuilds)
            downstreamBuilds.clear()
        } else {
            throw new MissingMethodException(name, BuildJobDelegate.class,
                    args)
        }
    }
}
