package com.bfds.ach.domain.reference;

public enum SystemName {
	DBF ("DBF"),
	DFS ("DFS"),
	DFZ ("DFZ"),
	DTP ("DTP"),
	DFI ("DFI"),	
	LFS ("LFS"),
	DFQ ("DFQ"),
	DFY ("DFY"),
	DFE ("DFE"),
	DTN ("DTN"),
	LEG ("LEG"),
	DTJ ("DTJ"),
	DFR ("DFR"),
	JHK ("JHK"),
	PMC ("PMC"),
	AMV ("AMV"),
	DTK ("DTK");
	
	private SystemName(String name) {
		this.name = name;
	}

	private final String name;
	
	public String toString() {
		return name;
	}

}
