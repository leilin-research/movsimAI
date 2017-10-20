package org.systemx.qlearning.commun;

import java.util.Random;

public class ExpBetaSelector {
	
	
	double calcActionWeight(Action action) {
		if (actions.contains(action)) {

		} else {
			return 0;
		}
	}

	double getExpBetaSelectedPlan() {
		Random rand = new Random();
		double sumWeights = 0;

		double minQ = getMinValue();
		double maxQ = getMaxValue();
		
		double minExp = minQ - ((maxQ-minQ)/100);
		double maxExp = maxQ;
		
	}

}
