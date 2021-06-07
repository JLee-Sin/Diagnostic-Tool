package GUI;

import Parts.Networking;
import eu.hansolo.medusa.Gauge;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

//This class is responsible for creating the pane variable that contains the gui for the network screen
public class NetworkingScreen extends Screens{

    //The pane variable containing all of the gui elements
    private BorderPane pane;

    //The networking variable that can retrieve all of the information about the network that the system is connected to
    private Networking n;

    //The gauges used to relay network information visually
    private Gauge g;
    private Gauge trans;
    private Gauge rec;

    //sets the network variable
    public NetworkingScreen() {
        n = Storage.getNetwork();
    }

    @Override
    public Pane getPane() {
        return pane;
    }

    //Creates the network screen
    @Override
    public void setPane() {
        Rectangle netInfo = new Rectangle(550,400);
        Rectangle indicators = new Rectangle(200, 400);

        ArrayList<Rectangle> rectangleArrayList = new ArrayList<>();
        rectangleArrayList.add(netInfo);
        rectangleArrayList.add(indicators);

        for(int i = 0; i < rectangleArrayList.size(); i++) {
            rectangleArrayList.get(i).setFill(Color.WHITE);
            rectangleArrayList.get(i).setStroke(Color.BLACK);
            rectangleArrayList.get(i).setStrokeWidth(2.5);
            rectangleArrayList.get(i).setArcHeight(30);
            rectangleArrayList.get(i).setArcWidth(30);
        }

        Label printout = new Label();
        try {
            printout = new Label(n.getNetworkInfo());
        } catch (IOException e) {
            e.printStackTrace();
        }

        rec = new Gauge(Gauge.SkinType.SLIM);
        rec.setTitle("Receive Rate");
        rec.setUnit("Mbps");
        rec.setAnimated(true);
        rec.setAnimationDuration(150);
        try {
            System.out.println(n.rawReceive());
            rec.setValue(n.rawReceive());
        } catch (IOException e) {
            e.printStackTrace();
        }
        rec.setMinValue(0);
        rec.setMaxValue(1000);
        rec.setAutoScale(true);
        rec.setMaxWidth(125);
        rec.setMaxHeight(125);
        rec.setBarColor(Color.GREEN);
        rec.setDecimals(1);
        rec.setTitleColor(Color.BLACK);
        rec.setUnitColor(Color.BLACK);
        rec.setValueColor(Color.BLACK);

        trans = new Gauge(Gauge.SkinType.SLIM);
        trans.setTitle("Transmit Rate");
        trans.setUnit("Mbps");
        trans.setAnimated(true);
        trans.setAnimationDuration(150);
        try {
            System.out.println(n.rawTransmit());
            trans.setValue(n.rawTransmit());
        } catch (IOException e) {
            e.printStackTrace();
        }
        trans.setMinValue(0);
        trans.setMaxValue(1000);
        trans.setAutoScale(true);
        trans.setMaxWidth(125);
        trans.setMaxHeight(125);
        trans.setBarColor(Color.PINK);
        trans.setDecimals(1);
        trans.setTitleColor(Color.BLACK);
        trans.setUnitColor(Color.BLACK);
        trans.setValueColor(Color.BLACK);

        g = new Gauge(Gauge.SkinType.DASHBOARD);
        g.setTitle("ms");
        g.setUnit("Ping");
        g.setAnimated(true);
        g.setAnimationDuration(200);
        g.setDecimals(0);
        try {
            g.setValue(n.getRawPing());
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.setMinMeasuredValue(0);
        g.setMaxMeasuredValue(100);
        g.setAutoScale(true);
        g.setMaxWidth(125);
        g.setMaxHeight(125);
        g.setBarColor(Color.RED);

        VBox guages = new VBox(10, g, rec, trans);
        StackPane visuals = new StackPane(indicators, guages);
        guages.setTranslateX(37);
        guages.setTranslateY(45);

        StackPane info = new StackPane(netInfo, printout);

        pane = new BorderPane();

        pane.setLeft(info);
        pane.setAlignment(info, Pos.CENTER_LEFT);
        pane.setMargin(info,new Insets(0,0,0,20));

        pane.setRight(visuals);
        pane.setAlignment(visuals,Pos.CENTER_RIGHT);
        pane.setMargin(visuals, new Insets(0,20,0,0));
        liveUpdate();
    }

    //Starts an infinite loop that constantly updates the network information
    private void liveUpdate() {
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        g.setValue(n.getRawPing());
                        trans.setValue(n.rawTransmit());
                        rec.setValue(n.rawReceive());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t2.start();
    }
}
