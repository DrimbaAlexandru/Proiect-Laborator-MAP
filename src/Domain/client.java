package Domain;

import java.io.Serializable;

/**
 * Created by Alex on 04.01.2017.
 */
public class client extends HasID<Integer> implements Serializable
{
    private static int last_cod=1;
    private String nume;
    private String adresa;

    public client(String nume, int cod_membru, String adresa)
    {
        super(cod_membru);
        this.nume=nume;
        this.adresa=adresa;
        if(last_cod<cod_membru)
            last_cod=cod_membru;
    }

    public client(String nume, String adresa)
    {
        super(last_cod+1);
        last_cod++;
        this.nume=nume;
        this.adresa=adresa;
    }

    public String getAdresa() {
        return adresa;
    }
    public String getNume() {
        return nume;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public boolean equals(Object obj) {
        try
        {
            client other=(client) obj;
            return (getId()==other.getId());
        }
        catch (ClassCastException e)
        {
            return false;
        }
    }
}