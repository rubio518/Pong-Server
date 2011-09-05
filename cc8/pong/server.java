//package programmingAssignment1;


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

public class server {
	
	private static Player player1 = new Player();
	private static Player player2 = new Player();
	private static Player server = new Player();
	private static	int height = 240;
	private static	int width = 320;
	//--------------------------
	private static	int bolah = 28; // bola height = bola width 
	private static	int barraw = 15;
	private static	int barrah = 80;
	private static	int yp1 = 0;  //y del player 1 (de la barra)   
	private static	int yp2 = 120;	//y del player 2 (de la barra)
	private static	int speed = 1;
	private static	int bolax = 160;
	private static	int bolay = 120;
	private static	int directx = 1;
	private static	int directy = 1;
	private static	int id = 1;
	
	private static	int puntos1 = 0;
	private static	int puntos2 = 0;
	
	private static byte[] msg = new byte[9];
	private static byte[] msg2 = new byte[9];
	private static Request respuesta1;
	private static Request respuesta2;
	
    public static void main(String argv[]) throws Exception {
		server.ip = InetAddress.getLocalHost();
		server.port = 8888;
		ServerSocket conn = new ServerSocket(8887);
		//-----------------------------------------------------
		Socket sp1 = conn.accept(); //sp1 = socket player 1
		relleno(sp1,player1);
		
		//-----------------------------------------------------
		Socket sp2 = conn.accept();  //sp2 = socket player 2	
		relleno(sp2,player2);
		
		//-----------------------------------------------------
		respuesta1 = new Request(sp1,server,player1,9999);
		respuesta2 = new Request(sp2,server,player2,9998);
		//Thread thread = new Thread(respuesta1);
		//Thread thread2 = new Thread(respuesta2);
		respuesta1.run();
		respuesta2.run();
		
		/* Thread Principal
			este thread lo que hara es controlar el juego, debido a que la realidad
			de todo el juego se hara en el server, para evitar incongruencias en 
			el tiempo de ejecucion de diferentes dispositivos.
			
			la resolucion "estandar" sera 240*320 
			barra = 15*80
			bola = 28*28
			
		*/
		//---------------screen-----
		
		Thread TP = new Thread(new Runnable(){
			public void run(){
				while(true){
					//---------------------------------p1----------------------------------
					
					if((bolay>height-bolah)||(bolay<0)){
						directy= directy*(-1);
					}
					if((bolax<15)&&(bolay<yp1+40)&&(bolay>yp1-40-bolah)){
						directx = directx*(-1);
					}else if(bolax<-10){
						puntos2++;
						bolax=160;
						bolay=120;
					}
					//----------------------------------p2---------------------------------
					
					if((bolax>width-15-bolah)&&(bolay<yp2-40)&&(bolay>yp2-40-bolah)){
						directx = directx*(-1);
					}else if(bolax>width-10){
						puntos1++;
						bolax=160;
						bolay=120;
						
					}
					//---------------------------------------------------------------------
					
					bolax = bolax+directx*speed;
					bolay = bolay+directy*speed;
					try{
					Thread.sleep(20);
					}catch(Exception e) {
						System.out.println(e);
					}
					
				}	
			}
		});
		//----------------------------------------------- player 1-------------------------------
		/* Thread del player 1
			este thread es el que enviara la informacion al celular, 
			se enviara	x de la bola
						y de la bola
						
						y de la barra enemiga
		*/
		Thread tp1 = new Thread(new Runnable(){
			public void run(){
				msg[2]=0;
				int yloca;
				while(true){
					//System.out.println("adfsad");
					msg[0]=(byte)(bolax/3); 
					msg[1]=(byte)(bolax%3);
					
					msg[2]=(byte)(bolay/2);
					msg[3]=(byte)(bolay%2);
					
					msg[4]=(byte)id;  //id del paquete
					
					msg[5]=(byte)(yp2/2);  //y de la barra enemiga
					msg[6]=(byte)(yp2%2);
					msg[7]=(byte)(puntos1);
					msg[8]=(byte)(puntos2);
					id++;
					if(id > 126){
						id = 0;
					}
					respuesta1.send(msg);
					//System.out.println("------"+id);
					try{
					Thread.sleep(20);
					}catch(Exception e) {
						System.out.println(e);
					}
					
				}
			}
		});
		/* Thread del player 1 receiver
			este thread es el que enviara la informacion al celular, 
			se enviara	x de la bola
						y de la bola
						x de la barra enemiga
						y de la barra enemiga
		*/
		Thread tp1r = new Thread(new Runnable(){
		byte[] res;
			public void run(){
				
				while(true){
					
              	    	
              	    
					res = respuesta1.receive();
					yp1 = res[0]*2;
					//System.out.println(" "+ yp1);
					
					/*
					try{
					//Thread.sleep(20);
					}catch(Exception e) {
						System.out.println(e);
					}
					*/
				}
			}
		});
		//-----------------------------------player 2-----------------------------------------
		/* Thread del player 2
			este thread es el que enviara la informacion al celular, 
			se enviara	x de la bola
						y de la bola
						x de la barra enemiga
						y de la barra enemiga
			**** este jugador tendra una perspectiva de juego diferente al player 1
			**** en su celular aparecera de la misma forma que si fuera player 1 
			**** pero en la aplicacion se le enviaran diferentes coordenadas
		*/
		Thread tp2 = new Thread(new Runnable(){
			public void run(){
				//msg[2]=0;
				int bx =0;
				int by = 0;
				int id2 = 0;
				int yloca;
				while(true){
					bx = width - bolax;
					by = height - bolay;
					//System.out.println("adfsad");
					msg2[0]=(byte)(bx/3);
					msg2[1]=(byte)(bx%3);
					
					msg2[2]=(byte)(by/2);
					msg2[3]=(byte)(by%2);
					yloca= height - yp1;
					msg2[4]=(byte)id2;
					msg2[5]=(byte)(yloca/2);
					msg2[6]=(byte)(yloca%2);
					msg2[7]=(byte)(puntos2);
					msg2[8]=(byte)(puntos1);
					
					id2++;
					if(id > 126){
						id = 0;
					}
					respuesta2.send(msg2);
					//System.out.println("------"+id);
					try{
					Thread.sleep(20);
					}catch(Exception e) {
						System.out.println(e);
					}
					
				}
			}
		});
		/* Thread del player 1 receiver
			este thread es el que enviara la informacion al celular, 
			se enviara	x de la bola
						y de la bola
						x de la barra enemiga
						y de la barra enemiga
		*/
		Thread tp2r = new Thread(new Runnable(){
		byte[] res;
			public void run(){
				
				while(true){
					
					res = respuesta2.receive();
					yp2 = height - res[0]*2;
					//System.out.println("--"+ yp2);
					
					/*
					try{
					//Thread.sleep(20);
					}catch(Exception e) {
						System.out.println(e);
					}
					*/
				}
			}
		});
		
		
		//---------------------------------------------------------------------------------------
		TP.start();
		tp1.start();
		tp2.start();
		tp1r.start();
		tp2r.start();
		
		
		
		
   
	

	}
	//------------------------- metodos
	public static void relleno(Socket s,Player p){
		p.port = s.getPort();
		p.ip = s.getInetAddress(); 
	}
	
}