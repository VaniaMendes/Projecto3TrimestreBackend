package aor.paj.proj_final_aor_backend.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class ActivityFilter implements Filter {



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

    @Override
    public void destroy() {
        // Limpeza do filtro, se necessário
        System.out.println("ActivityFilter destroyed");
    }


}
