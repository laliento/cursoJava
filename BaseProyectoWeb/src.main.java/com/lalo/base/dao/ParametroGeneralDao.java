package com.lalo.base.dao;

import com.lalo.base.model.ParametroGeneral;
import com.lalo.config.springHibernate.HibernateDao;

public interface ParametroGeneralDao extends HibernateDao<ParametroGeneral, String>{
	
	public ParametroGeneral findByClave(String clave);
	public Integer findByClaveReturnInteger(String clave);
	public boolean findByClaveReturnBoolean(String clave);
}
