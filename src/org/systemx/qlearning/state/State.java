package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.List;

import org.systemx.qlearning.commun.Action;
import org.systemx.qlearning.commun.QValues;

public class State {

	public QValues qValues;

	public CarState myCar;
	public List<CarState> AdjacentCars = new ArrayList<CarState>();
	
	public List<Integer> relatedStates = new ArrayList<Integer>();

	public State(State state) {
		this.qValues = new QValues(state.qValues);
		this.myCar = new CarState(state.myCar);

		for (int i = 0; i < state.AdjacentCars.size(); i++) {
			AdjacentCars.add(new CarState(state.AdjacentCars.get(i)));
		}
	}

	public State() {
		// TODO Auto-generated constructor stub
	}

	public double getQValue(Action action) {
		return qValues.getValue(action);
	}

	public double getMaxQValue() {
		return qValues.getMaxValue();
	}

	public void setQValue(Action action, double q) {
		qValues.setQValue(action, q);
	}

	public List<Action> getPossibleActions(int numberOfLanes, int speedLimit) {
		List<Action> actions = new ArrayList<>();
		actions.add(Action.noAction);

		if (myCar.lane != 0) {
			actions.add(Action.goLeft);
		}

		if (myCar.lane < numberOfLanes) {
			actions.add(Action.goRight);
		}

		if (myCar.speed < speedLimit) {
			actions.add(Action.incSpeed);
		}

		if (myCar.speed > 0) {
			actions.add(Action.decSpeed);
		}
		return actions;
	}

	State getNextState(Action action) {
		State nextState = this;
		switch (action) {
		case goRight:
			nextState.myCar.lane++;
			break;
		case goLeft:
			nextState.myCar.lane--;
			break;
		case incSpeed:
			nextState.myCar.speed++;
			break;
		case decSpeed:
			nextState.myCar.speed--;
			break;
		case noAction:
			break;
		default:
			break;
		}
		return nextState;
	}

	boolean matchesState(State state) {
		if(myCar.matchesCarState(state.myCar)) {
			if(AdjacentCars.size() == state.AdjacentCars.size()) {
				for (int i = 0; i < AdjacentCars.size(); i++) {
					if(!state.carAdjacent(AdjacentCars.get(i))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	boolean carAdjacent(CarState car) {
		for (int i = 0; i < AdjacentCars.size(); i++) {
			if(AdjacentCars.get(i).matchesCarState(car)) {
				return true;
			}
		}
		
		return false;
	}

}
