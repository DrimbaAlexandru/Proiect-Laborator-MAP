package MVC;

import Domain.client;
import MemoryRepositories.CachedAbstractRepository;
import SQL_Repositories.*;
import Validator.*;

import java.io.IOException;
import java.sql.Connection;

/**
 * Created by Alex on 04.01.2017.
 */
public class ControllerClientSQL extends CachedAbstractRepository<client,Integer> implements I_SQLRepository
{
    public ControllerClientSQL(ValidatorClient val,Connection con) throws IOException,ExceptieValidareInRepository
    {
        super(new clientSQLRepository(val,con));
    }
}