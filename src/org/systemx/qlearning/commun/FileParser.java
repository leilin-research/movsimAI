package org.systemx.qlearning.commun;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.systemx.qlearning.state.Action;
import org.systemx.qlearning.state.CarState;
import org.systemx.qlearning.state.QValues;
import org.systemx.qlearning.state.State;
import org.systemx.qlearning.state.StatesList;

public class FileParser {

	public static StatesList parseFile(String path) throws IOException {
		File file = new File(path);
		List<String> lines = readFile(file);
		System.out.println("Parsing StatesList:");

		ProgressBar progressBar = new ProgressBar(lines.size(), "Parsing StatesList");
		progressBar.setInverse(false);
		
		int numberOfLanes = Integer.parseInt(lines.get(0));
		int speedLimit = Integer.parseInt(lines.get(1));
		
		StatesList stateList = new StatesList(numberOfLanes, speedLimit);
		
		long reli =0;

		for (int i = 2; i < lines.size(); i++) {
			QValues qValuesTemp = new QValues();
			CarState myCarTemp = new CarState();
			List<CarState> AdjacentCarsTemp = new ArrayList<CarState>();
			List<String> relatedStatesIds = new ArrayList<String>();
			
			List<String> splitList = Arrays.asList(lines.get(i).split(";"));
			
			qValuesTemp.parseValues(splitList.get(1));
			
			if(!splitList.get(2).isEmpty()) {
				String[] relatedStatesString = splitList.get(2).split("_");	
				for (int j = 0; j < relatedStatesString.length; j++) {
					reli++;
					relatedStatesIds.add(relatedStatesString[j]);
				}
			}
		
			myCarTemp.parseValues(splitList.get(3));
			
			for (int j = 4; j < splitList.size(); j++) {
				CarState carTemp = new CarState();
				carTemp.parseValues(splitList.get(j));
				AdjacentCarsTemp.add(carTemp);
			}
			
			State stateTemp = new State(qValuesTemp, myCarTemp, AdjacentCarsTemp, relatedStatesIds);
			stateList.addState(stateTemp.getId(),stateTemp);
			progressBar.update(i);
		}
		System.out.println();
		System.out.println("Parsing StatesList done! " + reli);
		System.out.println();
		
		return stateList;
	}


	public static void writeFile(String path, StatesList statesListCopy) throws IOException {
		StatesList statesList = new StatesList(statesListCopy);
		
		File file = new File(path);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, false)));
		String s = "";
		System.out.println("Writing statesList");
		ProgressBar progressBar = new ProgressBar(statesList.getStates().size(),"Writing statesList");
		progressBar.setInverse(false);

		out.println(statesList.getNumberOfLanes());
		out.println(statesList.getSpeedLimit());
		
		List<String> keySet = new ArrayList<String>(statesList.getStates().keySet());
		
		long reli =0;
		int maxrel= 0;
		int zerorel= 0;
		int nonzerorel= 0;
		
		for (int i = 0; i < keySet.size(); i++) {
			State tempState = statesList.getStates().get(keySet.get(i));
			s = "";
			s = s + tempState.getId();
			s = s + ";";
			
			s = s + tempState.getQValues().serialiseValues();
			s = s + ";";
			
			for (int j = 0; j < tempState.getRelatedStatesIds().size(); j++) {
				reli ++;
				s = s + tempState.getRelatedStatesIds().get(j);
				if(j<tempState.getRelatedStatesIds().size() - 1) {
					s = s + "_";	
				}
			}
			if(maxrel<tempState.getRelatedStatesIds().size()) {
				maxrel = tempState.getRelatedStatesIds().size();
			}
			if(tempState.getRelatedStatesIds().size() == 0) {
				zerorel ++ ;
			}else {
				nonzerorel++; 
			}
			
			s = s + ";";
			
			s = s + tempState.getMyCar().serialiseValue();
			s = s + ";";
			
			for (int j = 0; j < tempState.getAdjacentCars().size(); j++) {
				s = s + tempState.getAdjacentCars().get(j).serialiseValue();
				s = s + ";";
			}
			
			out.println(s);
			progressBar.update(i);
		}
		
		System.out.println();
		System.out.println("Writing statesList done! total:" + reli + " max:" +  maxrel + " zero:" + zerorel +":" + nonzerorel);
		System.out.println();
		out.close();
	}

	private static List<String> readFile(File fin) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(fin));
		String line = null;
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		br.close();
		return lines;
	}
	
	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
