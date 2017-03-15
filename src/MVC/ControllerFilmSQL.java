package MVC;

import Domain.film;
import MemoryRepositories.CachedAbstractRepository;
import SQL_Repositories.*;
import Validator.*;

import java.io.IOException;
import java.sql.Connection;

/**
 * Created by Alex on 04.01.2017.
 */
public class ControllerFilmSQL extends CachedAbstractRepository<film,Integer> implements I_SQLRepository
{
    public ControllerFilmSQL(ValidatorFilm val,Connection con) throws IOException,ExceptieValidareInRepository
    {
        super(new filmSQLRepository(val,con));
    }
}