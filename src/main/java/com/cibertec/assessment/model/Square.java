package com.cibertec.assessment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Square {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private int npoints;

    @JsonProperty("xpoints")
    private String xPoints; // Se debe guardar un array de 4 puntos como string

    @JsonProperty("ypoints")
    private String yPoints; // Se debe guardar un array de 4 puntos como string
    
    @JsonProperty("polygons")
    private String polygons; // Se debe guardar un array con los poligonos que intercepte

}
