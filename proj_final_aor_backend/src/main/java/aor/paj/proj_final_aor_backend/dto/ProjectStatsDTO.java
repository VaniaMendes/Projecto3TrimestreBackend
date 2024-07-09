package aor.paj.proj_final_aor_backend.dto;

public class ProjectStatsDTO {

    private Long labId;
    private String labName;
    private Long totalProjectsSubmitted;
    private Double percentageOfTotalProjects;
    private Long approvedProjects;
    private Double percentageOfApprovedProjects;
    private Long finishedProjects;
    private Double percentageOfFinishedProjects;
    private Long cancelledProjects;
    private Double percentageOfCancelledProjects;
    private Double averageNumberOfMembers;
    private Double averageProjectExecutionTime;

    // Constructors, Getters, and Setters

    public ProjectStatsDTO() {
    }

    // Add all necessary constructors, getters, and setters here

    public Long getLabId() {
        return labId;
    }

    public void setLabId(Long labId) {
        this.labId = labId;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public Long getTotalProjectsSubmitted() {
        return totalProjectsSubmitted;
    }

    public void setTotalProjectsSubmitted(Long totalProjectsSubmitted) {
        this.totalProjectsSubmitted = totalProjectsSubmitted;
    }

    public Double getPercentageOfTotalProjects() {
        return percentageOfTotalProjects;
    }

    public void setPercentageOfTotalProjects(Double percentageOfTotalProjects) {
        this.percentageOfTotalProjects = percentageOfTotalProjects;
    }

    public Long getApprovedProjects() {
        return approvedProjects;
    }

    public void setApprovedProjects(Long approvedProjects) {
        this.approvedProjects = approvedProjects;
    }

    public Double getPercentageOfApprovedProjects() {
        return percentageOfApprovedProjects;
    }

    public void setPercentageOfApprovedProjects(Double percentageOfApprovedProjects) {
        this.percentageOfApprovedProjects = percentageOfApprovedProjects;
    }

    public Long getFinishedProjects() {
        return finishedProjects;
    }

    public void setFinishedProjects(Long finishedProjects) {
        this.finishedProjects = finishedProjects;
    }

    public Double getPercentageOfFinishedProjects() {
        return percentageOfFinishedProjects;
    }

    public void setPercentageOfFinishedProjects(Double percentageOfFinishedProjects) {
        this.percentageOfFinishedProjects = percentageOfFinishedProjects;
    }

    public Long getCancelledProjects() {
        return cancelledProjects;
    }

    public void setCancelledProjects(Long cancelledProjects) {
        this.cancelledProjects = cancelledProjects;
    }

    public Double getPercentageOfCancelledProjects() {
        return percentageOfCancelledProjects;
    }

    public void setPercentageOfCancelledProjects(Double percentageOfCancelledProjects) {
        this.percentageOfCancelledProjects = percentageOfCancelledProjects;
    }

    public Double getAverageNumberOfMembers() {
        return averageNumberOfMembers;
    }

    public void setAverageNumberOfMembers(Double averageNumberOfMembers) {
        this.averageNumberOfMembers = averageNumberOfMembers;
    }

    public Double getAverageProjectExecutionTime() {
        return averageProjectExecutionTime;
    }

    public void setAverageProjectExecutionTime(Double averageProjectExecutionTime) {
        this.averageProjectExecutionTime = averageProjectExecutionTime;
    }
}