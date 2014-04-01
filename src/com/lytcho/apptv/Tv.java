package com.lytcho.apptv;

class Tv {
	public String id;
	public String name;
	public String cmd;
	public String type;
	public String url;
	public String codec;
	
	public Tv() {}
	
	public Tv(String name, String cmd, String type) {
		this.name = name;
		this.cmd = cmd;
		this.type = type;
		this.codec = null;
		
		String[] parts = this.cmd.split("http");
		// when we only have the url without the codec in the cmd
		if(parts.length == 1) {
			this.url = "http" + parts[0];
		} else {
			this.url = "http" + parts[1];
			this.codec = parts[0];
		}
	}
	
}
