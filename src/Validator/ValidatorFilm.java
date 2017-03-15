package Validator;

import Domain.film;

/**
 * Created by Alex on 04.01.2017.
 */
public class ValidatorFilm implements Validator<film>
{
    public void Validate(film e) throws ExceptieValidator
    {
        if(e==null)
            throw new ExceptieValidator("Film nul!");
        if(e.getTitlu().isEmpty() || e.getAn_aparitie()<1900 || e.getRegizor().isEmpty()
                || e.getRegizor().contains("|") || e.getTitlu().contains("|"))
            throw new ExceptieValidator("Datele filmului contin caractere invalide sau anul nu apartine unui interval acceptabil!");
    }
}