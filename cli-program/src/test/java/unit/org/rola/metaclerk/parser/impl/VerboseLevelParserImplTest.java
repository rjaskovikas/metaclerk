package org.rola.metaclerk.parser.impl;

import org.junit.Test;
import org.rola.metaclerk.exception.parser.BadParamException;
import org.rola.metaclerk.printers.api.PrinterVerboseLevel;
import org.rola.metaclerk.utils.ArrayIterator;

import static org.junit.Assert.*;

public class VerboseLevelParserImplTest extends VerboseLevelParserImpl {

    @Test (expected = NullPointerException.class)
    public void initNulls_parse_throwsNullPointerException() throws Exception {
        setup(null);
        parseParam();
    }

    @Test
    public void parserObjec_getParamsDescriptionString_returnsTwoParams() throws Exception {
        assertTrue(getParamsDescriptionString().contains(VERBOSE_PARAM));
        assertTrue(getParamsDescriptionString().contains(VERY_VERBOSE_PARAM));
    }

    @Test
    public void parserObjec_getParamsUsageString_returnsTwolines() throws Exception {
        assertTrue(getParamsUsageString().contains(VERBOSE_PARAM));
        assertTrue(getParamsUsageString().contains(VERY_VERBOSE_PARAM));
    }

    @Test
    public void verboseParameter_parse_verboseLevelIsVERBOSE() throws Exception {
        prepareParser(new String[]{VERBOSE_PARAM});

        assertTrue(parseParam()); // check params belongs to parser
        assertEquals(PrinterVerboseLevel.VERBOSE, getVerboseLevel());
        checkMandatoryParams();
    }

    @Test
    public void veryVerboseParameter_parse_verboseLevelIsVERY_VERBOSE() throws Exception {
        prepareParser(new String[]{VERY_VERBOSE_PARAM});

        assertTrue(parseParam()); // check that param belongs to parser
        assertEquals(PrinterVerboseLevel.VERY_VERBOSE, getVerboseLevel());
    }

    @Test
    public void notParserParameter_parse_verboseLevelIsVERY_VERBOSE() throws Exception {
        prepareParser(new String[]{VERY_VERBOSE_PARAM +"a"});

        assertFalse(parseParam()); // check that param doesn't belong to parser
        assertEquals(PrinterVerboseLevel.NONE, getVerboseLevel());
    }

    @Test(expected = BadParamException.class)
    public void veryVerboseAndVerboseParameters_parse_throwsBadParamException() throws Exception {
        prepareParser(new String[]{VERY_VERBOSE_PARAM, VERBOSE_PARAM});

        assertTrue(parseParam()); // check that params belongs to parser
        assertEquals(PrinterVerboseLevel.VERY_VERBOSE, getVerboseLevel());

        paramIt.next(); //step on second parameter
        parseParam();
    }

    @Test(expected = BadParamException.class)
    public void verboseAndVeryVerboseParameters_parse_throwsBadParamException() throws Exception {
        prepareParser(new String[]{VERBOSE_PARAM, VERY_VERBOSE_PARAM});

        assertTrue(parseParam()); // check that params belongs to parser
        assertEquals(PrinterVerboseLevel.VERBOSE, getVerboseLevel());

        paramIt.next(); //step on second parameter
        parseParam();
    }


    private void prepareParser(String[] params) {
        setup(new ArrayIterator<>(params));
        assertEquals(PrinterVerboseLevel.NONE, getVerboseLevel());

        paramIt.next(); // step on first param
    }
}