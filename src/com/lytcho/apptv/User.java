package com.lytcho.apptv;

import java.util.Collection;

public class User {

	private Collection<Tv> subscribtions;
	//private
	
	public User(Collection<Tv> subscribtions){
		this.subscribtions = subscribtions;
	}

	public Collection<Tv> getSubscribtions() {
		return subscribtions;
	}

	public void setSubscribtions(Collection<Tv> subscribtions) {
		this.subscribtions = subscribtions;
	}
	

}
