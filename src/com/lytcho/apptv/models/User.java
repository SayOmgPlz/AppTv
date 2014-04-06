package com.lytcho.apptv.models;

import java.util.Collections;
import java.util.List;

public class User {

	private List<Tv> subscriptions;
	private Integer accountNumber;
	private String username;
	
	public User() {
		this.subscriptions = Collections.emptyList();
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
	}
	
	public boolean isEmpty() {
		return subscriptions.isEmpty() && (accountNumber == null) && (username == null);
	}
	

}
