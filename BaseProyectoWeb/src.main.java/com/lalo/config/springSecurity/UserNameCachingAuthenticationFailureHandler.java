package com.lalo.config.springSecurity;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.lalo.base.dao.LogginAttemptsDao;
import com.lalo.base.dao.ParametroGeneralDao;
import com.lalo.base.dao.UsuarioDao;
import com.lalo.base.model.LogginAttempts;
import com.lalo.base.model.Usuario;

@Component
public class UserNameCachingAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	/*
	 * Variabes independientes 
	 */
	private String usernameParameter = "app_username";
	private String passwordParameter = "app_password";
	private boolean postOnly = true;
	/*
	 * Inject
	 */
	@Autowired
	private UsuarioDao usuarioDao;
	@Autowired
	private LogginAttemptsDao logginAttemptsDao;
	@Autowired
	private ParametroGeneralDao parametroGeneralDao; 
	public UserNameCachingAuthenticationFailureHandler(){super("/login.xhtml?error=BadCredentials");}
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
    	if (postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Método de autentificación no soportado: " + request.getMethod());
		}
    	String mensajeRegreso="";
    	if(exception instanceof BadCredentialsException){
    		mensajeRegreso = evitaFuerzaBruta(request);
    	}else if (exception instanceof AccountStatusException){
    		mensajeRegreso = "Su cuenta se encuentra desabilitada.";
    	}else if(exception instanceof UsernameNotFoundException){
    		mensajeRegreso = "Username Not Found.";
    	}
    	AuthenticationException authenticationException = new AuthenticationException(mensajeRegreso) {
			private static final long serialVersionUID = 1L;};
    	super.onAuthenticationFailure(request, response, authenticationException);
    }
    private String evitaFuerzaBruta(HttpServletRequest request) {
    	String mensajeRegreso="";
    	if(obtainUsername(request) != null && StringUtils.isNotEmpty(obtainUsername(request))){
    		Integer minutosCuentaBloqueada = parametroGeneralDao.findByClaveReturnInteger("minutosCuentaBloqueada");
    		Integer intentosPermitidosLoggin = parametroGeneralDao.findByClaveReturnInteger("intentosPermitidosLoggin");
			Usuario usuario = usuarioDao.findByUserName(obtainUsername(request));
			if(usuario != null){
				Integer numeroDeIntentos = logginAttemptsDao.findNumeroIntentosByUserAndMinutos(usuario,minutosCuentaBloqueada); 
				if(superaNumeroIntentos(numeroDeIntentos,intentosPermitidosLoggin)){
					mensajeRegreso = "Cuenta bloquedada temporalmente por "+minutosCuentaBloqueada+" minutos.";
				}
				else{
					numeroDeIntentos++;
					mensajeRegreso = "Intento fallido "+numeroDeIntentos+" de "+intentosPermitidosLoggin+" intentos permitidos.";
					guardaNuevoIntentoFallido(usuario);
				}
			}else
				mensajeRegreso ="Credenciales invalidas";
		}else{
			mensajeRegreso ="Campos vacios!";
		}
		return mensajeRegreso;
	}
	private void guardaNuevoIntentoFallido(Usuario usuario) {
		logginAttemptsDao.saveOrUpdate(new LogginAttempts(usuario, new Date()));
		
	}
	private boolean superaNumeroIntentos(Integer numeroDeIntentos,Integer intentosPermitidosLoggin) {
		return numeroDeIntentos>=intentosPermitidosLoggin;
	}
	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(passwordParameter);
	}
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(usernameParameter);
	}
}
