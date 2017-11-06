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
	private boolean withMisb;

	public StatesList(int numberOfLanes, int speedLimit, boolean withMisb) {
		super();
		this.numberOfLanes = numberOfLanes;
		this.speedLimit = speedLimit;
		this.withMisb = withMisb;
	}

	public StatesList(StatesList statesList) {
		super();
		this.numberOfLanes = statesList.getNumberOfLanes();
		this.speedLimit = statesList.getSpeedLimit();
		this.withMisb = statesList.isWithMisb();

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

		List<Action> possibleActions = state.getPossibleActions(numberOfLanes, speedLimit, withMisb);
		return state.getMaxQValueAction(possibleActions);
	}

	public Action predictNextAction(State state) {
		List<Action> possibleActions = state.getPossibleActions(numberOfLanes, speedLimit, withMisb);
		ExpBetaSelector selector = new ExpBetaSelector(1);
		return selector.getExpBetaSelectedAction(state, possibleActions);
	}

	public Action predictSmartNextAction(State state) {
		if (state.getQValues().getQList().size() > 0) {
			return predictNextAction(state);
		} else {
			List<String> possibleStates = getFullPossibleStates(state);
			if (possibleStates.size() > 0) {
				Action maxAction = null;
				Double maxQ = -Double.MAX_VALUE;
				for (String StateId : possibleStates) {
					List<Action> possibleActions = states.get(StateId).getPossibleActions(numberOfLanes, speedLimit,
							withMisb);
					Double localMaxQ = states.get(StateId).getMaxQValue(possibleActions);
					if (localMaxQ > maxQ) {
						maxQ = localMaxQ;
						maxAction = states.get(StateId).getMaxQValueAction(possibleActions);
					}
				}
				if (maxQ > 0) {
					System.out.println("#################SmartAction:" + maxAction + " " + maxQ);
					return maxAction;
				} else {
					return predictNextAction(state);
				}

			} else {
				return predictNextAction(state);
			}
		}
	}

	public List<String> getFullPossibleStates(State state) {
		List<String> fullPossibleStatesIds = new ArrayList<String>();
		State lanePossibleState;
		for (int i = 1; i <= numberOfLanes; i++) {
			lanePossibleState = new State(state);
			lanePossibleState.getMyCar().lane = i;

			for (int j = 0; j < lanePossibleState.getAdjacentCars().size(); j++) {
				if (lanePossibleState.getAdjacentCars().get(j).lane + i < 1
						|| lanePossibleState.getAdjacentCars().get(j).lane + i > numberOfLanes) {
					lanePossibleState.getAdjacentCars().remove(j);
					j--;
				}
			}
			fullPossibleStatesIds.addAll(getPossibleStates(lanePossibleState));
		}
		return fullPossibleStatesIds;
	}

	public List<String> getPossibleStates(State state) {
		List<String> possibleStatesIds = new ArrayList<String>();
		State possibleState;
		state.id = state.calculateId();
		if (states.containsKey(state.id)) {
			possibleStatesIds.add(state.id);
		}

		List<AgentCarState> possibleAgentCars = state.getMyCar().possibleAdj(numberOfLanes, speedLimit);
		for (AgentCarState myCar : possibleAgentCars) {
			possibleState = new State(state);
			possibleState.setMyCar(myCar);
			possibleState.id = possibleState.calculateId();
			if (states.containsKey(possibleState.id)) {
				possibleStatesIds.add(possibleState.id);
			}
		}

		List<AdjacentCarState> possibleAdjCars;
		for (int i = 0; i < state.getAdjacentCars().size(); i++) {
			possibleAdjCars = state.getAdjacentCars().get(i).possibleAdj(numberOfLanes, speedLimit);
			for (AdjacentCarState possibleCar : possibleAdjCars) {
				possibleState = new State(state);
				possibleState.getAdjacentCars().set(i, possibleCar);
				possibleState.id = possibleState.calculateId();
				if (states.containsKey(possibleState.id)) {
					possibleStatesIds.add(possibleState.id);
				}
			}
		}

		if (state.getAdjacentCars().size() > 1) {
			possibleState = new State(state);
			for (int i = 0; i < state.getAdjacentCars().size(); i++) {
				AdjacentCarState carState = state.getAdjacentCars().get(i);
				carState.position++;
				possibleState.getAdjacentCars().set(i, new AdjacentCarState(carState));
			}
			possibleState.id = possibleState.calculateId();
			if (states.containsKey(possibleState.id)) {
				possibleStatesIds.add(possibleState.id);
			}

			possibleState = new State(state);
			for (int i = 0; i < state.getAdjacentCars().size(); i++) {
				AdjacentCarState carState = state.getAdjacentCars().get(i);
				carState.position--;
				possibleState.getAdjacentCars().set(i, new AdjacentCarState(carState));
			}
			possibleState.id = possibleState.calculateId();
			if (states.containsKey(possibleState.id)) {
				possibleStatesIds.add(possibleState.id);
			}
		}

		List<String> actionableStates = new ArrayList<String>();
		for (int i = 0; i < possibleStatesIds.size(); i++) {
			if (states.get(possibleStatesIds.get(i)).getQValues().qList.size() > 0) {
				actionableStates.add(possibleStatesIds.get(i));
			}
		}

		return actionableStates;
	}

	public Action predictRandomNextAction(State state) {
		List<Action> possibleActions = state.getPossibleActions(numberOfLanes, speedLimit, withMisb);
		Random rand = new Random();
		return possibleActions.get(rand.nextInt(possibleActions.size()));
	}

	public State setNextState(State currentState, State testState) {

		if (states.containsKey(testState.getId())) {
			if (!currentState.getRelatedStatesIds().contains(testState.getId())) {
				currentState.getRelatedStatesIds().add(testState.getId());
			}
			if (!testState.getRelatedStatesIds().contains(currentState.getId())) {
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

	public boolean isWithMisb() {
		return withMisb;
	}

	public void setWithMisb(boolean withMisb) {
		this.withMisb = withMisb;
	}

}
