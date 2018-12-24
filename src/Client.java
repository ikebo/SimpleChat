import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket connection;
    private DataInputStream in;
    private DataOutputStream out;

    public Client() throws IOException {
        this.connection = new Socket("localhost", 6000);
        System.out.println("Connection established.");
        this.in = new DataInputStream(connection.getInputStream());
        this.out = new DataOutputStream(connection.getOutputStream());
        startWork();
    }

    private void startWork() throws IOException {
        String receivedWord = new String("");
        while(!receivedWord.equalsIgnoreCase("bye")) {
            if (this.in.available() > 0) {
                receivedWord = this.in.readUTF();
                System.out.println(receivedWord);
                if (receivedWord.endsWith(":")) {
                    this.say();
                }
            }
        }
    }

    private void say() throws IOException {
        Scanner sc = new Scanner(System.in);
        this.out.writeUTF(sc.nextLine());
        sc.close();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
    }

}
