/**
 * This program implements a 2-dimensional optimized cell space *
 * author: Lewis Ntaimo
 * Date: April 16, 2003
 * Revision: , 2003
 *
 */
package twoDCellSpace;

import java.awt.*;

import GenCol.*;
import genDevs.modeling.*;
import genDevs.plots.*;
import simView.*;

public class TwoDimCellSpace
    extends ViewableDigraph {
  public int xDimCellspace; // Cell space x dimension size
  public int yDimCellspace; // Cell space x dimension size
  protected int xcoord; // A cell's x coordinate position in the cell space
  protected int ycoord; // A cell's y coordinate position in the cell space
  protected int my_xcoord; // This cell's x coordinate position in the cell space
  protected int my_ycoord; // This cell's y coordinate position in the cell space
  public int numCells;
  Cell d1;

  /**
   * Convenient constructor
   */
  public TwoDimCellSpace() {
    this("TwoDimCellSpace", 1, 1);
  }

  /**
   * Constructor
   */
  public TwoDimCellSpace(String nm, int xDimCellspace, int yDimCellspace) {
    super(nm);
    this.xDimCellspace = xDimCellspace;
    this.yDimCellspace = yDimCellspace;
    this.numCells = xDimCellspace * yDimCellspace;
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
    addOutport("out");
    addOutport("outN");
    addOutport("outNE");
    addOutport("outE");
    addOutport("outSE");
    addOutport("outS");
    addOutport("outSW");
    addOutport("outW");
    addOutport("outNW");
    addOutport("outTrans");
    addOutport("outDisplay");

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

/////////////////////////////////////////////////////////////////////////////////
  // These methods were taken from oneDimCellSpace.java by Dr. Zeigler      //
  // Some methods were modified to fit the 2-D cell space and that is noted //
  // wherever that is applicable.                                           //
  ////////////////////////////////////////////////////////////////////////////
  /**
   * See parent method.
   */
  public boolean layoutForSimViewOverride() {
    preferredSize = new Dimension(900, 1300);

    ViewableComponentUtil.layoutCellsInGrid(numCells, "cell_", 3, this,
                                            200, 70);
    return true;
    //return false;
  }

  public Cell withId(Pair cellId) {
    return (Cell) withName("Cell_" + cellId.toString());
  }

  public Cell withId(int xcoord, int ycoord) {
    return withId(new Pair(new Integer(xcoord), new Integer(ycoord)));
  }

  public Cell neighborOf(Cell c, int i, int j) {
    return withId(c.neighborId(i, j));
  }

  public void hideAll() {
    componentIterator it1 = components.cIterator();
    while (it1.hasNext()) {
      devs d1 = (devs) it1.nextComponent();
      ( (ViewableComponent) d1).setHidden(true); //hide cells
    }
  }

  public void doAllToAllCoupling() {
    componentIterator it1 = components.cIterator();
    while (it1.hasNext()) {
      devs d1 = (devs) it1.nextComponent();
      componentIterator it2 = components.cIterator();
      while (it2.hasNext()) {
        devs d2 = (devs) it2.nextComponent();
        if (!d1.equals(d2)) {
          addCoupling(d1, "out", d2, "in");
        }
      }
    }
  }

  public void coupleAllTo(String sourcePt, devs d, String destinPt) {
    componentIterator it1 = components.cIterator();
    while (it1.hasNext()) {
      devs d1 = (devs) it1.nextComponent();
      if (!d1.equals(d)) {
        addCoupling(d1, sourcePt, d, destinPt);
      }
    }
  }

  public void coupleOneToAll(devs d, String sourcePt, String destinPt) {
    componentIterator it1 = components.cIterator();
    while (it1.hasNext()) {
      devs d1 = (devs) it1.nextComponent();
      if (!d1.equals(d)) {
        addCoupling(d, sourcePt, d1, destinPt);
      }
    }
  }

  public void coupleAllToExcept(String sourcePt, devs d, String destinPt
                                , devs other) {

    componentIterator it1 = components.cIterator();
    while (it1.hasNext()) {
      devs d1 = (devs) it1.nextComponent();
      if (!d1.equals(d) && !d1.equals(other)) {
        addCoupling(d1, sourcePt, d, destinPt);
      }
    }
  }

  /**
   *  This method was modified to do neighbor to neighbor coupling for the
   *   cells
   */
  public void doNeighborToNeighborCoupling() {
    componentIterator it1 = components.cIterator();
    while (it1.hasNext()) {
      IODevs temp1 = it1.nextComponent();
      if(!(temp1 instanceof Cell)) continue;
      Cell d1 = (Cell) temp1;
      Pair myid = (Pair) d1.getId();
      Integer my_xint = (Integer) myid.getKey();
      Integer my_yint = (Integer) myid.getValue();
      int my_x = my_xint.intValue();
      int my_y = my_yint.intValue();
      //System.out.println();
      //System.out.println("My cell id:  " + myid.toString());
      //System.out.println();
      componentIterator it2 = components.cIterator();
      while (it2.hasNext()) {
    	IODevs temp2 = it2.nextComponent();
        if(!(temp2 instanceof Cell)) continue;
        Cell d2 = (Cell) temp2;
        Pair other_id = (Pair) d2.getId();
        if (!other_id.equals(myid)) {
          Integer other_xint = (Integer) other_id.getKey();
          Integer other_yint = (Integer) other_id.getValue();
          int other_x = other_xint.intValue();
          int other_y = other_yint.intValue();
          //System.out.println("Other cell id:  " + other_id.toString());
          // N Neighbor
          if (my_x == other_x && my_y == other_y - 1) {
            addCoupling(d1, "outN", d2, "inS");
          } // NE Neighbor
          else if (my_x == other_x - 1 && my_y == other_y - 1) {
            addCoupling(d1, "outNE", d2, "inSW");
          } // E Neighbor
          else if (my_x == other_x - 1 && my_y == other_y) {
            addCoupling(d1, "outE", d2, "inW");
            //System.out.println("My East Neighbor id:  " + other_id.toString());
          } // SE Neighbor
          else if (my_x == other_x - 1 && my_y == other_y + 1) {
            addCoupling(d1, "outSE", d2, "inNW");
          } // S Neighbor
          else if (my_x == other_x && my_y == other_y + 1) {
            addCoupling(d1, "outS", d2, "inN");
          } // SW Neighbor
          else if (my_x == other_x + 1 && my_y == other_y + 1) {
            addCoupling(d1, "outSW", d2, "inNE");
          } // W Neighbor
          else if (my_x == other_x + 1 && my_y == other_y) {
            addCoupling(d1, "outW", d2, "inE");
          } // NW Neighbor
          else if (my_x == other_x + 1 && my_y == other_y - 1) {
            addCoupling(d1, "outNW", d2, "inSE");
          }
        } // end if
      } // End inner while loop
    } // End outer while loop
  }

  /**
   *  This method was modified to do neighbor to neighbor coupling for the
   *   cells
   */
  public void doNeighborToNeighborCoupling(String port) {
    componentIterator it1 = components.cIterator();
    while (it1.hasNext()) {
      Cell d1 = (Cell) it1.nextComponent();
      Pair myid = (Pair) d1.getId();
      Integer my_xint = (Integer) myid.getKey();
      Integer my_yint = (Integer) myid.getValue();
      int my_x = my_xint.intValue();
      int my_y = my_yint.intValue();
      //System.out.println();
      //System.out.println("My cell id:  " + myid.toString());
      //System.out.println();
      componentIterator it2 = components.cIterator();
      while (it2.hasNext()) {
        Cell d2 = (Cell) it2.nextComponent();
        Pair other_id = (Pair) d2.getId();
        if (!other_id.equals(myid)) {
          Integer other_xint = (Integer) other_id.getKey();
          Integer other_yint = (Integer) other_id.getValue();
          int other_x = other_xint.intValue();
          int other_y = other_yint.intValue();
          //System.out.println("Other cell id:  " + other_id.toString());
          // N Neighbor
          if (my_x == other_x && my_y == other_y - 1) {
            addCoupling(d1, port + "outN", d2, port + "inS");
          } // NE Neighbor
          else if (my_x == other_x - 1 && my_y == other_y - 1) {
            addCoupling(d1, port + "outNE", d2, port + "inSW");
          } // E Neighbor
          else if (my_x == other_x - 1 && my_y == other_y) {
            addCoupling(d1, port + "outE", d2, port + "inW");
            //System.out.println("My East Neighbor id:  " + other_id.toString());
          } // SE Neighbor
          else if (my_x == other_x - 1 && my_y == other_y + 1) {
            addCoupling(d1, port + "outSE", d2, port + "inNW");
          } // S Neighbor
          else if (my_x == other_x && my_y == other_y + 1) {
            addCoupling(d1, port + "outS", d2, port + "inN");
          } // SW Neighbor
          else if (my_x == other_x + 1 && my_y == other_y + 1) {
            addCoupling(d1, port+ "outSW", d2, port+ "inNE");
          } // W Neighbor
          else if (my_x == other_x + 1 && my_y == other_y) {
            addCoupling(d1, port + "outW", d2, port + "inE");
          } // NW Neighbor
          else if (my_x == other_x + 1 && my_y == other_y - 1) {
            addCoupling(d1, port + "outNW", d2, port + "inSE");
          }
        } // end if
      } // End inner while loop
    } // End outer while loop
  }

  /**
   *  This method was modified to do neighbor to neighbor coupling for
   *  cells given the cell id and ports for coupling
   */
  public void doNeighborCoupling(Pair id, String sourcePt, String destPt) {
    componentIterator it1 = components.cIterator();
    boolean id_found = false;
    int my_x = 0;
    int my_y = 0;
    // Find cell with this id
    while (it1.hasNext()) {
      Cell d1 = (Cell) it1.nextComponent();
      Pair myid = (Pair) d1.getId();
      if (myid.equals(id)) {
        id_found = true;
        Integer my_xint = (Integer) myid.getKey();
        Integer my_yint = (Integer) myid.getValue();
        my_x = my_xint.intValue();
        my_y = my_yint.intValue();
        break;
      }
    }
    // if cell with this id is found find its neighbors and do coupling
    if (id_found) {
      componentIterator it2 = components.cIterator();
      while (it2.hasNext()) {
        Cell d2 = (Cell) it2.nextComponent();
        Pair other_id = (Pair) d2.getId();
        Integer other_xint = (Integer) other_id.getKey();
        Integer other_yint = (Integer) other_id.getValue();
        int other_x = other_xint.intValue();
        int other_y = other_yint.intValue();

        // N Neighbor
        if (my_x == other_x && my_y == other_y - 1) {
          addCoupling(d1, sourcePt, d2, destPt);
        } // NE Neighbor
        else if (my_x == other_x - 1 && my_y == other_y - 1) {
          addCoupling(d1, sourcePt, d2, destPt);
        } // E Neighbor
        else if (my_x == other_x - 1 && my_y == other_y) {
          addCoupling(d1, sourcePt, d2, destPt);
        } // SE Neighbor
        else if (my_x == other_x - 1 && my_y == other_y + 1) {
          addCoupling(d1, sourcePt, d2, destPt);
        }
        else if (my_x == other_x && my_y == other_y + 1) {
          addCoupling(d1, sourcePt, d2, destPt);
        } // SW Neighbor
        else if (my_x == other_x + 1 && my_y == other_y + 1) {
          addCoupling(d1, sourcePt, d2, destPt);
        } // W Neighbor
        else if (my_x == other_x + 1 && my_y == other_y) {
          addCoupling(d1, sourcePt, d2, destPt);
        } // NW Neighbor
        else if (my_x == other_x + 1 && my_y == other_y - 1) {
          addCoupling(d1, sourcePt, d2, destPt);
        }

      }
    } // End if

  }

  public void addPlots(double stateMax, int transitionMax, double timeMax) {
    CellGridPlot transitionP = new CellGridPlot(" Transition Plot", 1,
                                                "location", numCells,
                                                "transitions", transitionMax);
//transitionP.setHidden(false);
    add(transitionP);

    CellGridPlot stateP = new CellGridPlot(" State Plot", 1, "location",
                                           numCells,
                                           "state", stateMax);
    add(stateP);

    CellGridPlot sigmaP = new CellGridPlot(" Time Advance Plot", 1,
                                           "location", numCells,
                                           "time advance", 100);
    add(sigmaP);

    CellGridPlot timeP = new CellGridPlot(" Time Plot", 10, stateMax);
    timeP.setSpaceSize(100, 40);
    timeP.setCellSize(5);
    timeP.setTimeScale(timeMax);
    add(timeP);

    componentIterator it1 = components.cIterator();
    while (it1.hasNext()) {
      devs d1 = (devs) it1.nextComponent();
      if (!d1.eq(" State Plot")) {
        addCoupling(d1, "outDraw", stateP, "drawCellToScale");
      }
      if (!d1.eq(" Transition Plot")) {
        addCoupling(d1, "outNum", transitionP, "drawCellToScale");
      }
      if (!d1.eq(" Time Advance Plot")) {
        addCoupling(d1, "outSigma", sigmaP, "drawCellToScale");
      }
      if (!d1.eq(" Time Plot")) {
        addCoupling(d1, "outDrawTime", timeP, "timePlot");

      }
      setPlotLocations(stateP, timeP, transitionP, sigmaP);
    }

  }

  public void addPlots(String nm, String from) {
    CellGridView[] w = {
        ( (CellGridPlot) withName(from + " Plot")).getCellGridView(),
        ( (CellGridPlot) withName(from + " Transition Plot")).getCellGridView(),
        ( (CellGridPlot) withName(from + " Time Plot")).getCellGridView(),
        ( (CellGridPlot) withName(from + " Time Advance Plot")).getCellGridView(),
    };

    CellGridPlot stateP = new CellGridPlot(nm + " Plot", 1, w[0]);
    add(stateP);

    CellGridPlot transitionP = new CellGridPlot(nm + " Transition Plot", 1, w[1]);
    add(transitionP);

    CellGridPlot timeP = new CellGridPlot(nm + " Time Plot", 10, w[2]);
    add(timeP);

    timeP.setTimeScale( ( (CellGridPlot) withName(from + " Time Plot")).
                       getTimeScale());

    CellGridPlot sigmaP = new CellGridPlot(nm + " Time Advance Plot", 1, w[3]);
    add(sigmaP);

    doPlotCoupling(nm, stateP, timeP, transitionP, sigmaP);

  }

  protected void doPlotCoupling(String name, CellGridPlot stateP,
                                CellGridPlot timeP, CellGridPlot transitionP,
                                CellGridPlot sigmaP) {
    devs d = (devs) withName(name);
    addCoupling(d, "outDraw", stateP, "drawCellToScale");
    // addCoupling(d, "out", timeP, "timePlot");
    addCoupling(d, "outNum", transitionP, "drawCellToScale");
    addCoupling(d, "outSigma", sigmaP, "drawCellToScale");
  }

  public void setPlotLocations(CellGridPlot stateP,
                               CellGridPlot timeP, CellGridPlot transitionP,
                               CellGridPlot sigmaP) {
    boolean largeScreen = true;
    if (largeScreen) {
      stateP.setCellGridViewLocation(660, 1);
      timeP.setCellGridViewLocation(660, 440);
      transitionP.setCellGridViewLocation(200, 700);
      sigmaP.setCellGridViewLocation(660, 700);
    }
    else {
      stateP.setCellGridViewLocation(200, 20);
      timeP.setCellGridViewLocation(220, 40);
      transitionP.setCellGridViewLocation(240, 60);
      sigmaP.setCellGridViewLocation(260, 80);
    }

  }

  ///////////////////////////////////////////////////////////////////////////////

  /**
   * This method does neighbor to neighbor coupling for a given Cell
       * specified by its (x,y) coordinate position in the Cell space. It also couples
   * the Cell to all the default Cell space couplings.
   * @param _xcoord the x-coordinate of this Cell
   * @param _ycoord the y-coordinate of this Cell
   */
  public void doThisCellsNeighborCouplings(int _xcoord, int _ycoord) {
    componentIterator it1 = components.cIterator();
    while (it1.hasNext()) {
      d1 = (Cell) it1.nextComponent();
      my_xcoord = d1.getXcoord();
      my_ycoord = d1.getYcoord();
      if (my_xcoord == _xcoord && my_ycoord == _ycoord) { // Found this Cell!
        break;
      }
    }
    //Couple this Cell to all default Cellspace ports
    doCellToCellspaceCouplings(d1);

    // Find this Cell's neighbors and couple them to this Cell
    componentIterator it2 = components.cIterator();
    while (it2.hasNext()) {
      Cell d2 = (Cell) it2.nextComponent();
      // Check if Cell d2 has all the neighbor couplings
      if (!d2.isAllCoupled()) { //
        xcoord = d1.getXcoord();
        ycoord = d1.getYcoord();
        // N Neighbor
        if (my_xcoord == xcoord && my_ycoord == ycoord - 1) {
          addCoupling(d1, "outN", d2, "inS");
        } // NE Neighbor
        else if (my_xcoord == xcoord - 1 && my_ycoord == ycoord - 1) {
          addCoupling(d1, "outNE", d2, "inSW");
        } // E Neighbor
        else if (my_xcoord == xcoord - 1 && my_ycoord == ycoord) {
          addCoupling(d1, "outE", d2, "inW");
        } // SE Neighbor
        else if (my_xcoord == xcoord - 1 && my_ycoord == ycoord + 1) {
          addCoupling(d1, "outE", d2, "inW");
        } // S Neighbor
        else if (my_xcoord == xcoord && my_ycoord == ycoord + 1) {
          addCoupling(d1, "outS", d2, "inN");
        } // SW Neighbor
        else if (my_xcoord == xcoord + 1 && my_ycoord == ycoord + 1) {
          addCoupling(d1, "outSE", d2, "inNE");
        } // W Neighbor
        else if (my_xcoord == xcoord + 1 && my_ycoord == ycoord) {
          addCoupling(d1, "outW", d2, "inE");
        } // NW Neighbor
        else if (my_xcoord == xcoord + 1 && my_ycoord == ycoord - 1) {
          addCoupling(d1, "outNW", d2, "inSE");
        }
      } // End out if isOld
    } // End while loop

    //Set that this Cell has all the couplings done
    d1.setCouplingsDone();

  } //End method doThisCellsNeighborCoupling

  /**
   * This method performs all default couplings between a given
   * Cell and the Cellspace
   */
  public void doCellToCellspaceCouplings(Cell c) {
    addCoupling(this, "start", c, "start");
    addCoupling(this, "stop", c, "stop");
    addCoupling(this, "outCSM", c, "outCSM");

    //addCoupling(this, "inTwo", c, "inTwo");
    //addCoupling(this, "inThree", c, "inThree");
    //addCoupling(c, "outOne", this, "outOne");
    //addCoupling(c, "outTwo", this, "outTwo");
    //addCoupling(c, "outThree", this, "outThree");
  }

  /**
   * This method creates and adds a new Cell to the Cell space. Also performs
   * all appropriate couplings
   */

  public void addCell(Cell c) {
    this.add(c);
    int xcoord = c.getXcoord();
    int ycoord = c.getYcoord();
    doCellToCellspaceCouplings(c);
    // System.out.println("x = " + xcoord + "   y = " + ycoord);
    doThisCellsNeighborCouplings(xcoord, ycoord);
  }

  public void addCell(Cell c, int xDimCellpace,
                      int yDimCellspace) {
    this.add(c);
    int xcoord = c.getXcoord();
    int ycoord = c.getYcoord();
    ( (TwoDimCell) c).setTwoDimSpaceSize(xDimCellpace,
            yDimCellspace);
    ( (TwoDimCell) c).setCoordNPos(xcoord, ycoord);
    doCellToCellspaceCouplings(c);
    // System.out.println("x = " + xcoord + "   y = " + ycoord);
    doThisCellsNeighborCouplings(xcoord, ycoord);
  }


} // End class TwoDimCellSpace
