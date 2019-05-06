package org.cyk.system.file.server.business.impl.integration;

@javax.annotation.sql.DataSourceDefinition(
		name="java:/file/server/dataSource",
		className="org.h2.jdbcx.JdbcDataSource",
		url="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
		user="sa",
		password="sa"
)

/*
@javax.annotation.sql.DataSourceDefinition(
		name="java:/file/server/dataSource",
		className="com.mysql.jdbc.jdbc2.optional.MysqlXADataSource",
		url="jdbc:mysql://localhost:3306/kwordz_file",
		user="root",
		password="root"
)
*/
public class DataSourceDefinition {}
