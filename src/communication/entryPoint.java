package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import engine.Controller;

public class entryPoint extends Thread{


private Controller con;
	
	
//enable disabling peopel connecting	
private boolean isRunning = true;
	
//the server
private Socket Client;

private ServerSocket Server;

//port
private int port = 8888;
	
	public entryPoint(Controller Con) {
		
		con = Con;
		
	}
	
	
	
	public boolean isServerClosed() {
		return Server.isClosed();
	}
	
	public boolean isClientNull() {
		if(Client == null) {
			return true;
		}
		
		return false;
	}
	
	public void closeServer() {
		isRunning=false;
		
		try {
			
			Server.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		
		try {
			Server = new ServerSocket(port);
			Server.setSoTimeout(0);
			
			con.Log("Server is Running.");
			con.Log("");
			
			isRunning = true;
			
		} catch (IOException e) {
			con.Log("Couldnt create server.");
			con.Log("Please restart the server.");
			con.Log("");
			e.printStackTrace();
		}
		
		while(isRunning && !Server.isClosed()) {
			
			try {
				
				con.Log("Waiting for Connection");
				
				Client = Server.accept();
				
				con.Log("Server did accept");
				
				DataOutputStream  tempOut = new DataOutputStream(Client.getOutputStream());
				
				con.Log("Createt output");
				
				tempOut.flush();
				
				con.Log("Flushed output");
				
				DataInputStream  tempIn = new DataInputStream(Client.getInputStream());
				
				con.Log("Createt input");
				
				//test if it is a client
				
				User newUser = new User(con,Client,tempIn,tempOut);
				
				newUser.start();
				
			} catch (IOException e) {
				con.Log("Problem accepting a Client");
				//e.printStackTrace();
			}
			
		}
		
		System.out.println("Trying to close the server");
		
		try {
			
			Server.close();
			
		} catch (IOException e) {
			con.Log("Error trying to close Server");
		}
		
		System.out.println("Closed the server");
		
	}
	
	
}
