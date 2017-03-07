package com.lalo.base.view.menuPadre1;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lalo.base.dao.UsuarioDao;
import com.lalo.base.model.Usuario;
import com.lalo.config.view.BackingBean;
import com.lalo.config.view.ViewMethodDefault;


@Component
//@ManagedBean(name="pruebaView")
@SessionScoped
public class PruebaView extends BackingBean implements ViewMethodDefault{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private UsuarioDao usuarioDao;
	private List<Usuario> lstAdmon;
	@Override
	@PostConstruct
	public void initPage() {
		System.out.println("Inicio de log");
		log.info("Inicio de log");
		lstAdmon=usuarioDao.findAll();
	}
	@Override
	public String irPagina() {
		refreshComponent("tableBenefits");
		getSessionUser();
		return "otro";
	}
	@Override
	public void loadElements() {
	}
	@Override
	public void cleanPage() {
	}
	public List<Usuario> getLstAdmon() {
		return lstAdmon;
	}
	public void setLstAdmon(List<Usuario> lstAdmon) {
		this.lstAdmon = lstAdmon;
	}
	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}
	
}
