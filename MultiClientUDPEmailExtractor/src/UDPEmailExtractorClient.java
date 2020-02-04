
import java.io.*;
import java.net.*;
import java.util.*;


public class UDPEmailExtractorClient{
    public static void main(String[] args) throws IOException{

        InetAddress host;
        final int PORT=5555;
        DatagramSocket datagramSocket;
        DatagramPacket inPacket,outPacket;
        byte[] buffer;
        String hostName="192.168.68.102";

        try{
             datagramSocket=new DatagramSocket();
             Scanner userEntry=new Scanner(System.in);
             String input="",response="";
             System.out.print("Welcome to the UDP Email extractor, write the url to extract the emails from.\nURL: ");

             while((input = userEntry.next()) != null && !input.isEmpty()){

                host = InetAddress.getByName(hostName);

                outPacket=new DatagramPacket(input.getBytes(),input.length(),host,PORT);
                datagramSocket.send(outPacket);

                buffer=new byte[1024];
                inPacket=new DatagramPacket(buffer,buffer.length);
                datagramSocket.receive(inPacket);
                response=new String(inPacket.getData(),0,inPacket.getLength());

                System.out.println(response + "\n");

                System.out.print("URL: ");
           }
        }
        catch(IOException e){
            e.printStackTrace();
         }
    }
}
