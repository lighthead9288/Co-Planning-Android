package com.example.lighthead.androidcustomcalendar.helpers;

public class Credentials {

    private String name;
    private String surname;
    private String username;
    private String password;
	
	public String GetName() {
		return name;
	}	

	public String GetSurname() {
		return surname;
	}	
	
	public String GetUsername() {
		return username;
	}	
	
	public String GetPassword() {
		return password;
	}
	
	public Credentials(String name, String surname, String username, String password) {
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.password = password;
	}

}
