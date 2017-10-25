package org.systemx.qlearning.commun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.systemx.qlearning.state.Action;
import org.systemx.qlearning.state.QValues;
import org.systemx.qlearning.state.State;

public class ExpBetaSelector {
	double beta;

	public ExpBetaSelector(double beta) {
		super();
		this.beta = beta;
	}

	private double calcActionWeight(QValues qValues, Action action, double maxQ) {
		if (qValues.actions.contains(action)) {
			return Math.exp(this.beta * (qValues.getValue(action) - maxQ));
		} else {
			return Math.exp(this.beta * (0 - maxQ));
		}
	}

	private Map<Action, Double> calcWeights(State state, List<Action> possibleActions) {
		double maxQ = state.getMaxQValue(possibleActions);

		Map<Action, Double> weights = new HashMap<Action, Double>();

		for (Action a : possibleActions) {
			weights.put(a, this.calcActionWeight(state.getQValues(), a, maxQ));
		}
		return weights;
	}

	public Action getExpBetaSelectedAction(State state, List<Action> possibleActions) {
		Random rand = new Random();
		Map<Action, Double> weights = calcWeights(state, possibleActions);

		System.out.println("Qv:" + state.getQValues().serialiseValues());
		for (Action a : possibleActions) {
			System.out.print(a + ":" + weights.get(a) + " ");
		}
		System.out.println();
		System.out.println("------------------------------------------------------------");

		double sumWeights = 0.0;
		for (Double weight : weights.values()) {
			sumWeights += weight;
		}
		double selnum = sumWeights * rand.nextDouble();
		for (Action action : possibleActions) {
			selnum -= weights.get(action);
			if (selnum <= 0.0) {
				if(state.getMaxQValue(possibleActions) == state.getQValue(action)) {
					System.out.println("===============================BEST " + action);
				}else {
					System.out.println("===============================NOTBEST "+ action);
				}
				return action;
			}
		}
		
		return null;
	}

}
