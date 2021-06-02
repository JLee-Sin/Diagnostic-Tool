package Parts;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

//The class which uses the command line to read all the information about the network the system is connected to
public class Networking {

    //The string containing all of the connected network's information
    private String networkInfo;

    //The system's ping to google's server in the form of a string
    private String ping;

    //The system's ping to google's server in the form of an int
    private int rawPing;

    //The rate at which the system receives data from the network
    private String recieveRate;

    //The rate at which the system transmits data to the network
    private String transmitRate;

    public Networking() {
        networkInfo = "";
        ping = "";
        rawPing = 0;
        recieveRate = "";
        transmitRate = "";
    }

    private void refresh() throws IOException {
        networkInfo = "";
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "Netsh WLAN show interfaces");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        Scanner sn = new Scanner(new InputStreamReader(p.getInputStream(), StandardCharsets.US_ASCII));
        sn.nextLine();
        sn.nextLine();
        sn.nextLine();
        while (sn.hasNextLine()) {
            String line = sn.nextLine();
            //System.out.println(line);
            networkInfo += line + "\n";
            if(line.contains("Profile")) {
                break;
            }
            if(line.contains("Receive")) {
                recieveRate = line;
            }
            if(line.contains("Transmit")) {
                transmitRate = line;
            }
        }
    }

    private void ping() throws IOException{
        ping = "";
        String line = "";
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ping -l 1000 8.8.8.8");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        Scanner sn = new Scanner(new InputStreamReader(p.getInputStream(), StandardCharsets.US_ASCII));
        while (sn.hasNextLine()) {
            line = sn.nextLine();
        }
        ping = line.substring(46);
    }


    public int getRawPing() throws IOException {
        ping();
        String tmp = ping.substring(0,ping.length()-2).replaceAll("\\s","").replaceAll("[^\\d.]", "");
        rawPing = Integer.parseInt(tmp);
        return rawPing;
    }

    public double rawReceive() throws IOException {
        refresh();
        recieveRate = recieveRate.replaceAll("[^\\d.]", "");
        return Double.parseDouble(recieveRate);
    }

    public double rawTransmit() throws IOException {
        refresh();
        transmitRate = transmitRate.replaceAll("[^\\d.]","");
        return Double.parseDouble(transmitRate);
    }

    public String getNetworkInfo() throws IOException {
        refresh();
        return networkInfo;
    }
}
