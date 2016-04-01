
public abstract class Event{
	
	protected double time;
	protected int stationID;
	
	public Event(double time,int stationID){
		this.time = time;
		this.stationID = stationID;
	}
	
	abstract void handleEvent();  //Handle the different types of events - set parameters, update state, 
	
	protected double getTime(){
		return this.time;
	}

	protected int getStation(){
		return this.stationID;
	}
}