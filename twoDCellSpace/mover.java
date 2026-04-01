package twoDCellSpace;

import java.awt.*;

import GenCol.*;
import genDevs.modeling.*;
import genDevs.plots.*;
import statistics.*;

public class mover
    extends TwoDimCell {
  protected Pair inpair;
  /**
   * Default constructor
   */
  public mover() {
    this(35, 35);
  }

  /**
   * Constructor
   */
  public mover(int xcoord, int ycoord) {
    super(new Pair(new Integer(xcoord), new Integer(ycoord)));
    name = "mover " + name; //must distinguish this name from others in space
    r = new rand(2);

    // Add ports not in TwoDimCell
    addInport("in");

    addTestInput("in", new Pair(new Integer(33), new Integer(35)));
    addTestInput("in", new Pair(new Integer( -1), new Integer(0)));
    addTestInput("in", new Pair(new Integer(34), new Integer(35)));
    addTestInput("in", new Pair(new Integer( -10), new Integer(20)));
  } // End cell constructor

  /**
   * Initialization method
   */
  public void initialize() {
    super.initialize();
    holdIn("waitForDir", 0);
  }

  public int[] getDirectionToward(Pair inpair) {
    Integer i = (Integer) inpair.getKey();
    Integer j = (Integer) inpair.getValue();
    int[] dir = {
        i.intValue() - xcoord,
        j.intValue() - ycoord
    };
    return dir;
  }

  public int[] getDirectionSame(Pair inpair) {
    int[] dir = getDirectionToward(inpair);
    int[] rdir = {
         -dir[0], -dir[1]};
    return rdir;
  }

  public int[] advanceCoord(int[] dir) {
    int[] temp = {
        xcoord + dir[0],
        ycoord + dir[1]
    };
    return temp;
  }

  public int[] advanceCoord(Pair inpair) {
    return advanceCoord(getDirectionToward(inpair));
  }

  public void changeCoordNPos(int[] dir) {
    setCoordNPos(xcoord + dir[0], ycoord + dir[1]);
  }

  public void move(Pair inpair, int step) {
    int[] dir = getDirectionToward(inpair);
    dir[0] = step * dir[0];
    dir[1] = step * dir[1];
    changeCoordNPos(dir);
  }

  /**
   * External Transition Function
   */
  public void deltext(double e, message x) {
    Continue(e);
    // if (phaseIs("moving")||phaseIs("output"))return;
    if (somethingOnPort(x, "start")) {
      holdIn("active", 0);
    }
    else if (somethingOnPort(x, "stop")) {
      passivate();
    }
    else
    if (phaseIs("waitForDir")) {
      int dist = 1000000;
      for (int i = 0; i < x.getLength(); i++) {
        if (messageOnPort(x, "in", i)) {
          Pair inp = (Pair) x.getValOnPort("in", i);
          if (isMooreNeighbor(inp)) {
            //inpair = inp;

            int[] temp = pairToArray(inp);
            if (temp[0] * temp[0] + temp[1] * temp[1] < dist) {
              dist = temp[0] * temp[0] + temp[1] * temp[1];
              inpair = inp;
            }
            holdIn("output", 0);
          }
        }
      }
    }
  } // End deltext()

  /*
   * Internal Transition Function
   */

  public void deltint() {
    if (phaseIs("output")) {
      holdIn("moving", 0);
    }
    else if (phaseIs("moving")) {
      move(inpair, 2);
      inpair = null;
      if (xcoord <= 2 && ycoord <= 2) {
        passivate();
      }
      else {
        passivateIn("waitForDir");
      }
    }
    else if (phaseIs("waitForDir")) {
      passivateIn("waitForDir");
    }
  }

  public void deltcon(double e, message x) {
    deltext(e, x);
    deltint();
  }

  /*
   * Message out Function
   */
  public message out() {
    message m = super.out();
    if (phaseIs("output") || phaseIs("waitForDir")) {
      m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
          x_pos, y_pos, Color.red, Color.orange)));
    }
    return m;
  }

} // End class mover
