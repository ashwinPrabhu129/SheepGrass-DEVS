package GenCol;

import java.util.*;

public class RelationIterator implements Iterator{
private Relation r;
private DEVSQueue keys;
private Object curKey = null;
private Set curSet;
private DEVSQueue values;
private boolean change = true;

public RelationIterator(Relation R){
r = R;
Set keyset = R.keySet();
keys = DEVSQueue.set2Queue(keyset);
}

private Object Next(){
if (keys.isEmpty())return null;
if (change){
change = false;
curKey = keys.first();
curSet = r.getSet(curKey);
values = DEVSQueue.set2Queue(curSet);
}
if (values.isEmpty()) return null;
return new Pair(curKey,values.first());
}


private void removeNext(){
values.remove();
if (values.isEmpty()){
  keys.remove();
  change = true;
  }
}

public boolean hasNext(){
return Next() != null;
}

public Object next(){
Object ret = Next();
removeNext();
return ret;
}

public void remove(){
}

}
