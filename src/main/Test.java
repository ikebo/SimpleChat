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
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class Test implements Runnable {

	private JFrame frmSimplechat;
	private JTextField textField;
	private JTextArea textArea;
	
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
		this.frmSimplechat.setVisible(true);
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
				    String preText = this.textArea.getText();
				    String str = new String("");
				    if (preText.equals("")) {
				    	str = receivedWord;
				    } else {
				    	str = preText + "\n" + receivedWord;
				    }
				    this.textArea.setText(str);
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
		frmSimplechat = new JFrame();
		frmSimplechat.setBackground(Color.CYAN);
		frmSimplechat.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\34662\\Desktop\\320x0w.jpg"));
		frmSimplechat.setTitle("SimpleChat");
		frmSimplechat.addWindowListener(new WindowAdapter() {
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
		frmSimplechat.setResizable(false);
		frmSimplechat.setBounds(100, 100, 619, 414);
		frmSimplechat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSimplechat.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("聊天室");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		lblNewLabel.setIcon(null);
		lblNewLabel.setBounds(15, 15, 66, 32);
		frmSimplechat.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("输入:");
		lblNewLabel_1.setBounds(15, 289, 53, 21);
		frmSimplechat.getContentPane().add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBounds(65, 280, 388, 38);
		frmSimplechat.getContentPane().add(textField);
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
		frmSimplechat.getContentPane().add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(65, 51, 468, 214);
		frmSimplechat.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		
	}
}
