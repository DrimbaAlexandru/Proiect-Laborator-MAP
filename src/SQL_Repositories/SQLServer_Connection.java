package SQL_Repositories;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Alex on 03.01.2017.
 */
public class SQLServer_Connection
{
    private Connection con;
    public SQLServer_Connection(String _dbname, String _ip, String _port) throws IOException
    {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String connectionUrl = "jdbc:sqlserver://"+_ip+":"+_port+";" +
                    "databaseName="+_dbname+";integratedSecurity=true;";

            System.out.println(connectionUrl);
            con = DriverManager.getConnection(connectionUrl);
        }
        catch (ClassNotFoundException|SQLException e)
        {
            throw new IOException("Eroare la conectarea la baza de date. Asigura-te ca serviciul SQL Server e pornit.");
        }
    }

    public Connection getConnection(){return con;}
}
