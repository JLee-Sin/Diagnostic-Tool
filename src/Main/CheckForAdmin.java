package Main;

import GUI.Screen;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import static javafx.stage.StageStyle.UTILITY;

//This class is just responsible for testing to see if the user is running the program as an administrator and informs them if they are not
public class CheckForAdmin {

    public CheckForAdmin(Stage stage) {
        if(!isAdmin()) {
            stage.initStyle(UTILITY);
            stage.setTitle(" ");

            Button exit = new Button("Close");
            exit.setOnAction(e -> stage.close());
            exit.setTranslateY(25);
            exit.setDefaultButton(true);

            Text text = new Text("Please run the program as an administrator");
            text.setFont(new Font(15));
            text.setTranslateY(-25);

            StackPane layout = new StackPane(exit, text);
            Scene scene = new Scene(layout, 400,125);

            stage.setScene(scene);
            stage.show();
        }
        else {
            new Screen(stage);
        }
    }

    private static boolean isAdmin() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe");
            Process process = processBuilder.start();
            PrintStream printStream = new PrintStream(process.getOutputStream(), true);
            Scanner scanner = new Scanner(process.getInputStream());
            printStream.println("@echo off");
            printStream.println(">nul 2>&1 \"%SYSTEMROOT%\\system32\\cacls.exe\" \"%SYSTEMROOT%\\system32\\config\\system\"");
            printStream.println("echo %errorlevel%");

            boolean printedErrorlevel = false;
            while (true) {
                String nextLine = scanner.nextLine();
                if (printedErrorlevel) {
                    int errorlevel = Integer.parseInt(nextLine);
                    return errorlevel == 0;
                } else if (nextLine.equals("echo %errorlevel%")) {
                    printedErrorlevel = true;
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
