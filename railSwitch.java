import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
Name: Jackson Vaughn
Course: CNT 4714 Fall 2023
Assignment title: Project 2 – Multi-threaded programming in Java
Date: October 8, 2023
Class: rail switch
Description: switch class that contains the locks
*/

public class railSwitch{
	
	private int switchNum;
	
	private Lock lock = new ReentrantLock();

	public railSwitch(int switchNum) {
		super();
		this.switchNum = switchNum;
	}

	@Override
	public String toString() {
		return switchNum +"";
	}
	
	
	 public int getSwitchNum() {
		return switchNum;
	}

	public void setSwitchNum(int switchNum) {
		this.switchNum = switchNum;
	}
	
	
	//returns a value if the switch is locked or unlocked
	public boolean tryLock() {
        return lock.tryLock();
    }
	
	
	
	public void unLock() {
		lock.unlock();
		//System.out.println("Lock " + switchNum + " is unlocked by train" + trainNumber);
	}

}
