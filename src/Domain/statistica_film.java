package Domain;

/**
 * Created by Alex on 05.01.2017.
 */
public class statistica_film
{
    int id_film;
    String titlu;
    int inch;

    public statistica_film(int _id,String _titlu) {id_film=_id; titlu=_titlu; inch=0;}
    public int getInch() { return inch; }
    public int getId_film() { return id_film; }
    public String getTitlu() { return titlu; }
    public void incrementInch() { inch++; }
}