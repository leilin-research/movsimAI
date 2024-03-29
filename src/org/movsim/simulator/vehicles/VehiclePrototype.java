package org.movsim.simulator.vehicles;

import java.util.List;

import org.movsim.autogen.VehiclePrototypeConfiguration;
import org.movsim.consumption.model.EnergyFlowModel;
import org.movsim.simulator.vehicles.lanechange.LaneChangeModel;
import org.movsim.simulator.vehicles.longitudinalmodel.Memory;
import org.movsim.simulator.vehicles.longitudinalmodel.acceleration.EquilibriumProperties;
import org.movsim.simulator.vehicles.longitudinalmodel.acceleration.EquilibriumPropertiesFactory;
import org.movsim.simulator.vehicles.longitudinalmodel.acceleration.LongitudinalModelBase;
import org.movsim.simulator.vehicles.longitudinalmodel.acceleration.LongitudinalModelFactory;

import com.google.common.base.Preconditions;

import fr.ifsttar.licit.simulator.agents.perception.sensors.Sensor;
import fr.ifsttar.licit.simulator.agents.trust.models.base.TrustModel;
import fr.ifsttar.licit.simulator.agents.trust.models.base.TrustModelFactory;

class VehiclePrototype {

    private final VehiclePrototypeConfiguration configuration;

    private EnergyFlowModel energyFlowModel = null;

    private final EquilibriumProperties equiProperties;

    private final double simulationTimestep;

    VehiclePrototype(double simulationTimestep, VehiclePrototypeConfiguration configuration) {
        Preconditions.checkNotNull(configuration);
        this.configuration = configuration;
        this.simulationTimestep = simulationTimestep;
        equiProperties = EquilibriumPropertiesFactory.create(getLength(), createAccelerationModel());
    }

    /**
     * 
     * @return
     */
    boolean getExternalControl(){
    	return configuration.getExternalControl();
    }
    
    double getLength() {
        return configuration.getLength();
    }

    double getWidth() {
        return configuration.getWidth();
    }

    String getLabel() {
        return configuration.getLabel();
    }

    double getMaximumDeceleration() {
        return configuration.getMaximumDeceleration();
    }

    VehiclePrototypeConfiguration getConfiguration() {
        return configuration;
    }

    LongitudinalModelBase createAccelerationModel() {
        return LongitudinalModelFactory.create(getLength(), configuration.getAccelerationModelType(),
                simulationTimestep);
    }
    
    TrustModel createTrustModel() {
        return TrustModelFactory.create(configuration.getTrustModelType());
    }

    LaneChangeModel createLaneChangeModel() {
        return configuration.isSetLaneChangeModelType()
                && configuration.getLaneChangeModelType().isSetModelParameterMOBIL() ? new LaneChangeModel(
                configuration.getLaneChangeModelType()) : null;
    }

    Noise createAccNoiseModel() {
        return configuration.isSetNoiseParameter() ? new Noise(configuration.getNoiseParameter()) : null;
    }

    Memory createMemoryModel() {
        return configuration.isSetMemoryParameter() ? new Memory(configuration.getMemoryParameter()) : null;
    }

    EquilibriumProperties getEquiProperties() {
        return equiProperties;
    }

    double getSimulationTimestep() {
        return simulationTimestep;
    }

    EnergyFlowModel getEnergyFlowModel() {
        return energyFlowModel;
    }

    void setEnergyFlowModel(EnergyFlowModel energyFlowModel) {
        this.energyFlowModel = energyFlowModel;
    }
    
    
    public List<Sensor> getSensors(){
    	return this.configuration.getSensors();
    }
}
