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

import lombok.RequiredArgsConstructor;
import telran.java2022.forum.dao.ForumRepository;
import telran.java2022.forum.model.Post;
import telran.java2022.user.dao.UsersRepository;
import telran.java2022.user.model.User;

@Component
@Order(30)
@RequiredArgsConstructor
public class DeletePostFilter implements Filter {
	
	final ForumRepository forumRepository;
	final UsersRepository usersRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		if(checkEndPoint(request.getMethod(),request.getServletPath())) {
			String id = parseIdFromPAth(request.getRequestURI());
			Post post = forumRepository.findById(id).orElse(null);
			if (post == null) {
				response.sendError(404);
				return;
			}
			User user = usersRepository.findById(request.getUserPrincipal().getName()).get();
			if (!request.getUserPrincipal().getName().equals(post.getAuthor())
					&& !user.getRoles().contains("Moderator".toUpperCase())) {
				response.sendError(403);
				return;
			}
			
		}
		chain.doFilter(request, response);
	}

	private String parseIdFromPAth(String requestURI) {
		String[] parts = requestURI.split("/");
		String res = Arrays.asList(parts).get(parts.length-1);
		return res;
	}

	private boolean checkEndPoint(String method, String servletPath) {
		return "Delete".equalsIgnoreCase(method) && servletPath.matches("/forum/post/\\w+/?");
	}

}
