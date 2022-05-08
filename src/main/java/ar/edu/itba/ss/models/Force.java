package ar.edu.itba.ss.models;

public class Force {

    private Double x;
    private Double y;

    public Force(Double x, Double y){
        this.x = x;
        this.y = y;
    }
    
    public Double getX(){
        return this.x;
    }

    public Double getY(){
        return this.y;
    }

    public void setX(Double x){
        this.x = x;;
    }

    public void setY(Double y){
        this.y = y;
    }

    public void sum(double x, double y){
        this.x += x;
        this.y += y;
    }

    @Override
    public String toString() {
        return "Force{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
