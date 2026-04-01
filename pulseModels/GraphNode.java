package pulseModels;

import simView.*;

import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import java.util.StringTokenizer;



public class GraphNode extends ViewableAtomic {

  private double cost = 0;
  private String rpath = "";
  private double rcost = 0;
  private int numIn, numOut;

  public GraphNode( String nm, double cost, int numIn, int numOut ) throws Exception {
    super( nm );
    int i;
    this.cost = cost;
    if ( numIn < 0 ) throw ( new Exception( "numIn must not be negative" ) );
    this.numIn = numIn;
    for ( i = 1; i <= numIn; i++ ) addInport( "in" + i );
    if ( numOut < 0 ) throw ( new Exception( "numOut must not be negative" ) );
    this.numOut = numOut;
    for ( i = 1; i <= numOut; i++ ) addOutport( "out" + i );
  }

  public GraphNode( String nm, double cost ) throws Exception {
    this( nm, cost, 1, 1 );
  }

  public GraphNode() throws Exception {
    this( "C", 1 );
    addNameTestInput( "in1", "A:2", 2 );
  }

  public void initialize(){
    super.initialize();
    passivateIn( "active" );
  }


  public void deltext( double e, message x ) {
    Continue( e );
    if ( phaseIs( "active" ) ) {
      for ( int i = 1; i <= numIn; i++ ) {
        if ( somethingOnPort( x, ( "in" + i ) ) ) {
          StringTokenizer st =  new StringTokenizer( getNameOnPort( x, ( "in" + i ) ), ":" );
          rpath = new String( st.nextToken() + getName() );
          rcost = cost + Double.parseDouble( st.nextToken() );
          break;
        }
      }
      holdIn( "waiting", cost );
    }
  }

  public void deltint() {
    passivateIn("passive "+rcost);
  }

  public void deltcon( double e, message x ) {
    deltint();
  }

  public message out() {
    message m = new message();
    for ( int i = 1; i <= numOut; i++ )
        m.add( makeContent( ( "out" + i ), new entity( rpath + ":" + rcost ) ) );
    return m;
  }


  public String getTooltipText(){
    return super.getTooltipText() + "\n" +
           "cost: " + cost + "\n" +
           "in: " + numIn + " out: " + numOut + "\n" +
           "root_path: " + rpath + "\n" +
           "root_cost: " + rcost;
  }

  public String getFormattedPhase() {
    return getPhase() + "  c=" + cost;
  }

  public static void main(String args[]) {
    try {
      new  GraphNode();
    } catch ( Exception e ) { System.out.println( e ); }
  }
}





