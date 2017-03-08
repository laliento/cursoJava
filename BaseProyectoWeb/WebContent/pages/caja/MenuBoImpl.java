package mx.com.walmart.fyd.bo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mx.com.walmart.config.bo.BaseServiceImpl;
import mx.com.walmart.config.util.Response;
import mx.com.walmart.fyd.model.Accion;
import mx.com.walmart.fyd.model.Menu;
import mx.com.walmart.fyd.model.PermisoAccion;
import mx.com.walmart.fyd.model.PermisoMenu;
import mx.com.walmart.fyd.persistece.AccionDao;
import mx.com.walmart.fyd.persistece.MenuDao;
import mx.com.walmart.fyd.persistece.PermisoAccionDao;
import mx.com.walmart.fyd.persistece.PermisoMenuDao;
import mx.com.walmart.fyd.persistece.UsuarioPerfilDao;
import mx.com.walmart.fyd.vo.AccionVo;
import mx.com.walmart.fyd.vo.MenuVo;

import org.apache.commons.collections.CollectionUtils;

public class MenuBoImpl extends BaseServiceImpl implements MenuBo {
	
	private PermisoMenuDao permisoMenuDao;
	private PermisoAccionDao permisoAccionDao;
	private MenuDao menuDao;
	private AccionDao accionDao;
	private UsuarioPerfilDao usuarioPerfilDao;
	
	public PermisoMenuDao getPermisoMenuDao() {
		return permisoMenuDao;
	}
	public void setPermisoMenuDao(PermisoMenuDao permisoMenuDao) {
		this.permisoMenuDao = permisoMenuDao;
	}
	public PermisoAccionDao getPermisoAccionDao() {
		return permisoAccionDao;
	}
	public void setPermisoAccionDao(PermisoAccionDao permisoAccionDao) {
		this.permisoAccionDao = permisoAccionDao;
	}
	public MenuDao getMenuDao() {
		return menuDao;
	}
	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}
	public AccionDao getAccionDao() {
		return accionDao;
	}
	public void setAccionDao(AccionDao accionDao) {
		this.accionDao = accionDao;
	}
	
	public UsuarioPerfilDao getUsuarioPerfilDao() {
		return usuarioPerfilDao;
	}
	public void setUsuarioPerfilDao(UsuarioPerfilDao usuarioPerfilDao) {
		this.usuarioPerfilDao = usuarioPerfilDao;
	}
	@Override
	public List<Menu> findMenusByProfile(Integer idProfile) {
		
		List<Menu> menus = new ArrayList<Menu>();
		
		for (int i = 1; i < 5; i++) {
			Menu menu = new Menu();
			menu.setNombre("Menu " + i);
			menu.setSubmenus(new ArrayList<Menu>());
			for (int j = 1; j < 5; j++) {
				Menu submenu = new Menu();
				submenu.setNombre("Submenu " + i + "." + j);
				menu.getSubmenus().add(submenu);
			}
			menus.add(menu);
		}
		
		return menus;
	}
	
	/**
	 * @author Israel
	 * @param idPerfil
	 * @return
	 */
	public List<MenuVo> findMenusPermitidosByPerfil(Integer idPerfil) {
		
		
		
//        try {
//        	String cadena;
//            FileReader f = new FileReader("c:\\FyD\\menus.txt");
//            BufferedReader b = new BufferedReader(f);
//            while((cadena = b.readLine())!=null) {
//                
//            	String[] values = cadena.split(Pattern.quote("|"));
//            	
//            	Menu menu = menuDao.findById(Integer.parseInt(values[0]));
//            	
//            	menu.setOrderNum(Integer.parseInt(values[3]));
//            	if (values.length == 5) {
//            		menu.setIcon(values[4]);
//            	}
//            	menuDao.saveOrUpdate(menu);
//            	
//            }
//            b.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		List<MenuVo> listVO = new ArrayList<MenuVo>();
		
		List<PermisoMenu> submenusList = getPermisoMenuDao().findSubmenusByPerfil(idPerfil);
		
		Map<Integer, MenuVo> hashMenusVo = new LinkedHashMap<Integer, MenuVo>();
		
		for (PermisoMenu permisoMenu : submenusList) {
			
			MenuVo menuVo;
			
			if (hashMenusVo.containsKey(permisoMenu.getMenu().getMenuPadre().getId())) {
				
				menuVo = hashMenusVo.get( permisoMenu.getMenu().getMenuPadre().getId() );
				
			} else {
				
				menuVo = new MenuVo();
				menuVo.setLabel(
						permisoMenu.getMenu().getMenuPadre().getEtiqueta() == null ?
						"Label Unknow" : permisoMenu.getMenu().getMenuPadre().getEtiqueta().getEtiqueta()
					);
				menuVo.setOrder(getOrder(permisoMenu.getMenu().getMenuPadre()));
				menuVo.setIcon(permisoMenu.getMenu().getMenuPadre().getIcon());
				menuVo.setSubmenus( new ArrayList<MenuVo>() );
				
			}
			
			MenuVo submenu = new MenuVo();
			submenu.setLabel( permisoMenu.getMenu().getEtiqueta() == null ? "Label Unknow" : permisoMenu.getMenu().getEtiqueta().getEtiqueta());
			submenu.setUrl( permisoMenu.getMenu().getUrl() );
			submenu.setOrder( getOrder(permisoMenu.getMenu()) );
			submenu.setIcon(permisoMenu.getMenu().getIcon());
			menuVo.getSubmenus().add( submenu );
			
			hashMenusVo.put(permisoMenu.getMenu().getMenuPadre().getId(), menuVo);
		}
		
		for (MenuVo menu : hashMenusVo.values()) {
			listVO.add(menu);
		}
		
		sortMenuList(listVO);
		return listVO;
	}
	
	private int getOrder(Menu menu) {
		return menu.getOrderNum() == null ? 9999 : menu.getOrderNum();
	}

	private void sortMenuList(List<MenuVo> listVO) {
		Collections.sort(listVO, new MenuComparator());
		for (MenuVo menuVo : listVO) {
			if (CollectionUtils.isNotEmpty(menuVo.getSubmenus())) {
				sortMenuList(menuVo.getSubmenus());
			}
		}
	}
	
	public class MenuComparator implements Comparator<MenuVo> {

		@Override
		public int compare(MenuVo menu1, MenuVo menu2) {
	        return menu1.getOrder().compareTo(menu2.getOrder());
	    }
		
	}
	
	/**
	 * @author Israel
	 * @param idPerfil
	 * @return
	 */
	public List<MenuVo> findMenusByPerfil(Integer idPerfil) {
		List<Menu> submenusList = getMenuDao().findByMenuPadreNotNull();
		Map<Integer, MenuVo> menusPadresHash = new LinkedHashMap<Integer, MenuVo>();
		
		for (Menu submenu : submenusList) {
			MenuVo menuVo;
			if ( menusPadresHash.containsKey( submenu.getMenuPadre().getId() ) ) {
				menuVo = menusPadresHash.get( submenu.getMenuPadre().getId() );
			} else {
				menuVo = new MenuVo(
							submenu.getMenuPadre().getId(),
							submenu.getMenuPadre().getEtiqueta().getEtiqueta(),
							new ArrayList<MenuVo>()
						);
			}
			menuVo.getSubmenus().add( new MenuVo( 
						submenu.getId(),
						submenu.getEtiqueta().getEtiqueta(),
						null,
						getAccionesVoByMenu( submenu.getId() )
					));
			menusPadresHash.put( menuVo.getId(), menuVo );
		}
		
		List<MenuVo> listaMenus = new ArrayList<MenuVo>();
		
		ArrayDeque<Integer> idMenusPermitidos = convertListToDeque( getPermisoMenuDao().findIdMenuByPerfil( idPerfil ) );
		ArrayDeque<Integer> idAccionesPermitaidas = convertListToDeque(getPermisoAccionDao().findIdAccionByPerfil( idPerfil ));
		
		/*
		 * inicial el seteo de los booleanos habilitados
		 */
		for(MenuVo menuPadre : menusPadresHash.values() ) {
			
			if ( idMenusPermitidos.contains( menuPadre.getId() ) ) {
				
				boolean selectedMenu = true;
				
				for (MenuVo submenu : menuPadre.getSubmenus()) {
					
					if ( idMenusPermitidos.contains( submenu.getId() ) ) {
						
						boolean selectedSubmenu = true;
						
						for (AccionVo accionVo : submenu.getAcciones()) {
							if (idAccionesPermitaidas.contains(accionVo.getId())) {
								accionVo.setSelected(true);
							} else {
								accionVo.setSelected(false);
								selectedSubmenu = false;
							}
						}
						
						submenu.setSelected(selectedSubmenu);

					} else {
						setSekeccionadoAcciones(false, submenu);
						selectedMenu = false;
					}
				}
				
				menuPadre.setSelected(selectedMenu);
				
			} else {
				menuPadre.setSelected(false);
				setSeleccionadoASubmenus(false, menuPadre.getSubmenus());
			}
			listaMenus.add(menuPadre);
		}
		
		return listaMenus;
	}
	private void setSeleccionadoASubmenus(boolean selected, List<MenuVo> submenus) {
		for (MenuVo menuVo : submenus) {
			menuVo.setSelected(selected);
			setSekeccionadoAcciones(selected, menuVo);
		}
	}
	private void setSekeccionadoAcciones(boolean selected, MenuVo menuVo) {
		for (AccionVo accionVo: menuVo.getAcciones()) {
			accionVo.setSelected(selected);
		}
	}
	/**
	 * @author Israel
	 * @param id
	 * @return
	 */
	private ArrayList<AccionVo> getAccionesVoByMenu(Integer idMenu) {
		
		ArrayList<AccionVo> acciones = new ArrayList<AccionVo>();
		
		List<Accion> accionList = getAccionDao().findByMenu(idMenu);
		
		for (Accion accion : accionList) {
			acciones.add( new AccionVo(
					accion.getId(), 
					accion.getEtiqueta().getEtiqueta(),
					accion.getIdMenu()));
		}
		
		return acciones;
	}
	/**
	 * @author Israel
	 * @param findIdMenuByPerfil
	 * @return
	 */
	private ArrayDeque<Integer> convertListToDeque(
			List<Integer> findIdMenuByPerfil) {
		ArrayDeque<Integer> deque = new ArrayDeque<Integer>();
		for (Integer integer : findIdMenuByPerfil) {
			deque.add(integer);
		}
		return deque;
	}
	/**
	 * @see mx.com.walmart.fyd.bo.MenuBo#savePermisos(java.util.List)
	 */
	@Override
	public Response savePermisos(List<MenuVo> listMenu, Integer idPerfil) {
		
		Map<Integer, PermisoAccion> hashAcciones = getHashPermisosAccion(idPerfil);
		Map<Integer, PermisoMenu>   hashMenus    = getHashPermisosMenus(idPerfil);
		
		try {
			for (MenuVo menu : listMenu) {
				
				boolean deleteMenu = true;
				
				/*
				 * Borro lo que no este seleccionado
				 */
				for (MenuVo submenu: menu.getSubmenus()) {
					
					boolean deleteSubmenu = true;
					
					if (submenu.getAcciones().isEmpty() && submenu.getSelected()) {
						
						deleteSubmenu = false;
						deleteMenu = false;
						
					} else {
						
						for (AccionVo accion : submenu.getAcciones()) {
							
							if (accion.getSelected()) {
								//Si la accion esta seleccionada no borró el submenu y el menu
								deleteMenu = false;
								deleteSubmenu = false;
							} else if(hashAcciones.containsKey(accion.getId())) {
								//Si no esta marcado y el objeto existe entonces se elimina.
								getPermisoAccionDao().delete( hashAcciones.get(accion.getId()) );
							}
							
						}
					}
					
					
					if (deleteSubmenu && hashMenus.containsKey( submenu.getId() )) {
						getPermisoMenuDao().delete( hashMenus.get( submenu.getId() ) );
					}
					
				}
				
				if (deleteMenu && hashMenus.containsKey( menu.getId() )) {
					getPermisoMenuDao().delete( hashMenus.get( menu.getId() ) );
				}
				
				/**
				 * Empieza Proceso de Guardado
				 */
				PermisoMenu permisoMenu = hashMenus.get( menu.getId() );
				
				
				/*
				 * Se guardan los que esten marcados
				 */
				for (MenuVo submenu : menu.getSubmenus()) {
					
					PermisoMenu permisoSubmenu = hashMenus.get( submenu.getId() );
					
					if (submenu.getSelected() && submenu.getAcciones().isEmpty()) {
						
						insertPermiso(idPerfil, menu, permisoMenu,
								submenu, permisoSubmenu, null);
						
					} else {
						
						for (AccionVo accion : submenu.getAcciones() ) {
							
							
							if (accion.getSelected()) {
								//Guardo la accion.
								
								//Si la accion no estaba guardada entonces se guarda
								if (!hashAcciones.containsKey( accion.getId() )) {
									
									insertPermiso(idPerfil, menu, permisoMenu,
											submenu, permisoSubmenu, accion);
									
								}
								
							}
							
						}
						
					}
					
					
					
					
				}
				
			}
			
			setResponse(true, null);
			
		} catch (Exception e) {
			setResponse(e.getMessage(), null, Boolean.FALSE);
		}
		
		return getResponse();
	}
	private void insertPermiso(Integer idPerfil, MenuVo menu,
			PermisoMenu permisoMenu, MenuVo submenu,
			PermisoMenu permisoSubmenu, AccionVo accion) {
		if (permisoSubmenu == null) {
			
			if (permisoMenu == null) {
				permisoMenu = crearPermisoMenu( menu.getId(), idPerfil );
			}
			
			permisoSubmenu = crearPermisoMenu(submenu.getId(), idPerfil);
			
		}
		
		if (accion != null) {
			
			PermisoAccion permisoAccion = new PermisoAccion();
			permisoAccion.setAccion( new Accion( accion.getId() ));
			permisoAccion.setPermisoMenu( permisoSubmenu );
			
			getPermisoAccionDao().saveOrUpdate(permisoAccion);
			
		}
		
		
	}
	/**
	 * @author Israel
	 * @param idMenu
	 * @param idPerfil
	 * @return
	 */
	private PermisoMenu crearPermisoMenu(Integer idMenu, Integer idPerfil) {
		PermisoMenu pm = new PermisoMenu();
		pm.setIdPerfil(idPerfil);
		pm.setMenu( new Menu());
		pm.getMenu().setId(idMenu);
		return getPermisoMenuDao().saveOrUpdate(pm);
	}
	private Map<Integer, PermisoAccion> getHashPermisosAccion(Integer idPerfil) {
		List<PermisoAccion> list = getPermisoAccionDao().findByPerfil(idPerfil);
		
		Map<Integer, PermisoAccion> map = new LinkedHashMap<Integer, PermisoAccion>();
		
		for (PermisoAccion permisoAccion : list) {
			map.put(permisoAccion.getAccion().getId(), permisoAccion);
		}
		
		return map;
	}
	
	private Map<Integer, PermisoMenu> getHashPermisosMenus(Integer idPerfil) {
		List<PermisoMenu> list = getPermisoMenuDao().findByPerfil(idPerfil);
		
		Map<Integer, PermisoMenu> map = new LinkedHashMap<Integer, PermisoMenu>();
		
		for (PermisoMenu permisoMenu : list) {
			map.put(permisoMenu.getMenu().getId(), permisoMenu);
		}
		
		return map;
	}
	/**
	 * @see mx.com.walmart.fyd.bo.MenuBo#findPermisosByPerfil(java.lang.Integer)
	 */
	@Override
	public Map<String, Boolean> findPermisosByPerfil(Integer idPerfil) {
		Map<String, Boolean> map = new LinkedHashMap<String, Boolean>();
		List<Integer> idsAcciones = getAccionDao().findIdAccionByPerfil(idPerfil);
		Deque<Integer> idsAcconesDeque = new ArrayDeque<Integer>();
		for (Integer idAccion : idsAcciones) {
			idsAcconesDeque.add( idAccion );
		}
		List<Accion> accionesList = getAccionDao().findAll();
		for (Accion accion : accionesList) {
			map.put(accion.getIdHtml(), idsAcconesDeque.contains(accion.getId()));
		}
		return map;
	}

}
