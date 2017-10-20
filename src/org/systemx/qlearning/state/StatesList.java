package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.List;

import org.systemx.qlearning.commun.Action;
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

	public void addState(State state) {
		states.add(state);
	}
	
	public Action predictNextAction(State state) {
		List<Action> possibleActions = state.getPossibleActions(numberOfLanes, speedLimit);
		ExpBetaSelector selector = new ExpBetaSelector(1);
		return selector.getExpBetaSelectedAction(state, possibleActions);
	}
	
//	public State getNextState(State state, Action action) {
//		State testState = state.getNextState(action);
//		
//		for (int i = 0; i < state.getRelatedStates().size(); i++) {
//			if(testState.matchesState(states.get(state.getRelatedStates().get(i)))) {
//				return states.get(state.getRelatedStates().get(i));
//			}
//		}
//		
//		State nextState = new State(testState);
//		nextState.getRelatedStates().add(states.indexOf(state));
//		states.add(nextState);
//		state.getRelatedStates().add(states.indexOf(nextState));
//
//		return nextState;
//	}
	
	public State setNextState(State currentState, State testState) {
		
	for (int i = 0; i < currentState.getRelatedStates().size(); i++) {
		if(testState.matchesState(states.get(currentState.getRelatedStates().get(i)))) {
			return states.get(currentState.getRelatedStates().get(i));
		}
	}
	
	State nextState = new State(testState);
	nextState.getRelatedStates().add(states.indexOf(currentState));
	states.add(nextState);
	currentState.getRelatedStates().add(states.indexOf(nextState));

	return nextState;
}

}
