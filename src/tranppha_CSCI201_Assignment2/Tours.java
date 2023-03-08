package tranppha_CSCI201_Assignment2;

import java.util.ArrayList;
import java.util.Vector;


public class Tours implements Comparable<Tours> {
	Tours t;
	private String name;
	private String tour;
	private String localDate;
	private String venue;
	private int agents;
	private transient Vector<Agent> agentList;
	private transient Vector<Schedule> transactionList;
	
	public Tours(Tours t) {
		this.t = t;
		this.name = t.getName();
		this.tour = t.getTour();
		this.localDate = t.getLocalDate();
		this.venue = t.getVenue();
		this.agents = t.getAgents();
		this.agentList = new Vector<Agent>();
		this.transactionList = new Vector<Schedule>();
		for(int i = 0; i < agents; i++) {
			agentList.add(new Agent(this));
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setTour(String tour) {
		this.tour = tour;
	}
	
	public String getTour() {
		return tour;
	}
	
	public void setLocalDate(String localDate) {
		this.localDate = localDate;
	}
	
	public String getLocalDate() {
		return localDate;
	}
	
	public void setVenue(String venue) {
		this.venue = venue;
	}
	
	public String getVenue() {
		return venue;
	}
	
	public void setAgents(int agents) {
		this.agents = agents;
	}
	
	public int getAgents() {
		return agents;
	}
	
	public void setAgentList(Vector<Agent> agentList) {
		this.agentList = agentList;
	}
	
	public Vector<Agent> getAgentList(){
		return agentList;
	}
	
	public void setTransactionList(Vector<Schedule> transactionList) {
		this.transactionList = transactionList;
	}
	
	public Vector<Schedule> getTransactionList() {
		return transactionList;
	}
	
	public void activateAgent(Schedule s) {
		transactionList.add(s);
		for(int i = 0; i < agents; i++) {
			Agent newAgent = getAgentList().get(i);
			if(newAgent.isAgentAvailable()) {
				newAgent.pendingTransaction();
				break;
			} 	
		}
	}
	
	@Override
	public int compareTo(Tours tours) {
		// TODO Auto-generated method stub
		return this.getName().compareTo(tours.getName());
	}
	                                                   
}

class TourImportArrayList {
	private ArrayList<Tours> data;
	
	public void setTourImportArrayList(ArrayList<Tours> data) {
		this.data =  data;
	}
	
	public ArrayList<Tours> getTourImportArrayList(){
		return data;
	}

}