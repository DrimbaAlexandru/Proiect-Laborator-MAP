package GUI;

import Domain.user;
import Validator.ExceptieLogIn;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 05.01.2017.
 */
public class login_window
{
    private List<user> users;
    @FXML
    TextField TF_username;
    @FXML
    PasswordField TF_password;
    @FXML
    Button btn_login;
    @FXML
    ProgressIndicator ProgInd;
    @FXML
    Label Lbl_msg;

    boolean shouldReturn;

    public void setLbl_msg(String text){Lbl_msg.setText(text);}

    public login_window() throws IOException
    {
        File f=new File("users.txt");
        if(!f.exists() || f.length()==0)
        {
            users=new ArrayList<>();
            users.add(new user("admin","admin".hashCode(),true));
            users.add(new user("guest","".hashCode(),false));
        }
        else
            try {
                FileInputStream fileIn = new FileInputStream(f.getPath());
                ObjectInputStream in = new ObjectInputStream(fileIn);
                users = (ArrayList<user>) in.readObject();
                in.close();
                fileIn.close();
            }
            catch(ClassNotFoundException c) {
                c.printStackTrace();
                return;
            }
            catch (ClassCastException|IOException c)
            {
                c.printStackTrace();
                throw new IOException("Eroare la verificarea datelor de log in");
            }
    }

    private user check_user(String username, int password) throws ExceptieLogIn
    {
        user u=new user(username,password,false);
        for(user c:users)
            if(c.getUsername().equals(u.getUsername()))
                if(c.getPasswordHash()==u.getPasswordHash())
                    return c;
                else
                    throw new ExceptieLogIn("Parola gresita!");
        throw new ExceptieLogIn("Utilizatorul nu exista!");
    }

    private user retur=null;
    private Stage localStage=new Stage();

    public void run()
    {
        shouldReturn=false;
        retur=null;
        Group localroot=new Group();

        Scene localScene=new Scene(localroot, 400, 300, Color.color(1,1,1));
        localStage.getIcons().clear();
        localStage.getIcons().add(new Image("file:C:\\Users\\Alex\\IdeaProjects\\lab 1\\reel.png"));
        localStage.setResizable(false);
        localStage.setScene(localScene);
        localStage.setTitle("");

        GridPane mainLayout=new GridPane();
        try {
            FXMLLoader l=new FXMLLoader();

            File f=new File("fxml\\login.fxml");
            l.setLocation(new URL("file:///"+f.getAbsolutePath()));

            l.setController(this);
            mainLayout=l.load();

            TF_password.setOnKeyReleased((event ->
                {if (event.getCode()==KeyCode.ENTER) attempt_login(); }));
            TF_username.setOnKeyReleased((event ->
                {if (event.getCode()==KeyCode.ENTER) attempt_login(); }));
            btn_login.setOnMouseClicked((event -> { attempt_login();}));
        }
        catch (IOException e)
        {
            main_window.display_message_Box(e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        localroot.getChildren().add(mainLayout);
        localStage.showAndWait();
    }

    public user getLoggedUser()
    {
        return retur;
    }

    public void setLoading(boolean isLoading)
    {
        System.out.println("load");
        TF_password.setDisable(isLoading);
        TF_username.setDisable(isLoading);
        btn_login.setDisable(isLoading);
        ProgInd.setVisible(isLoading);
    }

    public void kill()
    {
        localStage.close();
    }

    private void attempt_login()
    {
        try
        {
            retur=check_user(TF_username.getText(),TF_password.getText().hashCode());
            shouldReturn=true;
            localStage.hide();
            setLoading(true);
            setLbl_msg("Conectare la baza de date...");
            localStage.show();

        }
        catch (ExceptieLogIn e)
        {
            main_window.display_message_Box(e.getMessage());
        }
    }

}
