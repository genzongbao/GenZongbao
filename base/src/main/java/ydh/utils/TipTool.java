package ydh.utils;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class TipTool {
	public static void success(String message, RedirectAttributes flash) {
		tip("success", message, flash);
	}
	
	public static void info(String message, RedirectAttributes flash) {
		tip("info", message, flash);
	}
	
	public static void warning(String message, RedirectAttributes flash) {
		tip("warning", message, flash);
	}
	
	public static void danger(String message, RedirectAttributes flash) {
		tip("danger", message, flash);
	}
	
	private static void tip(String type, String message, RedirectAttributes flash) {
		flash.addFlashAttribute("alertType", type);
		flash.addFlashAttribute("alertMsg",message);
	}
}
