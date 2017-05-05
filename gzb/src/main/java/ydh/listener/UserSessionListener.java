package ydh.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class UserSessionListener implements HttpSessionListener{

	@SuppressWarnings("rawtypes")
	private static HashMap userLoginMap = new HashMap();
	
	@SuppressWarnings("unchecked")
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		userLoginMap.put(arg0.getSession().getId(),arg0.getSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		userLoginMap.remove(arg0.getSession().getId());
	}   

	@SuppressWarnings("rawtypes")
	public static List<HttpSession> getSessions(){
		List<HttpSession> sessions = new ArrayList<HttpSession>();
		Iterator iter = userLoginMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			HttpSession val = (HttpSession)entry.getValue();
			sessions.add(val);
		}
		return sessions;
	}
	
	@SuppressWarnings("rawtypes")
	public static HashMap getUserLoginMap(){
		return userLoginMap;
	}
}
