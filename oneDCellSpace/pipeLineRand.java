
package  oneDCellSpace;


import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import genDevs.simulation.special.*;
import genDevs.simulation.realTime.*;
import GenCol.*;
import genDevs.plots.*;
import statistics.*;
import java.awt.*;
import oneDCellSpace.*;

class pulsePipe  extends oneDimCellSpace {

  protected rand rg = new rand( 1234567890 );

  ////////////////////////////////////////////////////////////////
  class pulseCell extends oneDimCell {

    protected double fireDelay=10;
    protected boolean IAmLast = false;

    public pulseCell( ) {
      super( 0, null );
    }

    public pulseCell( int id, double fireDelay ) {
      super( id, null );
      this.fireDelay = fireDelay;
      this.addOutport("outDraw");
    }

    public void setLast(){IAmLast=true;}

    public void initialize() {
      super.initialize();
      passivate();
    }

    public void deltext( double e, message x ) {
      Continue( e );
      if ( phaseIs( "passive" ) && somethingOnPort( x, "in" ) )
        holdIn( "fire", rg.expon( fireDelay ) );
      if(IAmLast)
          System.out.println(getName()+"---the last one becomes active!");
    }

    public void deltint() {
      passivate();
    }

    public message out(){
      message m = new message();
      m.add( makeContent("out", new doubleEnt( 2.0 ) ) );
//      m.add(makeContent("outDraw",new DrawCellEntity(id*1.0/2.0, 0, Color.black, Color.black)));
      m.add(makeContent("outDraw",new DrawCellEntity(id, id, Color.green,Color.blue)));
      return m;
    }

  }
  ////////////////////////////////////////////////////////////////

  public pulsePipe() {
    this( 4, 10.0 );
  }

  public pulsePipe( int numCells, double fireDelay ) {

    super( "pulsePipe "+numCells );
    this.numCells = numCells;
    addInport( "in" );
    addOutport("out");

      pulseCell pc;
//      pc = new pulseCell( 0, 10 ); // the first cell
      pc = new pulseCell( 0, fireDelay ); // the first cell
      add(pc);
      addCoupling( this, "in", pc, "in" );
      for(int i=1;i<numCells;i++){
//        pc = new pulseCell( i, 10 );
        pc = new pulseCell( i, fireDelay );
        addCell(i,pc);
      }
      pc.setLast(); // the last cell
      addCoupling(pc, "out", this, "out" );

      doNeighborCoupling( +1, "out", "in" );
  //    hideAll();  //hides only components so far

//    CellGridPlot t = new CellGridPlot("pipeLineRand Plot",5,10,6);
    CellGridPlot t = new CellGridPlot("pipeLineRand Plot",5,40,40);
    t.setCellSize(10);

    t.setCellGridViewLocation(600,300);
    add(t);
    //t.setHidden(false);
    coupleAllTo("outDraw",t,"drawCellToScale");

}
}


////////////////////////////////
class ef extends ViewableAtomic {
  protected double clock,interArrivalTime;
  protected int generated,received;

  public ef( String nm ) {
    super( nm );
    addInport( "in" );
    addOutport( "outp" );
    addOutport( "out" );

  }

 public ef(double interArrivalTime) {
    this( "ef" );
    this.interArrivalTime = interArrivalTime;
  }
  public ef() {
    this( "ef" );
  }

  public void initialize() {
    super.initialize();
    clock = 0;
    generated = 1;
    received = 0;
   // passivate();
    holdIn( "output", 0 );
  }

  public void deltext(double e, message x) {
    Continue(e);
    if ( phaseIs( "passive" ) ) passivateIn( "generateding" );
    else if ( phaseIs( "generating" ) ) {
      clock += e;
      //System.out.println("processing time is:"+clock);
      holdIn( "output", 0 );
     // generated++;
        received++;
    }
  }

  public void deltint() {
    passivateIn( "generating" );
      generated++;
      
//       if ( generated < 100 )
//       holdIn( "generating", interArrivalTime );
//       else passivateIn
//       ("thruput = " + received/((double)generated*interArrivalTime));

    if(generated==500){
	  passivateIn("stop");
	  System.out.println("total time="+clock+" received job="+received+" averagte turn around time is:"+clock/received);
  }
  
  }

  public message out() {

    return outputNameOnPort( "pulse", "outp" );
//    else return outputRealOnPort( clock / 100, "out" );
  }

  public String getTooltipText(){
    return super.getTooltipText() +
           "\n" + "clock = " + clock +
            "\n" + "generated = " + generated+
           "\n" + "received = " + received+
           "\n" + "ratio = " + received/(double)generated+
            "\n" + "thruput = " + received/((double)generated*interArrivalTime);

  }
}
////////////////////////////////////////////////
public class pipeLineRand extends ViewableDigraph {
  protected pulsePipe pipe;

  protected ef pg;

  public pipeLineRand(){
  this(4);  //27 -> .243 (27/10)*9/100 has optimal thruput
  }
  public pipeLineRand(int numCells) {
    super( "pipeLineRand "+numCells );
    addInport( "in" );
    addOutport( "out" );
    addTestInput( "in", new entity( "pulse" ) );

    pipe = new pulsePipe( numCells, 50/(double)numCells );
   //pipe.setHidden(false);
    //
    //pipe.setBlackBox(true);
    add( pipe );

    pg = new ef(10/(double)numCells);

    add( pg );

    addCoupling( this, "in", pg, "in" );
    addCoupling( pg, "outp", pipe, "in" );
    addCoupling( pipe, "out", pg, "in" );

  }

public static void main(String args[]){
int numcells = 50; //3000/*10000*/

System.out.println("Simulating pipeLineRand with numCells:"+numcells);

coordinator r = new coordinator(new pipeLineRand(numcells));
//TunableCoordinator r = new TunableCoordinator(new pipeLineRand(numcells));
//r.setTimeScale(0.1);

r.initialize();

r.simulate(100000);
}

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(1098, 540);
        if((ViewableComponent)withName("pulsePipe 4")!=null)
             ((ViewableComponent)withName("pulsePipe 4")).setPreferredLocation(new Point(-9, 101));
        if((ViewableComponent)withName("ef")!=null)
             ((ViewableComponent)withName("ef")).setPreferredLocation(new Point(16, 52));
    }
}
