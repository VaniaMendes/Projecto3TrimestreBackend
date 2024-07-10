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
            contentStream.get().setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.get().newLineAtOffset(100, 700);
            contentStream.get().showText("Project Statistics:");
            contentStream.get().setFont(PDType1Font.HELVETICA, 12);
            contentStream.get().newLineAtOffset(0, -25);

            // Start Y position for text
            final int[] yOffset = {675};

            // Define a method to add a section with a title
            java.util.function.BiConsumer<String, Integer> addSectionTitle = (title, offset) -> {
                try {
                    if (checkPageOverflow(document, yOffset, offset, contentStream, true)) {
                        yOffset[0] = 700;
                        contentStream.get().newLineAtOffset(0, yOffset[0]);
                    }
                    contentStream.get().setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.get().showText(title);
                    contentStream.get().setFont(PDType1Font.HELVETICA, 12);
                    contentStream.get().newLineAtOffset(0, -20);
                    yOffset[0] -= 20;
                } catch (IOException e) {
                    logger.error("Error writing section title", e);
                }
            };

            // Method to add content and handle new pages
            BiConsumer<String, Object> addContent = (key, value) -> {
                try {
                    if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                        yOffset[0] = 700;
                        contentStream.get().newLineAtOffset(0, yOffset[0]);
                    }
                    contentStream.get().showText(key + ": " + value);
                    contentStream.get().newLineAtOffset(0, -15);
                    yOffset[0] -= 15;
                } catch (IOException e) {
                    logger.error("Error adding content", e);
                }
            };


            if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                yOffset[0] = 700;
                contentStream.get().newLineAtOffset(0, yOffset[0]);
            }
            // Adding all sections with new page management
            addSectionTitle.accept("Number of Submitted Projects:", -15);
            for (Map<String, Long> map : statisticsData.getNumberOfSubmittedProjects()) {
                for (Map.Entry<String, Long> entry : map.entrySet()) {
                    if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                        yOffset[0] = 700;
                        contentStream.get().newLineAtOffset(0, yOffset[0]);
                    }
                    addContent.accept(entry.getKey(), entry.getValue());
                }
            }

            if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                yOffset[0] = 700;
                contentStream.get().newLineAtOffset(0, yOffset[0]);
            }
            addSectionTitle.accept("Percentage of Submitted Projects:", -15);
            for (Map<String, Double> map : statisticsData.getPercentageOfSubmittedProjects()) {
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                        yOffset[0] = 700;
                        contentStream.get().newLineAtOffset(0, yOffset[0]);
                    }
                    addContent.accept(entry.getKey(), entry.getValue() + "%");
                }
            }

            if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                yOffset[0] = 700;
                contentStream.get().newLineAtOffset(0, yOffset[0]);
            }
            addSectionTitle.accept("Number of Approved Projects:", -15);
            for (Map<String, Long> map : statisticsData.getNumberOfApprovedProjects()) {
                for (Map.Entry<String, Long> entry : map.entrySet()) {
                    if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                        yOffset[0] = 700;
                        contentStream.get().newLineAtOffset(0, yOffset[0]);
                    }
                    addContent.accept(entry.getKey(), entry.getValue());
                }
            }

            if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                yOffset[0] = 700;
                contentStream.get().newLineAtOffset(0, yOffset[0]);
            }
            addSectionTitle.accept("Percentage of Approved Projects:", -15);
            for (Map<String, Double> map : statisticsData.getPercentageOfApprovedProjects()) {
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                        yOffset[0] = 700;
                        contentStream.get().newLineAtOffset(0, yOffset[0]);
                    }
                    addContent.accept(entry.getKey(), entry.getValue() + "%");
                }
            }

            if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                yOffset[0] = 700;
                contentStream.get().newLineAtOffset(0, yOffset[0]);
            }
            addSectionTitle.accept("Number of Finished Projects:", -15);
            for (Map<String, Long> map : statisticsData.getNumberOfFinishedProjects()) {
                for (Map.Entry<String, Long> entry : map.entrySet()) {
                    if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                        yOffset[0] = 700;
                        contentStream.get().newLineAtOffset(0, yOffset[0]);
                    }
                    addContent.accept(entry.getKey(), entry.getValue());
                }
            }

            if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                yOffset[0] = 700;
                contentStream.get().newLineAtOffset(0, yOffset[0]);
            }
            addSectionTitle.accept("Percentage of Finished Projects:", -15);
            for (Map<String, Double> map : statisticsData.getPercentageOfFinishedProjects()) {
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                        yOffset[0] = 700;
                        contentStream.get().newLineAtOffset(0, yOffset[0]);
                    }
                    addContent.accept(entry.getKey(), entry.getValue() + "%");
                }
            }

            if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                yOffset[0] = 700;
                contentStream.get().newLineAtOffset(0, yOffset[0]);
            }
            addSectionTitle.accept("Number of Cancelled Projects:", -15);
            for (Map<String, Long> map : statisticsData.getNumberOfCancelledProjects()) {
                for (Map.Entry<String, Long> entry : map.entrySet()) {
                    if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                        yOffset[0] = 700;
                        contentStream.get().newLineAtOffset(0, yOffset[0]);
                    }
                    addContent.accept(entry.getKey(), entry.getValue());
                }
            }

            if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                yOffset[0] = 700;
                contentStream.get().newLineAtOffset(0, yOffset[0]);
            }
            addSectionTitle.accept("Percentage of Cancelled Projects:", -15);
            for (Map<String, Double> map : statisticsData.getPercentageOfCancelledProjects()) {
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                        yOffset[0] = 700;
                        contentStream.get().newLineAtOffset(0, yOffset[0]);
                    }
                    addContent.accept(entry.getKey(), entry.getValue() + "%");
                }
            }

            if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                yOffset[0] = 700;
                contentStream.get().newLineAtOffset(0, yOffset[0]);
            }
            addSectionTitle.accept("Average Number of Active Members:", -15);
            if (checkPageOverflow(document, yOffset, -15, contentStream, true)) {
                yOffset[0] = 700;
                contentStream.get().newLineAtOffset(0, yOffset[0]);
            }
            addContent.accept("Average Number of Active Members", statisticsData.getAverageNumberOfActiveMembers());

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

    // Utility method to handle page overflow and correctly start new page content stream
    private boolean checkPageOverflow(PDDocument document, int[] yOffset, int offset, AtomicReference<PDPageContentStream> contentStreamRef, boolean isContentPending) throws IOException {
        if (yOffset[0] + offset < 50 && isContentPending) {  // Check if current Y position + offset goes beyond bottom margin and there's content pending
            contentStreamRef.get().endText();
            contentStreamRef.get().close();
            PDPage newPage = new PDPage();
            document.addPage(newPage);
            PDPageContentStream newContentStream = new PDPageContentStream(document, newPage);
            contentStreamRef.set(newContentStream);
            newContentStream.beginText();
            newContentStream.setFont(PDType1Font.HELVETICA, 12);
            newContentStream.newLineAtOffset(100, 700);
            yOffset[0] = 700; // Reset yOffset for the new page
            return true;
        }
        return false;
    }
}