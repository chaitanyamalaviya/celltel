
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
//import java.util.Random;

// Library routine
public class Dist {
    
    //private static int seed = 1;
    private static final String distributionTestPath = "/Users/chaitanya/desktop/study/4.2/CE4015-SimulationandModeling/Project/distInput.csv";
    
    public static void main(String[] args) {
        
        ArrayList<String> headerNames = new ArrayList<>();
        ArrayList<Number> contents = new ArrayList<>();
        
        headerNames.add("Interarrival Time");
        headerNames.add("StationID");
        headerNames.add("Position");
        headerNames.add("Call Duration");
        headerNames.add("Speed");
        
        initializeCsv(distributionTestPath, headerNames); //Add above headers into csv file
        
        for (int i = 1; i <= 10000; i++) {   
        	//Generate all the parameters of simulation - 10000 entries
            
            double interArrivalTime = Dist.generateInterArrivalTime();
            contents.add(interArrivalTime);
            
            int stationId = Dist.generateStationId();
            contents.add(stationId);
            
            double position = Dist.generatePosition();
            contents.add(position);
            
            double duration = Dist.generateDuration();
            contents.add(duration);
            
            double carSpeed = Dist.generateCarSpeed();
            contents.add(carSpeed);
            
            saveToCsv(distributionTestPath, contents);  //Add entry to distribution csv file
            
            contents.clear(); //clear arraylist contents
            
        }
        
    }
    
    public static double generateInterArrivalTime() {
        
        double random = Math.random();
        
        return (double) Math.round(-1.36954*Math.log(1-random)*1000)/1000;
    }

    public static double generateCarSpeed() {
    	//Normal distribution - Variates calculated using convolution
        double random = 0;
        
        for (int i = 1; i <=12; i++) {
            random += Math.random();
        }
        
        random -= 6;
        
        return (random * 9.019) + 120.072;
    }

    public static int generateStationId() {
       
        double random = Math.random();
        
        return (int) (1 + Math.floor(random * 19));
    }

    public static double generatePosition() {

    	double random = Math.random();
    	
        return (double) Math.round(random*2000)/1000;
  
    }

    public static double generateDuration() {
        
        double random = Math.random();
        
        return (double) Math.round(-109.8359*Math.log(1-random)*1000)/1000;
        
    }

    static void saveToCsv(String filePath, ArrayList<Number> contents) {
        
        try
        {
            FileWriter writer = new FileWriter(filePath, true);
            
            Iterator<Number> iterator = contents.iterator();
            
            while(iterator.hasNext()) {
            
                writer.write(iterator.next().toString());
                writer.append(',');
            
            }
            
            writer.write(" ");
            writer.append("\n");
            
            writer.flush();
            writer.close();
            
        } catch (IOException e) {
            
            System.out.println(e.getMessage());
            
        }
        
    }

    static void initializeCsv(String filePath, ArrayList<String> headerNames) {
        
        try
        {
            FileWriter writer = new FileWriter(filePath);
            
            Iterator<String> iterator = headerNames.iterator();
            
            while(iterator.hasNext()) {
            
                writer.write(iterator.next());
                writer.append(',');
            
            }
            
            writer.write(" ");
            writer.append("\n");

            writer.flush();
            writer.close();
            
        } catch (IOException e) {
            
            System.out.println(e.getMessage());
            
        }
        
    }
    
}