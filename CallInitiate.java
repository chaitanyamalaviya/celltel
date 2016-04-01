
public class CallInitiate extends Event{

	protected double speed;
	protected double position;
	protected double duration;

	//StationID is from 1 to 20 when initialized
	public CallInitiate(double time, int stationID, double speed, double position, double duration){
		super(time,stationID);
		this.speed = speed;
		this.position = position;
		this.duration = duration;
	}
	
	protected double getPosition(){
		return this.position;
	}

	protected double getSpeed(){
		return this.speed;
	}
	
	protected double getDuration(){
		return this.duration;
	}
	
	void handleEvent(){
		
		//System.out.println("Call with duration "+ duration + " initiated at station "+ stationID);
		boolean res = Simulator.getReserveChannel(this,stationID);  // Check if a channel is free in the given stationID
		if (res == false) {
            //no channel is available when call is initialized
            Simulator.addBlockedCall();
            
            // generate new call
            double newEventTime = time + Dist.generateInterArrivalTime();
            double newSpeed = Dist.generateCarSpeed();
            int newStationId = Dist.generateStationId();
            double newPosition = Dist.generatePosition();
            double newDuration = Dist.generateDuration();
        
            Simulator.insert(new CallInitiate(newEventTime, newStationId, newSpeed,  newPosition, newDuration));
        
            return;
            
        }
        
        double distanceToNextStation = 2 - position;
        double timeToNextStation = (distanceToNextStation / speed) * 3600;
        
        //System.out.println("Duration: "+duration+" Time to next station: "+timeToNextStation);
        
        if (duration > timeToNextStation && stationID != 20) {
            //
            // If duration is longer than the time to next station and we are not at last station, add a handover event
            double newEventTime = time + timeToNextStation;
            int newEventStationId = stationID + 1;
            double remainingDuration = duration - timeToNextStation;
            //System.out.println("Handover event added at station " + newEventStationId+" and time: "+ newEventTime);
            Simulator.insert(new HandoverCall(newEventTime, newEventStationId, speed, remainingDuration));
            
        } else if (duration > timeToNextStation && stationID == 20) {
            
            // Car is at the last station and has traveled beyond system, add terminate event
            double newEventTime = time + timeToNextStation;
            
            Simulator.insert(new CallTerminate(newEventTime, stationID));
            
        } else {
            
            // No need to handover, call terminated in current base station, add terminate event
            double newEventTime = time + duration;
            
            Simulator.insert(new CallTerminate(newEventTime, stationID));
            
        }
        
        // Generate new call for distribution based simulation
        double newEventTime = time + Dist.generateInterArrivalTime();
        double newSpeed = Dist.generateCarSpeed();
        int newStationId = Dist.generateStationId();
        double newPosition = Dist.generatePosition();
        double newDuration = Dist.generateDuration();
        
        Simulator.insert(new CallInitiate(newEventTime, newStationId, newSpeed, newPosition, newDuration));
 	
        
        

	}
}
