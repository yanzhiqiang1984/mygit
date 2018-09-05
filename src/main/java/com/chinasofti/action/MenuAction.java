package com.chinasofti.action;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;


import com.chinasofti.action.base.BaseAction;
import com.chinasofti.pageModel.Menu;
import com.chinasofti.pageModel.base.Json;
import com.chinasofti.service.MenuServiceI;
import com.chinasofti.util.ExceptionUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 菜单ACTION
 * 
 */
@Action(value = "menuAction", results = { @Result(name = "menu", location = "/admin/menu.jsp"), @Result(name = "menuAdd", location = "/admin/menuAdd.jsp"), @Result(name = "menuEdit", location = "/admin/menuEdit.jsp") })
public class MenuAction extends BaseAction implements ModelDriven<Menu> {

	private static final Logger logger = Logger.getLogger(MenuAction.class);

	private MenuServiceI menuService;

	public MenuServiceI getMenuService() {
		return menuService;
	}

	@Autowired
	public void setMenuService(MenuServiceI menuService) {
		this.menuService = menuService;
	}

	private Menu menu = new Menu();

	public Menu getModel() {
		return menu;
	}

	/**
	 * 首页获得菜单树
	 */
	public void doNotNeedSession_tree() {
		super.writeJson(menuService.tree(menu, false));
	}

	public void doNotNeedSession_treeRecursive() {
		super.writeJson(menuService.tree(menu, true));
	}

	public String menu() {
		return "menu";
	}

	/**
	 * 获得菜单treegrid
	 */
	public void treegrid() {
		super.writeJson(menuService.treegrid(menu));
	}

	/**
	 * 删除一个菜单
	 */
	public void delete() {
		Json j = new Json();
		try {
			menuService.delete(menu);
			j.setSuccess(true);
			j.setMsg("删除成功！");
			j.setObj(menu.getId());
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
			j.setMsg("删除失败！");
		}
		super.writeJson(j);
	}

	public String menuAdd() {
		return "menuAdd";
	}

	public void add() {
		Json j = new Json();
		try {
			menuService.add(menu);
			j.setSuccess(true);
			j.setMsg("新增成功！");
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
			j.setMsg("新增失败！");
		}
		super.writeJson(j);
	}

	public String menuEdit() {
		return "menuEdit";
	}

	/**
	 * 修改菜单
	 */
	public void edit() {
		Json j = new Json();
		try {
			menuService.edit(menu);
			j.setSuccess(true);
			j.setMsg("修改成功!");
		} catch (Exception e) {
			logger.error(ExceptionUtil.getExceptionMessage(e));
			j.setMsg("修改失败！");
		}
		writeJson(j);
	}

}
