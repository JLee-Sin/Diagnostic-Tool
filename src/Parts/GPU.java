package Parts;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Gpu;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import java.util.List;

//The gpu class which represents the systems gpu and can pull its hardware information
public class GPU extends coreComponents{

    //The temperature of the gpu
    private double Temp;

    //The name of the gpu
    private String Name;

    public GPU() {
        Components components = JSensors.get.components();
        List<Gpu> gpus = components.gpus;
        if(gpus != null) {
            for(final Gpu gpu : gpus) {
                Name = gpu.name;
            }
        }
        else {
            Name = "Could not identify GPU";
        }
        if(gpus.size() > 0) {
            Gpu gpu = gpus.get(0);
            if (gpu.sensors.temperatures != null && gpu.sensors.temperatures.size() > 0) {
                for (Temperature temp : gpu.sensors.temperatures) {
                    if (temp.value != null) {
                        Temp = temp.value;
                    }
                    else {
                        Temp = CPU.getRaw();
                    }
                }
            }
        }
        if(Temp == 0.0) {
            Temp = CPU.getRaw()-5;
        }
    }

    public void updateTemp() {
        List<Gpu> gpus = JSensors.get.components().gpus;
        if(gpus.size() > 0) {
            Gpu gpu = gpus.get(0);
            if (gpu.sensors.temperatures != null && gpu.sensors.temperatures.size() > 0) {
                for (Temperature temp : gpu.sensors.temperatures) {
                    if (temp.value != null && temp.value != Temp) {
                        Temp = temp.value;
                    }
                }
            }
        }
        if(Temp == 0.0) {
            Temp = CPU.getRaw()-5;
        }
    }

    public double getTemp() {
        //System.out.println(Temp);
        return Temp;
    }

    public String getName(){
        System.out.println(Name);
        return Name;
    }
}
