package Parts;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Disk;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//The list of all disks in the system
public class DiskList {

    //The array list containing each disk
    private ArrayList<DISK> diskList;

    //The amount of space in all of the disks in a system in bytes
    private double rawSize;

    //The properly formatted amount of storage in the system in gigabytes or terabytes
    private String totalSize;

    //Creates a list of the disks in the system
    public DiskList() {
        rawSize = new File("/").getTotalSpace() / Math.pow(10,-12);
        //System.out.println(rawSize);
        diskList = new ArrayList<DISK>();
        Components components = JSensors.get.components();
        List<Disk> disks = components.disks;
        if(disks != null) {
            System.out.println("disks exist");
            for(final Disk disk : disks) {
                diskList.add(new DISK(disk.name, disk.sensors.temperatures));
            }
        }
        if(rawSize > 1.0) {
            //System.out.println("tb");
            rawSize = (rawSize/Math.pow(10,24));
            totalSize = round(rawSize,1) + "tb";
        }
        else {
            //System.out.println("gb");
            rawSize = (rawSize/Math.pow(10,24));
            rawSize *= 1000;
            totalSize = round(rawSize,1) + "gb";
        }
    }

    //gets the average temperature of all the disks
    public double getAverageTemp() {
        int num = 0;
        int div = 0;
        for(int i = 0; i < diskList.size(); i++) {
            num += diskList.get(i).getTemp();
            if(diskList.get(i).getTemp() != 0.0) {
                div++;
            }
        }
        return num/div;
    }

    public ArrayList<DISK> getDiskList() {
        return diskList;
    }

    //A helper method to round the amount of data in each disk
    private double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public String getTotalSize() {
        return totalSize;
    }

    //Updates all of the disks' temperatures
    public void UpdateTemps() {
        for(DISK disk : diskList) {
            disk.updateTemp();
        }
    }
}
