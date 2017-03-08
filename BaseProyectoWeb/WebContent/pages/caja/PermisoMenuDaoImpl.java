package mx.com.walmart.fyd.persistece;

import java.util.ArrayList;
import java.util.List;

import mx.com.walmart.config.persistence.HibernateDaoImpl;
import mx.com.walmart.fyd.model.Menu;
import mx.com.walmart.fyd.model.PermisoMenu;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class PermisoMenuDaoImpl extends HibernateDaoImpl<PermisoMenu, Integer>
		implements PermisoMenuDao {

	@Override
	public List<PermisoMenu> findByIdPerfilAndMenuPadreNotNull(Integer idPerfil) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> findIdMenuByPerfil(Integer idPerfil) {
		return super.findBySQLQuery("SELECT id_menu FROM REL_PERMISO_MENU WHERE id_perfil =  " + idPerfil);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PermisoMenu> findByPerfil(Integer idPerfil) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("idPerfil", idPerfil));
		return criteria.list();
	}

	@Override
	public PermisoMenu findPermisoMenuBySubmenu(Integer idSubmenu) {
		StringBuffer query = new StringBuffer();
		query.append( " SELECT pm.id, pm.id_menu, pm.id_perfil FROM REL_PERMISO_MENU pm " );
		query.append( " WHERE pm.id_menu = ( ");
		query.append( " 	SELECT sm.id_menu_padre FROM cat_menu sm ");
		query.append( " 	WHERE sm.id = 2 )");
		
		@SuppressWarnings("unchecked")
		List<Object[]> list = findBySQLQuery(query.toString());
		
		if (!list.isEmpty()) {
			PermisoMenu pm = new PermisoMenu();
			pm.setId((Integer)list.get(0)[0]);
			pm.setMenu(new Menu ());
			pm.getMenu().setId((Integer)list.get(0)[1]);
			pm.setIdPerfil((Integer)list.get(0)[2]);
			
			return pm;
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see mx.com.walmart.fyd.persistece.PermisoMenuDao#findSubmenusByPerfil(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PermisoMenu> findSubmenusByPerfil(Integer idPerfil) {
		
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("idPerfil", idPerfil));
		criteria = criteria.createCriteria("menu");
		criteria.add(Restrictions.isNotNull("menuPadre"));
		criteria.addOrder(Order.asc("orderNum"));
		
		List<PermisoMenu> listaEntrega=new ArrayList<PermisoMenu>();
		List<PermisoMenu> listaPermisoMenus = criteria.list();
		List<Integer>lisIntegers=new ArrayList<Integer>();
		int cont=0;
		for(PermisoMenu perm:listaPermisoMenus){
			if(cont>0){
				if(!lisIntegers.contains(perm.getMenu().getId())){
					lisIntegers.add(perm.getMenu().getId());
					listaEntrega.add(perm);	
				}
				cont++;
			}else{
				lisIntegers.add(perm.getMenu().getId());
				listaEntrega.add(perm);
				cont++;
			}
		}
		return listaEntrega;
//		StringBuffer hql = new StringBuffer();
//		
//		hql.append( " FROM PermisoMenu pm " );
//		hql.append( " 	 left join fetch pm.menu submenu" );
//		hql.append( "    left join fetch submenu.etiqueta etiSubmenu  ");
//		hql.append( " 	 left join fetch submenu.menuPadre menu " );
//		hql.append( "    left join fetch menu.etiqueta etiMenu ");
//		
//		hql.append( " WHERE pm.idPerfil in (" );
//		hql.append(" select distinct pm.id_menu ");
//		hql.append(" from PermisoMenu pm ");
//		hql.append(" left join fetch pm.menu  m ");
//		hql.append( " WHERE m.menuPadre is not null " );
//		hql.append("  ");
//		hql.append("  ");
//		hql.append("  ");
//		
//		
//		
//		
//		
//		and m.id_menu_padre in(select distinct pme.id_menu
//		                        from permiso_menu pme
//		                        where pme.id_perfil in(4,5))
//		and pm.id_perfil in(4,5)
//		
//		int cont =1;
//		for(Integer id: listaIds){
//			if(cont!= listaIds.size()){
//				hql.append(id+",");	
//			}else{
//				hql.append(id);
//			}
//			cont++;
//		}
//		hql.append(")");
//		return getHibernateTemplate().find(hql.toString());
	}
	
	

	
}
