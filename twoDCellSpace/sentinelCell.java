package twoDCellSpace;

import java.awt.*;

import GenCol.*;
import genDevs.modeling.*;
import genDevs.plots.*;

public class sentinelCell
    extends TwoDimCell {
  public boolean source = false, sink = false;
  protected double period = 4, threshold = 2.1, savedSigma;

  /**
   * Constructor
   */
  public sentinelCell(int xcoord, int ycoord) {
    super(new Pair(new Integer(xcoord), new Integer(ycoord)));

    // Add ports not in TwoDimCell

  } // End cell constructor

  /**
   * Initialization method
   */
  public void initialize() {
    super.initialize();
    if (source || sink) {
      holdIn("output", 0);
    }
    else {
      passivate();
    }
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
    else if (somethingOnPortType(x, "ping")) {
      // Message from neighbor cell
      if (sink) {
        //System.out.println("TRAVEL TIME IS "+e);
        if (e > threshold) {
          savedSigma = sigma;
          holdIn("notify", 0);
        }
        else {
          holdIn("respond", sigma);
        }
      }
    }
  } // End deltext()

  /*
   * Internal Transition Function
   */

  public void deltint() {
    if (phaseIs("output")) {
      if (sink) {
        holdIn("measure", period);
      }
      else { //if (source)
        holdIn("output", period);
      }
    }
    else if (phaseIs("respond")) {
      holdIn("measure", period);
    }
    else if (phaseIs("measure")) {
      holdIn("measure", period);
      //if (sink)System.out.println("TIMED OUT "+sigma);
    }
    else if (phaseIs("notify")) {
      holdIn("measure", savedSigma);
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
    if (phaseIs("output") || phaseIs("measure")) {
      if (source) {
        m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
            x_pos, y_pos, Color.orange, Color.orange)));
        m = propagate(m, "ping");
      }
      if (sink) {
        m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
            x_pos, y_pos, Color.green, Color.green)));
      }
    }
    if (phaseIs("notify") && sink) {
      m.add(makeContent("start", new entity("go")));
      m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
          x_pos, y_pos, Color.red, Color.red)));
    }

    return m;
  }

} // End class sentinelCell
