
package  oneDCellSpace;

import java.lang.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import util.*;
import simView.*;



public class minTree extends ViewableDigraph{



public minTree(){
this("minTree");
}


public minTree(String nm){
    super(nm);

}


public minSelect addMinSelect (String name){
minSelect ta = (minSelect)withName(name);
if (ta == null){
ta = new minSelect(name);
add(ta);
}
return ta;
}

public minSelect addMinSelect (String name,double tN){
minSelect ta = (minSelect)withName(name);
if (ta == null){
ta = new leafGen(name,tN);
add(ta);
}
return ta;
}



public minSelect addMinSelect(minSelect ta,minSelect[] children){
for (int i=0; i<children.length; i++)
   ta.addChild(children[i].getName());
return ta;
}

public minSelect addMinSelect(String name,minSelect[] children){
minSelect ta = addMinSelect(name);
return addMinSelect(ta,children);
}

public minSelect addMinSelect(String name,String[] childNames){
minSelect [] children = new minSelect[childNames.length];
for (int i=0; i<children.length; i++)
   children[i] = addMinSelect(childNames[i]);
return addMinSelect(name,children);
}

public minSelect addRootMinSelect(String name,minSelect[] children){
minSelect ta = addMinSelect(name,children);
ta.root = true;
ta.addOutport("go");
return ta;
}
public minSelect addRootMinSelect(String name,String[] childNames){
minSelect ta = addMinSelect(name,childNames);
ta.root = true;
ta.addOutport("go");
return ta;
}

public void doCoupling(){
minSelect topRoot = null;
componentIterator cit = iterator();
while(cit.hasNext()){
   minSelect ta = (minSelect)cit.nextComponent();
   if (ta.root)topRoot = ta;
   FunctionIterator it = (FunctionIterator)ta.de.iterator();
     while(it.hasNext())
            {
              Pair p = (Pair)it.next();
              Object ent = p.getKey();
              String pre = ent.toString();
              minSelect pt = addMinSelect(pre);
              addCoupling(pt,"out", ta,pre);
              ta.addInport(pre);
    }
}
///////////////
if (topRoot == null)return;
cit = iterator();
while(cit.hasNext()){
   minSelect ta = (minSelect)cit.nextComponent();
   if (ta instanceof leafGen)
   addCoupling(topRoot,"go", ta,"go");
}

}
}
