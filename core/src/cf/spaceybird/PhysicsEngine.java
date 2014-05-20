package cf.spaceybird;

import cf.spaceybird.actors.Obstacle;
import cf.spaceybird.actors.Player;

import com.badlogic.gdx.math.Vector2;

/**
 * The PhysicsEngine class contains a number of static methods useful for calculating
 * various object physics (e.g. gravitational attraction, kinematics)
 */

public class PhysicsEngine {
	
    public static final float GRAV_CONST = 20;
    
    /**Takes input data on two objects, represented as a position vector and mass
     * for each, and calculates the gravitational force between them.
     * 
     * @param fromPos The position vector of the object being pulled
     * @param fromMass The mass of the object being pulled
     * @param toPos The position vector of the object doing the pulling
     * @param toMass The mass of the object doing the pulling
     * @return the gravitational force vector on the first object in the direction of the second
     */
    public static Vector2 getGravForce(Vector2 fromPos, float fromMass, 
                                       Vector2 toPos, float toMass) {
        float r = fromPos.dst(toPos);
        float magnitude = (GRAV_CONST*fromMass*toMass)/(r*r);
        
        Vector2 force = new Vector2(toPos.sub(fromPos));
        force.scl(magnitude);
        
        return force;
    }
    
    /**Takes input data on a Player and Obstacle object and calculates the gravitational force
     * between them.
     * 
     * @param p The player object
     * @param o The obstacle object
     * @return the gravitational force vector on the player in the direction of the obstacle
     * @see cf.spaceybird.actors.Player
     * @see cf.spaceybird.actors.Obstacle
     */
    public static Vector2 getGravForce(Player p, Obstacle o) {
    	return getGravForce(p.getPosition(), p.getMass(), o.getPosition(), o.getMass());
    }
    
    /**
     * Get the acceleration due to a force acting on a mass
     * 
     * @param mass of the object being acted upon
     * @param force acting on the object
     * @return acceleration
     */
    public static Vector2 getAcceleration(float mass, Vector2 force) {
    	return new Vector2(force).scl(1/mass);
    }
    
    /**
     * Get the velocity of an accelerating object, where acceleration is known
     * 
     * @param initialVel The previous velocity of the object
     * @param acc The acceleration acting on the object
     * @param timestep The time (in seconds) that has passed between the previous velocity step and the
     * current one
     * @return the object's new velocity
     */
    public static Vector2 getVelocity(Vector2 initialVel, Vector2 acc, float timestep) {
    	Vector2 additionalVel = new Vector2(acc).scl(timestep);
    	return new Vector2(initialVel).add(additionalVel);
    }
    
    /**
     * Get the velocity of an accelerating object, where acceleration is unknown
     * 
     * @param initialVel The previous velocity of the object
     * @param force The force vector acting on the object
     * @param mass The mass of the object
     * @param timestep The time (in seconds) that has passed between the previous velocity step and the
     * current one
     * @return the object's new velocity
     */
    public static Vector2 getVelocity(Vector2 initialVel, Vector2 force, float mass, float timestep) {
    	return getVelocity(initialVel, getAcceleration(mass, force), timestep);
    }
}
