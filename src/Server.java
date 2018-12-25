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
    public void brodCast(String word, String type) throws IOException {
        for (Iterator<ServerThread> it = this.handlers.iterator(); it.hasNext(); ) {
            ServerThread serverThread = (ServerThread) it.next();
            if (type.equals("0") && word.split("说")[0].equals(serverThread.getNickName())) {
            	String[] arr = word.split("说");
            	String xword = "";
            	for (int i=1; i<arr.length; i++) {
            		xword += arr[i];
            	}
            	serverThread.say(serverThread.getNickName()+"(我)说"+xword, type);
            	continue;
            }
            serverThread.say(word, type);
        }
    }

    // 广播用户列表
    public void showAllUsers() throws IOException {
        StringBuffer word = new StringBuffer("");
        for (Iterator<ServerThread> it = this.handlers.iterator(); it.hasNext();) {
            word.append(((ServerThread)it.next()).getNickName()+"\n");
        }
        this.brodCast(word.toString(),"1");
    }
    
    public void removeHandler(ServerThread serverThread) {
    	for (Iterator<ServerThread> it = this.handlers.iterator(); it.hasNext();) {
    		if (((ServerThread)it.next()) == serverThread) {
    			this.handlers.remove(serverThread);
    		}
    	}
    }
    
    public void printHandlersCount() {
    	System.out.println("当前Server线程：" + this.handlers.size() + " 个");
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }

}
