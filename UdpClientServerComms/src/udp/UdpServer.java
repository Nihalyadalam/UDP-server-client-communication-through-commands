package udp;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UdpServer implements ServerCodes, ServerCommands{
	
	private byte[] receive = null;
	private byte[] receiveProcessed = null;
	private byte[] userChoice = null;
	private byte[] userMessage = null;
	
	// variable size to compute the non zero values in the receive buffer
	private List<Byte> byteArrayList = new ArrayList<Byte>();
	private DatagramPacket dataReceive = null;
	private int countNonZeroValues;
	private DatagramSocket ds = null;
	
	
	public UdpServer(int portno) throws IOException {
		
		System.out.println("Server started"); 
		
		// Step 1 : Create a socket to listen at port 1234
		ds = new DatagramSocket(portno);
		
        while (true)
        {	
        	// refresh count to zero
        	countNonZeroValues = 0; 
        	userChoice = new byte[1];
        	receive = new byte[256];
            // Step 2 : create a DatgramPacket to receive the data.
        	dataReceive = new DatagramPacket(receive, receive.length);
 
            // Step 3 : receive the data in byte buffer.
            ds.receive(dataReceive);
            
            System.out.println("buffer length: "+dataReceive.getLength());
            System.out.println("buffer data recieved: "+Arrays.toString(receive));
            System.out.println("Client data: " + dataToString(receive));
            	
            
            // get only non zero values from the receive buffer
            for(int i=0;i<receive.length;i++) {
            	if(receive[i]!=0) {
            		countNonZeroValues = countNonZeroValues+1;
            		byteArrayList.add(receive[i]);
            	}
            }
            
           // create a new byte array with the size of non-zero entries
           receiveProcessed = new byte[countNonZeroValues];
           
           // fill the non-zero values from byte list to newly created byte array 
           for(int i=0;i<byteArrayList.size();i++) {
        	   receiveProcessed[i] = byteArrayList.get(i);
        	   
           }
           
           // get the user choice
           userChoice[0] = receiveProcessed[0];
           
           // allocate remaining non zero bytes to user message
           userMessage = new byte[byteArrayList.size()-1];
           
           // remove the choice (number)
           byteArrayList.remove(0);
           
           // append the remaining non zero bytes to user message         
           for(int i=0;i<byteArrayList.size();i++)
           userMessage[i] = byteArrayList.get(i);
           
           System.out.println("Processed buffer data: "+Arrays.toString(receiveProcessed));
           System.out.println("User choice: "+dataToString(userChoice));
            	
            if(ServerCommands.requestTime.equals(dataToString(userChoice))) {
            
            	 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            	   								 LocalDateTime now = LocalDateTime.now();  
            	   								 System.out.println("System time is "+dtf.format(now));  
            	   								
            	
            }
            else if(ServerCommands.requestMessage.equals(dataToString(userChoice))) {
            	System.out.println("From server: "+dataToString(userMessage));          	
            }

         // Clear the buffer after every message.
	     receive = null;
	     receiveProcessed = null;
	     userChoice = null;
	     userMessage = null;
	     byteArrayList.clear();
	                
        }
		
	}
	
	//Conversion of Byte array to integer 
	private int datatoInteger(byte[] recieve) {
		int value = recieve[0];
		return value;
	}
	
	
	//Conversion of Byte array to UTF8 strings
	private static String dataToString(byte[] receive) {
		String s = new String(receive, StandardCharsets.UTF_8);
		return s;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 new UdpServer(1234);
	}

}
