/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aaron
 */
public class JarFileReader {

    private static Logger logger = Logger.getLogger(JarFileReader.class.getName());

    public static Collection<String> getClasses(String dirname, String jarname) {
        File file = new File(dirname, jarname);
        if (file.exists() && file.isFile()) {
            try {
                ArrayList<String> entrynames = new ArrayList<String>();
                Enumeration<JarEntry> entries = new JarFile(file).entries();
                while (entries.hasMoreElements()) {
                    String entryname = entries.nextElement().getName();
                    if (entryname.endsWith(".class") && (!entryname.contains("$"))) {
                        entryname = entryname.substring(0, entryname.length() - 6);
                        entryname = entryname.replace('/', '.');
                        entrynames.add(entryname);
                    }
                }
                return entrynames;
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }
}
