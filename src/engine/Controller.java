package engine;

import java.util.Scanner;

import communication.entryPoint;

public class Controller extends Thread{


public FileManager fiMa;
	
	
public String AcceptedClientVersion = "0.7_4";
	
private Scanner in = new Scanner(System.in);



private boolean running = false;

private entryPoint EP;
	
	public Controller() {
		
		fiMa = new FileManager(this);
		
		fiMa.LoadLog();
		
		fiMa.loadTextures();
		
		if(fiMa.getVersion() != null) {
			AcceptedClientVersion = fiMa.getVersion();
		}
		
		EP = new entryPoint(this);
		
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
				System.out.println("-Status : Displays the Current server Status");
			break;
			
			case("-SetVersion"):
				System.out.println("Current acppeted version : " + AcceptedClientVersion);
				AcceptedClientVersion = in.nextLine();
				fiMa.setVersion(AcceptedClientVersion);
			break;
			
			case("-GetVersion"):
				System.out.println("Current acppeted version : " + AcceptedClientVersion);
			break;
			
			case("-Update"):
				System.out.println("Updating textures");
				fiMa.loadTextures();
			break;
			
			case("-Status"):
				System.out.println("Server Thread Alive ? : " + EP.isAlive());
				System.out.println("Server closed ? : " + EP.isServerClosed());
			break;
			
			case("-Close"):
				
				this.running = false;
				EP.closeServer();
				fiMa.CloseLog();
				System.exit(0);
				
			break;
			
			}
			
		}
		
	}
	
	
	
	public String getAcceptedClientVersion(){
		return AcceptedClientVersion;
	}
	
	
	public void Log(String toLog) {
		System.out.println("[" + java.time.LocalDate.now() + " / " + new String("" + java.time.LocalTime.now()).substring(0,8) + "] : " + toLog);
		fiMa.Log("[" + java.time.LocalDate.now() + " / " + new String("" + java.time.LocalTime.now()).substring(0,8) + "] : " + toLog);
	}
}
