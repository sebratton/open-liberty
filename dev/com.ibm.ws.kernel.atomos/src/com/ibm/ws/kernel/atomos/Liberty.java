/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.kernel.atomos;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.felix.atomos.runtime.AtomosRuntime;

import com.ibm.ws.kernel.boot.BootstrapConfig;
import com.ibm.ws.kernel.boot.Launcher;
import com.ibm.ws.kernel.boot.LocationException;
import com.ibm.ws.kernel.boot.cmdline.Utils;
import com.ibm.ws.kernel.boot.internal.BootstrapConstants;
import com.ibm.ws.kernel.boot.internal.KernelUtils;

/**
 *
 */
public class Liberty {

    static String serverName = "defaultServer";
    static String LIB_PACKAGE_PREFIX = "liber-conf/";

    public static void main(String[] args) throws IOException {

        File lib = new File("lib").getAbsoluteFile();
        System.out.println("wlp/lib : " + lib);
        if (lib.exists()) {
            // set some statics for lib
            KernelUtils.setBootStrapLibDir(lib);
            Utils.setInstallDir(lib.getParentFile());

            //simple demo of command line launch: Check for a target war with an embedded Liberty config
            // and unpack to to disk in server config area.
            if (args.length > 0 && args[0].contains("/")) {
                File serverConfigDir = Utils.getServerConfigDir(serverName);
                if (serverConfigDir.exists() && !(serverConfigDir.list().length == 0)) {
                    throw new IOException("Config dir already exists and is not empty: " + serverConfigDir);
                }
                serverConfigDir.mkdirs();
                extract_liberty_conf(new File(args[0]), serverConfigDir.getParentFile());
                //copy war file itself to apps folder
                Path appsDir = Paths.get(serverConfigDir.getAbsolutePath(), "apps");
                Files.createDirectory(appsDir);
                Files.copy(Paths.get(args[0]), appsDir.resolve(Paths.get(args[0]).getFileName()));
                args = new String[] {};
            }

        }

        Launcher launcher = new Launcher() {
            @Override
            protected BootstrapConfig createBootstrapConfig() {
                BootstrapConfig config = new BootstrapConfig() {
                    @Override
                    protected void configure(Map<String, String> initProps) throws LocationException {
                        initProps.put(AtomosRuntime.ATOMOS_CONTENT_INSTALL, "false");
                        initProps.put(AtomosRuntime.ATOMOS_CONTENT_START, "false");
                        initProps.put(BootstrapConstants.LIBERTY_BOOT_PROPERTY, "true");
                        super.configure(initProps);
                    }
                };
                config.setAtomosRuntime(AtomosRuntime.newAtomosRuntime());
                return config;
            }
        };
        System.exit(launcher.createPlatform(args));
    }

    private static void extract_liberty_conf(File warFile, File serverConfigDir) throws IOException {
        boolean found = false;
        if (!warFile.exists()) {
            throw new IOException("Specified war file not found:" + warFile.getAbsolutePath());
        }
        //unpack server config
        ZipEntry entry;
        byte[] byteBuff = new byte[512];
        ZipInputStream zIS = new ZipInputStream(new FileInputStream(warFile));

        while ((entry = zIS.getNextEntry()) != null) {
            if (entry.getName().startsWith(LIB_PACKAGE_PREFIX)) {
                found = true;
                String path = serverConfigDir.getAbsolutePath() + File.separator +
                              entry.getName().substring(LIB_PACKAGE_PREFIX.length());
                if (!entry.isDirectory()) {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
                    int bytesRead = 0;
                    while ((bytesRead = zIS.read(byteBuff)) != -1) {
                        bos.write(byteBuff, 0, bytesRead);
                    }
                    bos.close();
                } else {
                    File dir = new File(path);
                    dir.mkdir();
                }
            }
            zIS.closeEntry();
        }
        zIS.close();

        if (!found) {
            throw new IOException("No " + LIB_PACKAGE_PREFIX + " folder found in war");
        }
    }
}
