package br.com.app.util;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class WeldUtil {

	private static Weld weldInstance;
	private static WeldContainer weldContainer;
	
	private WeldUtil() {
	}
	
	public static Weld getWeld() {
		if (weldInstance == null) {
			weldInstance = new Weld();
		}
		
		return weldInstance;
	}
	
	public static WeldContainer getContainer() {
		if (weldContainer == null) {
			weldContainer = getWeld().initialize();
		}
		
		return weldContainer;
	}
	
	public static <T> T select(Class<T> clazz) {
		return (T) getContainer().select(clazz).get();
	}
}
