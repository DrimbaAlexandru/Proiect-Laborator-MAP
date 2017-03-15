package Domain;

import java.io.Serializable;

/**
 * Created by Alex on 04.01.2017.
 */
public class film extends HasID<Integer> implements Serializable
{
    private static int last_cod=1;
    private String titlu;
    private int an_aparitie;
    private String regizor;

    public film(String nume,int an, int cod, String regizor)
    {
        super(cod);
        titlu=nume;
        an_aparitie=an;
        if(last_cod<cod)
            last_cod=cod;
        this.regizor=regizor;
    }

    public film(String nume,int an, String regizor)
    {
        super(last_cod+1);
        last_cod++;
        titlu=nume;
        an_aparitie=an;
        this.regizor=regizor;
    }

    public void setAn_aparitie(int an_aparitie) {
        this.an_aparitie = an_aparitie;
    }
    public void setRegizor(String regizor) {
        this.regizor = regizor;
    }
    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getRegizor() {
        return regizor;
    }
    public int getAn_aparitie() {
        return an_aparitie;
    }
    public String getTitlu() {
        return titlu;
    }

    public boolean equals(Object obj) {
        try
        {
            film other=(film) obj;
            return (getId()==other.getId());
        }
        catch (ClassCastException e)
        {
            return false;
        }
    }
}