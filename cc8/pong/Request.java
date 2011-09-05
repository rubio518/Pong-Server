
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
final class Request implements Runnable{
		private Player server;
		private Player playa; //el jugador al que este Request esta asignado
		private DatagramPacket receivePacket;
		private DatagramPacket sendPacket;
		private DatagramSocket serverSocket;
		Socket socket;
		private int port;
		public Request(Socket socket,Player p,Player y,int po) throws Exception {
			this.socket = socket;
			this.server = p;
			playa = y;
			port = po;
    	}
		public void send(byte[] x){
			//System.out.println();
			sendPacket.setData(x);
			sendPacket.setLength(x.length);
			try{
			serverSocket.send(sendPacket);
			}catch(Exception e) {
				System.out.println(e);
			}
		}
		public byte[] receive(){
			
			try{
			serverSocket.receive(receivePacket);
			}catch(Exception e) {
				System.out.println(e);
			}
			return receivePacket.getData();
		}
		public void run(){
        try {

				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(),true); //Autoflush
				output.println(port); //manda el puerto  
				//output.println((server.ip).toString().split("/")[1]);
				socket.close();
				serverSocket = new DatagramSocket(port);
				byte[] receiveData = new byte[1024];
				byte[] sendData = new byte[1024];
				
				int id = 0;
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				InetAddress IPAddress = receivePacket.getAddress();
				port = receivePacket.getPort();
				System.out.println(IPAddress+" "+ port);
				sendPacket = new DatagramPacket(sendData, sendData.length,IPAddress,port);
				
			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}