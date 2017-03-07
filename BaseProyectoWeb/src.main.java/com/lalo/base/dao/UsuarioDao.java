package com.lalo.base.dao;

import com.lalo.base.model.Usuario;
import com.lalo.config.springHibernate.HibernateDao;


public interface UsuarioDao extends  HibernateDao<Usuario, Integer>{

	Usuario findByUserName(String username);

}
