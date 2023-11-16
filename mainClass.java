/*
Name: Jackson Vaughn
Course: CNT 4714 Fall 2023
Assignment title: Project 2 – Multi-threaded programming in Java
Date: October 8, 2023
Class: main class
Description: runs the program and creates any threads needed
*/
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class mainClass {
	
	//adds trains to a list
	public static void addTrains(String fileName, ArrayList<train> fleet) throws IOException {
		
		train t;
		
		try {
			BufferedReader readIn = new BufferedReader(new FileReader(fileName));
			
			String line = "";
			
			
			line = readIn.readLine();	
			
			while(line != null) {
				//System.out.println(line);
				
				String line2[] = line.split(",");	//split the line from csv
				
				int tNum = Integer.parseInt(line2[0]);	//converting the item amount to a int
				int enter = Integer.parseInt(line2[1]);	//converting the item amount to a int
				int exit = Integer.parseInt(line2[2]);	//converting the item amount to a int

				railSwitch[] switches = {null,null,null};	//set rail array tp null
				int[] neededSwitches = {0,0,0};
				
				t = new train(tNum, enter, exit,switches,neededSwitches,true);	//create new train object
				
				fleet.add(t);
				line = readIn.readLine();
			}
			
			readIn.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to find file error");
		}
			
	}
	
	//adds track list to the trains
	private static void assignTracks(String fileName, ArrayList<train> fleet, int trainNum , ArrayList <railSwitch> switches) throws IOException {
		
		//take the first and last track for train I and serch the file to see if the train is valid
		
		try {
			BufferedReader readIn = new BufferedReader(new FileReader(fileName));
			
			
			String line = "";
			
			//detetming the end and exit tracks
			int startTrack = fleet.get(trainNum).getEntranceTrack();
			int endTrack = fleet.get(trainNum).getExitTrack();
			
			
			line = readIn.readLine();	
			
			while(line != null) {
				//System.out.println(line);
				
				String line2[] = line.split(",");	//split the line from csv
				
				int enter = Integer.parseInt(line2[0]);	//
				int s1 = Integer.parseInt(line2[1]);	//
				int s2 = Integer.parseInt(line2[2]);	//
				int s3 = Integer.parseInt(line2[3]);	//
				int exit = Integer.parseInt(line2[4]);	//
				
				//if the first and last values of the line equel the train values we return true
				
				if(enter == startTrack && exit == endTrack) {
					//assign switches to a train
					fleet.get(trainNum).setrequiredSwitched(s1, s2, s3);
					//exit out of the method
					//find each switch from the list of switches that has the same switch number
					
					
					
					fleet.get(trainNum).setSwitches(switches.get(s1-1), switches.get(s2-1), switches.get(s3-1));
					
					
					return;
					
				}
				
				else {
					//if the train does not have a valid path all of the tracks are set to -1. this will be checked later
					fleet.get(trainNum).setrequiredSwitched(-1, -1, -1);

				}
				

				line = readIn.readLine();
			}
			
			readIn.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to find file error");
		}
		
		
		
		// TODO Auto-generated method stub
	
	}
	
	//generic print list method
	public static <T> void printList(ArrayList<T> list) {
		for(int i = 0; i < list.size(); i ++) {
    		System.out.println(list.get(i));

        }
	}
	
	//if a trais hasa valid track
	public static boolean varifyTrains(int index, ArrayList<train> fleet) {
		boolean valid = true;
		for(int i = 0; i < 3; i ++) {
			
			if(fleet.get(index).getNeededSwitches(i) != -1) {
				valid = true;
			}
			
			else {
				valid = false;
			}
			
		}
		return valid;
		
	}
	//constants
	static final int MAXTRAINS = 30;
	static final int MAXALIGMENTS = 68;
	static final int MAXSWITCHES = 10;
	
	
	//main method
	public static void main(String[] args) throws IOException {
		
		
		//files to input
		String file1 = "theFleetFile.csv";
		String file2 = "theYardFile.csv";
		
		ArrayList <train> fleet = new ArrayList<train>(MAXTRAINS);
		
		//add the trains to the arraylist
		addTrains(file1,fleet);
		

		int size = fleet.size();
		
		ArrayList <railSwitch> switches = new ArrayList<railSwitch>(MAXSWITCHES);
		
		//add switches
		for(int i = 0; i < MAXSWITCHES; i ++) {
			railSwitch s = new railSwitch(i+1);	//index 0 will be switch 1
			switches.add(s);
		}
		
		//printList(switches);
		
		//FileWriter fileWriter = new FileWriter("output.txt");
        
        for(int i = 0; i < size; i ++) {	//assign railroads
        	
        	assignTracks(file2,fleet,i,switches);
        }
        
 
        //printList(fleet);
        
       
        
        //fileWriter.write("$ $ $ TRAIN MOVEMENT SIMULATION BEGINS……….. $ $ $");
        
        System.out.println("$ $ $ TRAIN MOVEMENT SIMULATION BEGINS……….. $ $ $");
        
        ExecutorService pool = Executors.newFixedThreadPool(MAXTRAINS);
        
        
        for(int i = 0; i < size; i ++) {
        	
        	if(!varifyTrains(i,fleet)) {	//if the train does have a valid path
        		System.out.println("*************");
        		System.out.println("train " + fleet.get(i).getTrainNumber() + " is on permanent hold and cannot be dispatched");
        		System.out.println("*************");
        		fleet.remove(i);	//removes it from the fleet
        		size--;	
        	}

        		pool.execute(fleet.get(i));
        	
        	
        }

 
        pool.shutdown();
        
        while (!pool.isTerminated()) {
            // wait until the threads finish
        }
        
        System.out.println("$ $ $ SIMULATION ENDS $ $ ");
        
        //fileWriter.close();
        

}

}
