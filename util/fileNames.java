/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */

package util;

import java.io.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class fileNames
{

 static public void printNames(String packageName)
    {
        // create a filename filter (to be used below) that will
        // match against ".class" files (and ignore inner classes)
        final String extension = ".java";
        FilenameFilter filter =  new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(extension) && name.indexOf('$') == -1;
            }
        };

        // find all java class files (that aren't inner classes) in the
        // models-path
        String modelsPath = "C:/GenDevsLast/src/"+packageName;
        ;
        File path = new File(modelsPath);
        File[] files = path.listFiles(filter);

        // if the models-path doesn't exist


        // sort the names of the java class files found above
        TreeSet sortedFiles = new TreeSet(Arrays.asList(files));


        // for each java class file in the models-path (we are assuming
        // each such file to be a compiled devs java model)


        Iterator i = sortedFiles.iterator();
        while (i.hasNext()) {
            // add this class file's name (minus its extension) to the box
            String name = ((File)i.next()).getName();
            System.out.println(name);


        }
    }

        public static void main(String[] args)
    {
    printNames("Introduction");
        printNames("Random");
            printNames("SimpArc");
              printNames("devsIntegration");
                printNames("Continuity");

    }
}