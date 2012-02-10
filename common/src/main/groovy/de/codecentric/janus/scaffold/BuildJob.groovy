package de.codecentric.janus.scaffold

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class BuildJob {
    String name
    boolean concurrentBuild = false
    List<BuildJobTask> tasks = []
}
