/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */

package util;
import java.awt.*;
/**
 * A utility class containing various methods for manipulating strings.
 *
 * @author      Jeff Mather
 */
public class StringUtil
{
    /**
     * Replaces all the occurrences of the given substring within the
     * given string with a given replacement substring.
     *
     * @param   string      The string in which to make the replacements.
     * @param   replace     The substring to look for and replace.
     * @param   with        The substring to replace with.
     *
     * @return              A new string with the replacements made.
     */
    static public String replaceAll(String string, String replace, String with)
    {
        // keep doing this
        StringBuffer result = new StringBuffer();
        result.append(string);
        int replaceLength = replace.length();
        int withLength = with.length();
        int index = 0, resultIndex = 0;
        while (true) {
            // if there is a next instance of the string to replace
            int oldIndex = index;
            index = string.indexOf(replace, index);
            if (index >= 0) {
                // do the replacement
                resultIndex += index - oldIndex;
                result.replace(resultIndex, resultIndex + replaceLength, with);
                resultIndex += withLength - replaceLength + 1;
                index++;
            }

            // otherwise, we are done
            else break;
        }

        return result.toString();
    }

    /**
     * The return value type for the indexOfIgnoreWhitespace method below.
     */
    static public class IndexOfIgnoreWhitespaceResult
    {
        /**
         * The index where the query string was found within the text.
         * -1 means no instance of the query string was found.
         */
        public int index = -1;

        /**
         * How many whitespace characters in the text were ignored
         * within the match found for the query string.
         */
        public int numWhitespaceCharsSkipped;
    }

    /**
     * Returns the index of the first occurrence of the query string
     * within the given text, starting the search from the given index.
     *
     * @param   text        The text within which to search.
     * @param   query       The query string for which to search.
     * @param   startIndex  The index within the text to start the search.
     */
    static public IndexOfIgnoreWhitespaceResult indexOfIgnoreWhitespace(
        String text, String query, int startIndex)
    {
        // for each character in the given text, starting at the given
        // start-index
        IndexOfIgnoreWhitespaceResult result =
            new IndexOfIgnoreWhitespaceResult();
        int textLength = text.length(), queryLength = query.length();
        for (int i = startIndex; i < textLength - queryLength; i++) {
            // keep doing this
            int textIndex = i, queryIndex = 0;
            result.numWhitespaceCharsSkipped = 0;
            while (true) {
                // if the current matching process has gone beyond the end
                // of the text, abort it
                if (textIndex >= textLength) break;

                // if the text and query string characters match
                char textChar = text.charAt(textIndex);
                if (textChar == query.charAt(queryIndex)) {
                    // advance to the next text and query string characters
                    textIndex++;
                    queryIndex++;

                    // if we have reached the end of the query string
                    if (queryIndex == queryLength) {
                        // a match was found, so return the index of the match
                        result.index = i;
                        return result;
                    }

                    continue;
                }

                // else, if the text character is whitespace
                else if (Character.isWhitespace(textChar)) {
                    // if the whitespace is at the front of the potential
                    // match, abort this match
                    if (textIndex == i) break;

                    // record that a whitespace character was ignored
                    result.numWhitespaceCharsSkipped++;

                    // advance to the next text character
                    textIndex++;

                    continue;
                }

                break;
            }
        }

        return result;
    }


    static public String concat(String args[],String separator){
                           //separator can be any string  notequal ""
                          //and should not be in args
       String result = separator;
       if (args == null) return result;
       for (int i = 0; i<args.length;i++)
          result = result + args[i]+separator;
        return result;
        }

    static public String[] segment(String s,String separator,int numsegs){
       String[] args = new String[numsegs];
       String remaining = s;
       for (int i = 0; i<numsegs;i++){
       int sep =  remaining.indexOf(separator);
       if (sep == -1)return args;
       String left = remaining.substring(0,sep);
       String right = remaining.substring(sep+separator.length(),remaining.length());
       int nextsep =  right.indexOf(separator);
       if (nextsep == -1)return args;
       args[i] = remaining.substring(sep+separator.length(),nextsep+separator.length());
       remaining = remaining.substring(nextsep+separator.length(),remaining.length());
      }
      return args;
      }
    static public String[] segment(String s,String separator){
      return segment(s,separator,numSegs(s,separator));
      }

    static public int numSegs(String s,String separator){
       if (s.equals(""))return 0;
       int num = 0;
       String remaining = s;
       while(!remaining.equals(separator)){
       int sep =  remaining.indexOf(separator);
       if (sep == -1)return 0;
       String left = remaining.substring(0,sep);
       String right = remaining.substring(sep+separator.length(),remaining.length());
       int nextsep =  right.indexOf(separator);
       if (nextsep == -1)return 0;
       num++;
       remaining = remaining.substring(nextsep+separator.length(),remaining.length());
      }
      return num;
      }

    static final public String separator = "...";

    static public String colorEncode(Color c){
      String args[] = {
      Integer.toString(c.getRed()),
      Integer.toString(c.getGreen()),
      Integer.toString(c.getBlue())
      };
      return concat(args,separator);
      }

   static public Color colorDecode(String s){
     String  args[] = segment(s,separator);
    return new Color(Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]));
   }

   static public String fourDoubleEncode(double i,double j,double k,double t){
      String args[] = {
      Double.toString(i),
      Double.toString(j),
      Double.toString(k),
      Double.toString(t)
      };
      return concat(args,separator);
      }

  static public double[] fourDoubleDecode(String s){
     String  args[] = segment(s,separator);
     double res[] = {
            Double.parseDouble(args[0]),
            Double.parseDouble(args[1]),
            Double.parseDouble(args[2]),
            Double.parseDouble(args[3])
            };
     return res;
   }

   static public String removeInitialSep(String s,String separator){
       String remaining = s;
       int sep =  remaining.indexOf(separator);
       if (sep == -1)return "";
       return  remaining.substring(sep+separator.length(),remaining.length());
   }

    public static void main(String[] args){
    String sep = "..";
    String as[] = {};
    System.out.println(segment(concat(as,sep),sep).length);
    String bs[] = {"aa","bbb","cc"};
    String res = concat(bs,sep);
    System.out.println(removeInitialSep(res,sep));

    System.out.println(numSegs(res,sep));
    bs = segment(res,sep);
    System.out.println(concat(bs," "));
    System.out.println(colorEncode(Color.darkGray));
    System.out.println(colorDecode(colorEncode(Color.darkGray)));
    System.out.println(colorEncode(Color.orange));
    System.out.println(colorDecode(colorEncode(Color.orange)));
    System.out.println(fourDoubleEncode(1.2,3.4,5.6,7.8));
    System.out.println(fourDoubleDecode
         (fourDoubleEncode(1.2,3.4,5.6,7.8))[1]);
    }


}