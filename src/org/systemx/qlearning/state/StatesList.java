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

	public StatesList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void addState(String id, State state) {
		states.put(id, state);
	}

	public Action getMaxQNextAction(State testState) {
		State state = testState;
		if(states.containsKey(testState.getId())) {
			state = states.get(testState.getId());
		}
		
		List<Action> possibleActions = state.getPossibleActions(numberOfLanes, speedLimit);
		return state.getMaxQValueAction(possibleActions);
	}

	public Action predictNextActionOld(State state) {
		List<Action> possibleActions = state.getPossibleActions(numberOfLanes, speedLimit);
		ExpBetaSelector selector = new ExpBetaSelector(1);
		return selector.getExpBetaSelectedAction(state, possibleActions);
	}

	public Action predictNextAction(State state) {
		List<Action> possibleActions = state.getPossibleActions(numberOfLanes, speedLimit);
		Random rand = new Random();
		return possibleActions.get(rand.nextInt(possibleActions.size()));
	}

	public State setNextState(State currentState, State testState) {

		if (states.containsKey(testState.getId())) {
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
		states.put(testState.getId(),testState);
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
