package telran.java2022.security.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(30)
public class AuthorFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		if(checkEndPoint(request.getMethod(),request.getServletPath())) {
			String loginFromPath = parseLoginFromPAth(request.getRequestURI());
			if (!request.getUserPrincipal().getName().equals(loginFromPath)){
				response.sendError(403);
				return;
			}
			
		}
		chain.doFilter(request, response);
	}

	private String parseLoginFromPAth(String requestURI) {
		String[] parts = requestURI.split("/");
		String res = Arrays.asList(parts).get(parts.length-1);
		return res;
	}

	private boolean checkEndPoint(String method, String servletPath) {
		return ("Post".equalsIgnoreCase(method) && servletPath.matches("/forum/post/\\w+/?"))
				|| ("Put".equalsIgnoreCase(method) && servletPath.matches("/forum/post/\\w+/comment/\\w+/?"));
	}

}
