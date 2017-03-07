package com.lalo.base.dao;

import java.io.Serializable;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lalo.base.model.Usuario;
import com.lalo.config.springHibernate.HibernateDaoImpl;

@Repository
public class UsuarioDaoImpl extends HibernateDaoImpl<Usuario, Integer> implements Serializable,UsuarioDao{
	private static final long serialVersionUID = 1L;

	@Override
	public Usuario findByUserName(String username) {
		return (Usuario) createCriteria().add(Restrictions.eq("username", username)).uniqueResult();
	}


}
