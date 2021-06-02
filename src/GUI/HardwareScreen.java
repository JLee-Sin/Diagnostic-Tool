package GUI;

import Parts.*;
import com.jfoenix.controls.JFXSlider;
import eu.hansolo.medusa.Gauge;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;

//This class is responsible for creating the pane variable that contains the gui for the hardware screen
public class HardwareScreen extends Screens{

    //The pane variable that contains all of the gui elements
    private AnchorPane pane;

    //The variable that represents the cpu and can retrieve all of its information
    private CPU c;

    //The variable that represents the gpu and can retrieve all of its information
    private GPU g;

    //The variable that represents all the disks in the system and can retrieve all of its information
    private DiskList d;

    //The variable that represents the ram in the system and can retrieve all of its information
    private RAM r;

    //The sliders used as thermometers to represent all of the internal components temperatures
    private JFXSlider CpuThermo;
    private JFXSlider GpuThermo;
    private JFXSlider[] DiskThermos;

    //The gauge that is used represent the amount of RAM in the system
    private Gauge ram;

    //The imageView and ImageView array used to represent each of the drives inside of the system
    private ImageView driveIcon = new ImageView("Drive.png");
    private ImageView[] driveIcons;

    public HardwareScreen() {
        c = Storage.getCpu();
        g = Storage.getGpu();
        d = Storage.getDisks();
        r = Storage.getRam();
        driveIcon.setSmooth(true);
        driveIcon.setPreserveRatio(true);
        driveIcon.setFitWidth(16);
        driveIcon.setFitWidth(16);
        driveIcons = new ImageView[d.getDiskList().size()];
        for(int i = 0; i < d.getDiskList().size(); i++) {
            driveIcons[i] = driveIcon;
        }
    }

    @Override
    public Pane getPane() {
        return pane;
    }

    @Override
    public void setPane() {
        Rectangle rC = new Rectangle(600,225);
        Rectangle rD = new Rectangle(400,200);
        Rectangle rG = new Rectangle(400,200);

        AnchorPane cpuPane = new AnchorPane();
        cpuPane.setPrefSize(600,225);
        Label cpu = new Label("CPU & RAM: ");
        Label cName = new Label("CPU Name: " + c.getName());
        Label cCores = new Label("Core Count: " + c.getCores()/2);
        Label cThreads = new Label("Thread Count: " + c.getCores());

        Font f = Font.font("Verdana", FontWeight.BLACK,12);
        cpu.setFont(f);
        cName.setFont(f);
        cCores.setFont(f);
        cThreads.setFont(f);

        CpuThermo = new JFXSlider(0,120,c.getTemp());
        CpuThermo.setValueChanging(false);
        CpuThermo.setOrientation(Orientation.VERTICAL);
        CpuThermo.getStylesheets().add("Thermo.css");
        CpuThermo.setShowTickMarks(true);
        CpuThermo.setShowTickLabels(true);

        ram = new Gauge(Gauge.SkinType.DASHBOARD);
        ram.setTitle("GB");
        ram.setUnit("RAM");
        ram.setAnimated(true);
        ram.setAnimationDuration(200);
        ram.setDecimals(0);
        ram.setValue(r.getRam());
        ram.setMinMeasuredValue(0);
        ram.setMaxValue(128);
        ram.setAutoScale(true);
        ram.setMaxWidth(150);
        ram.setMaxHeight(150);
        ram.setBarColor(Color.RED);

        cpuPane.getChildren().addAll(cpu, cName, cCores, cThreads, CpuThermo, ram);

        cpuPane.setTopAnchor(cpu, 20.0);
        cpuPane.setLeftAnchor(cpu, 20.0);

        cpuPane.setTopAnchor(cName, 70.0);
        cpuPane.setLeftAnchor(cName, 40.0);

        cpuPane.setTopAnchor(cCores, 90.0);
        cpuPane.setLeftAnchor(cCores, 40.0);

        cpuPane.setTopAnchor(cThreads, 110.0);
        cpuPane.setLeftAnchor(cThreads, 50.0);

        cpuPane.setTopAnchor(CpuThermo, 50.0);
        cpuPane.setLeftAnchor(CpuThermo, 300.0);

        cpuPane.setTopAnchor(ram, 50.0);
        cpuPane.setRightAnchor(ram, 20.0);

        AnchorPane gpuPane = new AnchorPane();
        gpuPane.setPrefSize(300,200);
        Label gpu = new Label("GPU: ");
        Label gName = new Label(g.getName());

        gpu.setFont(f);
        gName.setFont(f);

        GpuThermo = new JFXSlider(0,120,g.getTemp());
        GpuThermo.setValueChanging(false);
        GpuThermo.setOrientation(Orientation.VERTICAL);
        GpuThermo.getStylesheets().add("Thermo.css");
        GpuThermo.setShowTickMarks(true);
        GpuThermo.setShowTickLabels(true);
        GpuThermo.setMinWidth(30.0);

        gpuPane.getChildren().addAll(gpu, gName, GpuThermo);

        gpuPane.setTopAnchor(gpu, 20.0);
        gpuPane.setLeftAnchor(gpu, 20.0);

        gpuPane.setTopAnchor(gName, 70.0);
        gpuPane.setLeftAnchor(gName, 40.0);

        gpuPane.setTopAnchor(GpuThermo, 40.0);
        gpuPane.setLeftAnchor(GpuThermo, 250.0);

        AnchorPane diskPane = new AnchorPane();
        diskPane.setPrefSize(400,200);
        Label Disks = new Label("Disks: ");
        Label numDisks = new Label("Number of Disks: " + d.getDiskList().size());
        Label diskSize = new Label("Get Total Storage: " + d.getTotalSize());

        Disks.setFont(f);
        numDisks.setFont(f);
        diskSize.setFont(f);

        DiskThermos = new JFXSlider[d.getDiskList().size()];

        VBox thermos = new VBox(10);

        for(int x = 0; x < DiskThermos.length; x++) {
            DiskThermos[x] = new JFXSlider(0,120,d.getDiskList().get(x).getTemp());
            DiskThermos[x].setValueChanging(false);
            DiskThermos[x].setOrientation(Orientation.HORIZONTAL);
            DiskThermos[x].getStylesheets().add("Thermo.css");
            DiskThermos[x].setShowTickMarks(true);
            DiskThermos[x].setShowTickLabels(true);
            thermos.getChildren().add(new HBox(20,new Label(d.getDiskList().get(x).getName()), DiskThermos[x]));
        }

        diskPane.getChildren().addAll(Disks,numDisks, diskSize, thermos);

        diskPane.setTopAnchor(Disks, 20.0);
        diskPane.setLeftAnchor(Disks, 20.0);

        diskPane.setTopAnchor(numDisks, 40.0);
        diskPane.setLeftAnchor(numDisks, 40.0);

        diskPane.setTopAnchor(diskSize, 60.0);
        diskPane.setLeftAnchor(diskSize, 40.0);

        diskPane.setBottomAnchor(thermos, 20.0);
        diskPane.setRightAnchor(thermos, 30.0);

        ArrayList<Rectangle> rectangleArrayList = new ArrayList<>();
        rectangleArrayList.add(rC);
        rectangleArrayList.add(rG);
        rectangleArrayList.add(rD);

        for(int i = 0; i < rectangleArrayList.size(); i++) {
            rectangleArrayList.get(i).setFill(Color.WHITE);
            rectangleArrayList.get(i).setStroke(Color.BLACK);
            rectangleArrayList.get(i).setStrokeWidth(2.5);
            rectangleArrayList.get(i).setArcHeight(30);
            rectangleArrayList.get(i).setArcWidth(30);
        }

        pane = new AnchorPane(rC, rG, rD, cpuPane, gpuPane, diskPane);

        pane.setTopAnchor(rG, 20.0);
        pane.setLeftAnchor(rG, 20.0);

        pane.setTopAnchor(gpuPane, 20.0);
        pane.setLeftAnchor(gpuPane, 20.0);

        pane.setTopAnchor(rD, 20.0);
        pane.setRightAnchor(rD, 20.0);

        pane.setTopAnchor(diskPane, 20.0);
        pane.setRightAnchor(diskPane, 20.0);

        pane.setBottomAnchor(rC, 20.0);
        pane.setLeftAnchor(rC, 100.0);

        pane.setBottomAnchor(cpuPane, 20.0);
        pane.setLeftAnchor(cpuPane, 100.0);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() { liveUpdate(); }
        });
        t.start();
    }

    private void liveUpdate() {
        while(true) {
            c.updateTemp();
            g.updateTemp();
            d.UpdateTemps();
            CpuThermo.setValue(c.getTemp());
            GpuThermo.setValue(g.getTemp());
            for(int i = 0; i < DiskThermos.length; i++) {
                DiskThermos[i].setValue(d.getDiskList().get(i).getTemp());
            }
        }
    }


}
