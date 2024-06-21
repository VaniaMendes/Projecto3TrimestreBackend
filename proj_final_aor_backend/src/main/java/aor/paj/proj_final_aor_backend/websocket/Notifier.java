package aor.paj.proj_final_aor_backend.websocket;
import jakarta.ejb.Singleton;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Singleton
@ServerEndpoint("/websocket/notifier/{token}")
public class Notifier {


    private static final Logger logger = LogManager.getLogger(Notifier.class);


    // We use concurrent hash map to store the sessions of the clients. It is thread safe.
    HashMap<String, Session> sessions = new HashMap<String, Session>();

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        logger.debug("A new WebSocket session is opened for client with token: " + token);
        sessions.put(token, session);

        //Defines a timeout for the session
        session.setMaxIdleTimeout(24 * 60 * 60 * 1000);
    }

    public void sendNotification(String token, String message) {
        Session session = sessions.get(token);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
                logger.debug("Notification sent to session {}", session.getId());
            } catch (IOException e) {
                logger.error("Error sending notification to session {}: {}", session.getId(), e.getMessage());
            }
        } else {
            if (session == null) {
                logger.warn("Session not found for token {}", token);
            } else {
                logger.warn("Session closed for token {}", token);
            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.warn("Sessio close with reason  " + closeReason.getCloseCode() + ": " + closeReason.getReasonPhrase());

        //
        List<String> sessionIdsToRemove = new ArrayList<>();

        for (Map.Entry<String, Session> entry : sessions.entrySet()) {
            if (entry.getValue().equals(session)) {
                sessionIdsToRemove.add(entry.getKey());
            }
        }

        for (String sessionId : sessionIdsToRemove) {
            sessions.remove(sessionId);
        }
    }



}
