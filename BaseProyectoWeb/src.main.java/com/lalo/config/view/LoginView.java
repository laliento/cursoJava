package com.lalo.config.view;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lalo.base.dao.RelPerfilMenuDao;
import com.lalo.base.model.RelPerfilMenu;

@Component
@SessionScoped
@ManagedBean(name="loginView")
public class LoginView extends BackingBean implements ViewMethodDefault{
	private static final long serialVersionUID = 1L;
	@Autowired
	private RelPerfilMenuDao relPerfilMenuDao;
	private List<RelPerfilMenu> lstRelPerfilMenu;
	@Override
	public String irPagina() {
		String pagina =getPageByUser(); 
		if (pagina.contains("login"))
			return null;
		else{
			lstRelPerfilMenu=relPerfilMenuDao.findByUsuario(getSessionUser());
			return pagina;
		}
	}
	@Override
	public void initPage() {}
	@Override
	public void loadElements() {}
	@Override
	public void cleanPage() {}
	public List<RelPerfilMenu> getLstRelPerfilMenu() {
		return lstRelPerfilMenu;
	}
	public void setLstRelPerfilMenu(List<RelPerfilMenu> lstRelPerfilMenu) {
		this.lstRelPerfilMenu = lstRelPerfilMenu;
	}
}
