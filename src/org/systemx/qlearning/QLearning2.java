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

public class QLearning2 {

	private final double alpha = 0.1; // Learning rate
	private final double gamma = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future
	
	
	private final int numberOfLanes = 2; 
	private final int speedLimit = 20; 
	
	private final StatesList statesList = new StatesList();
	
	
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
			
			List<Action> actionsFromCurrentState = currentState.getPossibleActions(numberOfLanes, speedLimit);
			
			// Pick a random action from the ones possible
			int randIndex = rand.nextInt(actionsFromCurrentState.size());
			Action nextAction = actionsFromCurrentState.get(randIndex);

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
