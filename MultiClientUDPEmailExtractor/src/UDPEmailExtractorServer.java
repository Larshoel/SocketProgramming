
import java.net.*;
import java.io.*;
import java.util.ArrayList;


public class UDPEmailExtractorServer {
    private static final int portNumber=5555;
    private static DatagramSocket datagramSocket;


    private static void run() {
        try {
            String messageIn,messageOut;
            InetAddress clientAddress=null;
            int clientPort;
            byte[] buffer;
            DatagramPacket inPacket,outPacket;


            while(true) {
                buffer= new byte[1024];
                inPacket=new DatagramPacket(buffer,buffer.length);
                datagramSocket.receive(inPacket);
                clientAddress=inPacket.getAddress();
                clientPort=inPacket.getPort();

                messageIn=new String(inPacket.getData(),0,inPacket.getLength());
                System.out.print(clientAddress + " : " + messageIn +"\n");

                messageOut=findEmail(messageIn);
                outPacket=new DatagramPacket(messageOut.getBytes(),messageOut.length(),clientAddress,clientPort);
                datagramSocket.send(outPacket);
            }

        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static String findEmail(String inUrl){
        ArrayList<String> emailList = new ArrayList<String>();
        String message ="";

        try{
            //Get url from client
            URL url=new URL(inUrl);

            InputStream is = new BufferedInputStream(url.openConnection().getInputStream());
            BufferedReader inn=new BufferedReader(new InputStreamReader(is));

            //Loop throu alle the text on the given website, add every line that contains @ to arraylist
            String tmp="";
            while((tmp=inn.readLine())!=null){
                if(tmp.contains("@")){
                    emailList.add(tmp);
                }
            }

            //Check arraylist to know what returncode to send
            if(!emailList.isEmpty()){

                //Format email list
                message +=" Code 0: \n";
                for(String i : emailList){
                    message += i + "\n";

                }

            }
            else{
                message +="Code 1: !!!No email address found on the page!!!";
            }
        } catch (IOException e){
            message +=" Code 2: !!!Server couldn’t find the web page!!!";
        }


        System.out.println(message + "\n");

        return message;
    }

    public static void main(String[] args)
    {
        System.out.println("Connected");
        try {
            datagramSocket=new DatagramSocket(portNumber);
        }
        catch(SocketException sockEx) {
            System.out.println("unable to open ");
            System.exit(1);
        }
        run();
    }


}
