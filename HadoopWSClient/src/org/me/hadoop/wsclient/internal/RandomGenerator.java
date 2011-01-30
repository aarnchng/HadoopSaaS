/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.hadoop.wsclient.internal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Aaron
 */
public class RandomGenerator {

    private static Random rand = new Random((new Date()).getTime());
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static String getRandString() {
        return Long.toString(Math.abs(rand.nextLong()), 36);
    }

    public static String getRandomString() {
        return getRandString() + sdf.format(new Date());
    }
}
