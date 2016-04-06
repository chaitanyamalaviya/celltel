import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Simulator {

	//Statistical counters
	protected static int callsDropped;
	protected static int callsBlocked;
	protected static int totalCalls;
	//FCA Scheme
	protected static int FCA;
	
	private static double SimulationClock;
	
	//Describe state of system
	private static ArrayList<BaseStation> baseStations;
	private static LinkedList<Event> eventList;
	protected static List<ArrayList> inputData = new ArrayList<>();
	
	public static String path = "/Users/chaitanya/desktop/study/4.2/CE4015-SimulationandModeling/Project/PCS_TEST_DETERMINSTIC_S213141.csv";
	public static String csvPath = "/Users/chaitanya/desktop/study/4.2/CE4015-SimulationandModeling/Project/output.csv";

	private static int totalRunCount;
	private static final int TOTAL_SIMULATION_RUNS = 1000;
	private static final int TOTAL_SIMULATION_TIMES = 100000;
	private static final int TOTAL_WARMUP_TIMES = 20000;
	
	// Logic of the simulator is here, events scheduled and
	// executed from here
	public static void main(String args[]){
		
		System.out.println("Mobile Communications Simulator");
		FCA = Integer.parseInt(args[0]);
		
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("Arrival No");
		headers.add("Arrival Time");
		headers.add("Base Station");
		headers.add("Call Duration");
		headers.add("Velocity");
		inputData.add(0,headers);
		
		BufferedReader br = null;
		String line = "";
		int count=0;
		
		try {

			br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {

				if (count!=0){
				String[] entry = line.split(",");
				ArrayList<Number> ele = new ArrayList<Number>();
				ele.add(0,Integer.parseInt(entry[0]));
				ele.add(1,Double.parseDouble(entry[1]));
				ele.add(2,Integer.parseInt(entry[2]));
				ele.add(3,Double.parseDouble(entry[3]));
				ele.add(4,Double.parseDouble(entry[4]));

				inputData.add(count, ele);
				}
				count++;
			}
		}
		catch (FileNotFoundException e) {
				e.printStackTrace();
		} catch (IOException e) {
				e.printStackTrace();
		} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		}
		
		//System.out.println(inputData);
		
		//Generate inter-arrival time
//		br = null;
//		line = "";
//		count = 0;
//		double nasta=0;
//		ArrayList<Number> ele = new ArrayList<Number>();
//
//		try {
//			br = new BufferedReader(new FileReader(path));
//			while ((line = br.readLine()) != null) {
//
//				String[] entry = line.split(",");
//
//				if (count > 1){
//					
//					ele.add(count-1,
//							Double.parseDouble(entry[1])
//									- nasta);
//					nasta = Double.parseDouble(entry[1]);}
//				else if (count==1)
//					ele.add(0, Double.parseDouble(entry[1]));
//
//				count++;
//
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (br != null) {
//				try {
//					br.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		Dist.saveToCsv(
//				"/Users/chaitanya/desktop/study/4.2/CE4015-SimulationandModeling/Project/interarrival.csv",
//				ele);
	
		
		//Make output file and run simulation
		 ArrayList<String> headerNames = new ArrayList<>();
	     headerNames.add("Drop Rate");
	     headerNames.add("Block Rate");
	        
	     Dist.initializeCsv(csvPath, headerNames);
	     totalRunCount = 0;
	        
	     while(true) {
	        
	            initialize();
	            
	            while(true) {

	                Event currentEvent = schedule();
	                SimulationClock = currentEvent.getTime();
	                
	                execute(currentEvent);

	                if (totalCalls >= TOTAL_WARMUP_TIMES) {

	                    totalCalls = 0;
	                    callsDropped = 0;
	                    callsBlocked = 0;
	                    break;

	                }

	            }
	            
	            while(true) {
      	
	                Event currentEvent = schedule();
	                //System.out.println("");
	                //System.out.println("Simulation Clock : " + SimulationClock);
	                execute(currentEvent);
	                
	                
	           
	                if (totalCalls >= TOTAL_SIMULATION_TIMES) {
	                    
	                    double dropRate = (double) callsDropped / totalCalls;
	                    double blockRate = (double) callsBlocked / totalCalls;
	                    
//	                    System.out.println("Calls : " + totalCalls);
//	                    System.out.println("Calls dropped : " + callsDropped);
//	                    System.out.println("Calls blocked : " + callsBlocked);
//	                    System.out.println("Drop Rate : " + dropRate);
//	                    System.out.println("Block Rate : " + blockRate);

	                    
	                    ArrayList<Number> contents = new ArrayList<>();
	                    contents.add(dropRate);
	                    contents.add(blockRate);
	                    
	                    Dist.saveToCsv(csvPath, contents);
	                    totalRunCount++;
	                    
	                    break;
	                    
	                }
	            }
	            
                System.out.println(totalRunCount*100/TOTAL_SIMULATION_RUNS + "%\n");  // Percentage of simulation complete
	            
	            if (totalRunCount >= TOTAL_SIMULATION_RUNS) {
	                
	                System.out.println("Simulation over!");
	                break;
	                
	            }
	        }
		
		
		
		}

	public static void initialize(){
		
		SimulationClock = 0;
		callsBlocked = 0;
		callsDropped = 0;
		totalCalls = 0;
		baseStations = new ArrayList<>();
		
		// Reservation FCA Scheme
		if (FCA==1){
			for (int i=1;i<=20;i++){ 					
				BaseStation station = new BaseStation(i,1,10,1); //10 free channels and 1 reserved for handovers
				baseStations.add(station);
		}
		} 
		
		//No Reservation FCA Scheme
		else if (FCA==0){
			for (int i=1;i<=20;i++){ 					
				BaseStation station = new BaseStation(i,0,10,0); //10 free channels and none reserved for handovers
				baseStations.add(station); 
			}
		}
		
		eventList = new LinkedList<>();
		
		//Insert CallInitiate Events
//		for (int i=1;i<inputData.size();i++){
//			
//			//double time, int stationID, double speed, double position, double duration
//			CallInitiate newEvent = new CallInitiate((double)inputData.get(i).get(1), (int)inputData.get(i).get(2), (double)inputData.get(i).get(4), (double) Math.round(Math.random()*2000)/1000, (double)inputData.get(i).get(3)); 
//			eventList.add(i-1,newEvent);
//		}
		
		double newEventTime = 0;
        double newSpeed = Dist.generateCarSpeed();
        int newStationId = Dist.generateStationId();
        double newPosition = Dist.generatePosition();
        double newDuration = Dist.generateDuration();
		
        eventList.push(new CallInitiate(newEventTime, newStationId, newSpeed,  newPosition, newDuration));

	}
	
	public static void insert(Event e){
		
        boolean inserted = false;
        
        for (int i = 0; i < eventList.size(); i++) {
            
            if (eventList.get(i).getTime() > e.getTime()) {
                
                eventList.add(i, e);
                inserted = true;
                break;             
            }         
        }
        
        if (inserted == false) {
            
            eventList.addLast(e);
            
        }		
	}
	

	public static void release(int baseStationID){
		baseStations.get(baseStationID-1).leaveChannel();
	}
	
	public static void addCall(){
		//Record a normal call
		totalCalls++;
		//System.out.println("Total calls: "+ totalCalls);

	}
	
	public static boolean getReserveChannel(Event e, int BaseStationID){
		
        if (baseStations.get(BaseStationID-1).isFull(e))   {    
            return false;
        }

        baseStations.get(BaseStationID-1).occupyChannel();     
        return true;
    }
	
	
	public static void addBlockedCall(){
		
		totalCalls++;
		callsBlocked++;
		//System.out.println("Blocked:(");

	}
	
	public static void addDroppedCall(){
		
		totalCalls++;
		callsDropped++;
		//System.out.println("Dropped:(");

	}
	
    private static Event schedule() {
        //timing routine
        Event currentEvent = eventList.pop();
        SimulationClock = currentEvent.getTime(); 
        
        return currentEvent;
    }
	
	private static void execute(Event e) {
	        
	     e.handleEvent();
	        
    }
			
}
