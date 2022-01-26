package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.PublicKey;
import java.time.LocalDateTime;

import engine.Controller;

public class User extends Thread{

private Controller con;
	
private Boolean connected = false;
	
private Socket client;

private DataInputStream dataIn;
private DataOutputStream dataOut;

private PacketManager PaMa;
//private cryptingManager crMa;

public String userName = "Waiting For Keypairing";
public String userKey;

private PublicKey clientKey;
private boolean firstPacket = true;


private LocalDateTime deathTime;

	public User(Controller Con,Socket Client,DataInputStream in,DataOutputStream out) {
		
		con = Con;
		client = Client;
		dataIn = in;
		dataOut = out;
		
//		crMa = new cryptingManager();
	}
	
	
	
	//setPublicKeyOfServer
	public void setPublicKey(PublicKey sKey) {
//		System.out.println("Pup Key from Client : " + sKey);
		clientKey = sKey;
	}
	
	
	public void closeAll() {
		
		connected = false;
		
		try {
			
			dataOut.close();
			dataIn.close();
			client.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private boolean MMMMClient() {
		
		//verify User
		
				String ID;
				
				try {
					
					ID = dataIn.readUTF();
					
					con.Log("Read ID");
					
					if(ID.equals("MMMM_Client_03hg983tz3pgn3uzﬂ3toj09")) {
						con.Log("A Client connected");
						return true;
					}else {
						con.Log("Something else Tryt to connect");
						con.Log("Sended , As String : " + ID);
						client.close();
						client = null;
						return false;
					}
					
				} catch (IOException e2) {
					con.Log("Something else Tryt to connect");
					//e2.printStackTrace();
				}
		
				
				return false;
	}
	
	public void run() { //Zeitstopper rein packen von ca 1 stunde
		
		
		if(MMMMClient()) {
			

			//create shutdown Timer
			LocalDateTime currentTime = LocalDateTime.now();
			deathTime = currentTime.plusHours(1);
			
			//create PacketManager
					PaMa = new PacketManager(con,this);
			
					
			//create crypto Key
//			crMa.createKey();
			//TODO reactivaet when needed
			
//			System.out.println("send p key of server");
//			write("Key");
			
			
//			System.out.println("read p key of client");
//			read();
			
			// Username
			
			//set username
			try {
					
				byte[] UserName;
				
				UserName = new byte[dataIn.readInt()];
				dataIn.readFully(UserName);
				
//				UserName = crMa.decryptPacket(UserName);
				
				userName = new String(UserName);
						
				System.out.println("decoded name : " + userName);
				
				byte[] UserKey = new byte[dataIn.readInt()];
				dataIn.readFully(UserKey);
				
				userKey = new String(UserKey);
				
				String answer = con.fiMa.CheckUser(userName, userKey);
				
				dataOut.writeInt(answer.length());
				dataOut.write(answer.getBytes());
				dataOut.flush();
				
				if(!answer.equals("LogIn")) {
					
					return;
					
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
//			System.out.println("write Accepted Ver to Client");
			write("Version");
			
			connected = true;
				
			con.Log("User " + userName + " Connected succesfully");
			
			while(connected) {
				
				
				read();
				
				
				currentTime = LocalDateTime.now();
				
				if(currentTime.isAfter(deathTime)) {
					try {
						
						dataIn.close();
						dataOut.close();
						client.close();
					
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				
			}
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void read() {
		
		byte[] Packet = new byte[4096];
		
		try {
			
			Packet = new byte[dataIn.readInt()];
			
			dataIn.readFully(Packet);
			
//			Packet = crMa.decryptPacket(Packet);
			
//			System.out.println("Packet lenght Read:" + Packet.length);
			
			PaMa.workPacket(Packet);
			
		} catch (IOException e) {
			
			connected = false;
			
			e.printStackTrace();
		}
		
	}
	
	public void write(String packetType) {
		
		try {
			
			PaMa.newPacket(packetType);
			
			byte[] Packet = PaMa.getPacket();
			
			if(!firstPacket) {
//				Packet = crMa.encryptPacket(Packet, clientKey);
			}else {
				firstPacket = false;
			}
			
			//System.out.println("Packet lenght Write:" + Packet.length);
			
			dataOut.writeInt(Packet.length);
			dataOut.write(Packet);
			dataOut.flush();
			
		} catch (IOException e) {
			
			connected = false;
			
			e.printStackTrace();
		}
		
	}
	
	
	
	
	//File--------
	
	public void writeFile(File[] fileList,String fileType) {
		
//		System.out.println("Writing Files : "  + fileList.length);
//		System.out.println("File Type : " + fileType);
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		try {
			
			//send how many files will be send
			dataOut.writeInt(fileList.length);
			dataOut.flush();
			
			for(File file : fileList) {
				
				boolean isTexture = true;
				
//				System.out.println("File Name : " + file.getName());
				
				byte[] FileName = file.getName().getBytes();
				
				byte[] InfoPacket = new byte[1];
				
				InfoPacket[0] = (byte)FileName.length;
				
				byte[] HexId = new byte[4];
				
				switch(fileType) {
				
				case("ClientFiles"):
					isTexture = false;
					HexId = new String("0x32").getBytes();
				break;
				
				case("Map"):
					isTexture = false;
					HexId = new String("0x33").getBytes();
				break;
				
				case("MapAssets"):
					isTexture = false;
					HexId = new String("0x34").getBytes();
				break;
				
				case("TexturesArm"):
					HexId = new String("0x3C").getBytes();
				break;
				
				case("TexturesBelt"):
					HexId = new String("0x3D").getBytes();
				break;
				
				case("TexturesFace"):
					HexId = new String("0x3E").getBytes();
				break;
				
				case("TexturesLegLeft"):
					HexId = new String("0x3F").getBytes();
				break;
				
				case("TexturesLegRight"):
					HexId = new String("0x40").getBytes();
				break;
				
				case("TexturesTorso"):
					HexId = new String("0x41").getBytes();
				break;
				
				//game
				
				case("GameStable"):
					HexId = new String("0x50").getBytes();
				break;
				
				case("GameExperimentel"):
					HexId = new String("0x51").getBytes();
				break;
				
				}
				
				byte[] dataBuffer = InfoPacket;
				
				InfoPacket = new byte[dataBuffer.length + FileName.length];
				
				int pos = 0;
				
				for(byte B : dataBuffer) {
					
					InfoPacket[pos] = B;
					pos++;
				
				}
				
				for(byte B : FileName) {
					
					InfoPacket[pos] = B;
					pos++;
					
				}
				
				dataBuffer = InfoPacket;
				
				InfoPacket = new byte[dataBuffer.length + HexId.length];
				
				pos = 0;
				
				for(byte B : dataBuffer) {
					
					InfoPacket[pos] = B;
					pos++;
				
				}
				
				for(byte B : HexId) {
					
//					System.out.println("ADDING ID VAL : " + B);
					
					InfoPacket[pos] = B;
					pos++;
					
				}
				
				//info packet ready
				
				//crypt info packet
//				InfoPacket = crMa.encryptPacket(InfoPacket, clientKey);
				
				//send packet size
				dataOut.writeInt(InfoPacket.length);
				dataOut.flush();
				
				//send info packet
				dataOut.write(InfoPacket);
				dataOut.flush();
			
				//send file size
				dataOut.writeLong(file.length());
				dataOut.flush();
				
				
			    long filePos = 0;
			    
//			    System.out.println("File Size : " + file.length());
			    
//				System.out.println("filePos : " + filePos);
			    
			    byte[] FILE = new byte[(int) file.length()];
			    
			    try(FileInputStream inputStream = new FileInputStream(file)){

					FILE = inputStream.readAllBytes();
					
				}
				catch (Exception ex){
					 
					ex.printStackTrace();
				
				}
			    
				while(filePos < file.length()) {
					
					byte[] filePart = new byte[4096];
					
					if((file.length() - filePos)>= 4096) {
						filePart = new byte[4096];
					}else {
						filePart = new byte[(int) (file.length() - filePos)];
					}
					
					int count = 0;
					
					while(count < filePart.length) {
						
						filePart[count] = FILE[(int) filePos];
						
						count++;
						filePos++;
					}
					//System.out.println("Writing " + filePart.length + " bytes of File");
					
					dataOut.writeInt(filePart.length);
//					filePart = crMa.encryptPacket(filePart, clientKey);
					dataOut.write(filePart);
					dataOut.flush();
					
//					System.out.println("filePos : " + (int)filePos);
				}

				
//				System.out.println("---------------");
//				System.out.println("filePos : " + filePos);
				if(!isTexture) {
				System.out.println("Finisht File : " + file.getName());
				}
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public void readFile() {
		
		String curDir = new File("").getAbsolutePath();
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		byte[] Packet = new byte[4096];
		
		try {
			
			//TODO Add Crypto
			//buffer = crMa.decryptPacket(buffer);
			
			int FileAmount = dataIn.readInt();
			int FileCount = 1;
			
//			System.out.println("File Amount : " + FileAmount);
			
			//Loop for lotta files here
			
			while(FileAmount >= FileCount) {
				
				// Get InfoPacketSize
				Packet = new byte[dataIn.readInt()];
				
				//get Info Packet
				dataIn.readFully(Packet);
				
				//TODO Add Crypto
				//buffer = crMa.decryptPacket(buffer);
				
				//Get File Name from packet
				int nameSize = Packet[0];
				
				byte[] String = new byte[nameSize];
				
				int Pos = 0;
				int readPos = 1;
				for(@SuppressWarnings("unused") byte B : String) {
					String[Pos] = Packet[readPos];
					Pos++;
					readPos++;
				}
				
				String FileName = new String(String);
				
//				System.out.println("File Name : " + FileName);
				
				//Get File Type    Map , MapAssets , TextureXXX 
				// 0x32 = Client Update
				// 0x33 = Map
				// 0x34 = MapAsset (sound only)
				// 0x3C = Textures Arm
				// 0x3D = Belt
				// 0x3E = Face
				// 0x3F = LegLeft
				// 0x40 = LegRight
				// 0x41 = Torso
				
				
				//get HexId of File Type
				byte[] HexId = new byte[4];
				
				HexId[0] = Packet[readPos];
				readPos++;
				HexId[1] = Packet[readPos];
				readPos++;
				HexId[2] = Packet[readPos];
				readPos++;
				HexId[3] = Packet[readPos];
				readPos++;
				
				String ID =  new String(HexId);
				
				//TODO Add Crypto
				//buffer = crMa.decryptPacket(buffer);
				
//				System.out.println("File ID : " + ID);
				
				byte[] buffer = new byte[4096];
				
				File FILE = new File(FileName);
				
				switch(ID) {
				
				
				case("0x33"):
					FILE = new File("Levels/" + FileName);
				break;
				
				case("0x34"):
					
					byte[] LevelFolderName = new byte[Packet.length - readPos];
				
					int LFNcount = 0;
					while(LFNcount < LevelFolderName.length) {
						
						LevelFolderName[LFNcount] = Packet[readPos];
						
						LFNcount++;
						readPos++;
					}
					
					String LevelName = new String(LevelFolderName);
					
					FILE = new File("Levels/ASSETS/Sounds/" + LevelName);
					
					if(!FILE.exists()) {
						FILE.mkdir();
					}
					
					FILE = new File("Levels/ASSETS/Sounds/" + LevelName + "/" + FileName);
				break;
				
				case("0x3C"):
					FILE = new File(curDir + "/" + "Textures/Arm");
					
					if(!FILE.exists()) {
						FILE.mkdir();
					}
					
					FILE = new File(curDir + "/" + "Textures/Arm/" + FileName);
				break;
				
				case("0x3D"):
					FILE = new File(curDir + "/" + "Textures/Belt");
					
					if(!FILE.exists()) {
						FILE.mkdir();
					}
					
					FILE = new File(curDir + "/" + "Textures/Belt/" + FileName);
					
				break;
				
				case("0x3E"):
					FILE = new File(curDir + "/" + "Textures/Face");
					
					if(!FILE.exists()) {
						FILE.mkdir();
					}
					
					FILE = new File(curDir + "/" + "Textures/Face/" + FileName);
					
				break;
				
				case("0x3F"):
					FILE = new File(curDir + "/" + "Textures/LegLeft");
					
					if(!FILE.exists()) {
						FILE.mkdir();
					}
					
					FILE = new File(curDir + "/" + "Textures/LegLeft/" + FileName);
					
				break;
				
				case("0x40"):
					FILE = new File(curDir + "/" + "Textures/LegRight");
					
					if(!FILE.exists()) {
						FILE.mkdir();
					}
					
					FILE = new File(curDir + "/" + "Textures/LegRight/" + FileName);
					
				break;
				
				case("0x41"):
					FILE = new File(curDir + "/" + "Textures/Torso");
					
					if(!FILE.exists()) {
						FILE.mkdir();
					}
					
					FILE = new File(curDir + "/" + "Textures/Torso/" + FileName);
					
				break;
				
				}//case end
				
				boolean newFile = false;
				
//				System.out.println("Does the file exists ? ");
				
				if(FILE.exists()) {
//					System.out.println("	Yes");
					
					// is it your file ? TODO what about textures
					
					String FP = FILE.getAbsolutePath();
					
					if(FP.contains("/Textures/")) {
						
						dataOut.writeInt(0);
						
					}else 
					if(con.fiMa.checkMapUser(FileName,userName, userKey)) {
						
						newFile = true;
						FILE.createNewFile();
						dataOut.writeInt(1);
						
					}else {
						dataOut.writeInt(0);
					}
					
				}else {
//					System.out.println("	No");
					dataOut.writeInt(1);
					
					newFile = true;
					FILE.createNewFile();
				}
				
				dataOut.flush();
				
				if(newFile) {
					
					long FileSize = (int) dataIn.readLong();//ActualFileSize
					
					FileOutputStream FileWriter = new FileOutputStream(FILE);;
					
					while(FILE.length() < FileSize) {
						
						//read first MAX 4096 bytes of file
						buffer = new byte[dataIn.readInt()];
						dataIn.readFully(buffer);
						
						FileWriter.write(buffer);
						
					}
					
					FileWriter.flush();
					
					if(FileWriter != null) {
						FileWriter.close();
					}
					
				}
				
//				System.out.println("File Size : " + FILE.length());
				
				FileCount++;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		System.out.println("End");
		
	}
	
	
	
	
	
	
	
	//Level Files
	
	public void sendLevelData(String[][] levelData) {
		
		// 1 = level 2 = Level data
		// 0 = LevenName
		// 1 = Author
		// 2 = LevelType
		// 3 = Length
		// 4 = voting
		
		try {
			
			dataOut.writeInt(levelData.length);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for(String[] dataAr : levelData) {
			
			for(String info : dataAr){
				
				try {
					dataOut.writeInt(info.getBytes().length);
					dataOut.write(info.getBytes());
					dataOut.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
