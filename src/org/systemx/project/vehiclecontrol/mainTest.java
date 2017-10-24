package org.systemx.project.vehiclecontrol;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.systemx.project.control.VehicleControl;

public class mainTest {

	public static void main(String[] args) {

		mainDraw draw = new mainDraw();
		VehicleControl vc = new VehicleControl();

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainFrame frame = new mainFrame(draw,vc);
				frame.run();
			}
		});
		
		while (true) {
			System.out.println(vc.accelerationControl);
			System.out.println(vc.laneChangeControl);
			
			if(vc.accelerationControl>0) {
				vc.accelerationControl --;
			}
			if(vc.accelerationControl<0) {
				vc.accelerationControl ++;
			}
		}
	}

}
