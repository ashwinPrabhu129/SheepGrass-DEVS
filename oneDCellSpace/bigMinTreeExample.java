
package  oneDCellSpace;

import java.lang.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import GenCol.*;
import util.*;
import simView.*;
import genDevs.simulation.*;
import genDevs.modeling.*;

public class bigMinTreeExample extends minTree {
public minSelect root;
public double inc = 1;//.001;  //the larger inc the larger the imminents

public bigMinTreeExample(){
this(2);
}

public minSelect  addLeafBlock(String nm,int start, int size){
minSelect s[] = new minSelect[size];
for (int i = start;i<start+size;i++)
s[i-start] =  addMinSelect(nm+"_"+"l"+i,i*inc);
minSelect m = addMinSelect(nm,s);
return m;
}

public minSelect  addSecondLevelBlock(String nm, String ch,int start, int size){
minSelect s[] = new minSelect[size];
for (int i = start;i<start+size;i++)
s[i-start] =  addLeafBlock(nm+"_"+ch+i,i*size,size);
minSelect m = addMinSelect(nm,s);
return m;
}

public minSelect  addThirdLevelBlock(String nm, String ch,String gch,int start, int size){
minSelect s[] = new minSelect[size];
for (int i = start;i<start+size;i++)
s[i-start] =  addSecondLevelBlock(nm+"_"+ch+i,gch,i*size,size);
minSelect m = addRootMinSelect(nm,s);
return m;
}

public bigMinTreeExample(int base){
super("bigMinTreeExample "+base);

//addLeafBlock("h1",0, 2);
//addSecondBlock("h2", "lev",2, 2);

//root = addThirdLevelBlock("h","sec","third",0,base);
/**/
minSelect ta = addLeafBlock("h",0,1000);
ta.root = true;
ta.addOutport("go");

doCoupling();
}




public static void main(String args[]){

CoordinatorInterface r;
long initTime, termTime;

if (args[0] != null){
int param  = Integer.parseInt(args[0]);
bigMinTreeExample d =new bigMinTreeExample(param);
r = new coordinator(d);

System.out.println("Size = "+d.getComponents().size());
System.out.println("Simulating "+r.getCoupled().getName() +" "+param);
r.initialize();

initTime = System.currentTimeMillis();
System.out.println("Start time: "+initTime);
int runs =  5;//100;//
//while(d.root.numIter < runs)
//r.simulate(1);
//
r.simulate(runs);

termTime = System.currentTimeMillis();
System.out.println("End time "+termTime);
System.out.println("Execution Time in secs. for "+ runs +" runs: "
                    +((termTime-initTime)/1000.0));
}
System.exit(0);
}


}

