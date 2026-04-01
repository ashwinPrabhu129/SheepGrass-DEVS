package twoDCellSpace;

import java.awt.*;

import GenCol.*;
import genDevs.modeling.*;
import genDevs.plots.*;

public class antiCell
    extends TwoDimCell {
  protected double propTime = 1, revertTime = 30;
  protected boolean intiallyDormant = false;
  /**
   * Default constructor
   */
  public antiCell() {
    this(0, 0);

  }

  /**
   * Constructor
   */
  public antiCell(int xcoord, int ycoord) {
    super(new Pair(new Integer(xcoord), new Integer(ycoord)));
    // Add ports not in TwoDimCell

  } // End cell constructor

  /**
   * Initialization method
   */
  public void initialize() {
    super.initialize();
    if (intiallyDormant) {
      passivate();
    }
    else {
      holdIn("output", propTime);
    }
  }

  /**
   * External Transition Function
   */
  public void deltext(double e, message x) {
    Continue(e);
    if (somethingOnPort(x, "start")) {
      holdIn("output", 0);
    }
    else if (somethingOnPort(x, "stop")) {
      passivate();
    }

  } // End deltext()

  /*
   * Internal Transition Function
   */

  public void deltint() {
    if (phaseIs("output")) {
      holdIn("revert", revertTime);
    }
    else if (phaseIs("revert")) {
      if (intiallyDormant) {
        passivate();
      }
      else {
        myMultiCell.nextModel = "normalCell";
      }
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
          x_pos, y_pos, Color.blue, Color.blue)));
      m = propagate(m, "anti");
    }
    return m;
  }

} // End class antiCell
