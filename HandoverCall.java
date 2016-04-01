
public class HandoverCall extends Event {

	protected double speed;
	protected double position;
	protected double rem_duration;
	
	public HandoverCall(double time, int stationID, double speed, double rem_duration){
		super(time,stationID);
		this.speed = speed;
		this.rem_duration = rem_duration;
	}
	
	public double getSpeed(){
		return this.speed;
	}

	public double getRemDuration(){
		return this.rem_duration;
	}

	
	void handleEvent(){
		
		//update state of the simulator
		Simulator.release(stationID-1); //release channel in previous stationID
		boolean res = Simulator.getReserveChannel(this,stationID);  // Check if a channel is free in the given stationID
		
		//System.out.println("Call with duration "+ rem_duration + " handed over to station "+ stationID);

		if (res==false){ 
			//If no channels are available during handover, mark as dropped call
			Simulator.addDroppedCall();
			return;
		}
		
	    double distanceToNextStation = 2; //distance between stationID and stationID+1
	    double timeToNextStation = (distanceToNextStation / speed) * 3600;
	    
        //System.out.println("Rem_duration: "+rem_duration+" Time to next station: "+timeToNextStation);
	        
		 if (rem_duration > timeToNextStation && stationID != 20) {  
			 //not 2nd last station and remaining call duration longer than time to nextStation
		            
		            // Need to handover
		            double newEventTime = time + timeToNextStation;
		            int newEventStationId = stationID + 1;
		            double newRemainingDuration = rem_duration - timeToNextStation;
		            
		            Simulator.insert(new HandoverCall(newEventTime, newEventStationId, speed, newRemainingDuration));
		            //System.out.println("Handover event added at station " + newEventStationId+" and time: "+ newEventTime);
		            
		  } else if (rem_duration > timeToNextStation && stationID == 20) {
				 // 2nd last station and remaining call duration longer than time to nextStation

		            // the car has traveled outside of the highway
		            double newEventTime = time + timeToNextStation;
		            
		            Simulator.insert(new CallTerminate(newEventTime, stationID));
		            
		  } else {  
		            // No need to handover, remaining call duration<time to next station
		            double newEventTime = time + rem_duration;
		            
		            Simulator.insert(new CallTerminate(newEventTime, stationID));
		            
		}
	}
}
