
import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Class that serves clients in different threads
class EchoThread extends Thread{
    protected Socket socket;


    public EchoThread(Socket clientSocket){
        this.socket = clientSocket;
    }

    public void run(){
        //Initialize
        PrintWriter out = null;
        BufferedReader in = null;
        InetAddress clientAddr = null;

        try{

            out = new PrintWriter(socket.getOutputStream(), true);

            //Stream reader from the connection socket
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Get clinet IP adress
            clientAddr = socket.getInetAddress();

            System.out.println("\n" + clientAddr + " connected to server");

            String receivedText;

            while ((receivedText = in.readLine()) != null){

                System.out.println("\n"+ clientAddr + " : " + receivedText);
                String outText = findEmail(receivedText);

                //Send message to client
                out.println(outText);

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //Method that returns email (if they exist on given url), or error messages
    public String findEmail(String inputUrl){
        ArrayList<String> emailList = new ArrayList<String>();
        String message ="";

        try{
            //Get url from client
            URL url=new URL(inputUrl);

            InputStream is = new BufferedInputStream(url.openConnection().getInputStream());
            BufferedReader inn=new BufferedReader(new InputStreamReader(is));


            //Loop throu alle the text on the given website, add every line that contains @ to arraylist
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
                message +="Code 1: !!!No email address found on the page!!!’";
            }
        } catch (IOException e){
            message +=" Code 2: !!!Server couldn’t find the web page!!!";
        }


        System.out.println(message);

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
}

public class TCPEmailExtractorServer {

    /**
     * @deprecated overriding deprecated method
     */
    public static void main(String[] args) throws IOException {
        int portNumber = 5555;

            ServerSocket serverSocket = null;
            Socket socket = null;

            //Create new server socket with given portnumber
            try{
                serverSocket = new ServerSocket(portNumber);
                System.out.println("Started");
            }catch(IOException e){
                e.printStackTrace();
            }

            //Constantly listen for new clients tying to connect
            while(true){
                //Create and start a new EchoThread for each connected client
                try{
                    socket = serverSocket.accept();
                }catch (IOException e){
                    e.printStackTrace();
                }
                //New thread
                new EchoThread(socket).start();
            }

    }

}