package aor.paj.proj_final_aor_backend.util;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@WebServlet("/upload")
@MultipartConfig(
        location="/tmp",
        maxFileSize=20848820,
        maxRequestSize=418018841,
        fileSizeThreshold=1048576
)
public class UploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Part filePart = request.getPart("photo");
            if (filePart == null || filePart.getSize() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Nenhuma foto foi enviada.");
                return;
            }
            String submittedFileName = filePart.getSubmittedFileName();
            if (submittedFileName == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Nome do arquivo não fornecido.");
                return;
            }
            String uploadsDir = "C:\\wildfly-30.0.1.Final\\standalone\\uploads"; // Diretório de uploads
            File uploads = new File(uploadsDir);
            if (!uploads.exists()) {
                uploads.mkdirs(); // Cria o diretório se não existir
            }
            // Gera um nome de arquivo único usando UUID
            String uniqueFileName = UUID.randomUUID() + "_" + Paths.get(submittedFileName).getFileName().toString();
            File file = new File(uploads, uniqueFileName); // Cria o arquivo no diretório de uploads
            try (var input = filePart.getInputStream()) {
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            String photoUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/uploads/" + uniqueFileName; // URL da foto

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"photoUrl\":\"" + photoUrl + "\"}");
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao fazer upload da foto.");
        }
    }
}

