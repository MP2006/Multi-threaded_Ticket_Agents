package tranppha_CSCI201_Assignment2;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;


public class Main {
    public static void main(String []args) {
    	
    	BufferedReader br;
    	//using ArrayList so we can modify the array size when we add
    	ArrayList<Tours> toursList = new ArrayList<Tours>();
    	//using ArrayList to store the data from json file
    	TourImportArrayList jsonTourList = new TourImportArrayList();
    	//create a scanner for in
    	Scanner in = new Scanner(System.in);
    	
    	boolean isJSON = false;
    	System.out.println("What is the name of the event file?");
    	String fileNameEvent = in.nextLine();
    	
    	while(!isJSON)
		{
			try 
			{
				
				//Obtaining all data from file 
				Gson gson = new Gson();
				jsonTourList = gson.fromJson(new BufferedReader(new FileReader(fileNameEvent)), TourImportArrayList.class);
				
				//Creating each individual tours
				for(int i = 0; i < jsonTourList.getTourImportArrayList().size(); i++)
				{				
					Tours tours = new Tours(jsonTourList.getTourImportArrayList().get(i));
				    toursList.add(tours);
				}
				
				isJSON = true;
				
				// Make sure none of the data parameters in null
				for(int i = 0; i < toursList.size(); i++) {
					Tours tours = toursList.get(i);
					if(tours.getName().equals("") || tours.getTour().equals("") ||
							tours.getLocalDate().equals("") || tours.getVenue().equals("") ||
							tours.getAgents() == 0) {
						isJSON = false;
						System.out.println("Missing data parameters.\n");
					}
				}
				
			}
			
			catch (IOException e) {
				System.out.println("The file " + fileNameEvent + " could not be found.\n");
			}
			
			catch(NumberFormatException e)
			{
				System.out.println("The file contain invalid data type.\n");
			}
			
			catch(NullPointerException e) {
				isJSON = false;
    			System.out.println("Missing data parameters.\n");
    		}
    		
    		catch(ClassCastException | JsonSyntaxException e) {
    			System.out.println("The file " + fileNameEvent + " is not formatted properly.\n");
    		}
			
			catch(ArrayIndexOutOfBoundsException e)
			{
				System.out.println("Missing parameters for schedule file.\n");
			}

    		if(!isJSON) {
    			System.out.println("What is the name of the event file?");
    	    	fileNameEvent = in.nextLine();
    		}

		}
				
		if(isJSON)
		{
			System.out.println("The event file has been properly read.\n");
		}
		
		//create a vector to store a list of transaction
		Vector<Schedule> scheduleList = new Vector<Schedule>();
		boolean isCSV = false;
		System.out.println("\nWhat is the name of the schedule file?");
		String fileNameSchedule = in.nextLine();
		
		while(!isCSV) {
			try {
				br = new BufferedReader(new FileReader(fileNameSchedule));
				String scheduleLine = br.readLine();
				
				while(scheduleLine != null) {
					Tours t = null;
					String[] valueEntry = scheduleLine.split(",");
					int transactionTime = Integer.parseInt(valueEntry[0]);
					String artistName = valueEntry[1];
					int transactionType = Integer.parseInt(valueEntry[2]);
					int price = Integer.parseInt(valueEntry[3]);
					
					for(int i = 0; i < toursList.size(); i++) {
						if(artistName.equalsIgnoreCase(toursList.get(i).getName())) {
							t = toursList.get(i);
						}
					}
					
					Schedule newTransaction = new Schedule(t, transactionTime, artistName, transactionType, price);
					
					scheduleList.add(newTransaction);
					
					scheduleLine = br.readLine();
				}
				isCSV = true;
				
			}
			catch (IOException e) {
				System.out.println("The file " + fileNameSchedule + " could not be found.\n");
			}
			
			catch(NumberFormatException e)
			{
				System.out.println("The file contain invalid data type.\n");
			}
			
			catch(NullPointerException e) {
				isCSV = false;
    			System.out.println("Missing data parameters.\n");
    		}
    		
    		catch(ClassCastException | JsonSyntaxException e) {
    			System.out.println("The file " + fileNameSchedule + " is not formatted properly.\n");
    		}
			
			catch(ArrayIndexOutOfBoundsException e)
			{
				System.out.println("Missing parameters for schedule file.\n");
			}

    		if(!isCSV) {
    			System.out.println("What is the name of the schedule file?");
    	    	fileNameSchedule = in.nextLine();
    		}
			
		}
		if(isCSV)
		{
			System.out.println("The schedule file has been properly read.\n");
		}
		
		System.out.println("What is the initial balance?");
		int balance = getBalance(in);
		int invalidCounter = 0;
		
		System.out.println("Starting execution of program...");
		ExecutorService executors = Executors.newCachedThreadPool();
		
		for(int i = 0; i < scheduleList.size(); i++) {
			Schedule d = scheduleList.get(i);
			if(d.getTransactionType() < 0) {
				executors.execute(d);
				int change = - (d.getPrice()*d.getTransactionType());
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				balance = balance + change;
				System.out.println("Current Balance after trade: " + balance);
			}
			else {
				int change = d.getPrice()*d.getTransactionType();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(change > balance) {
					System.out.println("Transaction failed due to insufficient balance."
							+ "Unsucessful purchase of " + d.getTransactionType() + " tickets of " + d.getArtistName());
					invalidCounter++;
				}
				else {
					balance = balance - change;
					executors.execute(d);
					System.out.println("Current Balance after trade: " + balance);
				}
			}
		}
		
		executors.shutdown();
		while(!executors.isTerminated()) {
			Thread.yield();
		}
		
		Agent.noPendingTransaction();
		if(invalidCounter > 0) {
			System.out.println("All trades completed, except " + invalidCounter + " trades.");
		}
		else {
			System.out.println("All trades completed!");
		}
		
		//wake up all the agents, terminate the program
		for(int i = 0; i < toursList.size(); i++) {
			for(int agent = 0; agent < toursList.get(i).getAgentList().size(); agent++) {
				toursList.get(i).getAgentList().get(agent).pendingTransaction();
				
			}
		}
		System.exit(0);
		
		
    }
    public static int getBalance(Scanner in) {
    	boolean validInput = false;
    	int userInput = 0;
    	
    	while(!validInput) {
    		try {
    			String userChoiceString = in.nextLine();
        		userInput = Integer.parseInt(userChoiceString);
        		validInput = true;
    		}
    		//using catch when we cannot convert from string to int
    		catch(NumberFormatException e) {
    			System.out.println("That is not a valid option.");
    		}
    		
    	}
    	return userInput;
    }
}
		

