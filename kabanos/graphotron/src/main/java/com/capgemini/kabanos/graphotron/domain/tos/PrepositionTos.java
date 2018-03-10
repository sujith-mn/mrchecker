package com.capgemini.kabanos.graphotron.domain.tos;

public class PrepositionTos {
	
	private long id;
	private int implementations;
	private long totalNumber;
	private Long[] predecessors;
	private String loggerStep;
	private boolean wasFirstStep;
	private boolean wasLastStep;
	
	public Long[] getPredecessors() {
		return predecessors;
	}
	public void setPredecessors(Long[] predecessors) {
		this.predecessors = predecessors;
	}
	public String getLoggerStep() {
		return loggerStep;
	}
	public void setLoggerStep(String loggerStep) {
		this.loggerStep = loggerStep;
	}
	public int getImplementations() {
		return implementations;
	}
	public void setImplementations(int implementations) {
		this.implementations = implementations;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(long totalNumber) {
		this.totalNumber = totalNumber;
	}
	public boolean isWasLastStep() {
		return wasLastStep;
	}
	public void setWasLastStep(boolean wasLastStep) {
		this.wasLastStep = wasLastStep;
	}
	public boolean isWasFirstStep() {
		return wasFirstStep;
	}
	public void setWasFirstStep(boolean wasFirstStep) {
		this.wasFirstStep = wasFirstStep;
	}
}