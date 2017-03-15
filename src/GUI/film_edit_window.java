package GUI;

import Domain.film;
import MVC.FCIController;
import Validator.ExceptieValidareInRepository;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Alex on 01.01.2017.
 */
public class film_edit_window {

    private FCIController model;

    @FXML
    private Button btn_EF_confirm=new Button("A"),btn_EF_cancel=new Button("B");
    @FXML
    private TextField EF_textF1=new TextField(),EF_textF2=new TextField(),EF_textF3=new TextField(),EF_textF4=new TextField();

    public film_edit_window(main_window _gui)
    {
        model=_gui.getModel();
    }

    public void run(Integer key, String action)
    {

        final film old;
        if(action.contains("modifica")&& model.getFilmByID(key)!=null)
            old=model.getFilmByID(key);
        else
            old=null;

        Group localroot=new Group();
        Stage localStage=new Stage();
        Scene localScene=new Scene(localroot, 300, 256, Color.WHITESMOKE);
        File f=new File("reel.png");
        localStage.getIcons().add(new Image("file:"+f.getAbsolutePath()));
        localStage.setResizable(false);
        localStage.setScene(localScene);
        localStage.setTitle(action+" film");

        if(action.contains("modifica"))
        {
            if(old==null)
            {
                main_window.display_message_Box("Niciun element selectat!");
                return;
            }
        }

        VBox mainLayout=new VBox();
        try {
            FXMLLoader l=new FXMLLoader();
            File f1=new File("fxml\\edit_film.fxml");
            l.setLocation(new URL("file:///"+f1.getAbsolutePath()));
            l.setController(this);
            mainLayout=l.load();
            btn_EF_confirm.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int id,an;
                    String titlu=EF_textF2.getText();
                    String regizor=EF_textF4.getText();
                    try {
                        an=Integer.parseInt(EF_textF3.getText());
                        if(action.contains("adauga"))
                            model.adauga_film(new film(titlu,an,regizor));
                        if(action.contains("modifica"))
                        {
                            model.modifica_film(new film(titlu,an,old.getId(),regizor));
                        }
                        localStage.close();
                    }
                    catch (IOException | ExceptieValidareInRepository e)
                    {
                        main_window.display_message_Box(e.getMessage());
                    }
                    catch (NumberFormatException e)
                    {
                        main_window.display_message_Box("Anul trebuie sa fie un numar!");
                    }
                }
            });
            btn_EF_cancel.setOnMouseClicked((event)-> {
                    localStage.close();
            });

            localroot.setOnKeyReleased(event -> {
                if(event.getCode()== KeyCode.ESCAPE)
                    localStage.close();
                else if(event.getCode()==KeyCode.ENTER)
                    btn_EF_confirm.getOnMouseClicked().handle(null);
            });

            if(action.contains("modifica"))
            {
                if(old!=null)
                {
                    EF_textF1.setText(Integer.toString(old.getId()));
                    EF_textF2.setText(old.getTitlu());
                    EF_textF3.setText(Integer.toString(old.getAn_aparitie()));
                    EF_textF4.setText(old.getRegizor());
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        localroot.getChildren().add(mainLayout);
        localStage.show();
    }
}
