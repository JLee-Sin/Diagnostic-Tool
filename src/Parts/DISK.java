package Parts;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Disk;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import java.util.List;

//The disk class which represents an individual disk in the system
public class DISK extends coreComponents {

    //The temperature of the disk
    private double Temp;

    //The name of the disk
    private String Name;

    public DISK(String name, List<Temperature> temp) {
        Name = name;
        for(Temperature te : temp) {
            if(te.value != null) {
                Temp = te.value;
            }
            else {
                Temp = 0.0;
            }
        }
    }

    public void updateTemp() {
        List<Disk> disks = JSensors.get.components().disks;
        if(disks.size() > 0) {
            Disk disk = disks.get(0);
            if (disk.sensors.temperatures != null && disk.sensors.temperatures.size() > 0) {
                for (Temperature temp : disk.sensors.temperatures) {
                    if (temp.value != null && temp.value != Temp) {
                        Temp = temp.value;
                    }
                }
            }
        }
    }

    public String getName() {
        System.out.println(Name);
        return Name;
    }

    public double getTemp() {
        //System.out.println(Temp);
        return Temp;
    }
}
