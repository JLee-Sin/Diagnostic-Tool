package Parts;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

//The class representing the system's operating system
public class OS {

    //A boolean indicating the systems health
    private boolean IsHealthy;

    //A boolean checking if the command line has made changes to the system
    private boolean isRepaired;

    //The name of the OS
    private String Name;

    //The version of the installedOS
    private String OSver;

    //The file used for saving the OS's status
    private File file;

    public OS() {
        Name = System.getProperty("os.name");
        OSver = System.getProperty("os.version");
        IsHealthy = false;
        isRepaired = false;
        file = new File("./Sys.txt");
        if(!file.exists()) {
            try {
                file.createNewFile();
                save(3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkForHealth() throws IOException {
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "sfc /scannow");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        Scanner sn = new Scanner(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_16LE));
        while (sn.hasNextLine()) {
            String line = sn.nextLine();
            System.out.println(line);
            if(line.contentEquals("Windows Resource Protection did not find any integrity violations.")) {
                IsHealthy = true;
                break;
            }
            else if(line.contentEquals("Windows Resource Protection found corrupt files and successfully repaired them")) {
                IsHealthy = false;
                isRepaired = true;
            }
        }
        if(IsHealthy == true) {
            return true;
        }
        else if(!IsHealthy && isRepaired) {
            return true;
        }
        else {
            return false;
        }
    }

    public int getHealth() throws IOException {
        boolean h = checkForHealth();
        if(h) {
            System.out.println("Found no corruptions");
            save(0);
            return 0;
        }
        else if(h && isRepaired) {
            System.out.println("Corruptions found and repaired");
            save(1);
            return 1;
        }
        else {
            System.out.println("Corruptions found but not repaired");
            save(2);
            return 2;
        }
    }

    private void save(int score) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            dos.flush();
            dos.writeInt(score);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private int read() throws IOException {
        if (!new File("./Sys.txt").canRead()) {
            return 3;
        }
        try (DataInputStream dis = new DataInputStream(new FileInputStream("./Sys.txt"))) {
            return dis.readInt();
        } catch (IOException ignored) {
            return 3;
        }
    }

    public String checkForPreviousScan() throws IOException {
        int i = read();
        if(i == 0 || i == 1) {
            return "Healthy";
        }
        else if (i == 2) {
            return "Unhealthy";
        }
        else {
            return "Unknown";
        }
    }

    public String getName() { return Name;}

    public String getOSver(){return OSver;}
}