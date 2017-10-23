/*
 * Copyright (C) 2010, 2011, 2012 by Arne Kesting, Martin Treiber, Ralph Germ, Martin Budden
 *                                   <movsim.org@gmail.com>
 * -----------------------------------------------------------------------------------------
 * 
 * This file is part of
 * 
 * MovSim - the multi-model open-source vehicular-traffic simulator.
 * 
 * MovSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MovSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MovSim. If not, see <http://www.gnu.org/licenses/>
 * or <http://www.movsim.org>.
 * 
 * -----------------------------------------------------------------------------------------
 */
package org.movsim;

import java.util.Locale;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.movsim.input.MovsimCommandLine;
import org.movsim.input.ProjectMetaData;
import org.movsim.logging.LogFileAppender;
import org.movsim.logging.Logger;
import org.movsim.simulator.Simulator;
import org.systemx.project.control.VehicleControl;
import org.systemx.project.vehiclecontrol.mainDraw;
import org.systemx.project.vehiclecontrol.mainFrame;
import org.systemx.qlearning.QLearning;
import org.xml.sax.SAXException;

/**
 * The Class MovsimCoreMain.
 * 
 * MovSim core command line interface.
 * 
 */
public class MovsimCoreMain {

    /**
     * The main method.
     * 
     * @param args
     *            the command line arguments
     * @throws SAXException
     * @throws JAXBException
     * @throws ParserConfigurationException
     */
    public static void main(String[] args) throws JAXBException, SAXException {    	
    	
		// joseph =========================
		mainDraw draw = new mainDraw();
		VehicleControl vc = new VehicleControl();

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainFrame frame = new mainFrame(draw, vc);
				frame.run();
			}
		});

		// joseph =========================
    	
        Locale.setDefault(Locale.US);

        // final ProjectMetaData projectMetaData = ProjectMetaData.getInstance();
        // parse the command line, putting the results into projectMetaData
        Logger.initializeLogger();

        MovsimCommandLine.parse(args);
       
        /*while(ProjectMetaData.getInstance().hasNextProject()){
        	
        	
        	ProjectMetaData.getInstance().nextProject();*/
        	
        	if (!ProjectMetaData.getInstance().hasProjectName()) {
              System.err.println("no xml simulation configuration file provided.");
              System.exit(-1);
        	}

        	LogFileAppender.initialize(ProjectMetaData.getInstance());
    	
       
	        final Simulator simulator = new Simulator();
	         
	        simulator.initialize();
	        
	        //ChartView spaceTimeDiagram = 
	       
	        simulator.runToCompletion();
	        
	        /*final SpatioTemporalView spaceTimeDiagram = new SpatioTemporalView(simulator.getSpaceTimeSpeedDataSet(), null);
	        
	        spaceTimeDiagram.save(ProjectMetaData.getInstance().getPathToProjectFile()+File.separator+"results",ProjectMetaData.getInstance().getProjectName(), 1024,768, true);
	        
	        spaceTimeDiagram.dispose();
	        
	        simulator.getSpaceTimeSpeedDataSet().clear();
	        */
	     
      /*  }  */
        
	        //joseph
	        QLearning.SaveList();
        
    }
}
