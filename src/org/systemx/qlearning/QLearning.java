package org.systemx.qlearning;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.systemx.qlearning.commun.Action;
import org.systemx.qlearning.state.State;
import org.systemx.qlearning.state.StatesList;

public class QLearning {

	private final double alpha = 0.1; // Learning rate
	private final double gamma = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future
	
	private final StatesList statesList;
	
	public QLearning(int numberOfLanes, int speedLimit) {
		super();
		statesList = new StatesList(numberOfLanes, speedLimit);
		// TODO Auto-generated constructor stub
	}
	State getCurrentState(){
		return new State();
	}
	boolean isFinalState() {
		return false;
	}
	
	boolean isStateFinished() {
		return false;
	}
	
	int getReward() {
		return 0;
	}

	
	void calculateQ() {
		Random rand = new Random();
		
		State currentState = getCurrentState();
		statesList.addState(currentState);
		while (!isFinalState()) {
			Action nextAction = statesList.predictNextAction(currentState);
			State nextState = statesList.getNextState(currentState, nextAction);
			
			// Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma *
			// Max(next state, all actions) - Q(state,action))
			double q = currentState.getQValue(nextAction);
			
			double maxQ = nextState.getMaxQValue();
			int r = getReward();

			double value = q + alpha * (r + gamma * maxQ - q);
			currentState.setQValue(nextAction, value);
			currentState = nextState;
		}
	}

}
