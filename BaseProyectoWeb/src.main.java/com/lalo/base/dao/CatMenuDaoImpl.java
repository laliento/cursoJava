package com.lalo.base.dao;

import org.springframework.stereotype.Repository;

import com.lalo.base.model.CatMenu;
import com.lalo.config.springHibernate.HibernateDaoImpl;

@Repository
public class CatMenuDaoImpl extends HibernateDaoImpl<CatMenu, Integer> implements CatMenuDao{

}
