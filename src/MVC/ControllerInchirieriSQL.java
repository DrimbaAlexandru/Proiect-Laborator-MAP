package MVC;

import Domain.inchiriere;
import MemoryRepositories.CachedAbstractRepository;
import SQL_Repositories.*;
import Validator.*;

import java.io.IOException;
import java.sql.Connection;

/**
 * Created by Alex on 04.01.2017.
 */
public class ControllerInchirieriSQL extends CachedAbstractRepository<inchiriere,Integer> implements I_SQLRepository
{
    public ControllerInchirieriSQL(ValidatorInchiriere val,Connection _con) throws IOException,ExceptieValidareInRepository
    {
        super(new inchiriereSQLRepository(val,_con));
    }
}