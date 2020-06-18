/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.rest.handler.fat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import com.ibm.websphere.simplicity.log.Log;

import componenttest.custom.junit.runner.FATRunner;
import componenttest.rules.repeater.JakartaEE9Action;
import componenttest.topology.impl.LibertyServer;
import componenttest.topology.impl.LibertyServerFactory;
import componenttest.topology.utils.HttpUtils;

/**
 * Test rest handler protected feature in isolation.
 */
@RunWith(FATRunner.class)
public class RestHandlerTests {

    protected static LibertyServer server = null;

    private static final Class<?> c = RestHandlerTests.class;

    private static boolean isJakarta9;

    private static String bundleName = "test.wab1";

    @Rule
    public TestName name = new TestName();

    @BeforeClass
    public static void setUp() throws Exception {
        if (JakartaEE9Action.isActive()) {
            /* transform wab bundle to jakartaee-9 equivalents */
            Log.info(c, "setUp", "Transforming  bundles to Jakarta-EE-9: ");
            Path bundleFile = Paths.get("build", "lib", bundleName + ".jar");
            JakartaEE9Action.transformApp(bundleFile, Paths.get("publish", "productbundles", bundleName + ".jakarta.jar"));
            Log.info(c, "setUp", "Transformed bundle " + bundleFile);
            isJakarta9 = true;
        } else {
            isJakarta9 = false;
        }
        server = LibertyServerFactory.getLibertyServer("com.ibm.ws.rest.handler.feature.fat");
        Log.info(c, "setUp", "Installing product properties file for extension1");

        server.installProductExtension("extension1");
        Log.info(c, "setUp", "Installing product feature feature1");

        server.installProductFeature("extension1", "feature1" + (isJakarta9 ? "_jakarta" : ""));
        Log.info(c, "setUp", "Installing product bundles test.wab1");

        server.installProductBundle("extension1", "test.wab1" + (isJakarta9 ? ".jakarta" : ""));
        server.startServer();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        Log.info(c, "teardown", "===== Enter Class teardown =====");
        try {
            if (server != null && server.isStarted()) {
                server.stopServer();
            }

        } finally {
            if (server != null) {
                server.uninstallProductFeature("extension1", "feature1");
                server.uninstallProductExtension("extension1");
            }
        }
    }

    @Before
    public void beforeTest() throws Exception {
        Log.info(c, name.getMethodName(), "===== Starting test " + name.getMethodName() + " =====");
    }

    @After
    public void afterTest() throws Exception {
        Log.info(c, name.getMethodName(), "===== Stopping test " + name.getMethodName() + " =====");
    }

    @Test
    public void testAllHandlerTypes() throws Exception {
        HttpUtils.findStringInUrl(server, "/resthandler/servlet", "SUCCESS");
    }
}
