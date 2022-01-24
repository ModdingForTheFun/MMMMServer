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
				
				DataOutputStream  tempOut = new DataOutputStream(Client.getOutputStream());
				
				tempOut.flush();
				
				DataInputStream  tempIn = new DataInputStream(Client.getInputStream());
				
				//test if it is a client
				
				String ID = tempIn.readUTF();
				
				if(ID.equals("MMMM_Client_03hg983tz3pgn3uzﬂ3toj09")) {
					con.Log("A Client connected");
					
					User newUser = new User(con,Client,tempIn,tempOut);
					
					newUser.start();
				}else {
					con.Log("Something else Tryt to connect");
					con.Log("Sended , As String : " + ID);
					Client.close();
				}
				
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
