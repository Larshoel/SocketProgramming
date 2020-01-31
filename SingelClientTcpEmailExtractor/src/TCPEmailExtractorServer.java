
import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TCPEmailExtractorServer {


        public static String findEmail(String inputUrl){
            ArrayList<String> emailList = new ArrayList<String>();
            String message ="";

            try{
                URL url=new URL(inputUrl);
                InputStream is = new BufferedInputStream(url.openConnection().getInputStream());
                BufferedReader inn=new BufferedReader(new InputStreamReader(is));

                String tmp="";
                StringBuilder sb=new StringBuilder();
                while((tmp=inn.readLine())!=null){
                    if(tmp.contains("@")){
                        sb.append(tmp);
                        emailList.add(tmp);
                    }
                }

                if(!emailList.isEmpty()){

                    message =" Code 0: \n";
                    for(String i : emailList){
                        message += i + "\n";

                    }
                }
                else{
                    message ="Code 1: !!!No email address found on the page!!!’";
                }
            } catch (IOException e){
                message =" Code 2: !!!Server couldn’t find the web page!!!";
            }

            System.out.println(message);

            return message;
        }


    /**
     * @deprecated overriding deprecated method
     */
    public static void main(String[] args) throws IOException {
        int portNumber = 5555;

        System.out.println("Tilkoblet");


        try{
            ServerSocket serverSocket = new ServerSocket(portNumber);

            Socket connectSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(connectSocket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));

            InetAddress clientAddr = connectSocket.getInetAddress();
            int clientPort = connectSocket.getPort();
            String receivedText;

            while ((receivedText = in.readLine()) != null){

                System.out.println(receivedText);

                String outText = findEmail(receivedText);

                out.println(outText);

            }
        } catch (IOException e){
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

}