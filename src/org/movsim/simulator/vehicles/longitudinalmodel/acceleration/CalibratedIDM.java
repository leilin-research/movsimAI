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
package org.movsim.simulator.vehicles.longitudinalmodel.acceleration;

import java.util.List;

import org.movsim.autogen.ModelParameterCalibratedIDM;
import org.movsim.input.ProjectMetaData;
import org.movsim.simulator.vehicles.Vehicle;
import org.movsim.simulator.vehicles.longitudinalmodel.acceleration.parameter.IModelParameterCalibratedIDM;
import org.movsim.utilities.MyRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ifsttar.licit.simulator.calibration.CalibrationData;

/**
 * The Class IDM.
 * 
 * <p>
 * Implementation of the 'intelligent driver model'(IDM). <a
 * href="http://en.wikipedia.org/wiki/Intelligent_Driver_Model">Wikipedia article IDM.</a>
 * </p>
 * <p>
 * Treiber/Kesting: Traffic Flow Dynamics, 2013, chapter 11.3
 * </p>
 * <p>
 * see <a href="http://xxx.uni-augsburg.de/abs/cond-mat/0002177"> M. Treiber, A. Hennecke, and D. Helbing, Congested
 * Traffic States in Empirical Observations and Microscopic Simulations, Phys. Rev. E 62, 1805 (2000)].</a>
 * </p>
 * 
 * Model parameters:
 * <ul>
 * <li>safe time headway T (s)</li>
 * <li>minimum gap in standstill s0 (m)</li>
 * <li>maximum desired acceleration a (m/s^2)</li>
 * <li>comfortable (desired) deceleration (m/s^2)</li>
 * <li>acceleration exponent delta (1)</li>
 * <li>gap parameter s1 (m).</li>
 * </ul>
 * 
 * 
 * Model parameters:
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 */
// TODO reduce visibility
public class CalibratedIDM extends LongitudinalModelBase {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(CalibratedIDM.class);

    private IModelParameterCalibratedIDM param;

    CalibratedIDM(IModelParameterCalibratedIDM parameters) {
        super(ModelName.CalibratedIDM);
        this.param = parameters;
        
        this.calibrate();
    }

    /**
     * Check if a good calibration file was provided and if so, pick randomly parameters for the model in this file
     */
    private void calibrate() {

    	if(this.param.getCalibration() != null && this.param.getCalibration() != ""){
    		
    		String calibrationFilename = param.getCalibration();
    		
    		CalibrationData calibrationData = ProjectMetaData.getInstance().getCalibrationData(calibrationFilename);
    		
    		if(calibrationData!=null){
    			
    			List<String[]> data = calibrationData.getData();
    			
    			int randomLine = (int) Math.floor(MyRandom.nextAlternativeDouble() * (data.size() - 2)) + 1 ;
        		
    			String[] line = data.get(randomLine);
    			
    			if(line.length == 6){

    				final double v0 = Double.parseDouble(line[1].replace(',', '.'));
    				final double a  = Double.parseDouble(line[0].replace(',', '.'));
    				final double b  = Double.parseDouble(line[2].replace(',', '.'));
    				final double s1 = Double.parseDouble(line[3].replace(',', '.'));
    				final double T  = Double.parseDouble(line[4].replace(',', '.'));
    				
    				//System.out.println(v0 + " ; " + " ; " + a + " ; " + b + " ; " + s1 + " ; " + T);
    				
    				ModelParameterCalibratedIDM modelParameterCalibratedIDM = new ModelParameterCalibratedIDM();
    				modelParameterCalibratedIDM.setV0(Math.max(25, v0)); // FIXME: HACK
    				modelParameterCalibratedIDM.setA(a);
    				modelParameterCalibratedIDM.setB(b);
    				modelParameterCalibratedIDM.setT(T);
    				modelParameterCalibratedIDM.setS0(this.param.getS0());
    				modelParameterCalibratedIDM.setS1(s1);
    				modelParameterCalibratedIDM.setDelta(this.param.getDelta());
    				modelParameterCalibratedIDM.setCalibration(this.param.getCalibration());
			        this.param = modelParameterCalibratedIDM;
    			}
    		}
    	}	
	}
    
    /**
     * Constructor.
     * 
     * @param v0
     *            desired velocity, m/s
     * @param a
     *            maximum acceleration, m/s^2
     * @param b
     *            desired deceleration (comfortable braking), m/s^2
     * @param T
     *            safe time headway, seconds
     * @param s0
     *            bumper to bumper vehicle distance in stationary traffic, meters
     * @param s1
     *            gap parameter, meters
     */
    public CalibratedIDM(double v0, double a, double b, double T, double s0, double s1, String calibration) {
        super(ModelName.IDM);
        this.param = create(v0, a, b, T, s0, s1, calibration);
    }

    private static ModelParameterCalibratedIDM create(double v0, double a, double b, double T, double s0, double s1, String calibration) {
    	ModelParameterCalibratedIDM modelParameterCalibratedIDM = new ModelParameterCalibratedIDM();
    	modelParameterCalibratedIDM.setV0(v0);
    	modelParameterCalibratedIDM.setA(a);
    	modelParameterCalibratedIDM.setB(b);
    	modelParameterCalibratedIDM.setT(T);
    	modelParameterCalibratedIDM.setS0(s0);
    	modelParameterCalibratedIDM.setS1(s1);
    	modelParameterCalibratedIDM.setDelta(modelParameterCalibratedIDM.getDelta());
    	modelParameterCalibratedIDM.setCalibration(calibration);
        return modelParameterCalibratedIDM;
    }
    
    
    

    @Override
    public double calcAcc(Vehicle me, Vehicle frontVehicle, double alphaT, double alphaV0, double alphaA) {

    	//System.out.println(param.getA() + " " + param.getB() + " " + param.getS0() + " " + param.getS1());
    	
        // Local dynamical variables
        final double s = me.getNetDistance(frontVehicle);
        final double v = me.getSpeed();
        final double dv = me.getRelSpeed(frontVehicle);

        // space dependencies modeled by speedlimits, alpha's

        final double localT = alphaT * param.getT();
        // consider external speedlimit
        final double localV0;
        if (me.getSpeedlimit() != 0.0) {
            localV0 = Math.min(alphaV0 * getDesiredSpeed(), me.getSpeedlimit());
        } else {
            localV0 = alphaV0 * getDesiredSpeed();
        }
        final double localA = alphaA * param.getA();

        return acc(s, v, dv, localT, localV0, localA);
    }

    @Override
    public double calcAccSimple(double s, double v, double dv) {
        return acc(s, v, dv, param.getT(), param.getV0(), param.getA());
    }

    /**
     * Acc.
     * 
     * @param s
     *            the s
     * @param v
     *            the v
     * @param dv
     *            the dv
     * @param TLocal
     *            the t local
     * @param v0Local
     *            the v0 local
     * @param aLocal
     *            the a local
     * @return the double
     */
    private double acc(double s, double v, double dv, double TLocal, double v0Local, double aLocal) {
        // treat special case of v0=0 (standing obstacle)
        if (v0Local == 0.0) {
            return 0.0;
        }

        final double s0 = getMinimumGap();
        double sstar = s0 + TLocal * v + param.getS1() * Math.sqrt((v + 0.0001) / v0Local) + (0.5 * v * dv)
                / Math.sqrt(aLocal * param.getB());

        if (sstar < s0) {
            sstar = s0;
        }

        final double aWanted = aLocal * (1.0 - Math.pow((v / v0Local), param.getDelta()) - (sstar / s) * (sstar / s));

        LOG.debug("aWanted = {}", aWanted);
        return aWanted; // limit to -bMax in Vehicle
    }

    @Override
    protected IModelParameterCalibratedIDM getParameter() {
        return param;
    }

}
