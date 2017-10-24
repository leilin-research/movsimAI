package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.systemx.qlearning.commun.ExpBetaSelector;

public class StatesList {

	Map<String, State> states = new HashMap<String, State>();

	private int numberOfLanes;
	private int speedLimit;

	public StatesList(int numberOfLanes, int speedLimit) {
		super();
		this.numberOfLanes = numberOfLanes;
		this.speedLimit = speedLimit;
	}

	public StatesList(StatesList statesList) {
		super();
		this.numberOfLanes = statesList.getNumberOfLanes();
		this.speedLimit = statesList.getSpeedLimit();

		states = new HashMap<String, State>();

		List<String> keySet = new ArrayList<String>(statesList.getStates().keySet());

		for (int i = 0; i < keySet.size(); i++) {
			states.put(keySet.get(i), new State(statesList.getStates().get(keySet.get(i))));
		}
	}

	public StatesList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void addState(String id, State state) {
		states.put(id, state);
	}

	public Action getMaxQNextAction(State testState) {
		State state = testState;
		if (states.containsKey(testState.getId())) {
			state = states.get(testState.getId());
		}

		List<Action> possibleActions = state.getPossibleActions(numberOfLanes, speedLimit);
		return state.getMaxQValueAction(possibleActions);
	}

	public Action predictNextAction(State state) {
		List<Action> possibleActions = state.getPossibleActions(numberOfLanes, speedLimit);
		ExpBetaSelector selector = new ExpBetaSelector(1);
		return selector.getExpBetaSelectedAction(state, possibleActions);
	}

	public Action predictSmartNextAction(State state) {
		if (state.getQValues().getQList().size()>0) {
			return predictNextAction(state);
		}else {
			List<String> adjacentStates = getAdjacentStates(state);
			if(adjacentStates.size()>0) {
				Action maxAction = null;
				Double maxQ = - Double.MAX_VALUE;
				for (String StateId: adjacentStates) {
					List<Action> possibleActions = states.get(StateId).getPossibleActions(numberOfLanes, speedLimit);
					Double localMaxQ = states.get(StateId).getMaxQValue(possibleActions);
					if(localMaxQ > maxQ) {
						maxQ = localMaxQ;
						maxAction = states.get(StateId).getMaxQValueAction(possibleActions);
					}
				}
				if(maxQ>0) {
					System.out.println("#################SmartAction:" + maxAction + " " +maxQ);
					return maxAction;
				}else {
					return predictNextAction(state);
				}

			}else {
				return predictNextAction(state);
			}
		}
	}

	public List<String> getAdjacentStates(State state) {
		List<String> adjacentStatesIds = new ArrayList<String>();
		State adjState;

		List<CarState> possibleAdjCars = state.getMyCar().possibleAdj(numberOfLanes, speedLimit, true);
		for (CarState myCar : possibleAdjCars) {
			adjState = new State(state);
			adjState.setMyCar(myCar);
			adjState.id = adjState.calculateId();
			if (states.containsKey(adjState.id)) {
				adjacentStatesIds.add(adjState.id);
			}
		}

		for (int i = 0; i< state.getAdjacentCars().size();i++) {
			possibleAdjCars = state.getAdjacentCars().get(i).possibleAdj(numberOfLanes, speedLimit, false);
			for (CarState possibleCar : possibleAdjCars) {
				adjState = new State(state);
				adjState.getAdjacentCars().set(i, possibleCar);
				adjState.id = adjState.calculateId();
				if (states.containsKey(adjState.id)) {
					adjacentStatesIds.add(adjState.id);
				}
			}
		}
		
		if(state.getAdjacentCars().size()>1) {
			adjState = new State(state);
			for (int i = 0; i< state.getAdjacentCars().size();i++) {
				CarState carState = state.getAdjacentCars().get(i);
				carState.position++;
				adjState.getAdjacentCars().set(i, new CarState(carState));
			}
			adjState.id = adjState.calculateId();
			if (states.containsKey(adjState.id)) {
				adjacentStatesIds.add(adjState.id);
			}
			
			adjState = new State(state);
			for (int i = 0; i< state.getAdjacentCars().size();i++) {
				CarState carState = state.getAdjacentCars().get(i);
				carState.position--;
				adjState.getAdjacentCars().set(i, new CarState(carState));
			}
			adjState.id = adjState.calculateId();
			if (states.containsKey(adjState.id)) {
				adjacentStatesIds.add(adjState.id);
			}
		}		
		return adjacentStatesIds;
	}

	public Action predictRandomNextAction(State state) {
		List<Action> possibleActions = state.getPossibleActions(numberOfLanes, speedLimit);
		Random rand = new Random();
		return possibleActions.get(rand.nextInt(possibleActions.size()));
	}

	public State setNextState(State currentState, State testState) {

		if (states.containsKey(testState.getId())) {
			if(!currentState.getRelatedStatesIds().contains(testState.getId())) {
				currentState.getRelatedStatesIds().add(testState.getId());
			}
			if(!testState.getRelatedStatesIds().contains(currentState.getId())) {
				testState.getRelatedStatesIds().add(currentState.getId());
			}
			return states.get(testState.getId());
		}
		
		State nextState = new State(testState);
		nextState.getRelatedStatesIds().add(currentState.getId());
		states.put(nextState.getId(), nextState);
		currentState.getRelatedStatesIds().add(nextState.getId());

		return nextState;
	}

	public State addNewState(State testState) {
		if (states.containsKey(testState.getId())) {
			return states.get(testState.getId());
		}
		states.put(testState.getId(), testState);
		return testState;
	}

	public int getNumberOfLanes() {
		return numberOfLanes;
	}

	public void setNumberOfLanes(int numberOfLanes) {
		this.numberOfLanes = numberOfLanes;
	}

	public int getSpeedLimit() {
		return speedLimit;
	}

	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}

	public Map<String, State> getStates() {
		return states;
	}

	public void setStates(Map<String, State> states) {
		this.states = states;
	}

}
