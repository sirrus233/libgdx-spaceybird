package cf.spaceybird;

import cf.spaceybird.actors.Obstacle;
import cf.spaceybird.actors.Player;

import com.badlogic.gdx.math.Vector2;

/*
The PhysicsEngine class contains a number of static methods useful for calculating
various object physics (e.g. gravitational attraction, kinematics)
*/

public class PhysicsEngine {
    public static final float GRAV_CONST = 1;
    
    //Takes input data on two objects, represented as a position vector and mass
    //for each, and calculates the gravitational force between them. The force is
    //returned as a vector pointing from the position of the first object to the second.
    public static Vector2 getGravForce(Vector2 fromPos, float fromMass, 
                                       Vector2 toPos, float toMass) {
        float r = fromPos.dst(toPos);
        float magnitude = (GRAV_CONST*fromMass*toMass)/(r*r);
        
        Vector2 force = new Vector2(toPos.sub(fromPos));
        force.scl(magnitude);
        
        return force;
    }
    
    public static Vector2 getGravForce(Player p, Obstacle o) {
    	return getGravForce(p.getPosition(), p.getMass(), o.getPosition(), o.getMass());
    }
    
    public static Vector2 getAcceleration(float mass, Vector2 force) {
    	return new Vector2(force).div(mass);
    }
    
    public static Vector2 getVelocity(Vector2 initialVel, Vector2 acc, float timestep) {
    	Vector2 additionalVel = new Vector2(acc).scl(timestep);
    	return new Vector2(initialVel).add(additionalVel);
    }
    
    public static Vector2 getVelocity(Vector2 initialVel, Vector2 force, float mass, float timestep) {
    	return getVelocity(initialVel, getAcceleration(mass, force), timestep);
    }
}