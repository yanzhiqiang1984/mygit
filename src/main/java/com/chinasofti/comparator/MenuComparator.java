package com.chinasofti.comparator;

import java.util.Comparator;

import com.chinasofti.model.Tmenu;


/**
 * 菜单排序
 * 
 */
public class MenuComparator implements Comparator<Tmenu> {

	public int compare(Tmenu o1, Tmenu o2) {
		int i1 = o1.getSeq() != null ? o1.getSeq().intValue() : -1;
		int i2 = o2.getSeq() != null ? o2.getSeq().intValue() : -1;
		return i1 - i2;
	}

}
