package cf.spaceybird;

import java.awt.geom.Point2D;
import com.badlogic.gdx.math.Vector2;

/*
The PhysicsEngine class contains a number of static methods useful for calculating
various object physics (e.g. gravitational attraction, rising/falling)
*/

public class PhysicsEngine {
    public static final float GRAV_CONST = 1.0f;
    
    //Empty PhysicsEngine constructor
    public PhysicsEngine() {       
    }
    
    //Takes input data on two objects, represented as a location (point) and mass
    //for each, and calculates the gravitational force between them. The force is
    //returned as a vector pointing from the position of the first object to the second.
    public static Vector2 getGravForce(Point2D.Float fromPos, float fromMass, 
                                       Point2D.Float toPos, float toMass) {
        float r = (float) fromPos.distance(toPos);
        float magnitude = (GRAV_CONST*fromMass*toMass)/(r*r);
        
        Vector2 force = new Vector2((toPos.x - fromPos.x),(toPos.y - fromPos.y));
        force.scl(magnitude);
        
        return force;
    }
}