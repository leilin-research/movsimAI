package org.systemx.qlearning.state;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class QValues {

	public List<Action> actions = new ArrayList<Action>();
	List<Double> qList = new ArrayList<Double>();

	
	public String serialiseValues() {
		String s = "";
		for (int i = 0; i < actions.size(); i++) {
			s = s + actions.get(i) + ":" + qList.get(i);
			if(i<actions.size()-1) {
				s = s + "_";
			}
		}
		return s;
	}
	
	public void parseValues(String qValuesString) {	
		if(!qValuesString.isEmpty()) {
			List<String> qValuesSplit = Arrays.asList(qValuesString.split("_"));
			for (int i = 0; i < qValuesSplit.size(); i++) {
				String[] parts = qValuesSplit.get(i).split(":");
				String action = parts[0];
				String q = parts[1];
				setQValue(Action.valueOf(action), Double.valueOf(q));
			}
		}
	}

	public QValues() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public List<Double> getQList() {
		return qList;
	}

	public void setQList(List<Double> qList) {
		this.qList = qList;
	}

	public QValues(QValues qValues) {
		super();
		actions = new ArrayList<Action>();
		qList = new ArrayList<Double>();
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
		double maxValue = Double.MIN_VALUE;
		for (Action possibleAction : possibleActions) {
			if (actions.contains(possibleAction)) {
				double tempValue = qList.get(actions.indexOf(possibleAction));
				if (tempValue > maxValue) {
					maxValue = tempValue;
				}
			}
		}
		if (possibleActions.size() > 0) {
			return maxValue;
		} else {
			return 0;
		}
	}

	public Action getMaxValueAction(List<Action> possibleActions) {
		double maxValue = Double.MIN_VALUE;
		Action maxAction = Action.noAction;

		
		for (Action possibleAction : possibleActions) {
			if (actions.contains(possibleAction)) {
				double tempValue = qList.get(actions.indexOf(possibleAction));
			}
		}

		for (Action possibleAction : possibleActions) {
			if (actions.contains(possibleAction)) {
				double tempValue = qList.get(actions.indexOf(possibleAction));
				if (tempValue > maxValue) {
					maxValue = tempValue;
					maxAction = possibleAction;
				}
			}
		}
		return maxAction;
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
