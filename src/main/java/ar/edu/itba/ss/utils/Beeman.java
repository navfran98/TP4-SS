package ar.edu.itba.ss.utils;
import ar.edu.itba.ss.*;
import ar.edu.itba.ss.models.*;

public class Beeman implements AlgorithmInterface {

    public Particle getNextValues(Particle currentParticle, Particle prevParticle, double dt, boolean electric) {
        Force currentForce = getForce(electric, currentParticle);
        if(prevParticle.getX() == null) {
            prevParticle = Euler.getValuesForFirstPreviousParticle(currentParticle, currentForce, -dt);
        }
        Force prevForce = getForce(electric, prevParticle);
        double currentAcelerationX = currentForce.getX() / currentParticle.getMass();
        double currentAcelerationY = currentForce.getY() / currentParticle.getMass();
        double prevAcelerationX = prevForce.getX() / prevParticle.getMass();
        double prevAcelerationY = prevForce.getY() / prevParticle.getMass();

        double newX = currentParticle.getX() + currentParticle.getVx() * dt + (2.0*currentAcelerationX/3 - prevAcelerationX/6) * (dt * dt);
        double newY = currentParticle.getY() + currentParticle.getVy() * dt + (2.0*currentAcelerationY/3 - prevAcelerationY/6) * (dt * dt);

        double predVx = currentParticle.getVx() + (3.0*currentAcelerationX/2 - prevAcelerationX/2 ) * dt;
        double predVy = currentParticle.getVy() + (3.0*currentAcelerationY/2 - prevAcelerationY/2 ) * dt;
        
        Particle auxParticle = new Particle(currentParticle.getMass(), newX, newY, predVx, predVy, currentParticle.getCharge());
        Force auxForce = getForce(electric, auxParticle);
        double auxAcelerationX = auxForce.getX() / auxParticle.getMass();
        double auxAcelerationY = auxForce.getY() / auxParticle.getMass();

        double correctedVx = currentParticle.getVx() + (auxAcelerationX/3 + 5.0*currentAcelerationX/6 - prevAcelerationX/6) * dt ;
        double correctedVy = currentParticle.getVy() + (auxAcelerationY/3 + 5.0*currentAcelerationY/6 - prevAcelerationY/6) * dt ;
        return new Particle(currentParticle.getMass(), newX, newY, correctedVx, correctedVy, currentParticle.getCharge());
    }

    private Force getForce(boolean electric, Particle p){
        if(electric)
            return ElectricUniverse.calculateForce(p);
        else
            return OsciladorAmortiguado.calculateForce(p);
    }
}
