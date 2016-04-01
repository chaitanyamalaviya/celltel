import java.util.*;


public class ScheduleEvents {

	
	LinkedList<Event> EventQueue = new LinkedList<Event>();
	
	void addtoQueue(Event e) {

		double time = e.getTime();

		if (EventQueue.size() == 0)
			EventQueue.add(e);

		else if (EventQueue.get(0).getTime() > time)
			EventQueue.add(0, e);

		else if (EventQueue.getLast().getTime() < time)
			EventQueue.addLast(e);

		else {
			for (int i = 0; i < EventQueue.size(); i++) {
				if (EventQueue.get(i).getTime() <= time
						&& EventQueue.get(i + 1).getTime() >= time) {
					EventQueue.add(i, e);
					break;
				}
			}

		}
	}

	
	void dequeueEvent(){
		EventQueue.removeFirst();
	}
	
	Event getNextEvent(Event e){
		return EventQueue.get(EventQueue.indexOf(e)+1);
	}
	
	void sortEventsbyTime(){ 
		
	}
	
	int eventsInQueue(){
		return EventQueue.size();
	}
	
}
