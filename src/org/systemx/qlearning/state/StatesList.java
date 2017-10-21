package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.systemx.qlearning.commun.ExpBetaSelector;

public class StatesList {

	private List<State> states = new ArrayList<State>();

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


	public void addState(State state) {
		states.add(state);
	}
	
	public Action getMaxQNextAction(State testState) {
		State state = testState;
		for (int i = 0; i < states.size(); i++) {
			if(states.get(i).matchesState(testState)) {
				state = states.get(i);
			}				
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

		if(testState.matchesState(currentState)) {
			return currentState;
		}
		
		for (int i = 0; i < currentState.getRelatedStates().size(); i++) {
			if (testState.matchesState(states.get(currentState.getRelatedStates().get(i)))) {
				return states.get(currentState.getRelatedStates().get(i));
			}
		}
		
		for (int i = 0; i < states.size(); i++) {
			if(states.get(i).matchesState(testState)) {
				states.get(i).getRelatedStates().add(states.indexOf(currentState));
				currentState.getRelatedStates().add(i);
				return states.get(i);
			}				
		}

		State nextState = new State(testState);
		nextState.getRelatedStates().add(states.indexOf(currentState));
		states.add(nextState);
		currentState.getRelatedStates().add(states.indexOf(nextState));

		return nextState;
	}
	
	public State addNextState(State testState) {		
		for (int i = 0; i < states.size(); i++) {
			if(states.get(i).matchesState(testState)) {
				return states.get(i);
			}				
		}
		states.add(testState);
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

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}


}
