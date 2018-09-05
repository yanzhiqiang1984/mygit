package com.chinasofti.pageModel;

import java.math.BigDecimal;

import com.chinasofti.model.Tmenu;



public class Menu implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String pid;
	private String pname;
	private String state = "open";// 是否展开(open,closed)
	private String iconCls;// 前面的小图标样式
	
	private Tmenu tmenu;
	private String iconcls;
	private String name;
	private BigDecimal seq;
	private String url;

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Tmenu getTmenu() {
		return tmenu;
	}

	public void setTmenu(Tmenu tmenu) {
		this.tmenu = tmenu;
	}

	public String getIconcls() {
		return iconcls;
	}

	public void setIconcls(String iconcls) {
		this.iconcls = iconcls;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getCseq() {
		return seq;
	}

	public void setSeq(BigDecimal seq) {
		this.seq = seq;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
