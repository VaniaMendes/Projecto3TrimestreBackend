package aor.paj.proj_final_aor_backend.util;

import aor.paj.proj_final_aor_backend.bean.UserBean;
import jakarta.ejb.EJB;

import jakarta.ejb.Stateless;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;



    @WebListener
    public class SessionListener implements HttpSessionListener {


        @EJB
        UserBean userBean;


        public void sessionCreated(HttpSessionEvent se) {
            System.out.println("SessionListener: Sessão criada: " + se.getSession().getId());
        }


        @Override
        public void sessionDestroyed(HttpSessionEvent se) {
            String token = (String) se.getSession().getAttribute("token");
            Integer sessionTimeout = (Integer) se.getSession().getAttribute("sessionTimeout");
            Long lastActivityTime = (Long) se.getSession().getAttribute("lastActivityTime");

            System.out.println("SessionListener: Sessão destruída: " + se.getSession().getId());
            System.out.println("SessionListener: Token: " + token);
            System.out.println("SessionListener: SessionTimeout: " + sessionTimeout);
            System.out.println("SessionListener: LastActivityTime: " + lastActivityTime);

            if (sessionTimeout != null && lastActivityTime != null) {
                long currentTime = System.currentTimeMillis();
                long duration = currentTime - lastActivityTime;

                if (token != null && duration > (sessionTimeout * 60 * 1000)) {
                    userBean.logoutUser(token);
                    System.out.println("SessionListener: Sessão encerrada devido à inatividade");
                } else {
                    System.out.println("SessionListener: Sessão encerrada por outra razão ou timeout não ultrapassado");
                }
            } else {
                System.out.println("SessionListener: Sessão encerrada, sessionTimeout ou lastActivityTime não definido");
            }
        }


        public void updateSessionTimeout(HttpSession session, int timeout) {
            session.setMaxInactiveInterval( timeout *60);
        }

    }


