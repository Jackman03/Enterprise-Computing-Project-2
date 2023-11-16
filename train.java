import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
Name: Jackson Vaughn
Course: CNT 4714 Fall 2023
Assignment title: Project 2 – Multi-threaded programming in Java
Date: October 8, 2023
Class: train class
Description: contains the train object. ech train has a array of switch refrences
*/
public class train implements Runnable{
	
	//a train has a number, entrance and exit
	
	int trainNumber;
	int entranceTrack;
	int exitTrack;
		
    private railSwitch[] switches;
    private int[] neededSwitches;
    
    private boolean inQueue;
    
    static final int WAITTIME = 1000;
    
    static final int MOVETIME = 2000;

 
    

	
	public int getNeededSwitches(int index) {
		return neededSwitches[index];
	}





	public railSwitch[] getSwitches() {
		return switches;
	}

	
	public void setSwitches(railSwitch s1, railSwitch s2, railSwitch s3) {
		this.switches[0] = s1;
		this.switches[1] = s2;
		this.switches[2] = s3;
	}



	public train(int trainNumber, int entranceTrack, int exitTrack,railSwitch[] switches,int[] neededSwitches,boolean inQueue) {
		super();
		this.trainNumber = trainNumber;
		this.entranceTrack = entranceTrack;
		this.exitTrack = exitTrack;
		this.switches = switches;
		this.neededSwitches = neededSwitches;
		this.inQueue = inQueue;

	}
	






public void setrequiredSwitched(int s1, int s2, int s3) {
	this.neededSwitches[0] = s1;
	this.neededSwitches[1] = s2;
	this.neededSwitches[2] = s3;
}


	public int getTrainNumber() {
		return trainNumber;
	}





	public void setTrainNumber(int trainNumber) {
		this.trainNumber = trainNumber;
	}





	public int getEntranceTrack() {
		return entranceTrack;
	}





	public void setEntranceTrack(int entranceTrack) {
		this.entranceTrack = entranceTrack;
	}





	public int getExitTrack() {
		return exitTrack;
	}





	public void setExitTrack(int exitTrack) {
		this.exitTrack = exitTrack;
	}


	@Override
	public String toString() {
		return "train [trainNumber=" + trainNumber + ", entranceTrack=" + entranceTrack + ", exitTrack=" + exitTrack
				+ ", owns switches=" + Arrays.toString(switches) + ", neededSwitches=" + Arrays.toString(neededSwitches)
				+ "]";
	}



	public boolean stillInQueue() {
		return inQueue;
	}
	
	
	

	
	public boolean isInQueue() {
		return inQueue;
	}



	public void setInQueue(boolean inQueue) {
		this.inQueue = inQueue;
	}
	
	@Override
	public void run() {
	
		try {
			
		
			
		boolean allLocked;	//if a train has all locks
			
		
			
		while(isInQueue()) {	//if the train is in the queue
			//this varible is declared in the train class above
			
		
		allLocked = true;
			
			while(allLocked) {

				
				//get lock on the first switch
				if(switches[0].tryLock()) {

					System.out.println("train " + trainNumber + " holds lock on switch #" + switches[0].getSwitchNum());
					


					
					//gets lock on the 2nd switch
					if(switches[1].tryLock()) {
						
						
						System.out.println("train " + trainNumber + " holds lock on switch #" + switches[1].getSwitchNum());
						;

						
						//gets lock on third switch
						if(switches[2].tryLock()) {
							
							
							System.out.println("train " + trainNumber + " holds lock on switch #" + switches[2].getSwitchNum());
							

							Thread.sleep(MOVETIME);
							//if the trin holds the final lock thne it needs to go out of the yard
							
							System.out.println("Train " + trainNumber + " HOLDS ALL NEEDED SWITCH LOCKS – Train movement begins.");
							
							System.out.println("Train " +trainNumber + " Clear of yard Control");
							System.out.println("Train " +trainNumber + " Releasing all switch locks");
							System.out.println("Train " +trainNumber + " Unlocks Switch " + switches[0].getSwitchNum());
							System.out.println("Train " +trainNumber + " Unlocks Switch " + switches[1].getSwitchNum());
							System.out.println("Train " +trainNumber + " Unlocks Switch " + switches[2].getSwitchNum());
							System.out.println("Train " +trainNumber + " Has been dispatched and moves on down the line out of yard control into CTC");
							
			
							switches[0].unLock();
					
							switches[1].unLock();
						
							switches[2].unLock();
							
							
							System.out.println("\n");
							
							System.out.println("\t\t\t@@@ train " + trainNumber + " dispatched @@@");
							
							System.out.println("\n");
						
							
							
							//the train is no longet in the queue
							setInQueue(false);
							
							//fileWriter.close();
							
							return;
		
							
						}
						
						//cant get the third switch
						else {	
							
							allLocked = false;
							System.out.println("Train # " + trainNumber + " UNABLE TO LOCK third required switch: Switch # " + switches[2].getSwitchNum());
							System.out.println("\n");
							System.out.println("Train # " + trainNumber + " Releasing locks on first and second required switches: Switch " + 
							switches[0].getSwitchNum() + " Switch " + switches[1].getSwitchNum() + ". Train will wait...");

							
							//unlock the first and second switch
							switches[0].unLock();
							switches[1].unLock();
							
							Thread.sleep(WAITTIME);

						}
						
					}
					//cant get the 2nd seitch
					else {
						allLocked = false;
						System.out.println("train " + trainNumber + " UNABLE TO LOCK second required switch: Switch # " + switches[1].getSwitchNum());
						System.out.println("\n");
						System.out.println("Train # " + trainNumber + " Releasing locks on first required switches: Switch " + 
								switches[0].getSwitchNum() + ". Train will wait...");

						//unlock the first switch
						switches[0].unLock();
						
						Thread.sleep(WAITTIME);
						
					}
					
				}
				
				//cant get the first switch
				else {
					allLocked = false;
					System.out.println("train " + trainNumber + " UNABLE TO LOCK first required switch: Switch #" + switches[0].getSwitchNum() + ". Train will wait...");

					Thread.sleep(WAITTIME);

				}
				
			}
			
		}
		
		
		
		}catch(Exception e) {//something went terribly wrong...
			e.printStackTrace();
		}
		
		
	}
}
