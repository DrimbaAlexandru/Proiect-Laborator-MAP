package MVC;

import GUI.main_window;

/**
 * Created by Alex on 04.12.2016.
 */
public class Controller
{
    private FCIController ctr;
    private main_window view;

    public Controller(main_window GUI, FCIController ctrlr)
    {
        view=GUI;
        ctr=ctrlr;
        ctr.addObserver(view);
        ctr.myNotify("film");
        ctr.myNotify("client");
        ctr.myNotify("inchiriere");
    }

}
