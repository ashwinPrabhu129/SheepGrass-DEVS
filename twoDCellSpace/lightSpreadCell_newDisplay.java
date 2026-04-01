package twoDCellSpace;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import genDevs.plots.*;
import java.util.*;
import java.awt.*;
import java.text.*;
import java.io.*;
import statistics.*;
import quantization.*;

public class lightSpreadCell_newDisplay extends TwoDimCell {

	newCellGridView cellGridView;

	public boolean barrier = false;
	public boolean source = false, visible = true;
	public boolean sink = false;
	protected double period = 10;

	public void barrierRect(int bottx, int botty, int length, int width) {
		if (xcoord >= bottx && xcoord <= bottx + length
				&&
				ycoord >= botty && ycoord <= botty + width) {
			barrier = true;
		}
	}

	public void barrierRect(double x, double y, int bottx, int botty, int length,
			int width) {
		if (x >= bottx && x <= bottx + length
				&&
				y >= botty && y <= botty + width) {
			barrier = true;
		}
	}

	public void barrierRectRotate(double angle, int bottx, int botty, int length,
			int width) {
		vect2DEnt v = new vect2DEnt(xcoord, ycoord);
		v = v.rotate(angle);
		barrierRect(v.x, v.y, bottx, botty, length, width);
	}

	public void barrierCircle(int cx, int cy, int radius) {
		if (
				(xcoord - cx) * (xcoord - cx)
				+ (ycoord - cy) * (ycoord - cy)
				<= radius * radius
				) {
			barrier = true;
		}
	}

	/**
	 * Default constructor
	 */
	 public lightSpreadCell_newDisplay() {
		this(0, 0);
	}

	/**
	 * Constructor
	 */
	 public lightSpreadCell_newDisplay(int xcoord, int ycoord) {
		 super(new Pair(new Integer(xcoord), new Integer(ycoord)));

		 // Add ports not in TwoDimCell
	 } // End cell constructor

	 /**
	  * Initialization method
	  */
	 public void initialize() {

		 cellGridView = ((lightSpreadCellSpace_newDisplay)getParent()).plot.getCellGridView();

		 if (barrier) {
			 cellGridView.drawCellToScale(x_pos, y_pos, Color.black);      
			 passivateIn("refractive");
		 }
		 else if (source) {
			 cellGridView.drawCellToScale(x_pos, y_pos, Color.blue);      
			 holdIn("output", 0);
		 }
		 else if (sink) {
			 cellGridView.drawCellToScale(x_pos, y_pos, Color.green);      
			 holdIn("measure", period);
		 }
		 else {
			 cellGridView.drawCellToScale(x_pos, y_pos, Color.white);      
			 passivateIn("receptive");
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
		 else { // Message from neighbor cell
			 if (sink && phaseIs("measure")) {
				 System.out.println("TRAVEL TIME IS " + e);
				 passivateIn("refractive");
			 }
			 else if (phaseIs("receptive")) {
				 cellGridView.drawCellToScale(x_pos, y_pos, Color.blue);      
				 holdIn("output", 0.1);
			 }
		 } //
	 } // End deltext()

	 /*
	  * Internal Transition Function
	  */

	 public void deltint() {
		 if (phaseIs("output")) {
				 holdIn("refractive", 1);
		 }
		 else if (phaseIs("refractive")) {
			 if (source) {
				 holdIn("output", period - 1);
			 }
			 else {
				 passivateIn("receptive");
			 }
		 }
		 else if (phaseIs("measure")) {
			 holdIn("measure", period);
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
			 if (visible) {
				 if(!source)
					 cellGridView.drawCellToScale(x_pos, y_pos, Color.white);      
			 }
			 if (!sink) {
				 m.add(makeContent("outN",
						 new Pair(new Integer(xcoord), new Integer(ycoord))));

				 //   m.add(makeContent("outNE",new Pair(new Integer(xcoord), new Integer(ycoord))));

				 m.add(makeContent("outE",
						 new Pair(new Integer(xcoord), new Integer(ycoord))));

				 //   m.add(makeContent("outSE",new Pair(new Integer(xcoord), new Integer(ycoord))));

				 m.add(makeContent("outS",
						 new Pair(new Integer(xcoord), new Integer(ycoord))));

				 //  m.add(makeContent("outSW",new Pair(new Integer(xcoord), new Integer(ycoord))));

				 m.add(makeContent("outW",
						 new Pair(new Integer(xcoord), new Integer(ycoord))));

				 //   m.add(makeContent("outNW",new Pair(new Integer(xcoord), new Integer(ycoord))));

			 }
		 }
		 return m;
	 }

} // End class lightSpreadCell
