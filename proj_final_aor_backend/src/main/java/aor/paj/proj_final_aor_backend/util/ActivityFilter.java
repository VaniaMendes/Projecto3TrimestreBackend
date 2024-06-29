package aor.paj.proj_final_aor_backend.util;

import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.entity.AppSettingsEntity;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class ActivityFilter implements Filter {

    @Inject
    SessionListener sessionListener;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession(true);

            if (session != null) {
                SessionListener sessionListener = new SessionListener();
                sessionListener.updateLastActivityTime(session);
            }
        }

        chain.doFilter(request, response);
    }



}
