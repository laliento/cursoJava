package com.lalo.base.dao;

import org.springframework.stereotype.Repository;

import com.lalo.base.model.CatMenu;
import com.lalo.config.springHibernate.HibernateDao;

@Repository
public interface CatMenuDao extends HibernateDao<CatMenu, Integer>{
	
}
