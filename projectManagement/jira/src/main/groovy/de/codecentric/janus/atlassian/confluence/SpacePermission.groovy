package de.codecentric.janus.atlassian.confluence

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
enum SpacePermission {

    VIEW('VIEWSPACE'),
    CREATE_PAGE('EDITSPACE'),
    EXPORT_PAGE('EXPORTPAGE'),
    RESTRICT_PAGE('SETPAGEPERMISSIONS'),
    REMOVE_PAGE('REMOVEPAGE'),
    CREATE_NEWS('EDITBLOG'),
    REMOVE_NEWS('REMOVEBLOG'),
    COMMENT('COMMENT'),
    REMOVE_COMMENT('REMOVECOMMENT'),
    CREATE_ATTACHMENT('CREATEATTACHMENT'),
    REMOVE_ATTACHMENT('REMOVEATTACHMENT'),
    REMOVE_MAIL('REMOVEMAIL'),
    EXPORT_SPACE('EXPORTSPACE'),
    ADMINISTER_SPACE('SETSPACEPERMISSIONS')

    final key

    SpacePermission(String key) {
        this.key = key
    }
}
