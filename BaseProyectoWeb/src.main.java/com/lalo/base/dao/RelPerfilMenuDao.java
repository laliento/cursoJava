package com.lalo.base.dao;

import java.util.List;

import com.lalo.base.model.Perfil;
import com.lalo.base.model.RelPerfilMenu;
import com.lalo.base.model.Usuario;
import com.lalo.config.springHibernate.HibernateDao;

public interface RelPerfilMenuDao extends HibernateDao<RelPerfilMenu, Integer>{
	public List<RelPerfilMenu> findByPerfil(Perfil perfil);
	public List<RelPerfilMenu> findByUsuario(Usuario usuario);
}
