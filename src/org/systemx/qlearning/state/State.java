package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class State {

	private QValues qValues;
	private AgentCarState myCar;
	private List<AdjacentCarState> adjacentCars;
	private List<String> relatedStatesIds;
	String id;

	public State(State state) {
		qValues = new QValues();
		myCar = new AgentCarState();
		adjacentCars = new ArrayList<AdjacentCarState>();
		relatedStatesIds = new ArrayList<String>();

		id = state.getId();

		this.qValues = new QValues(state.qValues);
		this.myCar = new AgentCarState(state.myCar);

		for (int i = 0; i < state.adjacentCars.size(); i++) {
			adjacentCars.add(new AdjacentCarState(state.adjacentCars.get(i)));
		}

		for (int i = 0; i < state.relatedStatesIds.size(); i++) {
			relatedStatesIds.add(state.relatedStatesIds.get(i));
		}
	}

	public State(AgentCarState myCar, List<AdjacentCarState> adjacentCars) {
		super();
		this.qValues = new QValues();
		this.myCar = new AgentCarState();
		this.adjacentCars = new ArrayList<AdjacentCarState>();
		this.relatedStatesIds = new ArrayList<String>();

		this.myCar = new AgentCarState(myCar);

		for (int i = 0; i < adjacentCars.size(); i++) {
			this.adjacentCars.add(new AdjacentCarState(adjacentCars.get(i)));
		}

		this.id = calculateId();
	}

	public State(QValues qValues, AgentCarState myCar, List<AdjacentCarState> adjacentCars,
			List<String> relatedStates) {
		super();
		this.qValues = new QValues();
		this.myCar = new AgentCarState();
		this.adjacentCars = new ArrayList<AdjacentCarState>();
		this.relatedStatesIds = new ArrayList<String>();

		this.qValues = new QValues(qValues);
		this.myCar = new AgentCarState(myCar);

		for (int i = 0; i < adjacentCars.size(); i++) {
			this.adjacentCars.add(new AdjacentCarState(adjacentCars.get(i)));
		}

		for (int i = 0; i < relatedStates.size(); i++) {
			this.relatedStatesIds.add(relatedStates.get(i));
		}

		this.id = calculateId();
	}

	public QValues getQValues() {
		return qValues;
	}

	public void setQValues(QValues qValues) {
		this.qValues = qValues;
	}

	public AgentCarState getMyCar() {
		return myCar;
	}

	public void setMyCar(AgentCarState myCar) {
		this.myCar = myCar;
	}

	public List<AdjacentCarState> getAdjacentCars() {
		return adjacentCars;
	}

	public void setAdjacentCars(List<AdjacentCarState> adjacentCars) {
		this.adjacentCars = adjacentCars;
	}

	public List<String> getRelatedStatesIds() {
		return relatedStatesIds;
	}

	public void setRelatedStatesIds(List<String> relatedStatesIds) {
		this.relatedStatesIds = relatedStatesIds;
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

	public String getId() {
		return id;
	}

	// public void setId(String id) {
	// this.id = id;
	// }

	public List<Action> getPossibleActions(int numberOfLanes, int speedLimit, boolean withMisb) {
		List<Action> actions = new ArrayList<>();
		actions.add(Action.noAction);

		if (myCar.speed < speedLimit) {
			actions.add(Action.incSpeed);
		}

		if (myCar.speed > 0) {
			actions.add(Action.decSpeed);

			if (myCar.lane > 1) {
				actions.add(Action.goLeft);
			}

			if (myCar.lane < numberOfLanes) {
				actions.add(Action.goRight);
			}
		}

		if (withMisb) {
			boolean misbRight = false;
			boolean misbLeft = false;
			boolean misbFront = false;

			for (AdjacentCarState adjCar : adjacentCars) {
				if (adjCar.lane < 0 && adjCar.position > -10 && adjCar.speed > 1) {
					misbLeft = true;
				}
				if (adjCar.lane > 0 && adjCar.position > -10 && adjCar.speed > 1) {
					misbRight = true;
				}
				if (adjCar.lane == 0 && adjCar.position >= 0 && adjCar.speed > 1) {
					misbFront = true;
				}
			}

			if (misbFront) {
				actions.add(Action.misbFront);
			}
			if (misbLeft) {
				actions.add(Action.misbLeft);
			}
			if (misbRight) {
				actions.add(Action.misbRight);
			}
		}
		return actions;
	}

	private boolean matchState(State state) {
		if (state.getId().matches(id)) {
			return true;
		} else {
			return false;
		}
	}

	String calculateId() {
		String s = "" + myCar.lane + myCar.speed;

		Collections.sort(adjacentCars, new Comparator<AdjacentCarState>() {
			public int compare(AdjacentCarState c1, AdjacentCarState c2) {

				if (Math.abs(c1.lane) > Math.abs(c2.lane))
					return -1;
				if (Math.abs(c1.lane) < Math.abs(c2.lane))
					return 1;
				
				if (Math.abs(c1.lane) == Math.abs(c2.lane)) {
					if (Math.abs(c1.position) > Math.abs(c2.position))
						return -1;
					if (Math.abs(c1.position) < Math.abs(c2.position))
						return 1;

					if (Math.abs(c1.position) == Math.abs(c2.position)) {
						if (c1.speed > c2.speed)
							return -1;
						if (c1.speed < c2.speed)
							return 1;
					}
				}
				return 0;
			}
		});

		for (int i = 0; i < adjacentCars.size(); i++) {
			s = s + adjacentCars.get(i).lane + adjacentCars.get(i).position + adjacentCars.get(i).speed;
		}
		return s;
	}

	@Override
	public String toString() {
		return "State [myCar=" + myCar + ", AdjacentCars=" + adjacentCars + "]";
	}

}
