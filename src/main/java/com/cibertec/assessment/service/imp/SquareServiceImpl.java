package com.cibertec.assessment.service.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.assessment.model.Polygon;
import com.cibertec.assessment.model.Square;
import com.cibertec.assessment.repo.SquareRepo;
import com.cibertec.assessment.service.PolygonService;
import com.cibertec.assessment.service.SquareService;

@Service
public class SquareServiceImpl implements SquareService {

    @Autowired 
    SquareRepo squareRepo;

    @Autowired
    PolygonService polygonService;

    @Override
    public Square create(Square s) {
        List<Polygon> polygons = polygonService.list();
        List<Integer> intersectedPolygonIds = new ArrayList<>();

        int[] xPoints = Arrays.stream(s.getXPoints().replaceAll("[\\[\\] ]", "").split(",")).mapToInt(Integer::parseInt).toArray();
        int[] yPoints = Arrays.stream(s.getYPoints().replaceAll("[\\[\\] ]", "").split(",")).mapToInt(Integer::parseInt).toArray();

        System.out.println("xPoints: " + Arrays.toString(xPoints));
        System.out.println("yPoints: " + Arrays.toString(yPoints));

        for (Polygon polygon : polygons) {
            if (isIntersectingPolygon(xPoints, yPoints, polygon)) {
                intersectedPolygonIds.add(polygon.getId());
            }
        }

        if (!intersectedPolygonIds.isEmpty()) {
            System.out.println("Intersected polygon IDs: " + intersectedPolygonIds.toString());
            s.setPolygons(intersectedPolygonIds.toString());
        } else {
            System.out.println("No intersected polygons found.");
            // Asegúrate de asignar un valor vacío en lugar de null
            s.setPolygons("");
        }

        try {
            return squareRepo.save(s);
        } catch (Exception e) {
            System.err.println("Error saving square: " + e.getMessage());
            return null;
        }
    }

    private boolean isIntersectingPolygon(int[] xPoints, int[] yPoints, Polygon polygon) {
        int polySides = polygon.getNpoints();
        int[] polyXPoints = Arrays.stream(polygon.getXPoints().replaceAll("[\\[\\] ]", "").split(",")).mapToInt(Integer::parseInt).toArray();
        int[] polyYPoints = Arrays.stream(polygon.getYPoints().replaceAll("[\\[\\] ]", "").split(",")).mapToInt(Integer::parseInt).toArray();

        System.out.println("Polygon X points: " + Arrays.toString(polyXPoints));
        System.out.println("Polygon Y points: " + Arrays.toString(polyYPoints));

        // Verificar si algún punto del cuadrado está dentro del polígono
        for (int i = 0; i < xPoints.length; i++) {
            if (isInside(xPoints[i], yPoints[i], polyXPoints, polyYPoints, polySides)) {
                return true;
            }
        }

        // Verificar si alguna línea del cuadrado intersecta con alguna línea del polígono
        for (int i = 0; i < xPoints.length; i++) {
            int x1 = xPoints[i];
            int y1 = yPoints[i];
            int x2 = xPoints[(i + 1) % xPoints.length];
            int y2 = yPoints[(i + 1) % xPoints.length];
            for (int j = 0; j < polySides; j++) {
                int x3 = polyXPoints[j];
                int y3 = polyYPoints[j];
                int x4 = polyXPoints[(j + 1) % polySides];
                int y4 = polyYPoints[(j + 1) % polySides];
                if (lineIntersects(x1, y1, x2, y2, x3, y3, x4, y4)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isInside(int testX, int testY, int[] polyXPoints, int[] polyYPoints, int polySides) {
        boolean inside = false;
        int j = polySides - 1;
        for (int i = 0; i < polySides; i++) {
            if ((polyYPoints[i] < testY && polyYPoints[j] >= testY || polyYPoints[j] < testY && polyYPoints[i] >= testY) &&
                    (polyXPoints[i] + (testY - polyYPoints[i]) / (polyYPoints[j] - polyYPoints[i]) * (polyXPoints[j] - polyXPoints[i]) < testX)) {
                inside = !inside;
            }
            j = i;
        }
        return inside;
    }

    private boolean lineIntersects(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        int d1 = direction(x3, y3, x4, y4, x1, y1);
        int d2 = direction(x3, y3, x4, y4, x2, y2);
        int d3 = direction(x1, y1, x2, y2, x3, y3);
        int d4 = direction(x1, y1, x2, y2, x4, y4);
        return ((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0));
    }

    private int direction(int x1, int y1, int x2, int y2, int x3, int y3) {
        return (x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1);
    }

    @Override
    public List<Square> list() {
        return squareRepo.findAll();
    }
    
    @Override
    public Square update(Square s) {
        return squareRepo.save(s);
    }

    @Override
    public void delete(Integer id) {
        squareRepo.deleteById(id);
    }
}
