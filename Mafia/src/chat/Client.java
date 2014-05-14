package chat;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
	public void Start(String id, String serverIp) {
		try {
			// ������ �����Ͽ� ������ ��û�Ѵ�.
			Socket socket = new Socket(serverIp, 7777);
			System.out.println("������ ����Ǿ����ϴ�.");

			Thread sender = new Thread(new ClientSender(socket, id));
			Thread receiver = new Thread(new ClientReceiver(socket));

			// Ŭ���̾�Ʈ�� ������ ����
			sender.start(); // sender ������ ����
			receiver.start(); // receiver ������ ����
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
		}
	}

	public static class ClientSender extends Thread {
		Socket socket;
		DataOutputStream out;
		String name;
		public boolean chat = true;

		public ClientSender(Socket socket, String name) {
			this.socket = socket;
			try {
				out = new DataOutputStream(socket.getOutputStream());
				this.name = name;
			} catch (Exception e) {
			}
		}

		public void run() {
			Scanner scanner = new Scanner(System.in);
			String message = "";
			try {
				if (out != null) {
					out.writeUTF(name);
				}

				while (out != null) {
					if ((message = scanner.nextLine()).charAt(0) == '@') {
						continue;
					}
					if (chat){
						out.writeUTF("[" + name + "]" + message);
					}
				}
			} catch (IOException e) {
			}
		} // run()
	}

	public static class ClientReceiver extends Thread {
		Socket socket;
		DataInputStream in;

		public ClientReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
			}
		}

		public void run() {
			while (in != null) {
				try {
					System.out.println(in.readUTF());
				} catch (IOException e) {
				}
			}
		} // run
	}
} // class