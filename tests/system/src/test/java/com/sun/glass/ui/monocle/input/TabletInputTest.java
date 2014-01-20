/*
 * Copyright (c) 2013, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.glass.ui.monocle.input;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for events generated by a touch screen that declares itself as ID_INPUT_TABLET
 */
public class TabletInputTest extends TouchTestBase {

    @Before
    public void createDevice() throws Exception {
        TestApplication.showFullScreenScene();
        TestApplication.addMouseListeners();
        TestApplication.addTouchListeners();
        TestLog.reset();
        ui.processLine("OPEN");
        ui.processLine("VENDOR 0xd428");
        ui.processLine("PRODUCT 0x1364");
        ui.processLine("EVBIT EV_SYN");
        ui.processLine("EVBIT EV_KEY");
        ui.processLine("KEYBIT BTN_TOUCH");
        ui.processLine("KEYBIT BTN_TOOL_PEN");
        ui.processLine("EVBIT EV_ABS");
        ui.processLine("ABSBIT ABS_X");
        ui.processLine("ABSBIT ABS_Y");
        ui.processLine("ABSMIN ABS_X 0");
        ui.processLine("ABSMAX ABS_X 4095");
        ui.processLine("ABSMIN ABS_Y 0");
        ui.processLine("ABSMAX ABS_Y 4095");
        ui.processLine("PROPBIT INPUT_PROP_POINTER");
        ui.processLine("PROPBIT INPUT_PROP_DIRECT");
        ui.processLine("PROPERTY ID_INPUT_TABLET 1");
        ui.processLine("CREATE");
        setAbsScale(4096, 4096);
    }

    /**
     * Touch down and up
     */
    @Test
    public void tap() throws Exception {
        final int x = (int) Math.round(screen.getWidth() / 2.0);
        final int y = (int) Math.round(screen.getHeight() / 2.0);
        TestLog.reset();
        // tap
        ui.processLine("EV_KEY BTN_TOOL_PEN 1");
        ui.processLine("EV_KEY BTN_TOUCH 1");
        absPosition(x, y);
        ui.processLine("EV_SYN SYN_REPORT 0");
        TestLog.waitForLog("Mouse pressed: " + x + ", " + y, 3000);
        TestLog.waitForLog("Touch pressed: " + x + ", " + y, 3000);
        TestLog.waitForLogContaining("TouchPoint: PRESSED " + x + ", " + y, 3000);
        Assert.assertEquals(1, TestLog.countLogContaining("TouchPoint: PRESSED"));
        Assert.assertEquals(0, TestLog.countLogContaining("TouchPoint: MOVED"));
        Assert.assertEquals(0, TestLog.countLogContaining("TouchPoint: RELEASED"));
        // release
        ui.processLine("EV_KEY BTN_TOOL_PEN 0");
        ui.processLine("EV_KEY BTN_TOUCH 0");
        ui.processLine("EV_SYN SYN_REPORT 0");
        TestLog.waitForLog("Mouse released: "
                + x + ", " + y, 3000);
        TestLog.waitForLog("Mouse clicked: "
                + x + ", " + y, 3000);
        TestLog.waitForLog("Touch released: "
                                   + x + ", " + y, 3000);
        TestLog.waitForLogContaining("TouchPoint: RELEASED "
                                             + x + ", " + y, 3000);
        Assert.assertEquals(1, TestLog.countLogContaining("TouchPoint: PRESSED"));
        Assert.assertEquals(0, TestLog.countLogContaining("TouchPoint: MOVED"));
        Assert.assertEquals(1, TestLog.countLogContaining("TouchPoint: RELEASED"));
        Assert.assertEquals(1, TestLog.countLogContaining("Mouse pressed"));
        Assert.assertEquals(0, TestLog.countLogContaining("Mouse dragged"));
        Assert.assertEquals(1, TestLog.countLogContaining("Mouse clicked"));
        Assert.assertEquals(1, TestLog.countLogContaining("Mouse released"));
    }

}
