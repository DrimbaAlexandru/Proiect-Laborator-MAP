import Builder.Builder;
import Domain.client;
import Domain.user;
import GUI.login_window;
import GUI.main_window;
import MVC.*;
import MemoryRepositories.CrudRepository;
import SQL_Repositories.I_SQLRepository;
import SQL_Repositories.clientSQLRepository;
import Validator.*;
import SQL_Repositories.SQLServer_Connection;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Alex on 13.10.2016.
 */

public class main extends Application {
    private static main_window g;
    private static FCIController service;
    private static Controller ctrlr;



    public void start(Stage stage)
    {
        try
        {
            stage.getIcons().clear();
            stage.getIcons().add(new Image("file:C:\\Users\\Alex\\IdeaProjects\\lab 1\\reel.png"));
            login_window login=new login_window();
            user u;
            login.run();
            u=login.getLoggedUser();
            if(u==null)
                throw new ExceptieLogIn("Logare esuata!");

            Builder builder = new Builder();
            service=builder.build();
            g=new main_window(service,u);
            ctrlr=new Controller(g,service);
            login.setLoading(false);
            login.kill();

            while(g.run(stage)==true)
            {
                login.run();
                u=login.getLoggedUser();
                login.kill();
                if(u==null)
                    throw new ExceptieLogIn("Logare esuata!");
                g.setLoggedUser(u);
            }
            stage.setOnCloseRequest((event )-> {System.out.print("Gata");});
        }
        catch (IOException e)
        {
            main_window.display_message_Box(e.getMessage());
        }
        catch (ExceptieLogIn e)
        {
            main_window.display_message_Box(e.getMessage());
        }

    }

    public static void main(String[] args)
    {
        launch(args);
    }
}