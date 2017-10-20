package org.systemx.project;


import fr.ifsttar.licit.simulator.agents.communication.messages.Message;
import fr.ifsttar.licit.simulator.infrastructure.RoadSideUnit;

public class MessageRSU extends Message {

	private String content;
	public MessageRSU(RoadSideUnit sender, String txt) {
		super(sender);
		setContent(txt);
		// TODO Auto-generated constructor stub
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
