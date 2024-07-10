package aor.paj.proj_final_aor_backend.service;

import aor.paj.proj_final_aor_backend.bean.StatisticsBean;
import aor.paj.proj_final_aor_backend.bean.UserBean;
import aor.paj.proj_final_aor_backend.dto.ProjectStatsDTO;
import aor.paj.proj_final_aor_backend.dto.User;
import jakarta.ejb.EJB;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import jakarta.ws.rs.core.Response;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

@Path("/statistics")
public class StatisticsService {

    private static final Logger logger = LogManager.getLogger(StatisticsService.class);

    @EJB
    private StatisticsBean statisticsBean;

    @EJB
    private UserBean userBean;

    @Context
    private HttpServletRequest request;

    @GET
    @Path("/")
    @Produces("application/json")
    public Response getNumberOfSubmittedProjects() {
        logger.info("Received request to get number of submitted projects");
        return Response.ok(statisticsBean.getProjectStatistics()).build();
    }

    @GET
    @Path("/generate-pdf")
    @Produces("application/pdf")
    public Response generatePdf(@HeaderParam("token") String token) {
        logger.info("Received request to generate PDF for project statistics");

        User user = userBean.getUserByToken(token);
        if (user == null) {
            logger.error("User not found or unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found or unauthorized").build();
        }

        ProjectStatsDTO statisticsData = statisticsBean.getProjectStatistics();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            AtomicReference<PDPageContentStream> contentStream = new AtomicReference<>(new PDPageContentStream(document, page));

            contentStream.get().beginText();
            contentStream.get().setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.get().newLineAtOffset(100, 750);
            contentStream.get().showText("Project Statistics:");
            contentStream.get().setFont(PDType1Font.HELVETICA, 9);
            contentStream.get().newLineAtOffset(0, -25);

            // Start Y position for text
            final int[] yOffset = {750};

            // Define a method to add a section with a title
            java.util.function.BiConsumer<String, Integer> addSectionTitle = (title, offset) -> {
                try {
                    contentStream.get().setFont(PDType1Font.HELVETICA_BOLD, 9);
                    contentStream.get().showText(title);
                    contentStream.get().setFont(PDType1Font.HELVETICA, 9);
                    contentStream.get().newLineAtOffset(0, -20);
                    yOffset[0] -= 10;
                } catch (IOException e) {
                    logger.error("Error writing section title", e);
                }
            };

            // Method to add content and handle new pages
            BiConsumer<String, String> addConcatenatedContent = (title, concatenatedContent) -> {
                try {
                    contentStream.get().showText(title + " " + concatenatedContent);
                    contentStream.get().newLineAtOffset(0, -25);
                    yOffset[0] -= 10;
                } catch (IOException e) {
                    logger.error("Error adding content", e);
                }
            };


            // Adding all sections with new page management
            // Adding all sections with new page management
            addSectionTitle.accept("Number of Submitted Projects:", -15);
            StringBuilder concatenatedContentSP = new StringBuilder();
            for (Map<String, Long> map : statisticsData.getNumberOfSubmittedProjects()) {
                for (Map.Entry<String, Long> entry : map.entrySet()) {
                    if (concatenatedContentSP.length() > 0) {
                        concatenatedContentSP.append(", ");
                    }
                    concatenatedContentSP.append(entry.getKey()).append(": ").append(entry.getValue()).append("%");
                }
            }
            addConcatenatedContent.accept("Submitted Projects:", concatenatedContentSP.toString());

            // Use a different StringBuilder variable name for percentage to avoid redeclaration
            addSectionTitle.accept("Percentage of Submitted Projects:", -15);
            StringBuilder concatenatedContentSPP = new StringBuilder();
            for (Map<String, Double> map : statisticsData.getPercentageOfSubmittedProjects()) {
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    if (concatenatedContentSPP.length() > 0) {
                        concatenatedContentSPP.append(", ");
                    }
                    concatenatedContentSPP.append(entry.getKey()).append(": ").append(entry.getValue()).append("%");
                }
            }
            addConcatenatedContent.accept("Submitted Projects:", concatenatedContentSPP.toString());


            addSectionTitle.accept("Number of Approved Projects:", -15);
            StringBuilder concatenatedContentAP = new StringBuilder();
            for (Map<String, Long> map : statisticsData.getNumberOfApprovedProjects()) {
                for (Map.Entry<String, Long> entry : map.entrySet()) {
                    if (concatenatedContentAP.length() > 0) {
                        concatenatedContentAP.append(", ");
                    }
                    concatenatedContentAP.append(entry.getKey()).append(": ").append(entry.getValue()).append("%");
                }
            }
            addConcatenatedContent.accept("Approved Projects:", concatenatedContentAP.toString());


            addSectionTitle.accept("Percentage of Approved Projects:", -15);
            StringBuilder concatenatedContentAPP = new StringBuilder();
            for (Map<String, Double> map : statisticsData.getPercentageOfApprovedProjects()) {
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    if (concatenatedContentAPP.length() > 0) {
                        concatenatedContentAPP.append(", ");
                    }
                    concatenatedContentAPP.append(entry.getKey()).append(": ").append(entry.getValue()).append("%");
                }
            }
            addConcatenatedContent.accept("Approved Projects:", concatenatedContentAPP.toString());


            addSectionTitle.accept("Number of Finished Projects:", -15);
            StringBuilder concatenatedContentFP = new StringBuilder();
            for (Map<String, Long> map : statisticsData.getNumberOfFinishedProjects()) {
                for (Map.Entry<String, Long> entry : map.entrySet()) {
                    if (concatenatedContentFP.length() > 0) {
                        concatenatedContentFP.append(", ");
                    }
                    concatenatedContentFP.append(entry.getKey()).append(": ").append(entry.getValue()).append("%");
                }
            }
            addConcatenatedContent.accept("Finished Projects:", concatenatedContentFP.toString());

            addSectionTitle.accept("Percentage of Finished Projects:", -15);
            StringBuilder concatenatedContentFPP = new StringBuilder();
            for (Map<String, Double> map : statisticsData.getPercentageOfFinishedProjects()) {
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    if (concatenatedContentFPP.length() > 0) {
                        concatenatedContentFPP.append(", ");
                    }
                    concatenatedContentFPP.append(entry.getKey()).append(": ").append(entry.getValue()).append("%");
                }
            }
            addConcatenatedContent.accept("Finished Projects:", concatenatedContentFPP.toString());


            addSectionTitle.accept("Number of Cancelled Projects:", -15);
            StringBuilder concatenatedContentCP = new StringBuilder();
            for (Map<String, Long> map : statisticsData.getNumberOfCancelledProjects()) {
                for (Map.Entry<String, Long> entry : map.entrySet()) {
                    if (concatenatedContentCP.length() > 0) {
                        concatenatedContentCP.append(", ");
                    }
                    concatenatedContentCP.append(entry.getKey()).append(": ").append(entry.getValue()).append("%");
                }
            }
            addConcatenatedContent.accept("Cancelled Projects:", concatenatedContentCP.toString());


            addSectionTitle.accept("Percentage of Cancelled Projects:", -15);
            StringBuilder concatenatedContentCPP = new StringBuilder();
            for (Map<String, Double> map : statisticsData.getPercentageOfCancelledProjects()) {
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    if (concatenatedContentCPP.length() > 0) {
                        concatenatedContentCPP.append(", ");
                    }
                    concatenatedContentCPP.append(entry.getKey()).append(": ").append(entry.getValue()).append("%");
                }
            }
            addConcatenatedContent.accept("Cancelled Projects:", concatenatedContentCPP.toString());


            addSectionTitle.accept("Average Number of Active Members:", -15);
            addConcatenatedContent.accept("Average Number of Active Members", statisticsData.getAverageNumberOfActiveMembers().toString());

            contentStream.get().endText();
            contentStream.get().close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);

            return Response.ok(out.toByteArray(), MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=statistics.pdf")
                    .build();
        } catch (Exception e) {
            logger.error("Error generating PDF", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}