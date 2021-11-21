import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Server {
    Socket socket=null;
    ServerSocket serverSocket=null;
    BufferedWriter outBufferedWriter = null;
    BufferedReader inBufferedReader=null;
    FileInputStream fileInputStream;
    FileOutputStream fileOutputStream;
    public Server(int port) {
        try {
            serverSocket=new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client ~~~ ");
            socket=serverSocket.accept();
            System.out.println("Client accepted !");
            outBufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            inBufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line="";
            while (!line.equals("bye")) {
                try {
                    line=inBufferedReader.readLine();
                    System.out.println("Server received: " + line);
                    //respone back to the client
                    //outBufferedWriter.write(line);
                    //outBufferedWriter.newLine();
                    //outBufferedWriter.flush();
                    //Add(line,fileOutputStream,outBufferedWriter);
                    CheckCommand(line,outBufferedWriter,fileOutputStream,fileInputStream);
                    //SendResult(outBufferedWriter,line,fileInputStream);
                    //Delete(line,fileOutputStream,fileInputStream,outBufferedWriter);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            System.out.println("Closing connection");

            fileOutputStream.close();
            fileInputStream.close();
            inBufferedReader.close();
            outBufferedWriter.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Server innitial error");
        }
    }
    public static HashMap<String, String> readfile(FileInputStream fileInputStream){
        HashMap<String, String> dictionary = new HashMap<String, String>();
        Scanner scanner=new Scanner(fileInputStream);
        try {
            while (scanner.hasNextLine()) {
                String [] arrsplitStrings=scanner.nextLine().split(";",2);
                dictionary.put(arrsplitStrings[0], arrsplitStrings[1]);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Loi doc file scanner");
        }
        return dictionary;
    }
    public static String Translate(HashMap<String, String> dictionary,String input) {
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            if (entry.getKey().equals(input)) {
                return entry.getValue();
            }
        }
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            if (entry.getValue().equals(input)) {
                return entry.getKey();
            }
        }
        return "Không có trong từ điển !!";

    }
    public void SendResult(BufferedWriter outBufferedWriter,String input,FileInputStream fileInputStream) throws IOException {
        try{
            fileInputStream=new FileInputStream("./src/dictionary");
            HashMap<String,String> dictionary=new HashMap<String,String>();
            dictionary=readfile(fileInputStream);
            outBufferedWriter.write(Translate(dictionary,input));
            outBufferedWriter.newLine();
            outBufferedWriter.flush();

        }catch(FileNotFoundException e){
                outBufferedWriter.write("Lỗi đọc file !");
                outBufferedWriter.newLine();
                outBufferedWriter.flush();

        }

    }
    public static void Add(String input,FileOutputStream fileOutputStream,BufferedWriter outBufferedWriter) throws IOException {
        if(input.equals("bye")) return;
        try {
                fileOutputStream = new FileOutputStream("./src/dictionary", true);
                String[] arrayString = input.split(";", 3);
                byte tienganh[] = arrayString[1].getBytes();
                byte tiengviet[] = arrayString[2].getBytes();

                fileOutputStream.write(tienganh);
                fileOutputStream.write(59);
                fileOutputStream.write(tiengviet);
                fileOutputStream.write("\n".getBytes());
                //fileOutputStream.close();
                outBufferedWriter.write("Ghi " + arrayString[1] + "  " + arrayString[2]);
                outBufferedWriter.newLine();
                outBufferedWriter.flush();


            } catch (FileNotFoundException e) {
                outBufferedWriter.write("Lỗi ghi file !!");
                outBufferedWriter.newLine();
                outBufferedWriter.flush();
            }




    }
    public void CheckCommand(String input,BufferedWriter outBufferedWriter,FileOutputStream fileOutputStream,FileInputStream fileInputStream) throws IOException {
        String [] array=input.split(";",3);
        if(array[0].equals("ADD")){
            Add(input,fileOutputStream,outBufferedWriter);
        }else if (array[0].equals("DEL")){
            Delete(input,fileOutputStream,fileInputStream,outBufferedWriter);
        }else
        SendResult(outBufferedWriter,input,fileInputStream);


    }
    public static void Delete(String input,FileOutputStream fileOutputStream,FileInputStream fileInputStream,BufferedWriter outBufferedWriter)  {
       try{
           File temp=new File("./src/temp");
        File main=new File("./src/dictionary");
        fileOutputStream=new FileOutputStream(temp);
        fileInputStream=new FileInputStream(main);
        Scanner scanner=new Scanner(fileInputStream);
        while(scanner.hasNextLine()){
            String line= scanner.nextLine();
            if (!line.contains(input)){
                byte newline[]=line.getBytes();
                fileOutputStream.write(newline);
                fileOutputStream.write("\n".getBytes());
            }
        }

        main.delete();
        temp.renameTo(main);
        outBufferedWriter.write("Xóa Thành Công !!");
        outBufferedWriter.newLine();
        outBufferedWriter.flush();
       }catch(IOException e){
           try {
               System.out.println(e);
               outBufferedWriter.write("Xóa That Bai !!");
               outBufferedWriter.newLine();
               outBufferedWriter.flush();
           }catch (IOException e1){
               System.out.println(e1);
           }
       }
    }




    public static void main (String [] args) throws IOException {
       // FileInputStream fileInputStream=new FileInputStream("./src/dictionary");
     //   FileOutputStream fileOutputStream=new FileOutputStream("./src/temp.txt");

        //HashMap<String,String> dictionary=new HashMap<String,String>();
        //dictionary=readfile(fileInputStream);
        //Translate(dictionary,"school");
        Server server=new Server(6000);


    }
}
