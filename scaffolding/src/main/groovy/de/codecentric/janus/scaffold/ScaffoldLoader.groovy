package de.codecentric.janus.scaffold

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ScaffoldLoader {

    /**
     * The default loader for scaffolds. It uses the user's home directory to
     * load them, e.g., /home/user/.janus/scaffolds/quickstart.zip.
     * @param scaffold to be loaded
     * @return The file which represents the scaffold's ZIP file.
     */
    static File load(Scaffold scaffold) {
        String home = System.getProperty('user.home')
        String uri = home + File.separator + Catalog.SCAFFOLD_DIR +
                File.separator + scaffold.filename
        return new File(uri)
    }
}
