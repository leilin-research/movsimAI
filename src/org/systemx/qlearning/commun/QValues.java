package org.systemx.qlearning.commun;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class QValues {

	List<Action> actions = new ArrayList<Action>();
	List<Double> qList = new ArrayList<Double>();

	public QValues() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QValues(QValues qValues) {
		super();
		for (int i = 0; i < qValues.actions.size(); i++) {
			this.actions.add(qValues.actions.get(i));
		}
		for (int i = 0; i < qValues.qList.size(); i++) {
			this.qList.add(qValues.qList.get(i));
		}
	}

	public double getValue(Action action) {
		if (actions.contains(action)) {
			return qList.get(actions.indexOf(action));
		} else {
			return 0;
		}
	}

	public double getMaxValue(List<Action> possibleActions) {
		double maxValue=Double.MIN_VALUE;
		for (Action possibleAction: possibleActions) {
			if(actions.contains(possibleAction)) {
				double tempValue = qList.get(actions.indexOf(possibleAction));
				if( tempValue > maxValue) {
					maxValue = tempValue; 
				}
			}
		}
		if(possibleActions.size()>0) {
			return maxValue;
		}else {
			return 0;
		}
	}

	public void setQValue(Action action, double q) {
		if (!actions.contains(action)) {
			actions.add(action);
			qList.add(q);
		} else {
			qList.set(actions.indexOf(action), q);
		}
	}


}
