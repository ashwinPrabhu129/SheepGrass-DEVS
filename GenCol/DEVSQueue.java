
package GenCol;



import java.util.*;

public class DEVSQueue extends LinkedList{  //add is to end

public boolean remove_first(){
if (size()>0){
remove(0);
return true;
}
else return false;
}

public Object first(){
return get(0);
}


static public DEVSQueue set2Queue(Set s){
DEVSQueue q = new DEVSQueue();
Iterator it = s.iterator();
while (it.hasNext())q.add(it.next());
return q;
}

public Bag Queue2Bag(){
Bag b = new  Bag();
Iterator it = iterator();
while (it.hasNext())b.add(it.next());
return b;
}

}


