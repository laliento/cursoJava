package com.lalo.config.springHibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.stat.Statistics;
import org.hibernate.type.DbTimestampType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
/**
 * 
 * @author Eduardo Cruz Zamorano
 *
 * @param <T>
 * @param <ID>
 */
@Repository
@Transactional
public class HibernateDaoImpl<T, ID extends Serializable> extends HibernateDaoSupport
		implements HibernateDao<T, ID> {

	private Class<T> entity;
	/**
	 * 
	 */
	@Autowired
	public void init(SessionFactory sessionfactory){
	    setSessionFactory(sessionfactory);
	}
	
	@SuppressWarnings("unchecked")
	public HibernateDaoImpl() {
		this.entity = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Override
	public T findById(ID id) {
		return (T) getHibernateTemplate().get(entity, id);
	}

	@Override
	@Transactional
	public T saveOrUpdate(T entity) {
		getHibernateTemplate().saveOrUpdate( entity );
		return entity;
	}

	@Override
	@Transactional
	public void delete(T entity) {
		getHibernateTemplate().delete( entity );
	}

	@SuppressWarnings("unused")
	@Override
	@Transactional
	public void saveOrUpdateAll(List<T> entities) {
		for (T t : entities) {
			getHibernateTemplate().saveOrUpdate(entities);
		}
	}
	
	@Override
	@Transactional
	public void deleteAll(List<T> entities) {
		getHibernateTemplate().deleteAll(entities);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		try{
			Criteria criteria = createCriteria();
			return criteria.list(); 
		}catch(Exception e){
			e.printStackTrace();
			return super.getHibernateTemplate().loadAll(entity);
		}
	}
	/**
	 * 
	 * @return
	 */
	protected Criteria createCriteria() {
		return super.getSessionFactory().getCurrentSession().createCriteria(entity);
	}

	@SuppressWarnings("rawtypes")
	public List findBySQLQuery(String sQuery) {
		try{
			SQLQuery query = super.getSessionFactory().getCurrentSession().createSQLQuery(sQuery);
			return query.list();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findByHQL(String sQuery) {
		try{
			return (List<T>) getHibernateTemplate().find(sQuery);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void flush(){
		getHibernateTemplate().flush();
		
	}
	
	public void clear(){
		getHibernateTemplate().clear();
		
	}
	
	@Override
	@Transactional
	public Date getDateBd(){
		DbTimestampType  t= new DbTimestampType();
		Timestamp timeBd = (Timestamp)t.seed( (SessionImplementor)getSessionFactory() );
		return timeBd;
	}
	
	@Override
	@Transactional
	public T insert(T entity) {
		getHibernateTemplate().save( entity );
		return entity;
	}
	
	@Override
	@Transactional
	public T update(T entity) {
		getHibernateTemplate().update( entity );
		return entity;
	}
	
	public T refresh(T entity) {
		getHibernateTemplate().refresh(entity);
		return entity;
	}
	
	public Statistics getHibernateStatistics(){
		Statistics stats = getHibernateTemplate().getSessionFactory().getStatistics();
		stats.setStatisticsEnabled(true);
		return stats;
	}


}
