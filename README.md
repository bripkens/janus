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