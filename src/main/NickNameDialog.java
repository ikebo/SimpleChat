package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class NickNameDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private Test window;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NickNameDialog dialog = new NickNameDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NickNameDialog(Test window) {
		this.window = window;
		NickNameDialog that = this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeFrame();
				that.window.handleCloseDialog();
			}
		});
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("提示");
		setResizable(false);
		setBounds(100, 100, 450, 217);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("请输入用户名:");
			lblNewLabel.setBounds(15, 15, 126, 35);
			contentPanel.add(lblNewLabel);
		}
		{
			textField = new JTextField();
			textField.setFont(new Font("隶书", Font.PLAIN, 22));
			textField.setBounds(45, 54, 319, 47);
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 116, 444, 46);
			contentPanel.add(buttonPane);
			{
				JButton okButton = new JButton("确定");
				okButton.setBounds(250, 5, 73, 41);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String str = textField.getText();
						System.out.println(str);
						try {
							that.window.handleEnterNickName(str);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						closeFrame();
					}
				});
				buttonPane.setLayout(null);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("取消");
				
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						closeFrame();
						that.window.handleCloseDialog();
					}
				});
				cancelButton.setBounds(338, 5, 87, 41);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public void closeFrame() {
		this.dispose();
	}

}
