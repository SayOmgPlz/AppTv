package com.lytcho.apptv.models;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class User {

	private List<Tv> subscriptions;
	private Integer accountNumber;
	private String username;
	public List<Tv> favorites;
	
	public User() {
		this.subscriptions = Collections.emptyList();
		this.favorites = new Vector<Tv>();
	}
	
	public User(List<Tv> subscriptions){
		this.subscriptions = subscriptions;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Integer getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}

	public List<Tv> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<Tv> subscriptions) {
		this.subscriptions = subscriptions;
		for (Tv tv : subscriptions) {
			if(tv.favorite) 
				this.favorites.add(tv);
		}
	}
	
	public boolean isEmpty() {
		return subscriptions.isEmpty() && (accountNumber == null) && (username == null);
	}
	

}
