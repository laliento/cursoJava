package mx.com.walmart.fyd.view;

import java.util.List;

import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;

import mx.com.walmart.config.view.BackingBean;
import mx.com.walmart.fyd.vo.MenuVo;

import org.primefaces.component.toolbar.Toolbar;

/**
 * @author J. Israel Hernández G
 *
 */
@ManagedBean(name="menuView")
@SessionScoped
public class MenuView extends BackingBean {

	/**
	 * @author J. Israel Hernández G
	 */
	private static final long serialVersionUID = 247664422887178250L;
	
	private Toolbar toolbar;
	
	private HtmlCommandLink link;
	
	private List<MenuVo> leftMenuList;
	
	public Toolbar getToolbar() {
		
		if (toolbar == null) {
			 buildMenu();
		}
		
		return toolbar;
	}
	

	public void buildMenu() {
		List<MenuVo> list = getServiceFactory().getMenuBo().findMenusPermitidosByPerfil(getSessionUser().getPerfil().getId());
		for (MenuVo menu : list) {
			menu.setLabel( getProperty(menu.getLabel()) );
			menu.setIcon((menu.getIcon() == null ? "icon-stackoverflow" : menu.getIcon()) + " yellow i");
			for (MenuVo submenu : menu.getSubmenus()) {
				if (submenu.getUrl() != null) {
					HtmlCommandLink link = new HtmlCommandLink();
					link.setValue( getProperty(submenu.getLabel() ) );
					link.setStyleClass("marginLevel-1 " + (submenu.getIcon() == null ? "icon-check-1" : submenu.getIcon()) + " yellow i");
					link.setStyle("text-decoration: none;");
					link.setActionExpression(createActionExpression(submenu.getUrl()));
					submenu.setLink(link);
				}
			}
		}
		leftMenuList = list;
		
		for (MenuVo menuVo : list) {
			for (MenuVo submenu : menuVo.getSubmenus()) {
				if (submenu == null) {
					System.out.println("null");
				}
			}
		}
	}
	
	public void resetViews() {
		System.out.println("Reset");
	}

//	private ToolbarGroup getMonitorToolBar(int idNumber) {
//		ToolbarGroup monitorToolbar = new ToolbarGroup();
//		monitorToolbar.getChildren().add(new Separator());
//		monitorToolbar.setAlign("right");
//		MenuButton menuButton = new MenuButton();
//		menuButton.setValue("Rendimiento");
//		
//		MenuItem item = new MenuItem();
//		item.setValue("Monitor");
//		item.setId("idsubmenu" + idNumber++);
//		item.setActionExpression(createActionExpression("monitorView.irPagina"));
//		item.setStyle("width:100px!important; font-size:12px!important;");
//		menuButton.getChildren().add(item);
//		
//		monitorToolbar.getChildren().add(menuButton);
//		monitorToolbar.getChildren().add(new Separator());
//		return monitorToolbar;
//	}

	/**
	 * 
	 * @author: Ing. Juan Israel Hernández García
	 * @param url
	 * @return
	 */
	private MethodExpression createActionExpression(String url) {
		Application app = FacesContext.getCurrentInstance().getApplication();
		MethodExpression action = app.getExpressionFactory().createMethodExpression(
				FacesContext.getCurrentInstance().getELContext(), 
				"#{"+ url +"}", String.class, new Class[0]);
		return action;
	}

	public void setToolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
	}

	/**
	 * @return
	 */
	public String irPermisosView() {
		return "irPermisosView";
	}


	public HtmlCommandLink getLink() {
		link = new HtmlCommandLink();
		link.setActionExpression(createActionExpression("monitorView.irPagina"));
		return link;
	}


	public void setLink(HtmlCommandLink link) {
		this.link = link;
	}


	public List<MenuVo> getLeftMenuList() {
		if (leftMenuList == null) {
			buildMenu();
		}
		return leftMenuList;
	}


	public void setLeftMenuList(List<MenuVo> leftMenuList) {
		this.leftMenuList = leftMenuList;
	}
	
	

}
