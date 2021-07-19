package server;
import java.io.DataInputStream;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.net.*;

public class ServerSide {
    public static void main(String[] args) throws IOException {
        Server server=new Server(5000);
    }
}

class Server {
    public Socket socket = null;
    public ServerSocket serverSocket = null;
    public DataInputStream in = null;
    public DataOutputStream out = null;
    Date date = null;
    TimeZone timezone = null;

    Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            String str = "";
            date = new Date();
            timezone = TimeZone.getDefault();

            while (!str.equals("Over")) {
                try {
                    str = in.readUTF();
                    String[] timezones=str.split(",");
                    out.writeUTF(String.valueOf(timezones.length));
                    if(timezones.length==2){

                        LocalDateTime dt = LocalDateTime.now();
                        ZonedDateTime fromZonedDateTime = dt.atZone(ZoneId.of(timezones[0]));
                        ZonedDateTime toZonedDateTime = dt.atZone(ZoneId.of(timezones[1]));
                        long diff = Duration.between(fromZonedDateTime, toZonedDateTime).toMillis();

                        timezone = TimeZone.getTimeZone(timezones[1]);
                        timezone.setDefault(timezone);
                        DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        dformat.setTimeZone(timezone);
                        date.setTime(diff);
                        out.writeUTF(dformat.format(date));

                    }
                    else {
                        for (int i = 0; i < timezones.length; i++) {
                            timezone = TimeZone.getTimeZone(timezones[i]);
                            timezone.setDefault(timezone);
                            DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            dformat.setTimeZone(timezone);
                            out.writeUTF(dformat.format(date));
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket.close();
            in.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }


    }
}
