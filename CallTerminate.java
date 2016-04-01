
public class CallTerminate extends Event {

	public CallTerminate(double time, int station){
		super(time,station);
	}
	
	
	void handleEvent(){
		
		//System.out.println("Call ended at station "+ stationID);
		Simulator.release(stationID);
		Simulator.addCall();
	}
}
