package mx.com.walmart.fyd.vo;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlCommandLink;
/**
 * @author Israel
 */
public class MenuVo {
	
	private Integer id;
	private String label;
	private String url;
	private String icon;
	private int order;
	private List<MenuVo> submenus;
	private List<AccionVo> acciones;
	private Boolean selected;
	private HtmlCommandLink link;
	
	public MenuVo() {
		super();
	}
	
	public MenuVo(Integer id, String label, List<MenuVo> submenus) {
		this.id = id;
		this.label = label;
		this.submenus = submenus;
	}

	public MenuVo(Integer id, String label, ArrayList<MenuVo> submenus,
			ArrayList<AccionVo> acciones) {
		this.id = id;
		this.label = label;
		this.submenus = submenus;
		this.acciones = acciones;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<MenuVo> getSubmenus() {
		return submenus;
	}
	public void setSubmenus(List<MenuVo> submenus) {
		this.submenus = submenus;
	}
	public List<AccionVo> getAcciones() {
		return acciones;
	}
	public void setAcciones(List<AccionVo> acciones) {
		this.acciones = acciones;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public HtmlCommandLink getLink() {
		return link;
	}

	public void setLink(HtmlCommandLink link) {
		this.link = link;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * 
	 * @author Israel
	 * @return
	 */
	public int getColumns() {
		if (acciones == null || acciones.isEmpty()) {
			return 1;
		}
		if (acciones.size() < 6) {
			return acciones.size();
		}
		return 6;
	}

}
