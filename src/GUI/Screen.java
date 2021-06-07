package GUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerNextArrowBasicTransition;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import java.awt.*;
import java.awt.event.*;

//The class that determines which pane is being shown as well as surrounding gui elements
public class Screen {

    //The pane currently being displayed
    private Pane mainContent;

    //The StackPane containing all other panes including the sidebar
    private StackPane root;

    //The sidebar used for switching panes
    private BorderPane sidebar;

    //The stage that displays the gui
    private Stage stage;

    //The array of buttons used in the sidebar
    private JFXButton[] jfxButtons;

    //The objects representing the individual screens
    private Home H;
    private HardwareScreen HW;
    private HealthScreen HS;
    private NetworkingScreen N;
    private TestScreen T;

    //The boolean used to see if the screen is minimized
    private boolean min;

    //Starts the gui and creates the side bar as well as sets up the close to tray feature
    public Screen(Stage s) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                 H = new Home();
                 HW = new HardwareScreen();
                 HS = new HealthScreen();
                 N = new NetworkingScreen();
                 T = new TestScreen();
                H.setPane();
                HW.setPane();
                HS.setPane();
                N.setPane();
                T.setPane();
                Platform.runLater(() -> setHome());
            }
        });
        t.start();

        min = true;

        stage = s;
        stage = new Stage(StageStyle.DECORATED);

        stage.setTitle("");
        stage.getIcons().addAll(new javafx.scene.image.Image("TrayIcon.png"));

        root = new StackPane();
        mainContent = new Pane();
        sidebar = new BorderPane();

        jfxButtons = new JFXButton[]{
                new JFXButton("Home              "   , new FontAwesomeIconView(FontAwesomeIcon.HOME)),
                new JFXButton("Hardware        "     , new FontAwesomeIconView(FontAwesomeIcon.SERVER)),
                new JFXButton("System Health "       , new FontAwesomeIconView(FontAwesomeIcon.HEARTBEAT)),
                new JFXButton("Networking     "      , new FontAwesomeIconView(FontAwesomeIcon.GLOBE)),
                new JFXButton("Stress Testing   "    , new FontAwesomeIconView(FontAwesomeIcon.STACK_OVERFLOW)),
                //new JFXButton("Options            "  , new FontAwesomeIconView(FontAwesomeIcon.GEAR))
        };
        jfxButtons[0].setOnAction(e -> setHome());
        jfxButtons[1].setOnAction(e -> setHardware());
        jfxButtons[2].setOnAction(e -> setHealth());
        jfxButtons[3].setOnAction(e -> setNetworking());
        jfxButtons[4].setOnAction(e -> setTest());
        //jfxButtons[5].setOnAction(e -> options());

        jfxButtons[0].setStyle("-fx-background-color: #55f");

        JFXHamburger hamburger = new JFXHamburger();
        HamburgerNextArrowBasicTransition transition = new HamburgerNextArrowBasicTransition(hamburger);
        transition.setRate(-1);

        hamburger.setAlignment(Pos.BASELINE_LEFT);
        hamburger.setPadding(new Insets(10));
        hamburger.setStyle("-fx-background-color: #19f;");


        hamburger.setOnMouseClicked(event -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if (transition.getRate() == -1) {
                for (JFXButton jfxButton : jfxButtons) {
                    jfxButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                }
            } else {
                for (JFXButton jfxButton : jfxButtons) {
                    jfxButton.setContentDisplay(ContentDisplay.LEFT);
                }
            }
        });

        if (min) {
            Platform.setImplicitExit(false);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    stage.hide();
                    if (!SystemTray.isSupported()) {
                        return;
                    }
                    final PopupMenu p = new PopupMenu();

                    final Image image = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("TrayIcon.png"));

                    final TrayIcon tIco = new TrayIcon(image);

                    final SystemTray trayIcon = SystemTray.getSystemTray();

                    CheckboxMenuItem cb = new CheckboxMenuItem("Minimize");
                    MenuItem exit = new MenuItem("Exit");
                    MenuItem show = new MenuItem("Show");

                    cb.setState(true);

                    cb.addItemListener(new ItemListener() {
                        @Override
                        public void itemStateChanged(ItemEvent e) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    min = false;
                                    stage.show();
                                    trayIcon.remove(tIco);
                                    Platform.setImplicitExit(true);
                                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                        @Override
                                        public void handle(WindowEvent event) {
                                            System.exit(0);
                                        }
                                    });
                                }
                            });
                        }
                    });

                    exit.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    });

                    show.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    stage.show();
                                    trayIcon.remove(tIco);
                                }
                            });
                        }
                    });

                    p.add(cb);
                    p.addSeparator();
                    p.add(exit);
                    p.add(show);

                    tIco.setPopupMenu(p);
                    tIco.setImageAutoSize(true);

                    try {
                        trayIcon.add(tIco);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

            ScrollPane scrollPane = new ScrollPane();
            VBox vBox = new VBox();
            scrollPane.setContent(vBox);

            vBox.getStyleClass().add("content_scene_left");
            vBox.getChildren().add(hamburger);
            vBox.getChildren().addAll(jfxButtons);

            for (JFXButton jfxButton : jfxButtons) {
                jfxButton.setMaxWidth(Double.MAX_VALUE);
                jfxButton.setRipplerFill(Color.BLUE);
                VBox.setVgrow(jfxButton, Priority.ALWAYS);
                jfxButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }
            vBox.setFillWidth(true);

            sidebar.setLeft(scrollPane);
            sidebar.setPickOnBounds(false);

            stage.setResizable(false);
    }

    //Changes the screen based on user input
    private void changeScene() {
        root = new StackPane(mainContent, sidebar);
        root.setMinSize(900,500);
        root.setPrefSize(900,500);
        root.setMargin(mainContent, new Insets(0,0, 0, 40));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //sets all the buttons background color to white
    private void allButtonsWhite() {
        for(int i = 0; i < jfxButtons.length; i++) {
            jfxButtons[i].setStyle("-fx-background-color: #ffff");
        }
    }

    //Sets up the home screen and colors the appropriate button
    private void setHome() {
        mainContent = H.getPane();
        changeScene();
        allButtonsWhite();
        jfxButtons[0].setStyle("-fx-background-color: #55f");
    }

    //Sets up the hardware screen and colors the appropriate button
    private void setHardware() {
        mainContent = HW.getPane();
        changeScene();
        allButtonsWhite();
        jfxButtons[1].setStyle("-fx-background-color: #55f");
    }

    //Sets up the health screen and colors the appropriate button
    private void setHealth() {
        mainContent = HS.getPane();
        changeScene();
        allButtonsWhite();
        jfxButtons[2].setStyle("-fx-background-color: #55f");
    }

    //Sets up the network screen and colors the appropriate button
    private void setNetworking() {
        mainContent = N.getPane();
        changeScene();
        allButtonsWhite();
        jfxButtons[3].setStyle("-fx-background-color: #55f");
    }

    //Sets up the testing screen and colors the appropriate button
    private void setTest() {
        mainContent = T.getPane();
        changeScene();
        allButtonsWhite();
        jfxButtons[4].setStyle("-fx-background-color: #55f");
    }

}
