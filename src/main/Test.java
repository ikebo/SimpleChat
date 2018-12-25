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
import java.io.ObjectInputStream;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Test implements Runnable {

	private JFrame frmSimplechat;
	private JTextField textField;
	private JTextArea textArea;
	private JTextArea textArea_1;
	
	private Socket connection;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;
    
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
        this.objIn = new ObjectInputStream(connection.getInputStream());
        initialize();
		this.frmSimplechat.setVisible(true);
		dialog1.setVisible(true);
        this.MessageHandler.start();  
	}
	
	
	public void run() {
		String[] arr;
        while(true) {
			try {
				if (this.in.available() > 0 || this.objIn.available() > 0) {
				    arr = (String[])this.objIn.readObject();
				    if (arr[1].equals("0")) {
				    	System.out.println(arr[0]);
					    String preText = this.textArea.getText();
					    String str = new String("");
					    if (preText.equals("")) {
					    	str = arr[0];
					    } else {
					    	str = preText + "\n" + arr[0];
					    }
					    this.textArea.setText(str);
				    } else if (arr[1].equals("1")) {
				    	System.out.println("广播在线用户...");
				    	this.textArea_1.setText(arr[0]);
				    }
				}
				Thread.sleep(500);
			} catch (ClassNotFoundException | IOException | InterruptedException e) {
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
	}
	
	public void handleEnterNickName(String nickName) throws IOException {
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
		frmSimplechat.setBounds(100, 100, 740, 488);
		frmSimplechat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSimplechat.getContentPane().setLayout(null);
		
		
		JLabel lblNewLabel_1 = new JLabel("输入:");
		lblNewLabel_1.setBounds(15, 375, 53, 21);
		frmSimplechat.getContentPane().add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setFont(new Font("楷体", Font.PLAIN, 20));
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("pressed." + e.getKeyCode());
				if (e.getKeyCode() != 10) {
					return ;
				}
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
		textField.setBounds(65, 366, 388, 38);
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
		btnNewButton.setBounds(468, 366, 83, 38);
		frmSimplechat.getContentPane().add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(36, 73, 468, 250);
		frmSimplechat.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
		textArea.setEditable(false);
		
		JLabel label = new JLabel("消息列表");
		label.setFont(new Font("隶书", Font.PLAIN, 22));
		label.setBounds(26, 15, 96, 32);
		frmSimplechat.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("在线用户");
		label_1.setFont(new Font("隶书", Font.PLAIN, 22));
		label_1.setBounds(508, 22, 96, 21);
		frmSimplechat.getContentPane().add(label_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(540, 73, 144, 250);
		frmSimplechat.getContentPane().add(scrollPane_1);
		
		textArea_1 = new JTextArea();
		textArea_1.setFont(new Font("隶书", Font.PLAIN, 23));
		textArea_1.setEditable(false);
		scrollPane_1.setViewportView(textArea_1);
		
	}
}
