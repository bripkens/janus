package de.codecentric.janus

import groovy.util.logging.Slf4j
import java.util.concurrent.atomic.AtomicBoolean
import java.security.Policy

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@Slf4j
class Application {
    static AtomicBoolean bootstrapped = new AtomicBoolean()

    static void bootstrap() {
        if (bootstrapped.compareAndSet(false, true)) {
            new Application()
        } else {
            log.info 'Application already bootstrapped - skipping ' +
                    'additional bootstrap.'
        }

    }

    private Application() {
        log.info 'Starting application bootstrap.'

        log.info 'Bootstrapping JDK/GDK extensions...'
        bootstrapExtensions()

        log.info 'Bootstrapping security policies...'
        bootStrapSecurity()

        log.info 'Finished application bootstrap.'
    }

    private void bootstrapExtensions() {
        def toSingleLinePattern = ~/\s{2,}/
        String.metaClass.toSingleLine = {
            (delegate =~ toSingleLinePattern).replaceAll(' ')
        }
    }

    private void bootStrapSecurity() {
        log.info 'Checking whether a security manager is enabled...'
        if (System.securityManager != null) {
            log.info 'Security manager already enabled.'
            return;
        }

        log.info 'Not enabled, checking whether a security policy is ' +
                'defined...'
        if (System.getProperty('java.security.policy') != null) {
            log.info 'Security policy defined. Will start default ' +
                    'security manager.'
            System.setSecurityManager(new SecurityManager())
        } else {
            log.info 'No security policy defined. Will not start ' +
                    'security manager.'
        }
    }
}
