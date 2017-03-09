package com.lalo.config.springHibernate;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class ConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource  datasource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.jdbcAuthentication().dataSource(datasource)
		.usersByUsernameQuery(
				"select USERNAME,PASSWORD,ID_ESTADO_USUARIO FROM ENT_USUARIO WHERE USERNAME=?")
		.authoritiesByUsernameQuery(
				"SELECT eu.username,cp.descripcion from ENT_USUARIO eu "
				+ "inner join CAT_PERFIL cp on eu.id_perfil=cp.id_perfil "
				+ "where eu.username=?")
		.rolePrefix("ROLE_")
//				.passwordEncoder(new StandardPasswordEncoder("lalum@"))
				;
	}
	@Override
	  public void configure(WebSecurity web) throws Exception {
	    web
	      .ignoring()
	         .antMatchers("/resources/**","/"); // #No haga caso de cualquier solicitud que comienza con
	  }
	@Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf()//todo va con https excepto
	      .disable()
		  .authorizeRequests()
	      .antMatchers("/css/**","/vendor/**","/app/**", "/fonts/**", "/image/**", "/js/**").permitAll()
      .and().authorizeRequests()
        .antMatchers("/", "/home","/resources/**").permitAll() 
        .antMatchers("/pages/admin/**").hasRole("ADMIN") //hasRole a�ade el prefijo _ROLE
        .antMatchers("/pages/caja/**").access("hasRole('ADMIN') OR hasRole('CAJA')")
        .antMatchers("/pages/mesero/**").access("hasRole('ADMIN') OR hasRole('MESERO')")
        .antMatchers("/pages/lalo/**").access("isAuthenticated() and principal.username=='admonlalo'")
        .antMatchers("/pages/**").authenticated()
        .and().formLogin()  
	        .loginPage("/login.xhtml").failureUrl("/login.xhtml?error")
	        .loginProcessingUrl("/appLogin")
	        .usernameParameter("app_username")//nombre en formulario
            .passwordParameter("app_password")//nombre en formulario
            .defaultSuccessUrl("/login.xhtml")
	        .permitAll()
	     .and().logout()
			.logoutUrl("/appLogout") 
			.logoutSuccessUrl("/login.xhtml")
			.deleteCookies("JSESSIONID","remember_me_end")
	     .and().httpBasic()// forma de autententicar a un usuario en una apicaci�n directamente / se puede hacer autom�ticamente con REST
//	     	.realmName("") // nombre del reino ...
	     .and().rememberMe() // usa tokenRepository � key
	     	.rememberMeParameter("remember_me")//nombre en formulario
	     	.rememberMeCookieName("remember_me_end")
	     	.tokenRepository(persistentTokenRepository())//va a la tabla persistent_logins http://websystique.com/spring-security/spring-security-4-remember-me-example-with-hibernate/
	     	.tokenValiditySeconds(60)//numero de segundos que est� activa la session, por defaul son 2 semanas
//	     	.key("lalum@")//llave con la que se codifica la cookie, por defaul SpringSecured
	     .and().requiresChannel()
	     	.antMatchers("/").requiresSecure()//requiere https en esta parte
	     .and().exceptionHandling().accessDeniedPage("/accesDenied.xhtml")
//	     .and().authorizeRequests().anyRequest().permitAll()
	     ;
    }
	@Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        tokenRepositoryImpl.setDataSource(datasource);
        return tokenRepositoryImpl;
    }
	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	
}
