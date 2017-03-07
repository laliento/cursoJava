package com.lalo.config.springHibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.stat.Statistics;
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
public interface HibernateDao<T, ID extends Serializable> {
	/**
	 * 
	 * @param id
	 * @return
	 */
	public T findById(ID id);
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public T saveOrUpdate(T entity);
	/**
	 * 
	 * @param entity
	 */
	public void delete(T entity);
	/**
	 * 
	 * @param entities
	 */
	public void saveOrUpdateAll(List<T> entities);
	/**
	 * 
	 * @param entities
	 */
	public void deleteAll(List<T> entities);
	/**
	 * 
	 * @param entities
	 */
	
	/**
	 * @author Israel
	 * @return
	 */
	public List<T> findAll();
	
	
	public List<T> findByHQL(String sQuery);
	
	public void flush();
	
	public T refresh(T entity);
	
	public void clear();
	T insert(T entity);
	T update(T entity);
	Date getDateBd();
	
	/**
	 * 
	 * @return
	 */
	public Statistics getHibernateStatistics();

}
