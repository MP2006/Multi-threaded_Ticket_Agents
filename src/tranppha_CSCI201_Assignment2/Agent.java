package tranppha_CSCI201_Assignment2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Agent extends Thread{
	private Tours t;
	private boolean agentAvailable;
	private static boolean pendingTransaction;
	private Lock transactionLock;
	private Condition transactionComplete;
	
	public Agent(Tours t) {
		// TODO Auto-generated constructor stub
		this.t = t;
		agentAvailable = true;
		pendingTransaction = true;
		transactionLock = new ReentrantLock();
		transactionComplete = transactionLock.newCondition();
		this.start();
	}
	
	public void pendingTransaction() {
		try {
			transactionLock.lock();
			transactionComplete.signal();
		}
		finally {
			transactionLock.unlock();
		}
	}
	
	public boolean isAgentAvailable() {
		return this.agentAvailable;
	}
	
	public static void noPendingTransaction() {
		pendingTransaction = false;
	}
	
	public Tours getTourAgent() {
		return t;
	}
	
	public void run() {
		while(pendingTransaction) {
			boolean transactionEmpty = t.getTransactionList().isEmpty();
			while(!transactionEmpty) {
				Schedule startTransaction = null;
				
				//using synchronization to not duplicate agent
				synchronized(this) {
					startTransaction = t.getTransactionList().remove(0);
				}
				if(startTransaction.getTransactionType() >= 0) {
					startTransaction.startPurchase();
					startTransaction.endPurchase();
				}
				else {
					startTransaction.startSale();
					startTransaction.endSale();
				}
				
				try {
					transactionLock.lock();
					agentAvailable = true;
					transactionComplete.await();
					agentAvailable = false;
				} 
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally {
					transactionLock.unlock();
				}
				
			}
		}
	}

}
