/**
 * This program implements a 2-D cell for the dynamic forest fire model
 * with an optimized cell space
 * author: Lewis Ntaimo
 * Date:   April 15, 2003
 * Revision:
 *
 */
package twoDCellSpace;

import GenCol.*;
import genDevs.modeling.*;
import statistics.*;
import simView.*;

public abstract class TwoDimCell
    extends ViewableAtomic
    implements Cell {
  protected int numCells; // Total number of cells in the cell space
  protected int xcoord; // The x-coordinate of this cell in the cell space
  protected int ycoord; // The y-coordinate of this cell in the cell space
  protected double x_pos; // The x-coordinate of this cell on the cell space display
  protected double y_pos; // The y-coordinate of this cell on the cell space display
  protected Pair id; // Unique cell id: equals cell pos in cell space
  protected boolean coupled; // This is true if a cell has all the couplings done
  protected rand r;
  protected int drawPos, numTransitions, numTransAll;
  boolean initial;
  // Xsize and Ysize, Xiaolin Hu, June 12, 2003
  public int xDimCellspace; // Cell space x dimension size
  public int yDimCellspace; // Cell space x dimension size

  public multiCell myMultiCell; //for multicell

  public void setModel(String cellNm) {
    // System.out.println(myMultiCell);
    if (myMultiCell != null) {
      myMultiCell.nextModel = cellNm;
    }
  }

  /**
   * Default constructor
   */
  public TwoDimCell() {
    this(new Pair(new Integer(0), new Integer(0)));
  }

  /**
   * Default constructor
   */
  public TwoDimCell(Pair cellId) {
    super("Cell_" + cellId.toString());
//    super("Cell_" + cellId.getKey()+"_"+cellId.getValue());  //Xiaolin Hu
    id = cellId;
    Integer x = (Integer) cellId.getKey();
    Integer y = (Integer) cellId.getValue();
    int xcoord = x.intValue();
    int ycoord = y.intValue();
    //super("Cell_"+ xcoord + "_"+ ycoord);

    //id = new Pair(new Integer(xcoord), new Integer(ycoord));
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.x_pos = 0;
    this.y_pos = 0;
    coupled = false;
    addInport("start");
    addInport("stop");
    addInport("inN");
    addInport("inNE");
    addInport("inE");
    addInport("inSE");
    addInport("inS");
    addInport("inSW");
    addInport("inW");
    addInport("inNW");
    addOutport("outN");
    addOutport("outNE");
    addOutport("outE");
    addOutport("outSE");
    addOutport("outS");
    addOutport("outSW");
    addOutport("outW");
    addOutport("outNW");
    addOutport("outDraw");
    addOutport("outCoord");

    // Add test ports

    addRealTestInput("start", 1);
    addRealTestInput("stop", 1);
    addRealTestInput("in", 1);
    addRealTestInput("in", 2);
    addRealTestInput("in", 3);
    addRealTestInput("in", 1);
    addTestInput("inN", new Pair(new Integer(xcoord), new Integer(ycoord + 1)));
    addTestInput("inNE",
                 new Pair(new Integer(xcoord + 1), new Integer(ycoord + 1)));
    addTestInput("inE", new Pair(new Integer(xcoord + 1), new Integer(ycoord)));
    addTestInput("inSE",
                 new Pair(new Integer(xcoord + 1), new Integer(ycoord - 1)));
    addTestInput("inS", new Pair(new Integer(xcoord), new Integer(ycoord - 1)));
    addTestInput("inSW",
                 new Pair(new Integer(xcoord - 1), new Integer(ycoord - 1)));
    addTestInput("inW",
                 new Pair(new Integer(xcoord - 1), new Integer(ycoord - 1)));
    addTestInput("inNW",
                 new Pair(new Integer(xcoord - 1), new Integer(ycoord + 1)));
  }

  public void initialize() {
    super.initialize();
    initial = false;
    numTransitions = 0;
    passivate();
  }

  public void deltext(double e, message x) {
    Continue(e);
    for (int i = 0; i < x.getLength(); i++) {
      if (somethingOnPort(x, "start")) {
        holdIn("active", 0);
      }
      else if (somethingOnPort(x, "stop")) {
        passivate();
        Pair inpair;
        // Get notification message from neighbor cells
        if (somethingOnPort(x, "inN")) {
          inpair = (Pair) x.getValOnPort("inN", i);
        }
        else if (somethingOnPort(x, "inNE")) {
          inpair = (Pair) x.getValOnPort("inNE", i);
        }
        else if (somethingOnPort(x, "inE")) {
          inpair = (Pair) x.getValOnPort("inE", i);
        }
        else if (somethingOnPort(x, "inSE")) {
          inpair = (Pair) x.getValOnPort("inSE", i);
        }
        else if (somethingOnPort(x, "inS")) {
          inpair = (Pair) x.getValOnPort("inS", i);
        }
        else if (somethingOnPort(x, "inSW")) {
          inpair = (Pair) x.getValOnPort("inSW", i);
        }
        else if (somethingOnPort(x, "inW")) {
          inpair = (Pair) x.getValOnPort("inW", i);
        }
        else if (somethingOnPort(x, "inNW")) {
          inpair = (Pair) x.getValOnPort("inNW", i);
        }
      }
    }
  }

  public void deltint() {
    if (initial) {
      Integer my_int = (Integer) id.getKey();
      r = new rand(my_int.intValue());
      initial = false;
    }
    numTransitions++;
    passivate();
  }

  public message out() {
    message m = new message();
    m.add(makeContent("outCoord",
                      new Pair(new Integer(xcoord), new Integer(ycoord))));
    return m;
  }

  public String getTooltipText() {
    return
        super.getTooltipText()
        + "\n" + " Cell (" + xcoord + ", " + ycoord + ")";
  }

  /**
   * This methods sets the position for this  cell on the graph display
   * @param xPos x position
   * @param yPos y position
   */

  public int[] pairToArray(Pair inpair) {
    Integer i = (Integer) inpair.getKey();
    Integer j = (Integer) inpair.getValue();
    int[] arr = {
        i.intValue(),
        j.intValue()
    };
    return arr;
  }

  public void setPosXY(double xPos, double yPos) {
    //System.out.println("i = "+ xPos +" j=" + yPos);
    this.x_pos = xPos;
    this.y_pos = yPos;
    //System.out.println("xPos = "+ xPos +" yPos=" + yPos);
  }

  public void setTwoDimSpaceSize(int Xs, int Ys) { //Xiaolin Hu, June 12, 2003
	    this.xDimCellspace = Xs;
	    this.yDimCellspace = Ys;

  }

  public double[] coordToPos(int xcoord, int ycoord) {
    double temp[] = {
        (double) (xcoord * 10 - xDimCellspace * 5),
        (double) (ycoord * 10 - yDimCellspace * 5)
    };
    return temp;
  }

  public double[] getPos(Pair inpair) {
    int[] temp = pairToArray(inpair);
    return coordToPos(temp[0], temp[1]);
  }

  public void setPos() {
//    x_pos = (double) (xcoord * 10 - xDimCellspace * 5);
//    y_pos = (double) (ycoord * 10 - yDimCellspace * 5);
    x_pos = (double) (xcoord  - xDimCellspace /2);  // Xiaolin Hu, 11/09/2023
    y_pos = (double) (ycoord  - yDimCellspace /2);
  }

  public void setCoordNPos(int xcoord, int ycoord) {
    this.xcoord = xcoord;
    this.ycoord = ycoord;
//    x_pos = (double) (xcoord * 10 - xDimCellspace * 5); 
//    y_pos = (double) (ycoord * 10 - yDimCellspace * 5);
	  if(xcoord==1 && ycoord==20)
		  System.out.println("breakpoint");

    x_pos = (double) (xcoord  - xDimCellspace /2); // Xiaolin Hu, 11/09/2023
    y_pos = (double) (ycoord - yDimCellspace /2);
  }

  public void setCoordNPos(Pair inpair) {
    int[] arr = pairToArray(inpair);
    this.xcoord = arr[0];
    this.ycoord = arr[1];
    setPos();
  }

  /*/**
  * This method sets the drawing position for this cell
  * @param i drawing position
  */

 /**
  * This sets old to true: this implies that this cell has all the neighbor
  * couplings done
  */
 public void setCouplingsDone() {
   coupled = true;
 }

  /**
   * This method returns this cells id
   * @return id
   */
  public Pair getId() {
    return new Pair(new Integer(xcoord), new Integer(ycoord));
  }

  /**
   * This method convert the cell id to an one dimention id
   * @return
   */
  public int getOneDimId() { // Xiaolin Hu, June 12, 2003
    return ycoord * xDimCellspace + xcoord;
  }

  public int getOneDimId(int XBs, int YBs) { // Xiaolin Hu, June 15, 2003
    int b_num_x = (int) Math.floor( (double) xcoord / XBs); // the block number along x-coordinator
    int x_in_b = xcoord - b_num_x * XBs; // the x coordination inside the block
    int b_num_y = (int) Math.floor( (double) ycoord / YBs); // the block number along y-coordinator
    int y_in_b = ycoord - b_num_y * YBs; // the y coordination inside the block
    int id = XBs * YBs *
        (b_num_y * (int) Math.ceil( (double) xDimCellspace / XBs) + b_num_x) +
        y_in_b * XBs + x_in_b;
//  System.out.println("two dim x="+xcoord+ "  y="+ycoord+" convert to one dim Id="+id);
    return id;
  }

  /**
   * This method returns this cells x coordinate
   * @return xcoord
   */
  public int getXcoord() {
    return xcoord;
  }

  /**
   * This method returns this cells y coordinate
   * @return ycoord
   */
  public int getYcoord() {
    return ycoord;
  }

  public Pair neighborId(int i, int j) {
    int xc = xcoord + i;
    int yc = ycoord + j;
    return new Pair(new Integer(xc), new Integer(yc));
  }

  public boolean isNorthNeighbor(int i, int j) {
    return xcoord == i && ycoord == j - 1;
  }

  public boolean isSouthNeighbor(int i, int j) {
    return xcoord == i && ycoord == j + 1;
  }

  public boolean isEastNeighbor(int i, int j) {
    return xcoord == i - 1 && ycoord == j;
  }

  public boolean isWestNeighbor(int i, int j) {
    return xcoord == i + 1 && ycoord == j;
  }

  public boolean isMooreNeighbor(int i, int j) {
    return isNorthNeighbor(i, j)
        || isSouthNeighbor(i, j)
        || isEastNeighbor(i, j)
        || isWestNeighbor(i, j);
  }

  public boolean isMooreNeighbor(Pair cellId) {
    Integer i = (Integer) cellId.getKey();
    Integer j = (Integer) cellId.getValue();
    int ii = i.intValue();
    int jj = j.intValue();
    return isMooreNeighbor(ii, jj);
  }

  public boolean isClose(Pair cellId) {
    Integer i = (Integer) cellId.getKey();
    Integer j = (Integer) cellId.getValue();
    int ii = i.intValue();
    int jj = j.intValue();
    return (ii - xcoord) * (ii - xcoord)
        + (jj - ycoord) * (jj - ycoord) < 5;
  }

  /**
   * This method returns true if all the couplings are done for this cell
   * @return coupled
   **/

  public boolean isAllCoupled() {
    return coupled;
  }

  /**
   * This method does a neighbor-to-neighbor 2D coupling of cells in a 2D space
   * where each cell has 8 neighbors except the border cells.
   * @param otherID neighbor ID
   * @return returns true if otherID is this cell's neighbor
   */
  /*public boolean neighbor(int otherId){
    int idW = otherId+1;
    int idE = otherId-1;
    int idN = otherId+xDim;
    int idS = otherId-xDim;
    int idNW = otherId+xDim+1;
    int idNE = otherId+xDim-1;
    int idSW = otherId-xDim+1;
    int idSE = otherId-xDim-1;
    if ( (id+1)%xDim == 0 ) // RHS edge cells
      return (idW==id || idN==id || idS==id || idNW==id || idSW==id);
    else if (id%xDim == 0) // LHS edge cells
      return (idE==id|| idN==id || idS==id || idNE==id || idSE==id);
    else  // all other cells
      return (idW==id || idE==id|| idN==id || idS==id || idNW==id ||
              idNE==id || idSW==id || idSE==id);
   }
   */
  /**
   * propagate message m using port type pn to Moore neighbors
   * @return message
   **/

  public message propagate(message m, String pn) {

    m.add(makeContent(pn + "outN",
                      new Pair(new Integer(xcoord), new Integer(ycoord))));

    //   m.add(makeContent(pn+""outNE",new Pair(new Integer(xcoord), new Integer(ycoord))));

    m.add(makeContent(pn + "outE",
                      new Pair(new Integer(xcoord), new Integer(ycoord))));

    //   m.add(makeContent(pn+"outSE",new Pair(new Integer(xcoord), new Integer(ycoord))));

    m.add(makeContent(pn + "outS",
                      new Pair(new Integer(xcoord), new Integer(ycoord))));

    //  m.add(makeContent(pn+"outSW",new Pair(new Integer(xcoord), new Integer(ycoord))));

    m.add(makeContent(pn + "outW",
                      new Pair(new Integer(xcoord), new Integer(ycoord))));

    //   m.add(makeContent(pn+"outNW",new Pair(new Integer(xcoord), new Integer(ycoord))));

    return m;
  }

  /**
   * propagate message m using port type pn to Mealy neighbors
   * @return message
   **/

  public message propagateMealy(message m, String pn) {

    m.add(makeContent(pn + "outN",
                      new Pair(new Integer(xcoord), new Integer(ycoord))));

    m.add(makeContent(pn+"outNE",new Pair(new Integer(xcoord), new Integer(ycoord))));

    m.add(makeContent(pn + "outE",
                      new Pair(new Integer(xcoord), new Integer(ycoord))));

    m.add(makeContent(pn+"outSE",new Pair(new Integer(xcoord), new Integer(ycoord))));

    m.add(makeContent(pn + "outS",
                      new Pair(new Integer(xcoord), new Integer(ycoord))));

    m.add(makeContent(pn+"outSW",new Pair(new Integer(xcoord), new Integer(ycoord))));

    m.add(makeContent(pn + "outW",
                      new Pair(new Integer(xcoord), new Integer(ycoord))));

    m.add(makeContent(pn+"outNW",new Pair(new Integer(xcoord), new Integer(ycoord))));

    return m;
  }

  /**
   * is there something on port type pn in Mealy neighborhood
   * @return boolean
   **/

  public boolean somethingOnPortType(message x, String port) {
    return somethingOnPort(x, port + "inN")
        || somethingOnPort(x, port + "inNE")
        || somethingOnPort(x, port + "inE")
        || somethingOnPort(x, port + "inSE")
        || somethingOnPort(x, port + "inS")
        || somethingOnPort(x, port + "inSW")
        || somethingOnPort(x, port + "inW")
        || somethingOnPort(x, port + "inNW");
  }

} // End class TwoDimCell
