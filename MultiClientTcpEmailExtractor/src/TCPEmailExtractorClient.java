import java.io.*;
import java.net.*;

import static java.lang.System.exit;

public class TCPEmailExtractorClient {

    public static void main(String[] args) throws IOException {
        String hostname = "10.253.20.41"; //Server ip
        int portNumber = 5555;  //Default port
        Socket clinetSocket = new Socket();
        PrintWriter out = null;
        BufferedReader in = null;
        try{

            //Create TCP socket for hostname and port
            //clinetSocket = new Socket(hostname, portNumber);
            clinetSocket.connect(new InetSocketAddress(hostname, portNumber), 2000);

            //Stream reader to socket
            out = new PrintWriter(clinetSocket.getOutputStream(), true);

            //Stream reader from socket
            in = new BufferedReader(new InputStreamReader(clinetSocket.getInputStream()));

        }catch (IOException e){
            System.out.println(e);
            exit(0);
        }

        //Reads keaybord inputs
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        //Initialize userInput
        String userInput;

        String message = "Write url:";
        System.out.print(message);

        //While somthing is written in the input field, run loop
        while((userInput = stdIn.readLine()) != null && !userInput.isEmpty()){
           //Print user input on server
            out.println(userInput);

            String receivedText;
            String firstLine;

            //Check if the message from server is a code 0, if it is, it will print out multiple lines. If not, print first and only line in message
            if ((firstLine = in.readLine()).contains("Code 0")) {
               System.out.println(firstLine);
               //While line recived form server is not empty and while the stream is ready to be read
                while((receivedText = in.readLine()) != null && in.ready()){
                    System.out.println(receivedText);
                }
            }
            else{
                System.out.println(firstLine);
            }

            System.out.print(message);

        }
    }

}
