package org.systemx.qlearning;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.systemx.qlearning.state.Action;
import org.systemx.qlearning.state.State;
import org.systemx.qlearning.state.StatesList;
import org.systemx.qlearning.state.StatesListGroup;

public class QLearning {

	private static final double alpha = 0.01; // Learning rate
	private static final double gamma = 0.99; // Eagerness - 0 looks in the near future, 1 looks in the distant future

	public static final int numberOfLanes = 3;
	public static final int speedLimit = 20;

	private static StatesList statesList;

	private static State currentState;
	private static Action currentAction;

	private static boolean Initialized = false;

	private static String savePath = "./Qdata/QSave.q";

	public QLearning(Boolean loadFile) {
		super();
		if (loadFile) {

			try {
				System.out.println("Load Started!");
				statesList = StatesListGroup.read(savePath);
				System.out.println("Load Complete!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			statesList = new StatesList(numberOfLanes, speedLimit);
		}
	}

	static private Action init(State realCurrentState) {
		currentState = statesList.addNewState(realCurrentState);
		currentAction = statesList.predictNextAction(currentState);
		return currentAction;
	}

	static public Action realTimeCalculateQ(State realNextState) {
		if (!Initialized) {
			Initialized = true;
			return init(realNextState);
		}

		State nextState = statesList.setNextState(currentState, realNextState);
		
		double q = currentState.getQValue(currentAction);

		double maxQ = nextState.getMaxQValue(nextState.getPossibleActions(numberOfLanes, speedLimit));
		double r = getReward();

		double Qvalue = q + alpha * (r + gamma * maxQ - q);

		currentState.setQValue(currentAction, Qvalue);

		currentState = nextState;
		currentAction = statesList.predictNextAction(currentState);

		return currentAction;
	}

	static double getReward() {
		switch (currentAction) {
		case goLeft:
			return 0;
		case goRight:
			return 0;
		case incSpeed:
			return 10;
		case decSpeed:
			return -10;
		case noAction:
			return 0;
		default:
			return 0;
		}
	}

	static public Action realTimeTestQ(State realNextState) {
		Action action = statesList.getMaxQNextAction(realNextState);
		System.out.println(action);
		return action;
	}

	static public void realTimeCalculateQCrash() {
		double reward = -1000;
		double q = currentState.getQValue(currentAction);

		double maxQ = 0;
		double r = reward;

		double Qvalue = q + alpha * (r + gamma * maxQ - q);

		currentState.setQValue(currentAction, Qvalue);

		realTimeCalculateQReset();
	}

	static public void realTimeCalculateQReset() {
		currentState = null;
		Initialized = false;
	}

	static public void SaveList() {
		try {
			System.out.println("Attemting to save");
			StatesListGroup.write(statesList, savePath);
			System.out.println("ListSaved to " + savePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
