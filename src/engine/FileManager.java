package engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import java.util.zip.*;

public class FileManager {



	
	
	
	
	// Users
	
	public String CheckUser(String Name, String Key) {

		String curDir = new File("").getAbsolutePath();
		
		File UserFolder = new File(curDir + "/Users/");
		
		if(!UserFolder.exists()) {
			UserFolder.mkdir();
		}
		
		String[] Users = UserFolder.list();
		
		String userExist = "NewUser";
		
		for(String s : Users) {
			
			if(Name.equals(s)) {
				
				File userFile = new File(curDir + "/Users/" + s);
				
				FileReader fr;
				
				try {
					
					fr = new FileReader(userFile);
					BufferedReader br = new BufferedReader(fr);
					
					String uKey = br.readLine();
					
					br.close();
					
					if(!uKey.equals(Key)) {
						userExist = "UserNameInUse";
					}else {
						userExist = "LogIn";
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				
			}
			
		}
		
		if(userExist.equals("NewUser")) {
			addUser(Name,Key);
			userExist = "LogIn";
		}
		
		return userExist;
		
	}
	
	
	private void addUser(String Name, String Key) {
		
		String curDir = new File("").getAbsolutePath();
		
		File UserFolder = new File(curDir + "/Users/");
		
		if(!UserFolder.exists()) {
			UserFolder.mkdir();
		}
		
		// creat user file
		
		File userFile = new File(curDir + "/Users/" + Name);
		
		try {
			
			userFile.createNewFile();
			
			FileWriter FW = new FileWriter(userFile);
			
			BufferedWriter BW = new BufferedWriter(FW);
			
			BW.write(Key);
			BW.flush();
			
			BW.close();
			FW.close();
			
		} catch (IOException e) {
			System.out.println("Couldnt create File for user : " + Name);
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	public File[] getGameStable() {
		
		File[] gameFiles = new File[1];
		
		String curDir = new File("").getAbsolutePath();
		
		File gameZip = new File(curDir + "\\Game\\Stable.7z");
		
		gameFiles[0] = gameZip;
		
		return gameFiles;
		
	}

	public File[] getGameExperimentel() {
		
		File[] gameFiles = new File[1];
		
		String curDir = new File("").getAbsolutePath();
		
		File gameZip = new File(curDir + "\\Game\\Experimentel.7z");
		
		gameFiles[0] = gameZip;
		
		return gameFiles;
		
	}
	
	
	
	public File[] getClient(String type) {
		
		File[] clientFiles;
		
		String curDir = new File("").getAbsolutePath();
		
		File client;
		
		switch(type) {
		
		case("exejar"):
			clientFiles = new File[2];
		
			client = new File(curDir + "/ManicMinersModManager.exe");
			clientFiles[0] = client;
			
			client = new File(curDir + "/ManicMinersModManager.jar");
			clientFiles[1] = client;
		break;
		
		case("exe"):
			clientFiles = new File[1];
		
			client = new File(curDir + "/ManicMinersModManager.exe");
			clientFiles[0] = client;
		break;
		
		case("jar"):
			clientFiles = new File[1];
			client = new File(curDir + "/ManicMinersModManager.jar");
			clientFiles[0] = client;
		break;
		
		default:
			clientFiles = new File[1];
			client = new File(curDir + "/ManicMinersModManager.exe");
			clientFiles[0] = client;
		break;
		}
		
		return clientFiles;
		
	}
	
	
	
	//maps
	
	public boolean checkMapUser(String mapName,String userName,String userKey) {
		
		
		String curDir = new File("").getAbsolutePath();
		
		File LevelFile = new File(curDir + "/Levels/" + mapName);
		
		FileReader FR;
		
		try {
			
			FR = new FileReader(LevelFile);
			
			BufferedReader BR = new BufferedReader(FR);
			
			LinkedList<String> commentList = new LinkedList<String>();
			
			boolean commentEnd = false;
			
			while(!commentEnd) {
				
				try {
					
					String line = BR.readLine();
					
					commentList.add(line);
					
					if(line.contains("}")) {
						commentEnd = true;
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			BR.close();
			FR.close();
			
			String Author = "";
			int stringStart;
			
			for(String comment : commentList) {
				
				if(comment.contains("Author")) {
					
					stringStart = comment.indexOf(':') + 1;
					
					Author = comment.substring(stringStart,comment.length());
				}
				
			}
			
			if(Author.equals(userName)) {
				
				return true;
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	public File getMapAsFile(String levelname) {
		
		String RealName = levelname + ".dat";
		
		String curDir = new File("").getAbsolutePath();
		
		File LevelFile = new File(curDir + "/Levels/" + RealName);
		
		if(LevelFile.exists()) {
			return LevelFile;
		}
		
		System.out.println("[" + java.time.LocalDate.now() + " / " + java.time.LocalTime.now() + "] : " + "ERROR someone requst Level that dosnt exist");
		
		return null;
		
	}
	
	
	
	
	public String[][] getlevelData(){
		
		String curDir = new File("").getAbsolutePath();
		
//		System.out.println(curDir);
		
		File levelFolder = new File(curDir + "/Levels");
		
//		System.out.println(levelFolder.getAbsolutePath());
		
		List<String> LevelListList = Arrays.asList(levelFolder.list());
		
		LinkedList<String> LevelList = new LinkedList<String>();
		
		for(String item : LevelListList) {
			if(item.contains(".dat")) {
				LevelList.add(item);
			}
		}
		// 0 = LevenName
				// 1 = Author
				// 2 = LevelType
				// 3 = Length
				// 4 = voting
		
		String[][] levelData = new String[LevelList.size()][5];		
		
		int count = 0;
		for(String LevelName : LevelList) {
			
			File LevelFile = new File(curDir + "/Levels/" + LevelName);
			
			LinkedList<String> commentList = new LinkedList<String>();
			
			FileReader FR;
			
			try {
				
				FR = new FileReader(LevelFile);
				
				BufferedReader BR = new BufferedReader(FR);
				
				
				
				boolean commentEnd = false;
				
				while(!commentEnd) {
					
					try {
						
						String line = BR.readLine();
						
						commentList.add(line);
						
						if(line.contains("}")) {
							commentEnd = true;
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				
				BR.close();
				FR.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String Author = "";
			String LevelType1 = "";
			String LevelType2 = "";
			String Length = "";
			int stringStart;
			
			for(String comment : commentList) {
				
				if(comment.contains("Author")) {
					
					stringStart = comment.indexOf(':') + 1;
					
					Author = comment.substring(stringStart,comment.length());
				}
				
				if(comment.contains("LevelType1")) {
					
					stringStart = comment.indexOf(':') + 1;
					
					LevelType1 = comment.substring(stringStart,comment.length());
				}
				
				if(comment.contains("LevelType2")) {
					
					stringStart = comment.indexOf(':') + 1;
					
					LevelType2 = comment.substring(stringStart,comment.length());
				}
				
				if(comment.contains("Length")) {
					
					stringStart = comment.indexOf(':') + 1;
					
					Length = comment.substring(stringStart,comment.length());
				}
			}
			
			
			
			levelData[count][0] = LevelName.substring(0,LevelName.length() - 4);
			levelData[count][1] = Author;
			levelData[count][2] = LevelType1 + "," + LevelType2;
			levelData[count][3] = Length;
			
			//voting from extra file 
			
			File LevelVoteFile = new File(curDir + "/Levels/" + LevelName.substring(0,LevelName.length() - 4) + ".inf");
			
			String voteString = "0";
			
			if(LevelVoteFile.exists()) {
				
				try {
					
					FR = new FileReader(LevelVoteFile);
					
					BufferedReader BR = new BufferedReader(FR);
					
					try {
						
						voteString = BR.readLine();
						
						BR.close();
						FR.close();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
			}
			
			levelData[count][4] = voteString;
			
			count++;
		}
		
		
		
		return levelData;
		
	}
	
	public String getLevelInfo(String LevelName) {
		
		LinkedList<String> commentList = new LinkedList<String>();
		
		String curDir = new File("").getAbsolutePath();
		
		File LevelFile = new File(curDir + "/Levels/" + LevelName + ".dat");
		
		FileReader FR;
		
		try {
			
			FR = new FileReader(LevelFile);
			
			BufferedReader BR = new BufferedReader(FR);
			
			
			
			boolean commentEnd = false;
			
			while(!commentEnd) {
				
				try {
					
					String line = BR.readLine();
					
					if(line.contains("}")) {
						commentEnd = true;
					}else {
						commentList.add(line);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			BR.close();
			FR.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String Author = "";
		String LevelType1 = "";
		String LevelType2 = "";
		String Length = "";
		String AssetPath = "";
		String Info = "";
		int stringStart;
		
		boolean infoText = false;
		
		for(String comment : commentList) {
			
//			System.out.println(comment);
			
			if(comment.contains("Author")) {
				
				stringStart = comment.indexOf(':') + 1;
				
				Author = comment.substring(stringStart,comment.length());
			}
			
			if(comment.contains("LevelType1")) {
				
				stringStart = comment.indexOf(':') + 1;
				
				LevelType1 = comment.substring(stringStart,comment.length());
				
			}
			
			if(comment.contains("LevelType2")) {
				
				stringStart = comment.indexOf(':') + 1;
				
				LevelType2 = comment.substring(stringStart,comment.length());
				
			}
			
			if(comment.contains("Length")) {
				
				stringStart = comment.indexOf(':') + 1;
				
				Length = comment.substring(stringStart,comment.length());
			}
			
			if(infoText) {
				Info = Info + "\n" + comment;
			}
			
			if(comment.contains("Info")) {
				
				stringStart = comment.indexOf(':') + 1;
				
				Info = comment.substring(stringStart,comment.length());
				
				infoText = true;
			}
			
			if(comment.contains("Asset")) {
				
				stringStart = comment.indexOf(':') + 1;
				
				AssetPath = comment.substring(stringStart,comment.length());
				
			}
			
		}
		
		return Info;
		
	}
	
	public void VoteLevel(String LevelName,String User,int vote) {
		
		String curDir = new File("").getAbsolutePath();
		
		File LevelVoteFile = new File(curDir + "/Levels/" + LevelName.substring(0,LevelName.length()) + ".inf");
		
		if(LevelVoteFile.exists()) {
			
			LinkedList<String> text = new LinkedList<String>();
			
			try {
				
				FileReader FR = new FileReader(LevelVoteFile);
				
				BufferedReader BR = new BufferedReader(FR);
				
				try {
					
					String readLine = "";
					
					int curvote = 0;
					
					text.add(BR.readLine());
					
					
					while((readLine = BR.readLine()) != null) {
						text.add(readLine); // Username
						
						String val = BR.readLine(); // vote val
						
						if(readLine.equals(User)) {
							text.add(""+vote);
							curvote += vote;
						}else {
							text.add(val);
							curvote += Integer.parseInt(val);
						}
						
					}
					
					BR.close();
					FR.close();
					
					if(!text.contains(User)) {
						
						text.add(User);
						text.add("" + vote);
						
						text.set(0,"" + (curvote + vote ));
					}else {
						
						text.set(0,"" + curvote);
					
					}
					
					
					FileWriter FW = new FileWriter(LevelVoteFile);
					
					BufferedWriter BW = new BufferedWriter(FW);
					
					for(String s : text) {
						
						BW.write(s);
						BW.newLine();
						
					}
					
					BW.flush();
					BW.close();
					FW.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}else {
			try {
				
				LevelVoteFile.createNewFile();
				
				FileWriter FW = new FileWriter(LevelVoteFile);
				
				BufferedWriter BW = new BufferedWriter(FW);
				
				BW.write(vote + "");
				BW.newLine();
				BW.write(User);
				BW.newLine();
				
				BW.close();
				FW.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	
	
	
	
	//textures 
	
	private LinkedList<File> ArmFolder;
	private LinkedList<File> BeltFolder;
	private LinkedList<File> FaceFolder;
	private LinkedList<File> LegLeftFolder;
	private LinkedList<File> LegRightFolder;
	private LinkedList<File> TorsoFolder;
	
	public File[] getTextures(String type){
		
		File[] fileList;
		
		switch(type) {
		
		case("Arm"):
			fileList = ArmFolder.toArray(new File[ArmFolder.size()]);
		break;
		
		case("Belt"):
			fileList = BeltFolder.toArray(new File[BeltFolder.size()]);
		break;
		
		case("Face"):
			fileList = FaceFolder.toArray(new File[FaceFolder.size()]);
		break;
		
		case("LegLeft"):
			fileList = LegLeftFolder.toArray(new File[LegLeftFolder.size()]);
		break;
		
		case("LegRight"):
			fileList = LegRightFolder.toArray(new File[LegRightFolder.size()]);
		break;
		
		case("Torso"):
			fileList = TorsoFolder.toArray(new File[TorsoFolder.size()]);
		break;
		
		default:
			fileList = ArmFolder.toArray(new File[ArmFolder.size()]);
		break;
		
		}
		
		
		
		return fileList;
		
	}
	
	public void loadTextures(){
		
		ArmFolder = new LinkedList<File>();
		BeltFolder = new LinkedList<File>();
		FaceFolder = new LinkedList<File>();
		LegLeftFolder = new LinkedList<File>();
		LegRightFolder = new LinkedList<File>();
		TorsoFolder = new LinkedList<File>();
		
		String curDir = new File("").getAbsolutePath();
		
		File textureMainFolder = new File(curDir + "/Textures/");
		
		if(!textureMainFolder.exists()) {
			textureMainFolder.mkdir();
		}
		
		File armFolder = new File(curDir + "/Textures/Arm");
		File beltFolder = new File(curDir + "/Textures/Belt");
		File faceFolder = new File(curDir + "/Textures/Face");
		File legLeftFolder = new File(curDir + "/Textures/LegLeft");
		File legRightFolder = new File(curDir + "/Textures/LegRight");
		File torsoFolder = new File(curDir + "/Textures/Torso");
		
		if(!armFolder.exists()) {
			armFolder.mkdir();
		}
		
		if(!beltFolder.exists()) {
			beltFolder.mkdir();
		}
		
		if(!faceFolder.exists()) {
			faceFolder.mkdir();
		}
		
		if(!legLeftFolder.exists()) {
			legLeftFolder.mkdir();
		}
		
		if(!legRightFolder.exists()) {
			legRightFolder.mkdir();
		}
		
		if(!torsoFolder.exists()) {
			torsoFolder.mkdir();
		}
		
		String[] textureMainFolderList = textureMainFolder.list();
		
//		System.out.println(" FM ");
//		System.out.println("Folder List : ");
		
		for(String SubFolder : textureMainFolderList) {
			
//			System.out.println(" ");
//			System.out.println("	-" + SubFolder + "-");
			
			String[] folderItems = new File(curDir + "/Textures/" + SubFolder).list();
			
//			System.out.println("		Texture List : ");
			
			for(String texture : folderItems) {
				
//				System.out.println("		-" + texture + "-");
				
				if(texture.contains(".png")) {
					
					if(SubFolder.contains("Arm")) {
						ArmFolder.add(new File(curDir + "/Textures/" + SubFolder + "/" + texture));
					}
					
					if(SubFolder.contains("Belt")) {
						BeltFolder.add(new File(curDir + "/Textures/" + SubFolder + "/" + texture));
					}
					
					if(SubFolder.contains("Face")) {
						FaceFolder.add(new File(curDir + "/Textures/" + SubFolder + "/" + texture));
					}
					
					if(SubFolder.contains("LegLeft")) {
						LegLeftFolder.add(new File(curDir + "/Textures/" + SubFolder + "/" + texture));
					}
					
					if(SubFolder.contains("LegRight")) {
						LegRightFolder.add(new File(curDir + "/Textures/" + SubFolder + "/" + texture));
					}
					
					if(SubFolder.contains("Torso")) {
						TorsoFolder.add(new File(curDir + "/Textures/" + SubFolder + "/" + texture));
					}
					
				}
				
			}
			
		}
		
	}
	
	
	
	
	// Log File
	private BufferedWriter LogFileWriter;
	
	public void LoadLog() {
		String curDir = new File("").getAbsolutePath();
		
		File LogFile = new File(curDir + "/" + java.time.LocalDate.now() + "Server.log");
		
		try {
			
			LogFileWriter = new BufferedWriter(new FileWriter(LogFile));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void CloseLog() {
		
		try {
			
			LogFileWriter.flush();
			LogFileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void Log(String toLog) {
		
		try {
			
			LogFileWriter.append(toLog);
			LogFileWriter.newLine();
			LogFileWriter.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
