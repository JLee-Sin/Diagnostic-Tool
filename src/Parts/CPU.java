package Parts;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import java.util.List;

//The cpu class which represents the systems cpu and can pull its hardware information
public class CPU extends coreComponents{

    //The temperature of the cpu
    private double Temp;

    //The name of the cpu
    private String Name;

    //The number of cores in the cpu
    private int Cores;

    //A static representation of the temperature variable used for in-package calculations
    private static double sTemp;

    //Sets the information for the system's cpu
    public CPU() {
        Components components = JSensors.get.components();
        List<Cpu> cpus = components.cpus;
        if(cpus != null) {
            for(final Cpu cpu : cpus) {
                Name = cpu.name;
            }
        }
        else {
            Name = "Could not identify CPU";
        }
        if(cpus.size() > 0) {
            Cpu cpu = cpus.get(0);
            if (cpu.sensors.temperatures != null && cpu.sensors.temperatures.size() > 0) {
                for (Temperature temp : cpu.sensors.temperatures) {
                    if (temp.value != null) {
                        Temp = temp.value;
                    }
                    else {
                        Temp = 0.0;
                    }
                }
            }
        }
        Cores = Runtime.getRuntime().availableProcessors();
        sTemp = Temp;
    }

    public int getCores() {
        System.out.println(Cores);
        return Cores;
    }

    public double getTemp() {
        //System.out.println(Temp);
        return Temp;
    }

    public String getName(){
        System.out.println(Name);
        return Name;
    }

    //updates the cpu's temperature
    public void updateTemp() {
        List<Cpu> cpus = JSensors.get.components().cpus;
        if(cpus.size() > 0) {
            Cpu cpu = cpus.get(0);
            if (cpu.sensors.temperatures != null && cpu.sensors.temperatures.size() > 0) {
                for (Temperature temp : cpu.sensors.temperatures) {
                    if (temp.value != null && temp.value != Temp) {
                        Temp = temp.value;
                        sTemp = Temp;
                    }
                }
            }
        }
    }

    public static double getRaw() {
        return sTemp;
    }

}