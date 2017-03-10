package com.lalo.config.springHibernate;

import java.util.Properties;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"com.lalo.base.view","com.lalo.base.bi","com.lalo.base.dao","com.lalo.config.view","com.lalo.config.springSecurity"})// por default toma el paquete donde está ubicada ésta clase
@EnableTransactionManagement
public class ConfigSpring {

	@Bean(name = "jpaVendorAdapter")
	public HibernateJpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter
				.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
		return hibernateJpaVendorAdapter;
	}

	@Bean(name = "DataSource")
	public DriverManagerDataSource DataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		driverManagerDataSource
				.setUrl("jdbc:mysql://107.180.44.147:3306/WEBPROJECTBASE");
		driverManagerDataSource.setUsername("WEBJAVAUSERDB");
		driverManagerDataSource.setPassword("hs*5?#=WT3iCAV9l*yOqT]9%");
		return driverManagerDataSource;
	}

	@Bean(name = "defaulLobHandler")
	public DefaultLobHandler defaulLobHandler() {
		return new DefaultLobHandler();
	}

	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(DataSource());
		//para la persistencia de hibernet no de spring com.lalo.base.model Objetos anotados con Hibernate/JPA
		sessionFactory.setPackagesToScan(new String[] { "com.lalo.base.model"});
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}
	
	@Bean(name="transactionManager")
	public HibernateTransactionManager transactionManager(){
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory( sessionFactory().getObject());
		return hibernateTransactionManager;
	}
	
	//bean que ayuda al manejo de excepciones para la anotación @Repository Spring pag. 341
	@Bean
	public BeanPostProcessor persistenceTraslation(){
		return new PersistenceExceptionTranslationPostProcessor();
	}

	// properties de Hibernate
	@SuppressWarnings("serial")
	Properties hibernateProperties() {
		return new Properties() {
			{
				setProperty("hibernate.dialect",
						"org.hibernate.dialect.MySQLDialect");
				setProperty("hibernate.show_sql", "false");
				setProperty("hibernate.hbm2ddl.auto", "validate");//update(pasa los cambios de hibernate a la BD),validate(sólo valida)
				setProperty("hibernate.generate_statistics", "false");
				setProperty("hibernate.cfg.formatSQL", "false");
				setProperty("hibernate.cfg.useSqlComments", "false");
				setProperty("hibernate.cfg.batch.fetch.size", "16");
				setProperty("hibernate.connection.pool_size", "1");
				setProperty("current_session_context_class", "thread");
				setProperty("hibernate.enable_lazy_load_no_trans", "true");
			}
		};
	}
}