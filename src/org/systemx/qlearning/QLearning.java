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

	private static final double alpha = 0.1; // Learning rate
	private static final double gamma = 0.9; // Eagerness - 0 looks in the near future, 1 looks in the distant future

	private static final int numberOfLanes = 3;
	private static final int speedLimit = 20;

	private static final StatesList statesList = new StatesList(numberOfLanes, speedLimit);

	private static State currentState;
	private static Action currentAction;

	static void init(State realCurrentState) {
		currentState = realCurrentState;
		statesList.addState(currentState);
	}

	static Action realTimeCalculateQ(State realNextState, double reward) {
		State nextState = statesList.setNextState(currentState, realNextState);
		double q = currentState.getQValue(currentAction);

		double maxQ = nextState.getMaxQValue(nextState.getPossibleActions(numberOfLanes, speedLimit));
		double r = reward;

		double Qvalue = q + alpha * (r + gamma * maxQ - q);
		currentState.setQValue(currentAction, Qvalue);

		currentState = nextState;
		currentAction = statesList.predictNextAction(currentState);
		return currentAction;
	}

}
