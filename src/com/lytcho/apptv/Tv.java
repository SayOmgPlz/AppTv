package com.lytcho.apptv;

class Tv {
	public String id;
	public String name;
	public String cmd;
	public String type;
	
	public Tv() {}
	
	public Tv(String name, String cmd, String type) {
		this.name = name;
		this.cmd = cmd;
		this.type = type;
	}
}
