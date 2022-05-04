package ar.edu.itba.ss.utils;
import ar.edu.itba.ss.*;
import ar.edu.itba.ss.models.*;

public class Beeman implements AlgorithmInterface {

    public Particle getNextValues(Particle currentParticle, Particle prevParticle, double dt) {

        double currentForce = OsciladorAmortiguado.calculateForce(currentParticle);
        if(prevParticle.getX() == null) {
            prevParticle = Euler.getValuesForFirstPreviousParticle(currentParticle, currentForce, dt);
        }
        
        double prevForce = OsciladorAmortiguado.calculateForce(prevParticle);

        double currentAceleration = currentForce / currentParticle.getMass();
        double prevAceleration = prevForce / prevParticle.getMass();

        double newX = currentParticle.getX() + currentParticle.getVx() * dt + (2*currentAceleration/3 - prevAceleration/6) * (dt * dt);
        // double newY = currentPart.getY() + currentPart.getVy() * dt + ((2/3) * 0 * (dt * dt)) - ((1/6) * 0 * (dt * dt));

        double predVx = currentParticle.getVx() + (3*currentAceleration/2 - prevAceleration/2 ) * dt;
        // double prevVy = currentPart.getVy() + ((3/2) * 0 * dt) - ((1/2) * 0 * dt);
        
        Particle auxParticle = new Particle(currentParticle.getMass(), newX, 0.0, predVx, 0.0);
        double auxForce = OsciladorAmortiguado.calculateForce(auxParticle);
        double auxAceleration = auxForce / auxParticle.getMass();

        double newVx = currentParticle.getVx() + (auxAceleration/3 + 5*currentAceleration/6 - prevAceleration/6) * dt ;
        // double newVy = prevVy + ((1/3) * 0 * dt) + ((5/6) * 0 * dt) - ((1/6) * 0 * dt);
        return new Particle(currentParticle.getMass(), newX, 0.0, newVx, 0.0);
    }
}
