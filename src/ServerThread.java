import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread {
    private Socket connection;
    private Server Server;
    private DataInputStream in;
    private ObjectInputStream objIn;
    private DataOutputStream out;
    private String nickName;

    public ServerThread(Server Server,Socket connection) throws IOException {
        this.Server = Server;
        this.connection = connection;
        this.in = new DataInputStream(connection.getInputStream());
        this.objIn = new ObjectInputStream(connection.getInputStream());
        this.out = new DataOutputStream(connection.getOutputStream());
        start();
    }

    public void welcome() throws IOException {
        String nickName = readString("Please Enter Your NickName To Chat In The Room:");
        this.Server.brodCast("Welcome " + nickName + " Enter The Chatting Room!");
        this.nickName = nickName;
        //this.Server.showAllUsers();
    }

    public String readString(String tip) throws IOException {
        this.out.writeUTF(tip);
        String res = new String("");
        while (true) {
            if (this.in.available() > 0) {
                res = this.in.readUTF();
                break;
            }
        }
        return res;
    }

    public void run() {
    	String[] arr;
        while (true) {
            try {
				if (this.objIn.available() > 0 || this.in.available() > 0) {
					System.out.println("yes");
				    arr = (String[])this.objIn.readObject();
				    if (arr[1].equals("0")) {
				    	this.Server.brodCast("Welcome " + arr[0] + " Enter The Chatting Room!");
				        this.nickName = arr[0];
				    } else if (arr[1].equals("1")) {
				    	this.Server.brodCast(this.nickName+"说: " + arr[0]);
				    }
				}
				sleep(500);
			} catch (IOException | InterruptedException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
    }

    public void say(String word) throws IOException {
        this.out.writeUTF(word);
        //out.flush();
    }

    protected String getNickName() {
        return this.nickName;
    }

}
