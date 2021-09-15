package com.chineseall.authcenter.agent.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookiesUtil {

	public static String getSessionId(HttpServletRequest request) {
		String sessionId = request.getParameter("JSESSIONID");
		if (sessionId == null) {
			return getCookie(request, "JSESSIONID");
		}
		return sessionId;
	}
	
	public static String getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		String str = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (c.getName().equalsIgnoreCase(cookieName)) {
					str = c.getValue();
				}
			}
		}
		return str;
	}

	public static void setCookie(HttpServletResponse response, String domain,
			String cookieName, String cookieValue, Integer days) {
		Cookie cookies = null;
		cookies = new Cookie(cookieName, cookieValue);
		if (days != null) {
			cookies.setMaxAge(60 * 60 * 24 * days);
		}
		cookies.setPath("/");
		if (domain != null) {
			cookies.setDomain(domain);
		}
		response.addCookie(cookies);
	}

	public static void delCookie(HttpServletResponse response, String domain,
			String path, String cookieName, String cookieValue) {
		Cookie cookies = new Cookie(cookieName, cookieValue);
		if (domain != null) {
			cookies.setDomain(domain);
		}
		cookies.setPath(path);
		cookies.setMaxAge(0);
		response.addCookie(cookies);
	}

	public static void delAllCookie(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				c.setValue("");
				c.setMaxAge(0);
				response.addCookie(c);

			}
		}
	}
}
