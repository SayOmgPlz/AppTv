package com.lytcho.apptv.models;

public class Tv {
	public String id;
	public String name;
	public String cmd;
	public String type;
	public String url;
	public String codec;
	public Integer number;
	public Boolean favorite;
	
	public Status status;
	
	public static enum Status {
		CREATE_FAVOURITE, DELETE_FAVOURITE;
	}

	public Tv() {}
	
	public Tv(String name, String cmd, String type, Integer number) {
		this.name = name;
		this.cmd = cmd;
		this.type = type;
		this.codec = null;
		this.number = number;
		
		String[] parts = this.cmd.split("http");
		// when we only have the url without the codec in the cmd
		if(parts.length == 1) {
			this.url = "http" + parts[0];
		} else {
			this.url = "http" + parts[1];
			this.codec = parts[0];
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Tv))
			return false;
		else {
			return ((Tv)o).name.equals(name);
		}
	}
	
}
