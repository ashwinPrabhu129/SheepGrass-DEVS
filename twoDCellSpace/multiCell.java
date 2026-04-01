package twoDCellSpace;

import GenCol.*;
import genDevs.modeling.*;

public class multiCell
    extends TwoDimCell {
  protected TwoDimCell myModel;
  protected String nextModel = " ", lastModel = " ";

  public multiCell() {
    this(0, 0);
  }

  public double ta() {
    return myModel.ta();
  }

  public multiCell(int xcoord, int ycoord) {
    super(new Pair(new Integer(xcoord), new Integer(ycoord)));

    if (xcoord == 10 && ycoord == 0) {
      myModel = new infectedCell(xcoord, ycoord);
      nextModel = lastModel = "infectedCell";
      ( (infectedCell) myModel).source = true;
    }
    else if (xcoord == 19 && ycoord == 19) {
      myModel = new antiCell(xcoord, ycoord);
      nextModel = lastModel = "antiCell";
      ( (antiCell) myModel).intiallyDormant = true;
    }

    else if (xcoord == 0 && ycoord == 10) {
      myModel = new sentinelCell(xcoord, ycoord);
      nextModel = lastModel = "sentinelCell";
      ( (sentinelCell) myModel).source = true;
    }
    else if (xcoord == 19 && ycoord == 10) {
      myModel = new sentinelCell(xcoord, ycoord);
      nextModel = lastModel = "sentinelCell";
      ( (sentinelCell) myModel).sink = true;
    }
    else {
      myModel = new normalCell(xcoord, ycoord);
      nextModel = lastModel = "normalCell";

    }
    initializeMyModel();
    // Add ports not in TwoDimCell
  }

  public void initializeMyModel() {
    myModel.myMultiCell = this;
    myModel.initialize();
    myModel.xDimCellspace = xDimCellspace;
    myModel.yDimCellspace = yDimCellspace;
    myModel.setCoordNPos(xcoord, ycoord);

  }

  public void changeModel() {
    myModel.myMultiCell = this;
    if (!nextModel.equals(lastModel)) {
      if (nextModel.equals("infectedCell")) {
        myModel = new infectedCell(xcoord, ycoord);
      }
      else if (nextModel.equals("normalCell")) {
        myModel = new normalCell(xcoord, ycoord);
      }
      else if (nextModel.equals("antiCell")) {
        myModel = new antiCell(xcoord, ycoord);

      }
      lastModel = nextModel;
      initializeMyModel();
    }
  }

  public void initialize() {
    initializeMyModel();
  }

  public void deltext(double e, message x) {
    myModel.deltext(e, x);
    changeModel();
  } // End deltext()

  public void deltint() {
    myModel.deltint();
    changeModel();
  }

  public void deltcon(double e, message x) {
    deltint();
  }

  /*
   * Message out Function
   */
  public message out() {
    message m = super.out();
    m.addAll(myModel.out());
    return m;
  }

  public String getTooltipText() {
    return
        super.getTooltipText()
        + "\n" + " phase " + myModel.getPhase()
        + "\n" + " sigma " + myModel.getSigma();
  }

} // End class multiCell
