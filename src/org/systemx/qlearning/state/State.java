package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.List;

public class State {

	private QValues qValues;
	private CarState myCar;
	private List<CarState> adjacentCars;
	private List<Integer> relatedStates;

	public State(State state) {
		qValues = new QValues();
		myCar = new CarState();
		adjacentCars = new ArrayList<CarState>();
		relatedStates = new ArrayList<Integer>();
		
		this.qValues = new QValues(state.qValues);
		this.myCar = new CarState(state.myCar);

		for (int i = 0; i < state.adjacentCars.size(); i++) {
			adjacentCars.add(new CarState(state.adjacentCars.get(i)));
		}
	}
	

	public State(QValues qValues, CarState myCar, List<CarState> adjacentCars, List<Integer> relatedStates) {
		super();
		this.qValues = new QValues();
		this.myCar = new CarState();
		this.adjacentCars = new ArrayList<CarState>();
		this.relatedStates = new ArrayList<Integer>();
		
		this.qValues = new QValues(qValues);
		this.myCar = new CarState(myCar);

		for (int i = 0; i < adjacentCars.size(); i++) {
			this.adjacentCars.add(new CarState(adjacentCars.get(i)));
		}
		
		for (int i = 0; i < relatedStates.size(); i++) {
			this.relatedStates.add(relatedStates.get(i));
		}
	}



	public State() {
		qValues = new QValues();
		myCar = new CarState();
		adjacentCars = new ArrayList<CarState>();
		relatedStates = new ArrayList<Integer>();
	}
	

	public QValues getQValues() {
		return qValues;
	}

	public void setQValues(QValues qValues) {
		this.qValues = qValues;
	}

	public CarState getMyCar() {
		return myCar;
	}

	public void setMyCar(CarState myCar) {
		this.myCar = myCar;
	}

	public List<CarState> getAdjacentCars() {
		return adjacentCars;
	}

	public void setAdjacentCars(List<CarState> adjacentCars) {
		this.adjacentCars = adjacentCars;
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
	
	public Action getMaxQValueAction(List<Action> possibleActions) {
		return qValues.getMaxValueAction(possibleActions);
	}

	public void setQValue(Action action, double q) {
		qValues.setQValue(action, q);
	}

	public List<Action> getPossibleActions(int numberOfLanes, int speedLimit) {
		List<Action> actions = new ArrayList<>();
		actions.add(Action.noAction);

		if (myCar.lane >1 ) {
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

	public boolean matchesState(State state) {
		if(myCar.matchesCarState(state.myCar)) {
			if(adjacentCars.size() == state.adjacentCars.size()) {
				for (int i = 0; i < adjacentCars.size(); i++) {
					if(!state.carAdjacent(adjacentCars.get(i))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	boolean carAdjacent(CarState car) {
		for (int i = 0; i < adjacentCars.size(); i++) {
			if(adjacentCars.get(i).matchesCarState(car)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "State [myCar=" + myCar + ", AdjacentCars=" + adjacentCars + "]";
	}
	
	

}
