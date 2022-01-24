package engine;

import java.util.Scanner;

import communication.entryPoint;

public class Controller extends Thread{


public FileManager fiMa;
	
	
public String AcceptedClientVersion = "0.6_3";
	
private Scanner in = new Scanner(System.in);



private boolean running = false;

	
	public Controller() {
		
		fiMa = new FileManager();
		
		fiMa.loadTextures();
		
		entryPoint EP = new entryPoint(this);
		
		EP.start();
		
		running = true;
	}
	
	
	
	public void run(){
		
		
		while(running) {
			
			String input = in.nextLine();
		
			switch(input) {
			
			case("/Help"):
				System.out.println("-SetVersion : to set acceptedClient Version");
				System.out.println("-GetVersion :  to see acceptedClient Version");
				System.out.println("-Update : (Updates Database of Maps Textures and Client");
			break;
			
			case("-SetVersion"):
				System.out.println("Current acppeted version : " + AcceptedClientVersion);
				AcceptedClientVersion = in.nextLine();
			break;
			
			case("-GetVersion"):
				System.out.println("Current acppeted version : " + AcceptedClientVersion);
			break;
			
			case("-Update"):
				System.out.println("Updating textures");
				fiMa.loadTextures();
			break;
			
			}
			
		}
		
	}
	
	
	
	public String getAcceptedClientVersion(){
		return AcceptedClientVersion;
	}
	
	
	public void Log(String toLog) {
		System.out.println("[" + java.time.LocalDate.now() + " / " + new String("" + java.time.LocalTime.now()).substring(0,8) + "] : " + toLog);
	}
}
