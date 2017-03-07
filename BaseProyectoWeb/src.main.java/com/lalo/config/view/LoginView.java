package com.lalo.config.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name="loginView")
public class LoginView extends BackingBean implements ViewMethodDefault{
	private static final long serialVersionUID = 1L;
	@Override
	public String irPagina() {
		String pagina =getPageByUser(); 
		if (pagina.contains("login"))
			return null;
		else
		return pagina;
	}
	@Override
	public void initPage() {}
	@Override
	public void loadElements() {}
	@Override
	public void cleanPage() {}

}
