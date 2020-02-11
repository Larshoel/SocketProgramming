
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class UDPEmailExtractorServer {
    private static final int portNumber=5555;
    private static DatagramSocket datagramSocket;


    private static void run() {
        try {
            String messageIn,messageOut;
            InetAddress clientAddress;
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
                outPacket=new DatagramPacket(messageOut.getBytes(),messageOut.length(),clientAddress,clientPort); //messageOut.getBytes()
                datagramSocket.send(outPacket);
            }

        } catch(IOException e) {
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

            //Loop through all the text on the given website, add every line that contains @ to arraylist
            String tmp="";
            while((tmp=inn.readLine())!=null){
                String[] arrOfString = tmp.split("<|>|/");
                for(int j = 0; j < arrOfString.length; j++){
                    String text = arrOfString[j];
                    if(text.contains("@")){
                        String [] arrOfText = text.split(":|;|,| ");
                        for(int i = 0; i < arrOfText.length; i++){
                            if(isValid(arrOfText[i])){
                                emailList.add(arrOfText[i]);
                            }
                        }
                    }
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

    //Check if string containing @ is a valid email
    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static void main(String[] args) {
        try {
            datagramSocket=new DatagramSocket(portNumber);
            System.out.println("Connected");
        } catch(SocketException sockEx) {
            System.out.println("unable to open ");
            System.exit(1);
        }
        run();
    }


}
