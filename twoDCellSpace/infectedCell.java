package twoDCellSpace;

import java.awt.*;

import GenCol.*;
import genDevs.modeling.*;
import genDevs.plots.*;

public class infectedCell
    extends TwoDimCell {
  protected boolean source = false;
  protected double infectTime = 10, revertTime = INFINITY, reviveTime = 40;
  /**
   * Default constructor
   */
  public infectedCell() {
    this(0, 0);

  }

  /**
   * Constructor
   */
  public infectedCell(int xcoord, int ycoord) {
    super(new Pair(new Integer(xcoord), new Integer(ycoord)));
    addInport("infectinS");
    addOutport("infectoutN");

    // Add ports not in TwoDimCell

    addTestInput("infectinS", new entity());
  } // End cell constructor

  /**
   * Initialization method
   */
  public void initialize() {
    super.initialize();
    holdIn("output", 0);
  }

  /**
   * External Transition Function
   */
  public void deltext(double e, message x) {
    Continue(e);
    if (somethingOnPort(x, "start")) {
      holdIn("active", 0);
    }
    else if (somethingOnPort(x, "stop")) {
      passivate();
    }
    else
    if (somethingOnPortType(x, "infect")) {
      //from self stay in same state
    }
    else if (somethingOnPortType(x, "anti")) {
      if (source) {
        holdIn("output", reviveTime);
      }
      else {
        myMultiCell.nextModel = "antiCell";

      }
    }
  } // End deltext()

  /*
   * Internal Transition Function
   */

  public void deltint() {
    if (phaseIs("output")) {
      holdIn("infect", infectTime);
    }
    else if (phaseIs("infect")) {
      holdIn("revert", revertTime);
    }
    else if (phaseIs("revert")) {
      //  myMultiCell.setModel("normalCell");
      myMultiCell.nextModel = "normalCell";
      passivate();
    }
  }

  public void deltcon(double e, message x) {
    deltint();
  }

  /*
   * Message out Function
   */
  public message out() {
    message m = super.out();
    if (phaseIs("output")) {
      m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
          x_pos, y_pos, Color.black, Color.black)));
    }
    else if (phaseIs("infect")) {
      m = propagate(m, "infect");
    }
    return m;
  }

} // End class infectedCell
