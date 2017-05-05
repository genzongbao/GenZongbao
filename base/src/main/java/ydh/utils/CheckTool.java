package ydh.utils;

import java.util.List;
public class CheckTool {

	/**
	 * list是否为空
	 * @param list
	 * @return
	 */
	public static boolean checkListIsNotNull(List list){
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}
}
