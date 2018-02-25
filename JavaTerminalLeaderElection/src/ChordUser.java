import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ChordUser {
	int port;
	static Chord chord;

	private long md5(String objectName) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(objectName.getBytes());
			BigInteger bigInt = new BigInteger(1, m.digest());
			return Math.abs(bigInt.longValue());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();

		}
		return 0;
	}

	public ChordUser(int p) {
		port = p;

		Timer timer1 = new Timer();
		timer1.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				long guid = md5("" + port);

				try {
					chord = new Chord(port, guid);

					Files.createDirectories(Paths.get(guid + "/repository"));
				} catch (IOException e) {
					e.printStackTrace();

				}


				Scanner scan = new Scanner(System.in);
				String delims = "[ ]+";
				while (true) {
					System.out.println(
							"Usage: \n\tjoin <ip> <port>\n\twrite <file> (the file must be an integer stored in the working directory, i.e, ./"
									+ guid + "/file)");
					System.out.println("\tread <file>\n\tdelete <file>\n\tprint\n\tleave");
					String text = scan.nextLine();
					String[] tokens = text.split(delims);
					if (tokens[0].equals("join") && tokens.length == 3) {
						try {
							int portToConnect = Integer.parseInt(tokens[2]);
							chord.joinRing(tokens[1], portToConnect);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else if (tokens[0].equals("print")) {
						chord.Print();
					}
					else if (tokens[0].equals("write") && tokens.length == 2) {				
						try {
							long guidObject = md5(tokens[1]);
							String fileName = "./" + guid + "/" + tokens[1]; 
							//make the tokens[1]							
			 				InputStream file = new FileStream(fileName); //getting it from . directory.
							ChordMessageInterface peer = chord.locateSuccessor(guidObject);
							peer.put(guidObject, file); // put file into ring
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else if (tokens[0].equals("read") && tokens.length == 2) {
						long guidObject = md5(tokens[1]);
						try {
							
							//String fileName = "./" + guid + "/" + tokens[1];
							
							ChordMessageInterface peer = chord.locateSuccessor(guidObject);
							InputStream inputStream = peer.get(guidObject);
							
							String fileName = "./" + peer.getId() + "/repository/" + guidObject;
							
							Scanner reader = new Scanner(new File(fileName)); //read from this file
							FileOutputStream output = new FileOutputStream(fileName);
							
							while (inputStream.available() > 0)
								output.write(inputStream.read()); //wrote to file
							
							output.close();
							
							System.out.println(fileName + " :");
							while(reader.hasNext())
								System.out.println(reader.nextLine());
							
							reader.close();

						} catch (IOException e) {
							System.out.println(e);
						}

					}
					else if (tokens[0].equals("delete") && tokens.length == 2) {
						try {
							long guidObject = md5(tokens[1]);
							ChordMessageInterface peer = chord.locateSuccessor(guidObject);
							peer.delete(guidObject);
						} catch (IOException e) {
							System.out.println(e);
						}
					}
					else if (tokens[0].equals("leave")) {
						scan.close();
						try {
							chord.quit();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						System.out.println("Goodbye.");
						System.exit(1);
					}
				}
			}
		}, 1000, 1000);
	}

	@SuppressWarnings("unused")
	static public void main(String args[]) {
		if (args.length < 1) {
			throw new IllegalArgumentException("Parameter: <port>");
		}
		try {
			ChordUser chordUser = new ChordUser(Integer.parseInt(args[0]));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
