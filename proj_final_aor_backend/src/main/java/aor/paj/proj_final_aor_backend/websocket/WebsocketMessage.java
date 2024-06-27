package aor.paj.proj_final_aor_backend.websocket;

import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.bean.UserProjectBean;
import aor.paj.proj_final_aor_backend.dto.Message;
import aor.paj.proj_final_aor_backend.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton
@ServerEndpoint("/websocket/message/{token}")
public class WebsocketMessage {

    private static final Logger logger = LogManager.getLogger(Notifier.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @EJB
    UserBean userBean;
    @EJB
    UserProjectBean userProjectBean;

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        logger.info("A new WebSocket session is opened for client with token: " + token);
        sessions.put(token, session);

        // Defines a timeout for the session
        session.setMaxIdleTimeout(24 * 60 * 60 * 1000);
    }


    public void sendMessageTOUser(String msg) {
        try {
            // Desserializar a mensagem em um objeto Message
            Message message = objectMapper.readValue(msg, Message.class);

            // Serializar o objeto Notification para JSON
            String jsonMessage = objectMapper.writeValueAsString(message);
            logger.info("Message JSON: " + jsonMessage);

            for (Session session : sessions.values()) {
                if (session.isOpen()) {
                    try {
                        // Recuperar o usuário pelo token da sessão
                        User user = userBean.getUserByToken(session.getPathParameters().get("token"));


                        // Verificar se o ID do usuário coincide com o ID do receptor da notificação
                        if (user != null && user.getId()==(message.getReceiver().getId())) {
                            session.getBasicRemote().sendText(jsonMessage);
                            logger.info("Notification sent to session: " + session.getId());
                        }

                    } catch (IOException e) {
                        logger.error("Error sending notification to session " + session.getId(), e);
                    }
                }
            }

        } catch (IOException e) {
            logger.error("Erro ao desserializar a mensagem: " + e.getMessage());
        }
    }

    public void sendMessageToProject(String msg, long project_id) {
        try {
            // Desserializar a mensagem em um objeto Message
            Message message = objectMapper.readValue(msg, Message.class);

            // Serializar o objeto Notification para JSON
            String jsonMessage = objectMapper.writeValueAsString(message);
            logger.info("Message JSON: " + jsonMessage);

            for (Session session : sessions.values()) {
                if (session.isOpen()) {
                    try {
                        // Recuperar o usuário pelo token da sessão
                        User user = userBean.getUserByToken(session.getPathParameters().get("token"));


                        // Verificar se o ID do usuário coincide com o ID do receptor da notificação
                        if (user != null && userProjectBean.isUserInAProject(user.getId(), project_id)) {
                            session.getBasicRemote().sendText(jsonMessage);
                            logger.info("Message sent to session: " + session.getId());
                        }

                    } catch (IOException e) {
                        logger.error("Error sending notification to session " + session.getId(), e);
                    }
                }
            }

        } catch (IOException e) {
            logger.error("Erro ao desserializar a mensagem: " + e.getMessage());
        }
    }



    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.warn("Session closed with reason " + closeReason.getCloseCode() + ": " + closeReason.getReasonPhrase());

        List<String> sessionIdsToRemove = sessions.entrySet().stream()
                .filter(entry -> entry.getValue().equals(session))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        sessionIdsToRemove.forEach(sessions::remove);
    }
}
