/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *
 * @author Aaron
 */
public class CommonsIO {

    private static Logger logger = Logger.getLogger(CommonsIO.class.getName());

    public static boolean copyResource(String resname, String filename) {
        ClassLoader loader = CommonsIO.class.getClassLoader();
        return copy(loader.getResourceAsStream(resname), filename);
    }

    private static boolean copy(InputStream is, String filename) {
        File file = new File(filename);
        if ((is != null) && (!file.exists())) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                IOUtils.copy(is, fos);
                fos.close();
                return true;
            } catch (FileNotFoundException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return false;
    }

    public static Collection<File> listFiles(String dirname, String[] exts) {
        File dir = new File(dirname);
        if (dir.exists() && dir.isDirectory()) {
            return FileUtils.listFiles(dir, exts, true);
        }
        return null;
    }

    public static boolean createFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                FileUtils.touch(file);
                return true;
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return false;
    }

    public static boolean deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            return FileUtils.deleteQuietly(file);
        }
        return false;
    }

    public static boolean createDirectory(String dirname) {
        File dir = new File(dirname);
        if (!dir.exists()) {
            try {
                FileUtils.forceMkdir(dir);
                return true;
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return false;
    }

    public static boolean copyDirectory(String odirname, String dirname) {
        File odir = new File(odirname);
        if (odir.exists() && odir.isDirectory()) {
            File dir = new File(dirname);
            try {
                FileUtils.copyDirectory(odir, dir);
                return true;
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return false;
    }

    public static boolean copyDirectory(String odirname, String dirname,
            String wildcard) {
        File odir = new File(odirname);
        if (odir.exists() && odir.isDirectory()) {
            File dir = new File(dirname);
            FileFilter filter = new WildcardFileFilter(wildcard);
            try {
                FileUtils.copyDirectory(odir, dir, filter);
                return true;
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return false;
    }

    public static boolean deleteDirectory(String dirname) {
        File dir = new File(dirname);
        if (dir.exists() && dir.isDirectory()) {
            try {
                FileUtils.deleteDirectory(dir);
                return true;
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return false;
    }

    public static byte[] toByteArray(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                byte[] buffer = IOUtils.toByteArray(fis);
                fis.close();
                return buffer;
            } catch (FileNotFoundException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return null;
    }

    public static boolean write(byte[] buffer, String filename) {
        return write(buffer, filename, false);
    }

    public static boolean append(byte[] buffer, String filename) {
        return write(buffer, filename, true);
    }

    private static boolean write(byte[] buffer, String filename,
            boolean append) {
        if (buffer != null) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filename, append);
                IOUtils.write(buffer, fos);
                fos.close();
                return true;
            } catch (FileNotFoundException ex) {
                logger.log(Level.WARNING, null, ex);
            } catch (IOException ex) {
                logger.log(Level.WARNING, null, ex);
            }
        }
        return false;
    }
}
