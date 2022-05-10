package it.polito.tdp.borders.model;

import java.util.Objects;

public class Border {
	
	private Integer state1no;
	private Integer state2no;
	private Integer year;
	private Integer conttype;
	
	public Border(Integer state1no, Integer state2no, Integer year, Integer conttype) {
		super();
		this.state1no = state1no;
		this.state2no = state2no;
		this.year = year;
		this.conttype = conttype;
	}
	
	public Integer getState1no() {
		return state1no;
	}
	public Integer getState2no() {
		return state2no;
	}
	public Integer getYear() {
		return year;
	}
	public Integer getConttype() {
		return conttype;
	}

	@Override
	public int hashCode() {
		return Objects.hash(state1no, state2no);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Border other = (Border) obj;
		return Objects.equals(state1no, other.state1no) && Objects.equals(state2no, other.state2no);
	}


	
	

}
