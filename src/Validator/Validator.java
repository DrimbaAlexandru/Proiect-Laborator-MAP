package Validator;

import Domain.client;
import Domain.film;
import Domain.inchiriere;

/**
 * Created by Alex on 06.11.2016.
 */


public interface Validator<Elem>
{
    void Validate(Elem e) throws ExceptieValidator;
}


