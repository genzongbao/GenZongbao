package ydh.event.entity;

import java.util.List;

public class ListEventAndCondition {

	private List<Event> events;
	
	private List<EventChangeCondition> condition;

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public List<EventChangeCondition> getCondition() {
		return condition;
	}

	public void setCondition(List<EventChangeCondition> condition) {
		this.condition = condition;
	}
	
	
}
