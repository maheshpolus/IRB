package org.mit.irb.web.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.mit.irb.web.IRBProtocol.pojo.ProtocolGeneralInfo;
import org.springframework.hateoas.Identifiable;

public class ProtocolIdGenerator implements IdentifierGenerator{
	protected static Logger logger = Logger.getLogger(ProtocolIdGenerator.class.getName());

	@Override
	public Serializable generate(SessionImplementor sessionImplementor, Object object) throws HibernateException {
		String prefix ="";
		Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        prefix = String.valueOf(year)+String.valueOf(month);
		Connection connection = sessionImplementor.connection();
		try {
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("select count(1) from MITKC_IRB_PROTOCOL");

			if (rs.next()) {
				int id = rs.getInt(1);
				String generatedId = prefix + "-" + new Integer(id).toString();
				logger.info("Generated Id: " + generatedId);
				return generatedId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}



