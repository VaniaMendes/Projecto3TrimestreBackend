package aor.paj.proj_final_aor_backend.util;

import aor.paj.proj_final_aor_backend.bean.UserBean;
import jakarta.ejb.EJB;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;



    @WebListener
    public class SessionListener implements HttpSessionListener {


        @EJB
        UserBean userBean;


        @Override
        public void sessionCreated(HttpSessionEvent se) {

            System.out.println("Tempo de sessão iniciado");

        }


        @Override
        public void sessionDestroyed(HttpSessionEvent se) {
            String token = (String) se.getSession().getAttribute("token");
            Integer sessionTimeout = (Integer) se.getSession().getAttribute("sessionTimeout");
            if(sessionTimeout != null){
                Long lastActivityTimeAttribute = (Long) se.getSession().getAttribute("lastActivityTime");
                if (lastActivityTimeAttribute != null) {
                    long lastActivityTime = lastActivityTimeAttribute;
                    long currentTime = System.currentTimeMillis();
                    long duration = currentTime - lastActivityTime;

                    if (token != null) {
                        if (duration > (sessionTimeout * 60 * 1000)) {

                            userBean.logoutUser(token);
                            System.out.println("Session time out ultrapassado");
                        }
                    }
                } else {


                }
            }else{

            }
        }


        // Método para atualizar o tempo de última atividade da sessão
        public void updateLastActivityTime(HttpSession session) {
            session.setAttribute("lastActivityTime", System.currentTimeMillis());

            System.out.println("Tempo de última atividade atualizado");
        }


    }


