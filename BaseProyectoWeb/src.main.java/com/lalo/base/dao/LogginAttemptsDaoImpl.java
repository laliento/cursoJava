package com.lalo.base.dao;

import java.util.Calendar;
import java.util.Date;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lalo.base.model.LogginAttempts;
import com.lalo.base.model.Usuario;
import com.lalo.config.springHibernate.HibernateDaoImpl;
@Repository
public class LogginAttemptsDaoImpl extends HibernateDaoImpl<LogginAttempts, Integer> implements LogginAttemptsDao{

	@Override
	public Integer findNumeroIntentosByUserAndMinutos(Usuario usuario, Integer minutos) {
		Date tiempoAtras =  calculaTiempoatras(minutos);
		return  Math.toIntExact((Long) (createCriteria().add(Restrictions.eq("usuario", usuario)).
				add(Restrictions.between("tiempo", tiempoAtras, new Date())).
				setProjection(Projections.rowCount()).uniqueResult()));
	}

	private Date calculaTiempoatras(Integer minutos) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -minutos);
		return calendar.getTime();
	}

}
