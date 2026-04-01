package twoDCellSpace;

import java.awt.*;

import GenCol.*;
import genDevs.modeling.*;
import genDevs.plots.*;

public class normalCell
    extends TwoDimCell {
  protected static double propTime = .1;
  /**
   * Default constructor
   */
  public normalCell() {
    this(0, 0);

  }

  /**
   * Constructor
   */
  public normalCell(int xcoord, int ycoord) {
    super(new Pair(new Integer(xcoord), new Integer(ycoord)));
    // Add ports not in TwoDimCell

  } // End cell constructor

  /**
   * Initialization method
   */
  public void initialize() {
    super.initialize();
    passivateIn("receptive");
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
    else if (somethingOnPortType(x, "infect")) {

      //   myMultiCell.setModel("infectedCell");
      myMultiCell.nextModel = "infectedCell";

    }
    else if (somethingOnPortType(x, "anti")) {
      // Message from antiCell
      //myMultiCell.setModel("antiCell");
      myMultiCell.nextModel = "antiCell";
    }
    else if (somethingOnPortType(x, "ping")) {
      if (phaseIs("receptive")) {
        holdIn("output", propTime);
      }
    }

  } // End deltext()

  /*
   * Internal Transition Function
   */

  public void deltint() {
    if (phaseIs("output")) {
      holdIn("refractive", 4 * propTime);
    }
    else if (phaseIs("refractive")) {
      passivateIn("receptive");
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
          x_pos, y_pos, Color.yellow, Color.white)));
      m = propagate(m, "ping");
    }
    return m;
  }

} // End class normalCell
