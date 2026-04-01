package genDevs.simulation.heapSim;

public class HeapNode {
  int heap_index;
  Object simulator;
  double tN;
  public HeapNode(int ind,Object sim,double t) {
    heap_index=ind;
    simulator=sim;
    tN=t;
  }
}
