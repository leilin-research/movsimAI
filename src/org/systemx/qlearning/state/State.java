package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.List;

import org.systemx.qlearning.commun.Action;
import org.systemx.qlearning.commun.QValues;

public class State {

	private QValues qValues;
	private CarState myCar;
	private List<CarState> AdjacentCars;
	private List<Integer> relatedStates;

	public State(State state) {
		qValues = new QValues();
		myCar = new CarState();
		AdjacentCars = new ArrayList<CarState>();
		relatedStates = new ArrayList<Integer>();
		
		this.qValues = new QValues(state.qValues);
		this.myCar = new CarState(state.myCar);

		for (int i = 0; i < state.AdjacentCars.size(); i++) {
			AdjacentCars.add(new CarState(state.AdjacentCars.get(i)));
		}
	}

	public State() {
		qValues = new QValues();
		myCar = new CarState();
		AdjacentCars = new ArrayList<CarState>();
		relatedStates = new ArrayList<Integer>();
	}
	

	public QValues getqValues() {
		return qValues;
	}

	public void setqValues(QValues qValues) {
		this.qValues = qValues;
	}

	public CarState getMyCar() {
		return myCar;
	}

	public void setMyCar(CarState myCar) {
		this.myCar = myCar;
	}

	public List<CarState> getAdjacentCars() {
		return AdjacentCars;
	}

	public void setAdjacentCars(List<CarState> adjacentCars) {
		AdjacentCars = adjacentCars;
	}

	public List<Integer> getRelatedStates() {
		return relatedStates;
	}

	public void setRelatedStates(List<Integer> relatedStates) {
		this.relatedStates = relatedStates;
	}

	public double getQValue(Action action) {
		return qValues.getValue(action);
	}

	public double getMaxQValue(List<Action> possibleActions) {
		return qValues.getMaxValue(possibleActions);
	}

	public void setQValue(Action action, double q) {
		qValues.setQValue(action, q);
	}

	public List<Action> getPossibleActions(int numberOfLanes, int speedLimit) {
		List<Action> actions = new ArrayList<>();
		actions.add(Action.noAction);

		if (myCar.lane != 0 && myCar.lane%1 == 0) {
			actions.add(Action.goLeft);
		}

		if (myCar.lane < numberOfLanes && myCar.lane%1 == 0) {
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
