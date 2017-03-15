package Domain;

import java.io.Serializable;

/**
 * Created by Alex on 13.10.2016.
 */


public class inchiriere extends HasID<Integer> implements Serializable
{
    private int cod_f;
    private int cod_c;
    private static int last_id=1;

    @Override
    public void setIdUsed(Integer cod)
    {
        if(cod>=last_id)
            last_id=cod+1;
    }

    public inchiriere(film f,client c)
    {
        super(last_id);
        last_id++;
        this.cod_f=f.getId();
        this.cod_c=c.getId();
    }

    public inchiriere(int cod_film, int cod_client)
    {
        super(last_id);
        last_id++;
        this.cod_f=cod_film;
        this.cod_c=cod_client;
    }

    public inchiriere(int cod,film f,client c)
    {
        super(cod);
        if(cod>=last_id)
            last_id=cod+1;
        cod_f=f.getId();
        cod_c=c.getId();
    }

    public inchiriere(int cod,int cod_film,int cod_client)
    {
        super(cod);
        if(cod>=last_id)
            last_id=cod+1;
        cod_f=cod_film;
        cod_c=cod_client;
    }

    public int getCod_c() {
        return cod_c;
    }
    public int getCod_f() {
        return cod_f;
    }
}


