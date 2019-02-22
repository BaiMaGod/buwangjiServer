/** 
* @author  yc 
* @parameter  
*/
package com.ttxs.buwangji.socket;

import java.io.BufferedWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yc
 *
 */
public class OnlineSocket {
	
	public static Map<String,BufferedWriter> onlineSockets = new HashMap<String,BufferedWriter>();
	
	
//	public void checkLogout(Socket taskSocket) {
//		new CheckLogoutThread(taskSocket).start();
//	}
//	
//	class CheckLogoutThread extends Thread{
//		Socket taskSocket;
//		
//		public CheckLogoutThread(Socket taskSocket) {
//			this.taskSocket = taskSocket;
//		}
//		
//		@Override
//		public void run() {
//			while(this.isAlive()) {
//				try{
//					taskSocket.sendUrgentData(0xFF);
//		        }catch(Exception e){
//		            return;
//		        }
//			}
//		}
//	}
}
