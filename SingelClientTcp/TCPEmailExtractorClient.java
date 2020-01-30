import java.io.*;
import java.net.*;

public class TCPEmailExtractorClient {

    public static void main(String[] args) throws IOException {
        String hostname = "127.0.0.1";
        int portNumber = 5555;

        Socket clinetSocket = new Socket(hostname, portNumber);

        PrintWriter out = new PrintWriter(clinetSocket.getOutputStream(), true);

        BufferedReader in = new BufferedReader(new InputStreamReader(clinetSocket.getInputStream()));


        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String userInput;


        while((userInput = stdIn.readLine()) != null && !userInput.isEmpty()){
          out.println(userInput);

            String receivedText;
            String firstLine;

            if ((firstLine = in.readLine()).contains("Code 0")) {
               System.out.println(firstLine);
                while((receivedText = in.readLine()) != null && in.ready()){
                    System.out.println(receivedText);
                }
            }
            else{
                System.out.println(firstLine);
            }



        }



    }

}
