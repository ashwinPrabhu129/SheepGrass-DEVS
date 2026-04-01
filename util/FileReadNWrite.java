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

public class FileReadNWrite
{
    static public String getContentsAsString(File file)
    {
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader in = new BufferedReader(new FileReader(file));
            while (true) {
                String line = in.readLine();
                if (line == null) break;
                buffer.append(line);
                buffer.append("\n");
            }

            return buffer.toString();
        } catch (IOException e) {e.printStackTrace(); return null;}
    }

   static public void writeString(String s,String fnm){
     PrintWriter dout = null;
                try {
                 dout = new PrintWriter(new FileOutputStream(fnm,true));
                } catch(FileNotFoundException ee) {
                 System.out.println("could not open file"+fnm);
                }
            dout.println(s);
            dout.close();
       }
}