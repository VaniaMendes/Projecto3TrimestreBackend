package aor.paj.proj_final_aor_backend.util;

import aor.paj.proj_final_aor_backend.bean.SettingsBean;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class ActivityFilter implements Filter {


    @Inject
    private SessionListener sessionListener;
    @Inject
    private SettingsBean settingsBean;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        System.out.println("ActivityFilter initialized");
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession(false);

            if (session != null) {
                session.setAttribute("lastActivityTime", System.currentTimeMillis());


                System.out.println("ActivityFilter: Tempo de última atividade atualizado para a sessão " + session.getId());
            } else {
                System.out.println("ActivityFilter: Sessão não encontrada");
            }
        }
        chain.doFilter(request, response);
    }




}
