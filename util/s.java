package util;

public class s
{
    static public void s(String message) {System.out.println(message);}
    static public void ss(String message) {System.out.print(message);}
    static public void t(String message) {s(message);}
    static public void e(Exception e) {e.printStackTrace();}
    static public void st() {try {throw new Exception();} catch (Exception ex) {e(ex);}}
    static public void st(String message) {s(message); st();}
    static public void sleep(long millis) {try {Thread.sleep(millis);} catch (InterruptedException ex) {}}
    static public void sleep(Thread thread, long millis) {try {thread.sleep(millis);} catch (InterruptedException ex) {}}
    static public long time() {return System.currentTimeMillis();}
}