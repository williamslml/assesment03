package com.cibertec.assessment.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolygonBean {
	
    private Integer id;

    private String name;

	private int npoints;
	
	private Integer[] xPoints;
	
	private Integer[] yPoints;
}
