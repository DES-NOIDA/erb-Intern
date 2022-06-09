package com.thales.ERB.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="EMPLOYEES")
public class Employee implements Comparable<Employee> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMPLOYEE_ID")
	private Long employeeId;

	@Column(name = "EMPLOYEE_TGI", unique = true, nullable = false)
	private String employeeTGI;

	@Column(name = "EMPLOYEE_NAME")
	private String employeeName;

	@Column(name = "NUMBER_OF_REFERALS", nullable = true)
	private Integer numberOfReferrals;

	@Column(name = "TOTAL_POINTS")
	private Long totalPoints;

	@Column(name = "ONBOARD_POINTS")
	private Long onboardPoints;

	@Column(name = "EXTRA_POINTS")
	private Long extraPoints;

	@OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
	private List<Referral> referral;

	public List<Referral> getReferral() {
		return referral;
	}

	public void setReferral(List<Referral> referral) {
		this.referral = referral;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeTGI() {
		return employeeTGI;
	}

	public void setEmployeeTGI(String employeeTGI) {
		this.employeeTGI = employeeTGI;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Integer getNumberOfReferrals() {
		return numberOfReferrals;
	}

	public void setNumberOfReferrals(Integer numberOfReferrals) {
		this.numberOfReferrals = numberOfReferrals;
	}

	public Long getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Long totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Employee() {
		super();

	}

	public Long getOnboardPoints() {
		return onboardPoints;
	}

	public void setOnboardPoints(Long onboardPoints) {
		this.onboardPoints = onboardPoints;
	}

	public Long getExtraPoints() {
		return extraPoints;
	}

	public void setExtraPoints(Long extraPoints) {
		this.extraPoints = extraPoints;
	}

	public Employee(String employeeTGI, String employeeName, Integer numberOfReferrals, Long totalPoints,
			Long onboardPoints, Long extraPoints, List<Referral> referral) {
		super();
		this.employeeTGI = employeeTGI;
		this.employeeName = employeeName;
		this.numberOfReferrals = numberOfReferrals;
		this.totalPoints = totalPoints;
		this.onboardPoints = onboardPoints;
		this.extraPoints = extraPoints;
		this.referral = referral;
	}

//	@Override
//	public String toString() {
//		return "Employee [employeeId=" + employeeId + ", employeeTGI=" + employeeTGI + ", employeeName=" + employeeName
//				+ ", numberOfReferrals=" + numberOfReferrals + ", totalPoints=" + totalPoints + ", onboardPoints="
//				+ onboardPoints + ", extraPoints=" + extraPoints + ", referral=" + referral + "]";
//	}

	@Override
	public int compareTo(Employee compareEmployee) {
		return compareEmployee.totalPoints.intValue() - this.totalPoints.intValue();
	}

}
