package de.codecentric.janus.scaffold

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
@RunWith(Parameterized.class)
class ScaffoldExecutorValidationTest {
    String pckg
    Scaffold scaffold

    ScaffoldExecutorValidationTest(String pckg) {
        this.pckg = pckg
        scaffold = new Scaffold([filename: 'quickstart.zip'])
    }

    @Parameters static Collection<Object[]> data() {
        def data = ['.', 'com.', '.com', 'com.example.', 'com..example']
        return data.collect { [it] as Object[] }
    }
    
    @Test(expected=ScaffoldingException.class) void testInvalidPackages() {
        new ScaffoldExecutor(scaffold, pckg)
    }
}
