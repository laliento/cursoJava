package com.lalo.base.dao;

import com.lalo.base.model.LogginAttempts;
import com.lalo.base.model.Usuario;
import com.lalo.config.springHibernate.HibernateDao;

public interface LogginAttemptsDao extends HibernateDao<LogginAttempts, Integer>{
	
	public Integer findNumeroIntentosByUserAndMinutos(Usuario usuario,Integer minutos);
}
