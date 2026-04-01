package genDevs.simulation.heapSim;

import GenCol.*;
import java.util.*;
import genDevs.simulation.*;

  public class minHeap  {
  protected double[] a;
  protected HeapNode[] p;
  protected ensembleSet s;
  protected int numCells;
  protected int capacity;

  public minHeap(int numCells, int capacity) {
    this.numCells = numCells;
    this.capacity = capacity;
    p = new HeapNode[capacity];
  }

  public minHeap(int capacity){
    this(0, capacity);
  }

  public void enlarge(){
    HeapNode[] rp = new HeapNode[2*capacity];
    for (int i = 0; i < capacity; i++){
      rp[i] = p[i];
    }
    capacity = capacity * 2;
    p = rp;
  }

  public void buildHeap () {
    int i = numCells/2;
    while (i >0) {
     // System.out.println(i);
      minHeapify(p, i);
      i--;
    }
  }

public void minHeapify (HeapNode[] p, int i) {
  double min =p[i].tN;
  //System.out.println(min);
  int mm = i;

  if (2*i <= numCells ) {
    double l = p[2 * i].tN;
    if (l < min) {
      mm = 2 * i;
      min =l;
    }
  }
  if (2*i+1 <= numCells ) {
    double r = p[2*i+1].tN;
    if(r<min) {
      mm = 2 * i + 1;
      min = r;
    }
  }
  if (!(mm ==i) ) {
      HeapNode tem_p=p[mm];
      p[mm].simulator=p[i].simulator;
      p[mm].tN=p[i].tN;
      p[i].simulator=tem_p.simulator;
      p[i].tN=tem_p.tN;
      update_SimIndex(mm);
      update_SimIndex(i);
      minHeapify(p,mm);
  }
}

public void insert(Object simO, double t){
  if (!(t == Double.POSITIVE_INFINITY)){
    numCells++;
    if (numCells == capacity) enlarge();
    int n = numCells;
    if (simO instanceof HeapSim)
      p[n] = new HeapNode(n, (HeapSim) simO, t);
    else if (simO instanceof HeapCoupledCoord)
      p[n] = new HeapNode(n, (HeapCoupledCoord) simO, t);
    update_SimIndex(n);
    pullUp(n);
  }
}

public void delete(int idx){
  double newtN=p[numCells].tN;
  double oldtN=p[idx].tN;
  swap(p[idx],p[numCells]);
  numCells--;
  if(oldtN > newtN) pullUp(idx);
  else if(oldtN < newtN) shiftDown(idx);
  p[numCells+1].heap_index = 0;
  if (p[numCells+1].simulator instanceof HeapSim)
  {
    HeapSim ds = (HeapSim) p[numCells+1].simulator;
    ds.myheapIdx = 0;
  }
  else {
    HeapCoupledCoord ds = (HeapCoupledCoord) p[numCells+1].simulator;
    ds.myheapIdx = 0;
  }
  p[numCells+1] = null;
}


public void update_SimIndex(int idx){
  Object simO=p[idx].simulator;
      if (simO instanceof HeapSim) {
        ((HeapSim) simO).myheapIdx=idx;
      }
      else if(simO instanceof HeapCoupledCoord){
        ((HeapCoupledCoord)simO).myheapIdx=idx;
      }

}

public void swap(HeapNode h1, HeapNode h2){
  Object o1= h1.simulator;
      double t1=h1.tN;
      h1.simulator=h2.simulator;
      h1.tN=h2.tN;
      h2.simulator=o1;
      h2.tN=t1;
      update_SimIndex(h1.heap_index);
      update_SimIndex(h2.heap_index);
}

public void updateTN(atomicSimulator iod, double newtN){
  int idx;
  if (iod instanceof HeapSim) idx = ((HeapSim)iod).myheapIdx;
  else idx = ((HeapCoupledCoord) iod).myheapIdx;
  if (idx == 0 && newtN < Double.POSITIVE_INFINITY){
      insert(iod, newtN);
  }
  else if (idx != 0 && newtN < Double.POSITIVE_INFINITY){
    double oldtN = p[idx].tN;
    p[idx].tN = newtN;
    if(oldtN > newtN) pullUp(idx);
    else if(oldtN < newtN) shiftDown(idx);
  }
  else if (idx != 0 && !(newtN < Double.POSITIVE_INFINITY)){
    delete(idx);
  }
}

public void pullUp(int idx){
  int i=idx;
  if (i>1 && p[i].tN<p[i/2].tN){
    swap(p[i],p[i/2]);
    pullUp(i/2);
  }
}

public void shiftDown (int idx){
  int min=idx;
  int l_child=idx*2,r_child=2*idx+1;

  if (l_child<=numCells && p[l_child].tN<p[min].tN) min=l_child;
  if (r_child <=numCells && p[r_child].tN<p[min].tN) min=r_child;
  if (min!=idx){
    swap(p[idx], p[min]);
    shiftDown(min);
  }
}
 /* public static void main(String[] args) {
    minHeap heap = new minHeap(10);
  }*/
}
