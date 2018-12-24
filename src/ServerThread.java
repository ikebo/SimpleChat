import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread {
    private Socket connection;
    private Server Server;
    private DataInputStream in;
    private DataOutputStream out;
    private String nickName;

    public ServerThread(Server Server,Socket connection) throws IOException {
        this.Server = Server;
        this.connection = connection;
        this.in = new DataInputStream(connection.getInputStream());
        this.out = new DataOutputStream(connection.getOutputStream());
        start();
    }

    public void welcome() throws IOException {
        String nickName = readString("Please Enter Your NickName To Chat In The Room:");
        this.Server.brodCast("Welcome " + nickName + " Enter The Chatting Room!");
        this.nickName = nickName;
        this.Server.showAllUsers();
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
        try {
            welcome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void say(String word) throws IOException {
        out.writeUTF(word);
    }

    protected String getNickName() {
        return this.nickName;
    }

}
