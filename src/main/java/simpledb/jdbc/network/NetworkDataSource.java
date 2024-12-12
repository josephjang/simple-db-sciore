package simpledb.jdbc.network;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class NetworkDataSource implements DataSource {
    private String host;

    @Override
    public Connection getConnection() throws SQLException {
        Driver driver = new NetworkDriver();
        return driver.connect("jdbc:simpledb://" + host, null);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {}

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {}

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
