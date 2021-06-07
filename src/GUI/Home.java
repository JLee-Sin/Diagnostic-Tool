package GUI;

import Parts.*;
import com.jfoenix.controls.JFXSlider;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.io.*;
import java.util.ArrayList;
import eu.hansolo.medusa.*;

//This class is responsible for creating the pane variable that contains the gui for the home screen
public class Home extends Screens{

    //The pane variable responsible for holding all of the gui elements
    private AnchorPane pane;

    //The variable that represents the cpu and can retrieve all of its information
    private CPU c;

    //The variable that represents the gpu and can retrieve all of its information
    private GPU g;

    //The variable that represents the ram in the system and can retrieve all of its information
    private RAM r;

    //The variable that represents all the disks in the system and can retrieve all of its information
    private DiskList d;

    //The os variable that can retrieve information from the windows command line
    private OS sys;

    //The tester variable responsible for retrieving data from all past tests
    private Tester t;

    //The networking variable that can retrieve all of the information about the network that the system is connected to
    private Networking n;

    //The label depicting the status of the OS
    private Label status;

    //The sliders used as thermometers to represent each of the internal components
    private JFXSlider CpuThermo;
    private JFXSlider GpuThermo;
    private JFXSlider DiskThermo;

    //The shapes used to divide the different areas of the screen
    private Rectangle hw;
    private Rectangle os;
    private Rectangle ts;
    private Rectangle ns;

    //The gauges used to relay network information visually
    private Gauge rec;
    private Gauge trans;
    private Gauge ge;

    //The labels representing the record benchmark and the latest benchmark
    private Label recBench;
    private Label lateBench;

    //Sets the variables for the home screen
    public Home() {
        c = Storage.getCpu();
        g = Storage.getGpu();
        r = Storage.getRam();
        d = Storage.getDisks();
        sys = Storage.getSystem();
        t = Storage.getStress();
        n = Storage.getNetwork();
    }

    @Override
    public Pane getPane() {
        return pane;
    }

    //Creates the home screen
    @Override
    public void setPane() {
        //shapes
        Thread setupRectangles = new Thread(new Runnable() {
            @Override
            public void run() {
                hw = new Rectangle(400,200);
                os = new Rectangle(600,200);
                ts = new Rectangle(205,100);
                ns = new Rectangle(200,450);


                ArrayList<Rectangle> rectangleArrayList = new ArrayList<>();
                rectangleArrayList.add(hw);
                rectangleArrayList.add(os);
                rectangleArrayList.add(ts);
                rectangleArrayList.add(ns);

                for(int i = 0; i < rectangleArrayList.size(); i++) {
                    rectangleArrayList.get(i).setFill(Color.WHITE);
                    rectangleArrayList.get(i).setStroke(Color.BLACK);
                    rectangleArrayList.get(i).setStrokeWidth(2.5);
                    rectangleArrayList.get(i).setArcHeight(30);
                    rectangleArrayList.get(i).setArcWidth(30);
                }
            }
        });
        setupRectangles.start();


        //hw rectangle
        CpuThermo = new JFXSlider(0,120,c.getTemp());
        GpuThermo = new JFXSlider(0,120,g.getTemp());
        DiskThermo = new JFXSlider(0,120,d.getAverageTemp());

        CpuThermo.setValueChanging(false);
        GpuThermo.setValueChanging(false);
        DiskThermo.setValueChanging(false);


        CpuThermo.setOrientation(Orientation.VERTICAL);
        GpuThermo.setOrientation(Orientation.VERTICAL);
        DiskThermo.setOrientation(Orientation.HORIZONTAL);

        CpuThermo.getStylesheets().add("Thermo.css");
        GpuThermo.getStylesheets().add("Thermo.css");
        DiskThermo.getStylesheets().add("Thermo.css");

        CpuThermo.setShowTickMarks(true);
        CpuThermo.setShowTickLabels(true);

        GpuThermo.setShowTickMarks(true);
        GpuThermo.setShowTickLabels(true);

        DiskThermo.setShowTickMarks(true);
        DiskThermo.setShowTickLabels(true);

        Label cpu = new Label("CPU");
        Label gpu = new Label("GPU");
        Label disk = new Label("Number of Disks: " + d.getDiskList().size() + "\n" + "Total Space: " + d.getTotalSize());
        Label diskT = new Label("Disks*");
        Label warn = new Label("*This is the average temperature of all the disks in your system and may not be a completely accurate representation of your hardware, please see the hardware panel for a more in depth look");
        Label ram = new Label("RAM: " + r.getRam());

        Font labels = Font.font("Verdana", FontWeight.BLACK,12);
        cpu.setFont(labels);
        gpu.setFont(labels);
        diskT.setFont(labels);

        Font rf = Font.font("Verdana", FontWeight.SEMI_BOLD,16);
        ram.setFont(rf);

        Font df = Font.font("Verdana", FontWeight.THIN,12);
        disk.setFont(df);

        warn.setWrapText(true);
        warn.setFont(new Font(8));

        //System rectangle
        Font f = Font.font("System", FontWeight.BOLD,16);
        Label name = new Label("OS Name: " + sys.getName());
        Label ver = new Label("OS Version: " + sys.getOSver());
        Label s = new Label("Status: ");

        name.setFont(f);
        ver.setFont(f);
        s.setFont(f);

        try {
            setStatus();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recBench = new Label("Record Benchmark Score: " + t.getTopBench());
        lateBench = new Label("Latest Benchmark Score: " + t.getLatestBench());
        recBench.setFont(df);
        lateBench.setFont(df);

        rec = new Gauge(Gauge.SkinType.SLIM);
        rec.setTitle("Receive Rate");
        rec.setUnit("Mbps");
        rec.setAnimated(true);
        rec.setAnimationDuration(200);
        try {
            System.out.println(n.rawReceive());
            rec.setValue(n.rawReceive());
        } catch (IOException e) {
            e.printStackTrace();
        }
        rec.setMinValue(0);
        rec.setMaxValue(1000);
        rec.setAutoScale(true);
        rec.setMaxWidth(140);
        rec.setMaxHeight(140);
        rec.setBarColor(Color.GREEN);
        rec.setDecimals(1);
        rec.setTitleColor(Color.BLACK);
        rec.setUnitColor(Color.BLACK);
        rec.setValueColor(Color.BLACK);

        trans = new Gauge(Gauge.SkinType.SLIM);
        trans.setTitle("Transmit Rate");
        trans.setUnit("Mbps");
        trans.setAnimated(true);
        trans.setAnimationDuration(200);
        try {
            System.out.println(n.rawTransmit());
            trans.setValue(n.rawTransmit());
        } catch (IOException e) {
            e.printStackTrace();
        }
        trans.setMinValue(0);
        trans.setMaxValue(1000);
        trans.setAutoScale(true);
        trans.setMaxWidth(140);
        trans.setMaxHeight(140);
        trans.setBarColor(Color.PINK);
        trans.setDecimals(1);
        trans.setTitleColor(Color.BLACK);
        trans.setUnitColor(Color.BLACK);
        trans.setValueColor(Color.BLACK);

        ge = new Gauge(Gauge.SkinType.DASHBOARD);
        ge.setTitle("ms");
        ge.setUnit("Ping");
        ge.setAnimated(true);
        ge.setAnimationDuration(200);
        ge.setDecimals(0);
        try {
            ge.setValue(n.getRawPing());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ge.setMinMeasuredValue(0);
        ge.setMaxMeasuredValue(100);
        ge.setAutoScale(true);
        ge.setMaxWidth(150);
        ge.setMaxHeight(150);
        ge.setBarColor(Color.RED);

        pane = new AnchorPane(hw, os, ts, ns, CpuThermo, GpuThermo, DiskThermo, cpu, gpu, disk, diskT, ram, warn, name, ver, s, status, recBench, lateBench, ge, rec, trans);

        pane.setTopAnchor(hw, 30.0);
        pane.setBottomAnchor(hw,200.0);
        pane.setLeftAnchor(hw, 20.0);
        pane.setRightAnchor(hw, 280.0);

        pane.setTopAnchor(CpuThermo,50.0);
        pane.setLeftAnchor(CpuThermo, 60.0);

        pane.setTopAnchor(GpuThermo,50.0);
        pane.setLeftAnchor(GpuThermo, 140.0);

        pane.setTopAnchor(DiskThermo, 160.0);
        pane.setLeftAnchor(DiskThermo, 260.0);

        pane.setTopAnchor(cpu, 200.0);
        pane.setLeftAnchor(cpu, 60.0);

        pane.setTopAnchor(gpu, 200.0);
        pane.setLeftAnchor(gpu, 140.0);

        pane.setTopAnchor(disk, 120.0);
        pane.setLeftAnchor(disk, 260.0);

        pane.setTopAnchor(diskT, 200.0);
        pane.setLeftAnchor(diskT, 320.0);

        pane.setBottomAnchor(warn, 12.5);
        pane.setLeftAnchor(warn, 20.0);

        pane.setTopAnchor(ram, 70.0);
        pane.setLeftAnchor(ram, 210.0);

        pane.setBottomAnchor(os, 30.0);
        pane.setLeftAnchor(os, 20.0);
        pane.setRightAnchor(os, 170.0);

        pane.setBottomAnchor(name, 190.0);
        pane.setLeftAnchor(name, 50.0);

        pane.setBottomAnchor(ver, 170.0);
        pane.setLeftAnchor(ver, 50.0);

        pane.setBottomAnchor(s, 60.0);
        pane.setLeftAnchor(s, 200.0);

        pane.setBottomAnchor(status, 40.0);
        pane.setLeftAnchor(status, 280.0);

        pane.setTopAnchor(ts, 50.0);
        pane.setBottomAnchor(ts, 220.0);
        pane.setLeftAnchor(ts, 432.5);

        pane.setTopAnchor(recBench,80.0);
        pane.setLeftAnchor(recBench, 440.0);

        pane.setTopAnchor(lateBench, 100.0);
        pane.setLeftAnchor(lateBench, 440.0);

        pane.setTopAnchor(ns, 30.0);
        pane.setBottomAnchor(ns, 30.0);
        pane.setRightAnchor(ns, 10.0);

        pane.setTopAnchor(ge, 30.0);
        pane.setRightAnchor(ge, 40.0);

        pane.setTopAnchor(rec, 175.0);
        pane.setRightAnchor(rec,40.0);

        pane.setTopAnchor(trans, 325.0);
        pane.setRightAnchor(trans, 40.0);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                liveUpdate();
            }
        });
        thread.start();
    }

    //Starts an infinite loop that constantly update the temperature of the each component
    private void liveUpdate() {
        while(true) {
            c.updateTemp();
            g.updateTemp();
            d.UpdateTemps();
            CpuThermo.setValue(c.getTemp());
            GpuThermo.setValue(g.getTemp());
            DiskThermo.setValue(d.getAverageTemp());
            try {
                ge.setValue(n.getRawPing());
                trans.setValue(n.rawTransmit());
                rec.setValue(n.rawReceive());
                setStatus();
                recBench.setText("Record Benchmark Score: " + t.getTopBench());
                lateBench.setText("Latest Benchmark Score: " + t.getLatestBench());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Sets the status of operating system on the home screen based on previous scans
    private void setStatus() throws IOException {
        status = new Label(sys.checkForPreviousScan());
        status.setFont(new Font(64));
        if(sys.checkForPreviousScan().equalsIgnoreCase("Unknown")) {
            status.setTextFill(Color.GRAY);
        }
        else if(sys.checkForPreviousScan().equalsIgnoreCase("Healthy")) {
            status.setTextFill(Color.GREEN);
        }
        else {
            status.setTextFill(Color.RED);
        }
    }

}
