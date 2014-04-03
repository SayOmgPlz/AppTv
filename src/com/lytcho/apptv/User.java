package com.lytcho.apptv;

import java.util.Collections;
import java.util.List;

public class User {

	private List<Tv> subscribtions;
	private Integer accountNumber;
	private String username;
	
	public User() {
		this.subscribtions = Collections.emptyList();
	}
	
	public User(List<Tv> subscribtions){
		this.subscribtions = subscribtions;
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

	public List<Tv> getSubscribtions() {
		return subscribtions;
	}

	public void setSubscribtions(List<Tv> subscribtions) {
		this.subscribtions = subscribtions;
	}
	
	public boolean isEmpty() {
		return subscribtions.isEmpty() && (accountNumber == null) && (username == null);
	}
	

}
