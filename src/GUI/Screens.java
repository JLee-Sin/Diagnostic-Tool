package GUI;


import javafx.scene.layout.*;
import java.io.FileNotFoundException;

//The abstract class used as a template for all screen classes
public abstract class Screens {

    //The pane variable used to contain the gui elements in a screen object
    private Pane pane;

    //The method that retrieves the pane from the correct screen object
    public abstract Pane getPane();

    //The set pane method that adds all of the gui elements to the pane
    public abstract void setPane() throws FileNotFoundException;

}
