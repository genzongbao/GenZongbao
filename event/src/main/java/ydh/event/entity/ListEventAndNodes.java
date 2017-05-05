package ydh.event.entity;

import java.util.List;

/**
 * 事件详情
 * @author tearslee
 *
 */
public class ListEventAndNodes {
	
	private Event	mainEvent;
	
	private	List<Event> nodes;

	public Event getMainEvent() {
		return mainEvent;
	}

	public void setMainEvent(Event mainEvent) {
		this.mainEvent = mainEvent;
	}

	public List<Event> getNodes() {
		return nodes;
	}

	public void setNodes(List<Event> nodes) {
		this.nodes = nodes;
	}
	
	
}
