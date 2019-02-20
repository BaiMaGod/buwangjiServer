/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.frame;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.ttxs.buwangji.socket.NoteSocket;
import com.ttxs.buwangji.socket.TaskSocket;
import com.ttxs.buwangji.socket.UserSocket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;

/**
 * @author yc
 * 服务器入口，包含简单界面
 */
public class ServerFrame extends JFrame {

	private JPanel contentPane;
	private UserSocket userSocket;
	private NoteSocket noteSocket;
	private TaskSocket taskSocket;
	private boolean serverTrue=false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame frame = new ServerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerFrame() {
		JLabel lblNewLabel;
		
		setTitle("不忘记APP服务器");
//		setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\yc.java\\javasystem\\image\\\u5E94\u7528\u56FE\u6807.jpg"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setToolTipText("");
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		lblNewLabel = new JLabel("\u670D\u52A1\u5668\u672A\u6253\u5F00");
		lblNewLabel.setBounds(150, 94, 96, 20);
		panel.add(lblNewLabel);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenu mnNewMenu = new JMenu("\u5F00/\u5173\u670D\u52A1\u5668");
		menuBar.add(mnNewMenu);
		
		JMenuItem menuItem = new JMenuItem("\u5F00\u542F\u670D\u52A1\u5668");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 start();
				lblNewLabel.setText("服务器已打开...");
			}
		});
		mnNewMenu.add(menuItem);
		
		JMenuItem menuItem_1 = new JMenuItem("\u5173\u95ED\u670D\u52A1\u5668");
//		menuItem_1.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				server.colse();
//				lblNewLabel.setText("服务器未打开");
//			}
//		});
		mnNewMenu.add(menuItem_1);
		
		JMenu mnNewMenu_1 = new JMenu("\u5BA2\u6237\u7AEF");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem menuItem_2 = new JMenuItem("\u6240\u6709\u7528\u6237");
		mnNewMenu_1.add(menuItem_2);
		
		JMenuItem menuItem_3 = new JMenuItem("\u5728\u7EBF\u7528\u6237");
		mnNewMenu_1.add(menuItem_3);
			
	}
	public void start(){
		if(serverTrue==false){
			userSocket = new UserSocket();
			noteSocket = new NoteSocket();
			taskSocket = new TaskSocket();
			serverTrue=true;
		}
	}
}
