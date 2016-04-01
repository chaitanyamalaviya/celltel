
public class BaseStation {

	int stationID;
	int reservedChannels;
	int freeChannels;
	int range;
	int FCA;

	public BaseStation(int stationID,int reservedChannels,int freeChannels, int FCA){
		this.stationID = stationID;
		this.reservedChannels = reservedChannels;
		this.freeChannels = freeChannels;
		this.range = 2;
		this.FCA = FCA;
	}
	
	public int getID(){
		return this.stationID;
	}
	
	public int getRange(){
		return this.range;
	}
	
	public int getReservedChannelCount(){
		return this.reservedChannels;
	}
	
	public int getFreeChannelCount(){
		return this.freeChannels;
	}
	
	protected void leaveChannel(){
		if (reservedChannels==0 && FCA==1) //if a reserved channel was occupied, means all other 9 channels were occupied as well
			this.reservedChannels++;
		this.freeChannels++;
	}
	
	protected void occupyChannel(){
		if (this.freeChannels==1 && this.reservedChannels==1) // If only the reservedChannel is free, occupy.
			this.reservedChannels--;
		this.freeChannels--;
	}

	public boolean isFull(Event e){

		//System.out.println("StationID: " + this.stationID + " Free: " + this.getFreeChannelCount()+ " Reserved: " + this.getReservedChannelCount());
		if (e instanceof CallInitiate && (this.getFreeChannelCount() - this.getReservedChannelCount()) > 0)
		//Can't use reserved channels for CallInitiate event
			return false;
		
		else if (e instanceof HandoverCall && this.getFreeChannelCount()>0)
			return false;
	
		return true;	
	}
	
}
