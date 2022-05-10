package it.polito.tdp.borders.model;

import java.util.Objects;

public class Country {
	
	private String stateAbb;
	private Integer cCcode;
	private String stateNm;
	
	public Country(String stateAbb, Integer cCode, String stateNme) {
		super();
		this.stateAbb = stateAbb;
		this.cCcode = cCode;
		this.stateNm = stateNme;
	}
	
	public String getStateAbb() {
		return stateAbb;
	}
	public Integer getCCode() {
		return cCcode;
	}
	public String getStateNme() {
		return stateNm;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cCcode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		return Objects.equals(cCcode, other.cCcode);
	}
	
	@Override
	public String toString() {
		return stateAbb + " " + stateNm;
	}

}
