package Parts;

import java.lang.management.ManagementFactory;

//The class representing the system's memory
public class RAM {

    //The amount of ram in bytes
    private long ramSize;

    //The amount of ram formatted into gigabytes
    private int ramInGB;

    public RAM() {
        ramSize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        ramInGB = (int) (ramSize/1000000000);

    }

    public int getRam() {
        return ramInGB;
    }
}
