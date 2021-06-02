package GUI;

import Parts.*;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import static javafx.stage.StageStyle.UTILITY;

//This class is responsible for creating the pane variable that contains the gui for the stress testing and benchmarking screen
public class TestScreen extends Screens{

    //The pane variable that contains all of the gui elements
    private StackPane pane;

    //The variable representing the cpu that can retrieve hardware information
    private CPU c;

    //The tester variable that allows the user to stress test and benchmark their system as well as save the results
    private Tester t;

    public TestScreen() {
        c = Storage.getCpu();
        t = Storage.getStress();
    }

    @Override
    public Pane getPane() {
        return pane;
    }

    @Override
    public void setPane() {
        ArrayList<Rectangle> rectangleArrayList = new ArrayList<>();

        Rectangle left = new Rectangle(300,450);
        Rectangle right = new Rectangle(300,450);

        rectangleArrayList.add(right);
        rectangleArrayList.add(left);

        for(int i =0; i < rectangleArrayList.size(); i++) {
            rectangleArrayList.get(i).setFill(Color.WHITE);
            rectangleArrayList.get(i).setStroke(Color.BLACK);
            rectangleArrayList.get(i).setStrokeWidth(2.5);
            rectangleArrayList.get(i).setArcHeight(30);
            rectangleArrayList.get(i).setArcWidth(30);
        }

        pane = new StackPane(left, right);
        pane.setMargin(left, new Insets(0,10,0,70));
        pane.setMargin(right, new Insets(0,70,0,10));
        pane.setAlignment(left, Pos.CENTER_LEFT);
        pane.setAlignment(right, Pos.CENTER_RIGHT);

        setLeft();
        setRight();
    }

    private void setRight() {
        Label title = new Label("Stress Test");
        title.setFont(Font.font("Verdana", FontWeight.BLACK,12));
        Label exp = new Label("Stress Testing is the act of purposefully putting a computer under an intense load in order to reveal possible instabilities. Starting the test will run a series of complex algorithms designed push a CPU until the user stops the test revealing any possible issues with the machine.");
        Button stress = new JFXButton("Start Test");

        exp.setWrapText(true);
        exp.setMaxWidth(280);
        //exp.setPadding(new Insets(0,0,80,0));

        stress.setStyle("-fx-background-color: gray");
        //bench.setPadding(new Insets(40,0,0,0));
        stress.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(stress.getText().equalsIgnoreCase("Start Test")) {
                    stress.setText("Stop Test");
                }
                else {
                    stress.setText("Start Test");
                }


                Thread tx = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        t.toggleOn();
                    }
                });
                tx.start();

            }
        });

        VBox rightV = new VBox(30,title,exp);
        rightV.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(rightV, stress);
        pane.setMargin(rightV, new Insets(70,70,0,0));
        rightV.setTranslateX(240);
        rightV.setTranslateY(-150);
        stress.setTranslateX(225);
        stress.setTranslateY(75);
    }

    private void setLeft() {
        Label title = new Label("Benchmark");
        title.setFont(Font.font("Verdana", FontWeight.BLACK,12));
        Label exp = new Label("Benchmarking allows this program to test the processing capability of your machine in order to return a performance metric that can easily be compared to both other machines and different computer configurations. This test is time based and therefore a lower score symbolizes a faster machine.");
        Button bench = new JFXButton("Start Benchmark");

        exp.setWrapText(true);
        exp.setMaxWidth(280);
        //exp.setPadding(new Insets(0,0,80,0));

        bench.setStyle("-fx-background-color: gray");
        //bench.setPadding(new Insets(40,0,0,0));
        bench.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final int[] score = {0};

                Thread tx = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            score[0] = t.bench(c.getCores());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                tx.start();

                try {
                    tx.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Stage s2 = new Stage(UTILITY);
                s2.setAlwaysOnTop(true);
                Button exit = new Button("Close");
                exit.setOnAction(e -> s2.close());
                exit.setTranslateY(25);
                exit.setDefaultButton(true);

                Text text = new Text("Score: " + score[0]);
                text.setFont(new Font(15));
                text.setTranslateY(-25);

                StackPane layout = new StackPane(exit, text);
                Scene sc = new Scene(layout, 400,125);

                s2.setScene(sc);
                s2.show();
            }
        });

        VBox leftV = new VBox(30,title,exp);
        leftV.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(leftV, bench);
        pane.setMargin(leftV, new Insets(70,0,0,70));
        leftV.setTranslateX(-240);
        leftV.setTranslateY(-150);
        bench.setTranslateX(-225);
        bench.setTranslateY(75);
    }
}
