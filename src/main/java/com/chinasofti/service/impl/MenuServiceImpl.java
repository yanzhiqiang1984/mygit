package com.chinasofti.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chinasofti.comparator.MenuComparator;
import com.chinasofti.dao.MenuDaoI;
import com.chinasofti.model.Tmenu;
import com.chinasofti.pageModel.Menu;
import com.chinasofti.pageModel.base.TreeNode;
import com.chinasofti.service.MenuServiceI;

@Service("menuService")
public class MenuServiceImpl implements MenuServiceI {

	private MenuDaoI menuDao;

	public MenuDaoI getMenuDao() {
		return menuDao;
	}

	@Autowired
	public void setMenuDao(MenuDaoI menuDao) {
		this.menuDao = menuDao;
	}

	public List<TreeNode> tree(Menu menu, Boolean b) {
		List<Object> param = new ArrayList<Object>();
		String hql = "from Tmenu t where t.tmenu is null order by t.seq";
		if (menu != null && menu.getId() != null && !menu.getId().trim().equals("")) {
			hql = "from Tmenu t where t.tmenu.id = ? order by t.seq";
			param.add(menu.getId());
		}
		List<Tmenu> l = menuDao.find(hql, param);
		List<TreeNode> tree = new ArrayList<TreeNode>();
		for (Tmenu t : l) {
			tree.add(this.tree(t, b));
		}
		return tree;
	}

	private TreeNode tree(Tmenu t, boolean recursive) {
		TreeNode node = new TreeNode();
		node.setId(t.getId());
		node.setText(t.getName());
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("url", t.getUrl());
		node.setAttributes(attributes);
		if (t.getIconcls() != null) {
			node.setIconCls(t.getIconcls());
		} else {
			node.setIconCls("");
		}
		if (t.getTmenus() != null && t.getTmenus().size() > 0) {
			node.setState("closed");
			if (recursive) {// 递归查询子节点
				List<Tmenu> l = new ArrayList<Tmenu>(t.getTmenus());
				Collections.sort(l, new MenuComparator());// 排序
				List<TreeNode> children = new ArrayList<TreeNode>();
				for (Tmenu r : l) {
					TreeNode tn = tree(r, true);
					children.add(tn);
				}
				node.setChildren(children);
				node.setState("open");
			}
		}
		return node;
	}

	public List<Menu> treegrid(Menu menu) {
		List<Tmenu> l;
		if (menu != null && menu.getId() != null) {
			l = menuDao.find("from Tmenu t where t.tmenu.id = ? order by t.seq", new Object[] { menu.getId() });
		} else {
			l = menuDao.find("from Tmenu t where t.tmenu is null order by t.seq");
		}
		return changeModel(l);
	}

	private List<Menu> changeModel(List<Tmenu> Tmenus) {
		List<Menu> l = new ArrayList<Menu>();
		if (Tmenus != null && Tmenus.size() > 0) {
			for (Tmenu t : Tmenus) {
				Menu m = new Menu();
				BeanUtils.copyProperties(t, m);
				if (t.getTmenu() != null) {
					m.setPid(t.getTmenu().getId());
					m.setPname(t.getTmenu().getName());
				}
				if (countChildren(t.getId()) > 0) {
					m.setState("closed");
				}
				if (t.getIconcls() == null) {
					m.setIconCls("");
				} else {
					m.setIconCls(t.getIconcls());
				}
				l.add(m);
			}
		}
		return l;
	}

	/**
	 * 统计有多少子节点
	 */
	private Long countChildren(String pid) {
		return menuDao.count("select count(*) from Tmenu t where t.tmenu.id = ?", new Object[] { pid });
	}

	public void delete(Menu menu) {
		del(menu.getId());
	}

	private void del(String cid) {
		Tmenu t = menuDao.get(Tmenu.class, cid);
		if (t != null) {
			Set<Tmenu> menus = t.getTmenus();
			if (menus != null && !menus.isEmpty()) {
				// 说明当前菜单下面还有子菜单
				for (Tmenu tmenu : menus) {
					del(tmenu.getId());
				}
			}
			menuDao.delete(t);
		}
	}

	public void add(Menu menu) {
		Tmenu t = new Tmenu();
		BeanUtils.copyProperties(menu, t);
		t.setId(UUID.randomUUID().toString());
		if (menu.getPid() != null) {
			t.setTmenu(menuDao.get(Tmenu.class, menu.getPid()));
		}
		if (menu.getIconCls() != null) {
			t.setIconcls(menu.getIconCls());
		}
		menuDao.save(t);
	}

	public void edit(Menu menu) {
		Tmenu t = menuDao.get(Tmenu.class, menu.getId());
		BeanUtils.copyProperties(menu, t);
		if (menu.getIconCls() != null) {
			t.setIconcls(menu.getIconCls());
		}
		if (menu.getPid() != null && !menu.getPid().equals(menu.getId())) {
			Tmenu pt = menuDao.get(Tmenu.class, menu.getPid());
			if (pt != null) {
				if (isDown(t, pt)) {// 要将当前节点修改到当前节点的子节点中
					Set<Tmenu> tmenus = t.getTmenus();// 当前要修改的节点的所有下级节点
					if (tmenus != null && tmenus.size() > 0) {
						for (Tmenu tmenu : tmenus) {
							if (tmenu != null) {
								tmenu.setTmenu(null);
							}
						}
					}
				}
				t.setTmenu(pt);
			}

		}
	}

	/**
	 * 判断是否是将当前节点修改到当前节点的子节点
	 * 
	 * @param t
	 * @param pt
	 * @return
	 */
	private boolean isDown(Tmenu t, Tmenu pt) {
		if (pt.getTmenu() != null) {
			if (pt.getTmenu().getId().equals(t.getId())) {
				return true;
			} else {
				return isDown(t, pt.getTmenu());
			}
		}
		return false;
	}

}
