package GUI;

import Parts.OS;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import static javafx.stage.StageStyle.UTILITY;

//This class is responsible for creating the pane variable that contains the gui for the hardware screen
public class HealthScreen extends Screens{

    //The pane variable containing all of the gui elements
    private BorderPane pane;

    //The os variable that can retrieve information from the windows command line
    private OS os;

    //The string that is updated with the status of the OS
    private String status;

    public HealthScreen() {
        os = Storage.getSystem();
    }

    @Override
    public Pane getPane() {
        return pane;
    }

    @Override
    public void setPane() {
        Rectangle r = new Rectangle(650,450);
        r.setFill(Color.WHITE);
        r.setStroke(Color.BLACK);
        r.setStrokeWidth(2.5);
        r.setArcHeight(30);
        r.setArcWidth(30);

        Label name = new Label("OS Name: " + os.getName());
        Label ver = new Label("OS Version: " + os.getOSver());

        name.setFont(Font.font("Verdana", FontWeight.BLACK,12));
        ver.setFont(Font.font("Verdana", FontWeight.BLACK,12));

        VBox v = new VBox(20, name, ver);
        v.setAlignment(Pos.CENTER_LEFT);
        v.setPadding(new Insets(0,0,0,130));

        Label title = new Label("OS Scan");
        Label exp = new Label("This OS scan feature utilizes window's inbuilt command line and system resource protection features to look for errors and then attempts to repair them. Regardless of the result the system then reports the result as a pop up and on the Home screen.");
        JFXButton scan = new JFXButton("Run Scan");

        title.setPadding(new Insets(50,0,0,0));
        title.setFont(Font.font("Verdana", FontWeight.BLACK,12));

        scan.setDefaultButton(true);
        scan.setStyle("-fx-background-color: gray");
        scan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int y = os.getHealth();
                            if(y == 0 || y == 1) {
                                status = "Healthy";
                            }
                            else {
                                status = "Unhealthy";
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();

                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Stage s2 = new Stage(UTILITY);
                Button exit = new Button("Close");
                exit.setOnAction(e -> s2.close());
                exit.setTranslateY(25);
                exit.setDefaultButton(true);

                Text text = new Text("Your system is now: " + status);
                text.setFont(new Font(15));
                text.setTranslateY(-25);

                StackPane layout = new StackPane(exit, text);
                Scene sc = new Scene(layout, 400,125);

                s2.setScene(sc);
                s2.show();
            }
        });

        exp.setMaxWidth(400);
        exp.setWrapText(true);
        exp.setPadding(new Insets(0,0,100,0));

        VBox features = new VBox(30, title,exp, scan,v);
        features.setAlignment(Pos.TOP_CENTER);

        StackPane stack = new StackPane(r, features);

        pane = new BorderPane(stack);
    }
}
