import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    Socket socket=null;
    BufferedWriter outBufferedWriter=null;
    BufferedReader inBufferedReader=null;
    BufferedReader stdIn=null;
    String address;
    int port;
    public Client(String address, int port) throws UnknownHostException, IOException {


        socket=new Socket(address,port);
        System.out.println("Connected");
        outBufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        inBufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        stdIn=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Nhập thông tin vào : ");
        String lineString ="";
        while (!lineString.equals("bye")) {

            lineString=stdIn.readLine();
            System.out.println("Client gửi đi : " + lineString);
            outBufferedWriter.write(lineString);
            outBufferedWriter.newLine();
            outBufferedWriter.flush();
            //Server send back and client print it out
            System.out.println("Server gửi lại : "+inBufferedReader.readLine());

        }

        inBufferedReader.close();
        outBufferedWriter.close();
        socket.close();
    }

    public Client(){

    }

    public static void main (String [] args) throws UnknownHostException, IOException {
        Client client=new Client("127.0.0.1", 6000);
    }
}
