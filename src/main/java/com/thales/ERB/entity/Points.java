package com.thales.ERB.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "POINTS")
public class Points {

	@Id
	@Column(name = "POINT_KEY")
	private String pointKey;

	@Column(name = "VALUE")
	private String value;

	public String getPointkey() {
		return pointKey;
	}

	public void setpointKey(String pointKey) {
		this.pointKey = pointKey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Points(String pointKey, String value) {
		super();
		this.pointKey = pointKey;
		this.value = value;
	}

	public Points() {
		super();
	}

	@Override
	public String toString() {
		return "Points [pointKey=" + pointKey + ", value=" + value + "]";
	}

}