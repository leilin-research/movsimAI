package org.systemx.qlearning.state;

import java.util.ArrayList;
import java.util.List;

import org.systemx.qlearning.commun.Action;

public class StatesList {
	
	List<State> states = new ArrayList<State>();
	
	public void addState(State state) {
		states.add(state);
	}
	
	public State getNextState(State state, Action action) {
		State testState = state.getNextState(action);
		for (int i = 0; i < state.relatedStates.size(); i++) {
			if(testState.matchesState(states.get(state.relatedStates.get(i)))) {
				return states.get(state.relatedStates.get(i));
			}
		}
		
		State nextState = new State(testState);
		nextState.relatedStates.add(states.indexOf(state));
		states.add(nextState);
		state.relatedStates.add(states.indexOf(nextState));

		return nextState;
	}
	

}
