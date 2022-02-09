package communication;

import java.io.File;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import engine.Controller;

public class PacketManager {

	
private Controller con;
//private cryptingManager crMa;	
private User user;

private int readPos = 0;

private byte[] data = new byte[4096];

	public PacketManager(Controller Con,User User) {
		con = Con;
		user = User;
	}
	
	
	
	//returns the current Packet
	public byte[] getPacket() {
		return data;
	}

	//creates a new Packet for sending
	public void newPacket(String packetType) {
		
		data = new byte[0];
		
		String HEXid = "0x00";
		
		//Request MapList 0x0F
	
		//Request MapUpload 0x10
				
		//Request MapDownload 0x11
				
		//Request TextureList 0x14
				
		//Request TextureUpload 0x15
				
		//Request TextureDownload x16
				
		//--------------
				
		//public Key Packet 0xFF
				
		//Version Packet 0x0B
				
		//Request UpdateDownload Packet 0x0C
				
		//Disconnect Packet 0x0A
		
		switch(packetType) {
		
		//15 - 22
		
			case("RequestMapList"):
				HEXid = "0x0F";
				addToPacket(HEXid.getBytes());
			break;
			
			case("RequestMapUpload"):
				HEXid = "0x10";
				addToPacket(HEXid.getBytes());
			break;
			
			case("RequestMapDownload"):
				HEXid = "0x11";
				addToPacket(HEXid.getBytes());
			break;
			
			case("MapInfo"):
				HEXid = "0x13";
				addToPacket(HEXid.getBytes());
				addToPacket(mapInfo.getBytes());
			break;
			
			case("RequestTextureList"):
				HEXid = "0x14";
				addToPacket(HEXid.getBytes());
			break;
			
			case("RequestTextureUpload"):
				HEXid = "0x15";
				addToPacket(HEXid.getBytes());
			break;
			
			case("RequestTextureDownload"):
				HEXid = "0x16";
				addToPacket(HEXid.getBytes());
			break;
		
			//-------------------------------
		
		//10 - 12
			
			case("Key"):
				HEXid = "0xFF";
				addToPacket(HEXid.getBytes());
//				addToPacket(crMa.getPublicKey().getEncoded());
			break;
		
			case("Version"):
				HEXid = "0x0B";
				addToPacket(HEXid.getBytes());
				addToPacket(con.getAcceptedClientVersion().getBytes());
			break;
		
			case("RequestUpdate"):
				HEXid = "0x0C";
				addToPacket(HEXid.getBytes());
			break;
			
			case("Disconnect"):
				HEXid = "0x0A";
				addToPacket(HEXid.getBytes());
			break;
			
			// big files
			
		//50
			
			case("LatestStable"):
				HEXid = "0x32";
				addToPacket(HEXid.getBytes());
			break;
			
			case("LatestExperimental"):
				HEXid = "0x33";
				addToPacket(HEXid.getBytes());
			break;
			
		}
		
	}
	
	

	//private function to add Info to packet
	private void addToPacket(byte[] Data) {
		
		byte[] dataBuffer = data;
		
		data = new byte[dataBuffer.length + Data.length];
		
		int pos = 0;
		
		for(byte B : dataBuffer) {
			
			data[pos] = B;
			pos++;
		
		}
		
		for(byte B : Data) {
			
			data[pos] = B;
			pos++;
			
		}
		
	}








	private String mapInfo;
	
	//set the current packet for reading from it
	public void workPacket(byte[] Packet) {
		
		data = Packet;
		readPos = 0;
		
		String PID = getPacketType();
		
		System.out.print("[" + java.time.LocalDate.now() + " / " + new String("" + java.time.LocalTime.now()).substring(0,8) + "] : " + user.userName +" sended packet : " + PID);
		
		switch(PID) {
		
		case("0x0F"): //MapList
			con.Log(" - MapList ");
			//get map data
			String[][] LevelData = con.fiMa.getlevelData();
			//Send Level Data
			user.sendLevelData(LevelData);
		break;
		
		case("0x10"): //MapUpload from Client
			con.Log(" - MapUpload ");
			user.readFile();
		break;
		
		case("0x11"): //MapDownload
			con.Log(" - MapDownload ");
			//get Levelname
			String Levelname = getString();
			//get Level As File
			File[] mapFiles = con.fiMa.getMapFiles(Levelname);
			//write file
			user.writeFile(mapFiles,"Map");
			
		break;
		
		case("0x12"): //AssetsUpload from Client
			con.Log(" - Assets Upload ");
			user.readFile();
		break;
		
		case("0x13"): //MapInfo request from Client
			con.Log(" - MapInfo ");
			
			String LevelInfoName = getString();
		
			mapInfo = con.fiMa.getLevelInfo(LevelInfoName);
			
			user.write("MapInfo");
		break;
		
		case("0x14"): //TextureList
			con.Log(" - Texture List ");
			con.fiMa.loadTextures();
			user.writeFile(con.fiMa.getTextures("Arm"),"TexturesArm");
			user.writeFile(con.fiMa.getTextures("Belt"),"TexturesBelt");
			user.writeFile(con.fiMa.getTextures("Face"),"TexturesFace");
			user.writeFile(con.fiMa.getTextures("LegLeft"),"TexturesLegLeft");
			user.writeFile(con.fiMa.getTextures("LegRight"),"TexturesLegRight");
			user.writeFile(con.fiMa.getTextures("Torso"),"TexturesTorso");
		break;
		
		case("0x15"): //TextureUpload
			con.Log(" - Texture Upload ");
			user.readFile();
		break;
		
		case("0x16"): //TextureDownload
			con.Log(" - Texture Download ");
			//wont be used
		break;
		
		//-----------------------------------
		
		case("0xFF"): //Key Packet
			con.Log(" - Key Packet ");
			user.setPublicKey(getKey());
		break;
		
		case("0x0B"): //Version Packet
			con.Log(" - Version Packet ");
		break;
		
		case("0x0C"): //UpdateDownload Packet
			con.Log(" - Request UpdateDownload Packet ");
			//send Current Client
			
			String type = getString();
			
			File[] fileArray = con.fiMa.getClient(type);
		
			user.writeFile(fileArray,"ClientFiles");
			
			user.closeAll();
		break;
		
		case("0x0A"): //Disconnect Packet
			con.Log(" - Disconect Packet ");
			user.closeAll();
		break;
		
		// big files
		
		//50
						
		case("0x32"): // get latest Stable
			user.writeFile(con.fiMa.getGameStable(),"GameStable");
		break;
						
		case("0x33"): // get latest Experimental
			user.writeFile(con.fiMa.getGameExperimentel(),"GameExperimentel");
		break;
		
		case("0x20"): // upvote a map
			con.Log(" - Up Voteing Packet");
			
			String levelName = getString();
			
			
			con.fiMa.VoteLevel(levelName, user.userKey, +1);
		break;
		
		case("0x21"): // upvote a map
			con.Log(" - Down Voteing Packet");
			
			String levelName1 = getString();
			
			con.fiMa.VoteLevel(levelName1, user.userKey, -1);
		break;
		
		}
		
	}
















	//returns the Type of the Packet
	private String getPacketType() {
		
		//con.Log("Length:" + data.length);
		
		byte[] HexId = new byte[4];
		
		HexId[readPos] = data[readPos];
		readPos++;
		HexId[readPos] = data[readPos];
		readPos++;
		HexId[readPos] = data[readPos];
		readPos++;
		HexId[readPos] = data[readPos];
		readPos++;
		
		return new String(HexId);
	}
	
	//Returns the public Key from a Key packet
	private PublicKey getKey() {
		
		byte[] Key = new byte[data.length - readPos];
		
		int Pos = 0;
		for(@SuppressWarnings("unused") byte B : Key) {
			Key[Pos] = data[readPos];
			Pos++;
			readPos++;
		}
		
		PublicKey publicKey = null;
		
		try {
			
			publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Key));
			
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return publicKey;
	}


	private String getString() {
		
		byte[] String = new byte[data.length - readPos];
		
		int Pos = 0;
		for(@SuppressWarnings("unused") byte B : String) {
			String[Pos] = data[readPos];
			Pos++;
			readPos++;
		}
		
		return new String(String);
		
	}
	
}
