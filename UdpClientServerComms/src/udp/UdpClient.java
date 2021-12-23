package udp;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;



public class UdpClient {
	
	private byte[] buf = null;
	private InetAddress ip = null;
	private static int portno = 1234;
	private DatagramSocket ds = null;
	private String message;
	private Scanner sc = new Scanner(System.in);

	public UdpClient(int portno,String hostname) throws IOException {
				
        // Step 1: Create the socket object for carrying the data.
		ip = InetAddress.getByName(hostname);
		ds = new DatagramSocket();
 
        while (true)
        {	
        	// padding required
        	buf = new byte[1024];
        	message = "";
        	System.out.println("Enter the command that needs to be executed at the server");
        	System.out.println("1: Time 2: Message server 3: two's compliment of an integer");
            String choice = sc.next();
            
            switch(choice) {
            
            	case "1": 	sendDataToServer(ds, ip, choice, message);
            			  	break;
            	
            	case "2": 	System.out.println("Please enter a message to send to server");
            				message = sc.nextLine();
            				sendDataToServer(ds, ip, choice, message);
            				break;
            	
            	default: break;
            		
            }
            
            buf = null;
         }
	}
	



	public void sendDataToServer(DatagramSocket datasocket, InetAddress ip, String choice, String message) throws IOException,PortUnreachableException {
				
		if(choice.equals("1")) {		
			
			buf = choice.getBytes(StandardCharsets.UTF_8);			
			System.out.println("buffer data: "+Arrays.toString(buf));
		}
		else if(choice.equals("2"))  {
			
			message = sc.next();
			String userData = choice + message;
			buf = userData.getBytes();	
		}
				
		// Step 2 : create the datagramPacket for sending the data.
        DatagramPacket DpSend =
              new DatagramPacket(buf, buf.length, ip, portno);

        // Step 3 : invoke the send call to actually send the data.
        datasocket.send(DpSend);
			
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new UdpClient(1234, "Nihal-Yadalam");
	}

}
