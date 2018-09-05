package com.chinasofti.dao.impl;

import org.springframework.stereotype.Repository;
import com.chinasofti.dao.MenuDaoI;
import com.chinasofti.dao.base.impl.BaseDaoImpl;
import com.chinasofti.model.Tmenu;

@Repository("menuDao")
public class MenuDaoImpl extends BaseDaoImpl<Tmenu> implements MenuDaoI{

}
