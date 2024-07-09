package aor.paj.proj_final_aor_backend.dto;

import java.util.List;
import java.util.Map;

public class ProjectStatsDTO {

    private List<Map<String, Long>> numberOfSubmittedProjects;
    private List<Map<String, Double>> percentageOfSubmittedProjects;
    private List<Map<String, Long>> numberOfApprovedProjects;
    private List<Map<String, Double>> percentageOfApprovedProjects;
    private List<Map<String, Long>> numberOfFinishedProjects;
    private List<Map<String, Double>> percentageOfFinishedProjects;
    private List<Map<String, Long>> numberOfCancelledProjects;
    private List<Map<String, Double>> percentageOfCancelledProjects;
    private Double averageNumberOfActiveMembers;

    // Constructors, getters, and setters

    public ProjectStatsDTO() {
    }

    public List<Map<String, Long>> getNumberOfSubmittedProjects() {
        return numberOfSubmittedProjects;
    }

    public void setNumberOfSubmittedProjects(List<Map<String, Long>> numberOfSubmittedProjects) {
        this.numberOfSubmittedProjects = numberOfSubmittedProjects;
    }

    public List<Map<String, Long>> getNumberOfApprovedProjects() {
        return numberOfApprovedProjects;
    }

    public void setNumberOfApprovedProjects(List<Map<String, Long>> numberOfApprovedProjects) {
        this.numberOfApprovedProjects = numberOfApprovedProjects;
    }

    public List<Map<String, Long>> getNumberOfFinishedProjects() {
        return numberOfFinishedProjects;
    }

    public void setNumberOfFinishedProjects(List<Map<String, Long>> numberOfFinishedProjects) {
        this.numberOfFinishedProjects = numberOfFinishedProjects;
    }

    public List<Map<String, Long>> getNumberOfCancelledProjects() {
        return numberOfCancelledProjects;
    }

    public void setNumberOfCancelledProjects(List<Map<String, Long>> numberOfCancelledProjects) {
        this.numberOfCancelledProjects = numberOfCancelledProjects;
    }

    public Double getAverageNumberOfActiveMembers() {
        return averageNumberOfActiveMembers;
    }

    public void setAverageNumberOfActiveMembers(Double averageNumberOfActiveMembers) {
        this.averageNumberOfActiveMembers = averageNumberOfActiveMembers;
    }

    public List<Map<String, Double>> getPercentageOfSubmittedProjects() {
        return percentageOfSubmittedProjects;
    }

    public void setPercentageOfSubmittedProjects(List<Map<String, Double>> percentageOfSubmittedProjects) {
        this.percentageOfSubmittedProjects = percentageOfSubmittedProjects;
    }

    public List<Map<String, Double>> getPercentageOfApprovedProjects() {
        return percentageOfApprovedProjects;
    }

    public void setPercentageOfApprovedProjects(List<Map<String, Double>> percentageOfApprovedProjects) {
        this.percentageOfApprovedProjects = percentageOfApprovedProjects;
    }

    public List<Map<String, Double>> getPercentageOfFinishedProjects() {
        return percentageOfFinishedProjects;
    }

    public void setPercentageOfFinishedProjects(List<Map<String, Double>> percentageOfFinishedProjects) {
        this.percentageOfFinishedProjects = percentageOfFinishedProjects;
    }

    public List<Map<String, Double>> getPercentageOfCancelledProjects() {
        return percentageOfCancelledProjects;
    }

    public void setPercentageOfCancelledProjects(List<Map<String, Double>> percentageOfCancelledProjects) {
        this.percentageOfCancelledProjects = percentageOfCancelledProjects;
    }
}