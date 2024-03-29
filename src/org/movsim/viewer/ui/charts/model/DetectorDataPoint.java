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
package org.movsim.viewer.ui.charts.model;

public class DetectorDataPoint {

    private final double time;
    private final double flow;
    private final double density;
    private final double speed;

    public DetectorDataPoint(double time, double flow, double density, double speed) {
        this.time = time;
        this.flow = flow;
        this.density = density;
        this.speed = speed;
    }

    public double getTime() {
        return time;
    }

    public double getFlow() {
        return flow;
    }

    public double getDensity() {
        return density;
    }

    public double getSpeed() {
        return speed;
    }

}
