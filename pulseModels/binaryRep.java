/*      Copyright 2002 Arizona Board of regents on behalf of
 *                  The University of Arizona
 *                     All Rights Reserved
 *         (USE & RESTRICTION - Please read COPYRIGHT file)
 *
 *  Version    : DEVSJAVA 2.7
 *  Date       : 08-15-02
 */

package pulseModels;
import java.util.*;
import statistics.*;

public class binaryRep{


public binaryRep (){

}

public static int numTrue(BitSet b){
int count = 0;
for (int i = 0;i<b.size();i++)
if (b.get(i))count++;
return count;
}

public static boolean differAt(BitSet b,BitSet c,int i){
b.xor(c);
return b.get(i);
}

public static int numDiffer(BitSet b,BitSet c){
b.xor(c);
return numTrue(b);
}

public static int bitsToInt(BitSet b){
int sum = 0, mult = 1;
for (int i = 0;i<b.size();i++){
if (b.get(i))sum += mult;
mult = 2*mult;
}
return sum;
}

public static BitSet intToBits(int num){
BitSet d = new BitSet();
int dig = 0;
while (num > 0){
int res = (num % 2);
if (res == 1) d.set(dig);
num = num - res;
num = num/2;
dig++;
}
return d;
}

public static boolean bitAt(int num,int i){
BitSet d = intToBits(num);
return d.get(i);
}

public static boolean differAt(int m, int n, int i){
BitSet b = intToBits(m);
BitSet c = intToBits(n);
return differAt(b,c,i);
}

public static int numDiffer(int m, int n){
BitSet b = intToBits(m);
BitSet c = intToBits(n);
return numDiffer(b,c);
}


public static int randIntGen(randUniform r,int state[]){

int sn = (int)Math.floor(r.sample()*state.length);
int ss = sn;
while(ss < state.length  && state[ss] != 0) ss++;
if (ss == state.length){
ss = sn;
while(state[ss] != 0 && ss >0) ss--;
}
return ss;
}


public static void main(String args[]){

int nbits = 20;//64;
randUniform r = new randUniform(55599);
int sl = 1;
int runlength  = 0, maxlength = 0, changeCount = 0;
double sum = 0;
//int state[] = new int[(int)Math.pow(2,nbits)];
//int numIter = state.length;
int numIter = (int)Math.pow(2,nbits);// nbits;
int i;
for (i= 0;i<numIter;i++){
//int sn = randIntGen(r,state);
//int sn = i;
//int sn = (i+13)%state.length;
//int sn = (sl+73)%state.length;
// state[sn] = 1;
 int sn = Math.abs((numIter-13)*sl)%numIter;
   //31 is .56, 33 is .34, 63 is .65, 65 is .29, 127 is .7, 129 is .25
 if (sn == 0)break;
 //
 System.out.println(sn);
 if (i >0){
  sum += (double)numDiffer(sl,sn)/nbits;
 //System.out.println(numDiffer(sl,sn));
  //System.out.println(bitAt(sl,0)+ " "+ bitAt(sn,0));
  if (differAt(sl,sn,8))
  changeCount++;
  }
 sl = sn;
 }
System.out.println("For "+i+" iterations out of "+numIter+": Avg activity = "+(sum/(i-1)));
System.out.println("Avg run length = "+ (double)(i-1)/changeCount);
/*
//int[] state = {44,51,101,33,22,11};
randUniform r = new randUniform(555999);
int s = 1, sn = 0, mod = (int)Math.pow(2,21);//10000;//000;
int runlength  = 0, maxlength = 0, changeCount = 0;
double sum = 0;
int state[] = new int[mod];
state[0] = 1;
for (int i = 0;i<mod;i++){
//sn = (s*2)%mod;
//sn = state[i];
sn = (int)Math.floor(r.sample()*mod);
int ss = sn;
while(ss < mod  && state[ss] != 0) ss++;
if (ss == mod){
ss = sn;
while(state[ss] != 0 && ss >0) ss--;
}
if (ss == 0) break;
sn = ss;
state[sn] = 1;
//System.out.println(sn);//+ " "+numDiffer(s,sn));
sum += (double)numDiffer(s,sn)/mod;
//if ((s-sn)%2 == 1){
BitSet bs = intToBits(s-sn);
if (bs.get(9)){
changeCount++;
//System.out.println("run length "+runlength);
if (runlength > maxlength) maxlength = runlength;
runlength = 0;
}
else runlength++;
s = sn;
}
//
System.out.println("Avg activity = "+sum/mod);
System.out.println("Avg run length = "+ (double)mod/changeCount);
System.out.println("Max run length = "+ maxlength);
*/
}
}