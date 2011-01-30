/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.utils;

import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aaron
 */
public class TrueZIP {

    private static Logger logger = Logger.getLogger(TrueZIP.class.getName());

    public static String zip(String source, String destination) {
        File dir = new File(source);
        if (dir.exists() && dir.isDirectory()) {
            destination += ".zip";
            if (!dir.copyAllTo(new File(destination))) {
                destination = null;
            }
            try {
                File.umount();
                return destination;
            } catch (ArchiveException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }

    public static String zip(String source, String destination, String wildcard) {
        File dir = new File(source);
        if (dir.exists() && dir.isDirectory()) {
            CommonsIO.copyDirectory(source, destination, wildcard);
            dir = new File(destination);
            destination += ".zip";
            if (!dir.copyAllTo(new File(destination))) {
                destination = null;
            }
            try {
                File.umount();
                return destination;
            } catch (ArchiveException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }

    public static String uncompress(String source, String destination) {
        File file = new File(source);
        if (file.exists() && file.isArchive()) {
            destination = MiscUtils.appendFileSeparator(destination);
            if (!file.copyAllTo(new File(destination))) {
                destination = null;
            }
            try {
                File.umount();
                return destination;
            } catch (ArchiveException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }
}
