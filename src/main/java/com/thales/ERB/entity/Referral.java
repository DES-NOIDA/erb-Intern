package com.thales.ERB.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="REFERRAL")
public class Referral {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REFERRAL_ID")
	private Long referralId;

	@Column(name = "REFERRAL_TGI", nullable = true)
	private String referralTGI;

	@Column(name = "REFERRAL_NAME")
	private String referralName;

	@Column(name = "OFFER", nullable = true)
	private String offer;

	@Column(name = "ONBOARDED")
	private String onboarded;

	@Column(name = "CANDIDATE_ID", unique = true)
	private String candidateId;

	@Column(name = "POSITION_OFFERED")
	private String positionOffered;

	@Column(name = "DIVERSITY")
	private String diversity;

	@Column(name = "PROBATION", nullable = true)
	private String probation;

	@Column(name = "DATE_OF_JOINING", nullable = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfJoining;

	@Column(name = "DATE_OF_PROBATION", nullable = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfProbation;

	@Column(name = "DATE_OF_REFERRAL")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfReferral;

	@Column(name = "REFERRAL_STATUS")
	private String referralStatus;

	@Column(name = "REFERREE_TGI")
	private String referreeTGI;

	@Column(name = "REFERRAL_POINTS")
	private Long referralPoints;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "EMPLOYEE_ID")
	private Employee employee;

	public Long getReferralId() {
		return referralId;
	}

	public void setReferralId(Long referralId) {
		this.referralId = referralId;
	}

	public String getReferralTGI() {
		return referralTGI;
	}

	public void setReferralTGI(String referralTGI) {
		this.referralTGI = referralTGI;
	}

	public String getReferralName() {
		return referralName;
	}

	public void setReferralName(String referralName) {
		this.referralName = referralName;
	}

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}

	public String getOnboarded() {
		return onboarded;
	}

	public void setOnboarded(String onboarded) {
		this.onboarded = onboarded;
	}

	public String getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}

	public String getPositionOffered() {
		return positionOffered;
	}

	public void setPositionOffered(String positionOffered) {
		this.positionOffered = positionOffered;
	}

	public String getDiversity() {
		return diversity;
	}

	public void setDiversity(String diversity) {
		this.diversity = diversity;
	}

	public String getProbation() {
		return probation;
	}

	public void setProbation(String probation) {
		this.probation = probation;
	}

	public LocalDate getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(LocalDate dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public LocalDate getDateOfProbation() {
		return dateOfProbation;
	}

	public void setDateOfProbation(LocalDate dateOfProbation) {
		this.dateOfProbation = dateOfProbation;
	}

	public LocalDate getDateOfReferral() {
		return dateOfReferral;
	}

	public void setDateOfReferral(LocalDate dateOfReferral) {
		this.dateOfReferral = dateOfReferral;
	}

	public String getReferralStatus() {
		return referralStatus;
	}

	public void setReferralStatus(String referralStatus) {
		this.referralStatus = referralStatus;
	}

	public String getReferreeTGI() {
		return referreeTGI;
	}

	public void setReferreeTGI(String referreeTGI) {
		this.referreeTGI = referreeTGI;
	}

	public Long getReferralPoints() {
		return referralPoints;
	}

	public void setReferralPoints(Long referralPoints) {
		this.referralPoints = referralPoints;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Referral(String referralTGI, String referralName, String offer, String onboarded, String candidateId,
			String positionOffered, String diversity, String probation, LocalDate dateOfJoining,
			LocalDate dateOfProbation, LocalDate dateOfReferral, String referralStatus, String referreeTGI,
			Long referralPoints, Employee employee) {
		super();
		this.referralTGI = referralTGI;
		this.referralName = referralName;
		this.offer = offer;
		this.onboarded = onboarded;
		this.candidateId = candidateId;
		this.positionOffered = positionOffered;
		this.diversity = diversity;
		this.probation = probation;
		this.dateOfJoining = dateOfJoining;
		this.dateOfProbation = dateOfProbation;
		this.dateOfReferral = dateOfReferral;
		this.referralStatus = referralStatus;
		this.referreeTGI = referreeTGI;
		this.referralPoints = referralPoints;
		this.employee = employee;
	}

	public Referral() {
		super();
	}

//	@Override
//	public String toString() {
//		return "Referral [referralId=" + referralId + ", referralTgi=" + referralTgi + ", referralName=" + referralName
//				+ ", offer=" + offer + ", onboarded=" + onboarded + ", candidateId=" + candidateId
//				+ ", positionOffered=" + positionOffered + ", diversity=" + diversity + ", probation=" + probation
//				+ ", dateOfJoining=" + dateOfJoining + ", dateOfProbation=" + dateOfProbation + ", dateOfReferral="
//				+ dateOfReferral + ", referralStatus=" + referralStatus + ", employeeTgi=" + employeeTgi
//				+ ", referralPoints=" + referralPoints + ", employee=" + employee + "]";
//	}

}
