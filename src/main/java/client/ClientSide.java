package client;

import java.net.*;
import java.io.*;

public class ClientSide {

    public static void main(String[] args){
        String str="192.168.0.103";
        int port=5000;
        Client client=new Client(str,port);
    }
}

class Client{

    public  Socket socket=null;
    public  DataInputStream in=null;
    public  DataOutputStream out=null;
   public DataInputStream input=null;
    Client(String ip,int port){
        try{
            socket=new Socket(ip,port);
            in=new DataInputStream(System.in);
            input=new DataInputStream(socket.getInputStream());
            out=new DataOutputStream(socket.getOutputStream());
            String str="";

            while(!str.equals("Over")) {
                str=in.readLine();
                out.writeUTF(str);
                String time="";
                int length=Integer.parseInt(input.readUTF());
                while(length>0)
                {
                    System.out.println(input.readUTF());
                    length--;
                }
            }
            socket.close();
            in.close();
            out.close();
        }catch(IOException ex){
            System.out.println(ex);
        }
    }
}