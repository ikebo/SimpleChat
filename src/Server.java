import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

public class Server {
    protected LinkedList<ServerThread> handlers;
    private ServerSocket server;

    public Server() throws IOException {
        this.server = new ServerSocket(6000);
        this.handlers = new LinkedList<>();
        System.out.println("Server started on " + server.getLocalPort());
        startServer();
    }


    public void startServer() throws IOException {
        while (true) {
            Socket connection = this.server.accept();
            System.out.println("New Thread.");
            ServerThread handler = new ServerThread(this, connection);
            this.handlers.add(handler);
        }
    }

    // 广播
    public void brodCast(String word) throws IOException {
        for (Iterator<ServerThread> it = this.handlers.iterator(); it.hasNext(); ) {
            ServerThread serverThread = (ServerThread) it.next();
            serverThread.say(word);
        }
    }

    // 广播用户列表
    public void showAllUsers() throws IOException {
        StringBuffer word = new StringBuffer("All Users: \n");
        for (Iterator<ServerThread> it = this.handlers.iterator(); it.hasNext();) {
            word.append(((ServerThread)it.next()).getNickName()+"\n");
        }
        this.brodCast(word.toString());
    }
    
    public void removeHandler(ServerThread serverThread) {
    	for (Iterator<ServerThread> it = this.handlers.iterator(); it.hasNext();) {
    		if (((ServerThread)it.next()) == serverThread) {
    			this.handlers.remove(serverThread);
    		}
    	}
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }

}
