package Validator;

import Domain.client;

/**
 * Created by Alex on 04.01.2017.
 */
public class ValidatorClient implements Validator<client>
{
    public void Validate(client c ) throws ExceptieValidator
    {
        if(c==null)
            throw new ExceptieValidator("Client nul!");
        if(c.getNume().isEmpty() || c.getAdresa().isEmpty() || c.getAdresa().contains("|") || c.getNume().contains("|"))
            throw new ExceptieValidator("Datele clientului contin caractere invalide!");
    }
}