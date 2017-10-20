package org.systemx.project.control;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.systemx.project.BehaviorEnum;
import org.systemx.project.ProjectVehicle;
import org.systemx.qlearning.state.State;

public class Controller {

	static VehicleControl vc;
	static Boolean vcActive;
	static long vcid;

	static State currentState;

	public void checkNewVehicle(long id) {
		if (vcActive == null || !vcActive) {
			vcid = id;
			vc = new VehicleControl();
			vcActive = true;
			System.out.println("NEW===============================" + id);
		}
	}
	
	public void checkVehicleCrash(long id) {
		if (vcActive != false && vcid == id) {
			vcActive = false;
			vcid = -1;
			
			System.out.println("CRASHED===============================" + id);
		}
	}
	

	public void controlDecision(ProjectVehicle vehicle) {
		if (vcid == vehicle.getId()) {

			System.out.println(vehicle.getId() + " ====================================");
			List<Long> ids = new ArrayList<>(vehicle.getCommunicatingVehicles().keySet());
			for (int i = 0; i < ids.size(); i++) {
				System.out.println(vehicle.getCommunicatingVehicles().get(ids.get(i)));
			}

			vehicle.getCommunicatingVehicles().clear();
			
			if (vehicle.getDistanceToRoadSegmentEnd() < 30) {
				vcActive = false;
				return;
			}
			
			vehicle.setColorObject(Color.red);

			if (vc.acceleration == 1) {
				if (vehicle.getSpeed() < 13) {
					vehicle.modifyDesiredSpeed(-Double.MAX_VALUE);
					System.out.println("Speed: " + vehicle.getSpeed());
				} else {
					vehicle.modifyDesiredSpeed(0);
				}
				vc.acceleration = 0;
			} else if (vc.acceleration == -1) {
				vehicle.modifyDesiredSpeed(0.000000001);
				System.out.println("Speed: " + vehicle.getSpeed());
				vc.acceleration = 0;
			} else {
				vehicle.modifyDesiredSpeed(0);
			}

			// if (this.longitudinalModel instanceof IDM) {
			// IDM IDMModel = (IDM) this.longitudinalModel;
			// System.out.println("vc.acceleration = 0 : " + IDMModel.getDesiredSpeed() + "
			// " + getSpeed());
			// }

			if (vc.laneChange < 0) {
				vc.laneChange = 0;
				if (vehicle.getContinousLane() % 1 == 0) {
					if (vehicle.getLane() > 0) {
						vehicle.modifiedDesiredLane(BehaviorEnum.rightToleft);
					}
				}
			}

			if (vc.laneChange > 0) {
				vc.laneChange = 0;
				if (vehicle.getContinousLane() % 1 == 0) {
					vehicle.modifiedDesiredLane(BehaviorEnum.leftToright);
				}

			}

		}
	}

}
