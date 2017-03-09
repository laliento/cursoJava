package com.lalo.base.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lalo.base.model.Perfil;
import com.lalo.base.model.RelPerfilMenu;
import com.lalo.base.model.Usuario;
import com.lalo.config.springHibernate.HibernateDaoImpl;


@SuppressWarnings("unchecked")
@Repository
public class RelPerfilMenuDaoImpl extends HibernateDaoImpl<RelPerfilMenu, Integer> implements RelPerfilMenuDao{

	@Override
	public List<RelPerfilMenu> findByPerfil(Perfil perfil) {
		return currentSession().createCriteria(RelPerfilMenu.class, "rpm").
				add(Restrictions.eq("rpm.perfil", perfil)).add(Restrictions.eq("rpm.catMenu.menuPadre", null)).list();
	}

	@Override
	public List<RelPerfilMenu> findByUsuario(Usuario usuario) {
		return findByPerfil(usuario.getPerfil());
	}

}
