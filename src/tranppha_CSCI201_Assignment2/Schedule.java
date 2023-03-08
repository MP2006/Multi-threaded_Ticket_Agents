package tranppha_CSCI201_Assignment2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Schedule extends Thread{
	private Tours t;
	private int transactionTime;
	private String artistName;
	private int transactionType;
	private int price;
	private Lock transactionLock;
	private Condition transactionComplete;
	
	public Schedule(Tours t, int transactionTime, String artistName, int transactionType, int price) {
		this.t = t;
		this.transactionTime = transactionTime*1000;
		this.artistName = artistName;
		this.transactionType = transactionType;
		this.price = price;
		transactionLock = new ReentrantLock();
		transactionComplete = transactionLock.newCondition();
	}
	
	public void setTours(Tours t) {
		this.t = t;
	}
	
	public Tours getTours() {
		return t;
	}
	
	public void setTransactionTime(int transactionTime) {
		this.transactionTime = transactionTime;
	}
	
	public int getTransactionTime() {
		return transactionTime;
	}
	
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	
	public String getArtistName() {
		return artistName;
	}
	
	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}
	
	public int getTransactionType() {
		return transactionType;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getPrice() {
		return price;
	}
	
	//create function for start purchase
	public void startPurchase() {
		System.out.println("[" + java.time.LocalTime.now() + "]" + " Staring purchase of " + transactionType + " tickets of " + artistName);		
	}
	
	//create function for end purchase
	public void endPurchase() {
		System.out.println("[" + java.time.LocalTime.now() + "]"  + " Finishing purchase of " + transactionType + " tickets of " + artistName);
		try {
			transactionLock.lock();
			transactionComplete.signal();
		}
		finally {
			transactionLock.unlock();
		}
	}
	//create function for start sale
	public void startSale() {
		int value = -transactionType;
		System.out.println("[" + java.time.LocalTime.now() + "]"  + " Staring sale of " + value + " tickets of " + artistName);		
	}
	
	//create function for end sale
	public void endSale() {
		int value = -transactionType;
		System.out.println("[" + java.time.LocalTime.now() + "]"  + " Finishing sale of " + value + " tickets of " + artistName);
		try {
			transactionLock.lock();
			transactionComplete.signal();
		}
		finally {
			transactionLock.unlock();
		}
	}
	
	
	public void run() {
		try {
			Thread.sleep(transactionTime);
			t.activateAgent(this);
			transactionLock.lock();
			transactionComplete.await();
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		finally {
			transactionLock.unlock();
		}
	}
}
