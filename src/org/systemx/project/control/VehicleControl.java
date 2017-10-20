package org.systemx.project.control;

public class VehicleControl {

    public static int acceleration;
    public static int laneChange;

    public void moveRight() {
    	laneChange = 1;
    }

    public void moveLeft() {
    	laneChange = -1;
    }

    public void moveDown() {
    	acceleration = -1;
    }

    public void moveUp() {
    	acceleration = 1;
    }

	@Override
	public String toString() {
		return "vehicleControl [acceleration="+acceleration+" lane="+laneChange+"]";
	}
	
    
}
