package Validator;

import Domain.inchiriere;

/**
 * Created by Alex on 04.01.2017.
 */
public class ValidatorInchiriere implements Validator<inchiriere>
{
    public void Validate(inchiriere i) throws ExceptieValidator
    {
        if(i==null)
            throw new ExceptieValidator("Inchiriere nula!");
    }
}