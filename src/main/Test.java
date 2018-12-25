package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Test implements Runnable {

	private JFrame frame;
	private final JTextPane textPane = new JTextPane();
	private JTextField textField;
	
	private Socket connection;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectOutputStream objOut;
    
    private NickNameDialog dialog1;
    
    private Thread MessageHandler;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test window = new Test();
					//window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws InterruptedException 
	 */
	public Test() throws UnknownHostException, IOException, InterruptedException {
		this.dialog1 = new NickNameDialog(this);
		this.MessageHandler = new Thread(this);
		this.connection = new Socket ("localhost", 6000);
        System.out.println("Connection established.");
        this.in = new DataInputStream(connection.getInputStream());
        this.out = new DataOutputStream(connection.getOutputStream());
        this.objOut = new ObjectOutputStream(connection.getOutputStream());
        initialize();
		this.frame.setVisible(true);
		dialog1.setVisible(true);
        this.MessageHandler.start();
	}
	
	
	public void run() {
		String receivedWord = new String("");
        while(!receivedWord.equalsIgnoreCase("bye")) {
            try {
				if (this.in.available() > 0) {
				    receivedWord = this.in.readUTF();
				    System.out.println(receivedWord);
				    String preText = this.textPane.getText();
				    String str = new String("");
				    if (preText.equals("")) {
				    	str = receivedWord;
				    } else {
				    	str = preText + "\n" + receivedWord;
				    }
				    this.textPane.setText(str);
				}
				Thread.sleep(500);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
	}
	
	
	public void handleCloseDialog() {
		System.exit(-1);
	}
	
	public void postMessage(String word, String type) throws IOException {
		this.objOut.writeObject(new String[] {word, type});
		this.objOut.flush();
		System.out.println("sent");
		//this.out.writeUTF(word);
	}
	
	public void handleEnterNickName(String nickName) throws IOException {
		//this.textPane.setText(nickName);
		// 将昵称发到Server端广播
		postMessage(nickName, "0");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Test that = this;
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					postMessage("", "-1");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 619, 414);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		textPane.setEditable(false);
		textPane.setBounds(55, 55, 472, 210);
		frame.getContentPane().add(textPane);
		
		JLabel lblNewLabel = new JLabel("聊天室");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		lblNewLabel.setIcon(null);
		lblNewLabel.setBounds(15, 15, 66, 32);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("输入:");
		lblNewLabel_1.setBounds(15, 289, 53, 21);
		frame.getContentPane().add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBounds(65, 280, 388, 38);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("发送");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = that.textField.getText();
				if (str.equals("")) {
					return ;
				}
				try {
					postMessage(str, "1");
					that.textField.setText("");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(468, 280, 83, 38);
		frame.getContentPane().add(btnNewButton);
	}
}
