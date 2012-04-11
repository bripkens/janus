# Janus - bootstrap development environments [![Build Status](https://secure.travis-ci.org/bripkens/janus.png)](https://secure.travis-ci.org/#!/bripkens/janus)

If you have to bootstrap software development environments on a regular basis,
Janus might be of interest to you. What it does is, it extends Apache Maven's
project archetype system by providing a general purpose scaffolding
mechanism. When it's done, it's able to generate the following from
project templates (so called scaffolds).

 - a project scaffold similar to Apache Maven archetypes which is not bound to
   a specific build system.
 - creation of version control system repositories
 - auto-generated Jenkins build jobs
 - JIRA project creation

A Janus user interface is available through a [Jenkins plug-in](https://github.com/bripkens/janus-plugin).

# Version History

## 0.4

 - scaffold DSL now supports a wider range of standard build job steps:
   Maven, Ant, Shell, Batch and automatic fail (UC-10).
 - new DSL option "no vcs trigger" to deactivate VCS polling.
 - Jenkins CI config generator support for the new build steps (UC-10).


## 0.3

 - addition of static types to enable library usage from statically typed
   languages.
 - fixed a bug in the ScaffoldExecutor which was responsible for overwriting
   context parameters.

## 0.2

 - exposing additional functionality through the API for user interfaces.

## 0.1

 - initial project structure.
 - reading and parsing scaffolds and scaffold catalogs (UC-5, UC-9).
 - simple DSL for continuous integration system config files (UC-10).

# License

Copyright (C) 2012 codecentric AG, Licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).