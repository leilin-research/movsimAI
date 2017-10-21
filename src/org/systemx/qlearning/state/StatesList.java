package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
