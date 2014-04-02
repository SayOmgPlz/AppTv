package com.lytcho.apptv;

import java.util.Collection;

public class User {

	private Collection<Tv> subscribtions;
	
	public User(Collection<Tv> subscribtions){
		this.subscribtions = subscribtions;
	}
}
