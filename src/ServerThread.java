import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread {
    private Socket connection;
    private Server Server;
    private DataInputStream in;
    private ObjectInputStream objIn;
    private DataOutputStream out;
    private ObjectOutputStream objOut;
    private String nickName;
    private boolean running;

    public ServerThread(Server Server,Socket connection) throws IOException {
        this.Server = Server;
        this.connection = connection;
        this.in = new DataInputStream(connection.getInputStream());
        this.objIn = new ObjectInputStream(connection.getInputStream());
        this.out = new DataOutputStream(connection.getOutputStream());
        this.objOut = new ObjectOutputStream(connection.getOutputStream());
        this.running = true;
        start();
    }


    public void run() {
    	String[] arr;
        while (this.running) {
            try {
				if (this.objIn.available() > 0 || this.in.available() > 0) {
					System.out.println("yes");
				    arr = (String[])this.objIn.readObject();
				    if (arr[1].equals("0")) {
				    	this.Server.brodCast(arr[0] + "已加入群聊", "0");
				        this.nickName = arr[0];
				        this.Server.showAllUsers();  // 广播在线用户
				    } else if (arr[1].equals("1")) {
				    	this.Server.brodCast(this.nickName+"说: " + arr[0], "0");
				    } else if (arr[1].equals("-1")) {
				    	this.running = false;
				    	System.out.println(this.nickName + "下线");
				    	this.Server.brodCast(this.nickName + "已退出群聊", "0");
				    	this.in.close();
				    	this.out.close();
				    	this.Server.removeHandler(this);
				    	this.Server.showAllUsers();   // 广播在线用户
				    	this.Server.printHandlersCount();
				    }
				}
				sleep(500);
			} catch (IOException | InterruptedException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
    }

    public void say(String word, String type) throws IOException {
        this.objOut.writeObject(new String[] {word, type});
        this.objOut.flush();
    }

    protected String getNickName() {
        return this.nickName;
    }

}
