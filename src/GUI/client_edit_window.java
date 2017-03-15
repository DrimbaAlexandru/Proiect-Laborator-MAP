package GUI;

import Domain.client;
import MVC.FCIController;
import Validator.ExceptieValidareInRepository;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by Alex on 01.01.2017.
 */
public class client_edit_window {
    private FCIController model;

    public client_edit_window(main_window _gui)
    {
        model=_gui.getModel();
    }

    public void run(int key,String action)
    {
        final client old;

        if(action.contains("modifica") && model.getClientByID(key)!=null)
            old=model.getClientByID(key);
        else
        {
            old=null;
        }

        Group localroot=new Group();
        Stage localStage=new Stage();
        Scene localScene=new Scene(localroot, 256, 200, Color.WHITESMOKE);
        File f=new File("reel.png");
        localStage.getIcons().add(new Image("file:"+f.getAbsolutePath()));
        localStage.setResizable(false);
        localStage.setScene(localScene);
        localStage.setTitle(action +" client");
        VBox mainLayout=new VBox(10);
        HBox row1=new HBox();
        HBox row2=new HBox();
        HBox row3=new HBox();
        HBox row5=new HBox(50);
        mainLayout.setAlignment(Pos.CENTER);
        Separator invisep=new Separator(Orientation.HORIZONTAL);
        Separator sep=new Separator(Orientation.HORIZONTAL);
        sep.setPadding(new Insets(0,0,10,0));
        invisep.setVisible(false);
        mainLayout.getChildren().addAll(invisep,row1,row2,row3,sep,row5);

        int space=75;

        Label lbl1=new Label("ID: ");
        lbl1.setPrefWidth(space);
        TextField textF1=new TextField();
        textF1.setEditable(false);
        textF1.setPromptText("Va fi generat automat");
        row1.getChildren().addAll(lbl1,textF1);
        row1.setAlignment(Pos.CENTER);

        Label lbl2=new Label("Nume: ");
        lbl2.setPrefWidth(space);
        TextField textF2=new TextField();
        row2.getChildren().addAll(lbl2,textF2);
        row2.setAlignment(Pos.CENTER);

        Label lbl3=new Label("Adresa: ");
        lbl3.setPrefWidth(space);
        TextField textF3=new TextField();
        row3.getChildren().addAll(lbl3,textF3);
        row3.setAlignment(Pos.CENTER);

        mainLayout.setPadding(new Insets(0,0,0,7));

        Button btn_confirm=new Button("OK");
        Button btn_cancel=new Button("Cancel");
        btn_confirm.setPrefWidth(100);
        btn_cancel.setPrefWidth(100);
        btn_confirm.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                String nume=textF2.getText();
                String adresa=textF3.getText();
                try {
                    if(action.contains("adauga"))
                        model.adauga_client(new client(nume,adresa));
                    if(action.contains("modifica"))
                    {
                        model.modifica_client(new client(nume,old.getId(),adresa));

                    }
                    localStage.close();
                }
                catch (IOException | ExceptieValidareInRepository e)
                {
                    main_window.display_message_Box(e.getMessage());
                }
            }
        });
        btn_cancel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                localStage.close();
            }
        });

        row5.getChildren().addAll(btn_confirm,btn_cancel);
        row5.setAlignment(Pos.CENTER);

        if(action.contains("modifica"))
        {
            if(old==null)
            {
                main_window.display_message_Box("Niciun element selectat!");
                return;
            }
            else
            {
                textF1.setText(Integer.toString(old.getId()));
                textF2.setText(old.getNume());
                textF3.setText(old.getAdresa());
            }
        }

        localroot.setOnKeyReleased(event -> {
        if(event.getCode()== KeyCode.ESCAPE)
            localStage.close();
        else if(event.getCode()==KeyCode.ENTER)
            btn_confirm.getOnMouseClicked().handle(null);
    });

        localroot.getChildren().add(mainLayout);
        localStage.show();
    }
}
