package org.entervalov.psyEngine.api;

@SuppressWarnings("unused")
public class PhysicsProperties {

    private float mass = 1.0f;
    private float drag = 0.05f;
    private float friction = 0.6f;
    private float bounciness = 0.2f;

    private float buoyancy = 0.1f;

    private float breakThreshold = 0.0f;

    private float thermalConductivity = 0.3f;
    private float meltingPoint = 1000.0f;

    private String behaviorType = "DEFAULT";

    public PhysicsProperties() {}

    public PhysicsProperties setMass(float m) {
        this.mass = Math.max(0.1f, m);
        return this;
    }

    public PhysicsProperties setDrag(float d) {
        this.drag = Math.max(0.0f, Math.min(1.0f, d));
        return this;
    }

    public PhysicsProperties setFriction(float f) {
        this.friction = Math.max(0.0f, Math.min(1.0f, f));
        return this;
    }

    public PhysicsProperties setBounciness(float b) {
        this.bounciness = Math.max(0.0f, Math.min(1.0f, b));
        return this;
    }

    public PhysicsProperties setBuoyancy(float b) {
        this.buoyancy = Math.max(0.0f, b);
        return this;
    }

    public PhysicsProperties setBreakThreshold(float t) {
        this.breakThreshold = Math.max(0.0f, t);
        return this;
    }

    public PhysicsProperties setThermalConductivity(float c) {
        this.thermalConductivity = Math.max(0.0f, Math.min(1.0f, c));
        return this;
    }

    public PhysicsProperties setMeltingPoint(float m) {
        this.meltingPoint = Math.max(0.0f, m);
        return this;
    }

    public PhysicsProperties setBehaviorType(String type) {
        this.behaviorType = type != null ? type : "DEFAULT";
        return this;
    }

    public float getMass() { return mass; }
    public float getDrag() { return drag; }
    public float getFriction() { return friction; }
    public float getBounciness() { return bounciness; }
    public float getBuoyancy() { return buoyancy; }
    public float getBreakThreshold() { return breakThreshold; }
    public float getThermalConductivity() { return thermalConductivity; }
    public float getMeltingPoint() { return meltingPoint; }
    public String getBehaviorType() { return behaviorType; }

    @Override
    public PhysicsProperties clone() {
        return new PhysicsProperties()
                .setMass(mass)
                .setDrag(drag)
                .setFriction(friction)
                .setBounciness(bounciness)
                .setBuoyancy(buoyancy)
                .setBreakThreshold(breakThreshold)
                .setThermalConductivity(thermalConductivity)
                .setMeltingPoint(meltingPoint)
                .setBehaviorType(behaviorType);
    }

    public void copyFrom(PhysicsProperties other) {
        if (other == null) return;
        this.mass = other.mass;
        this.drag = other.drag;
        this.friction = other.friction;
        this.bounciness = other.bounciness;
        this.buoyancy = other.buoyancy;
        this.breakThreshold = other.breakThreshold;
        this.thermalConductivity = other.thermalConductivity;
        this.meltingPoint = other.meltingPoint;
        this.behaviorType = other.behaviorType;
    }

    @Override
    public String toString() {
        return "PhysicsProperties{" +
                "mass=" + mass +
                ", drag=" + drag +
                ", friction=" + friction +
                ", bounciness=" + bounciness +
                ", buoyancy=" + buoyancy +
                ", breakThreshold=" + breakThreshold +
                ", thermalConductivity=" + thermalConductivity +
                ", meltingPoint=" + meltingPoint +
                ", behaviorType='" + behaviorType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhysicsProperties props)) return false;

        if (Float.compare(props.mass, mass) != 0) return false;
        if (Float.compare(props.drag, drag) != 0) return false;
        if (Float.compare(props.friction, friction) != 0) return false;
        if (Float.compare(props.bounciness, bounciness) != 0) return false;
        if (Float.compare(props.buoyancy, buoyancy) != 0) return false;
        if (Float.compare(props.breakThreshold, breakThreshold) != 0) return false;
        if (Float.compare(props.thermalConductivity, thermalConductivity) != 0) return false;
        if (Float.compare(props.meltingPoint, meltingPoint) != 0) return false;
        return behaviorType.equals(props.behaviorType);
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(mass);
        result = 31 * result + Float.floatToIntBits(drag);
        result = 31 * result + Float.floatToIntBits(friction);
        result = 31 * result + Float.floatToIntBits(bounciness);
        result = 31 * result + Float.floatToIntBits(buoyancy);
        result = 31 * result + Float.floatToIntBits(breakThreshold);
        result = 31 * result + Float.floatToIntBits(thermalConductivity);
        result = 31 * result + Float.floatToIntBits(meltingPoint);
        result = 31 * result + behaviorType.hashCode();
        return result;
    }

    public static PhysicsProperties light() {
        return new PhysicsProperties()
                .setMass(0.4f)
                .setDrag(0.1f)
                .setFriction(0.3f)
                .setBounciness(0.2f)
                .setBuoyancy(0.9f);
    }

    public static PhysicsProperties normal() {
        return new PhysicsProperties()
                .setMass(1.0f)
                .setDrag(0.05f)
                .setFriction(0.6f)
                .setBounciness(0.3f)
                .setBuoyancy(0.2f);
    }

    public static PhysicsProperties heavy() {
        return new PhysicsProperties()
                .setMass(1.8f)
                .setDrag(0.02f)
                .setFriction(0.8f)
                .setBounciness(0.1f)
                .setBuoyancy(0.0f);
    }

    public static PhysicsProperties bouncy() {
        return new PhysicsProperties()
                .setMass(1.2f)
                .setDrag(0.03f)
                .setFriction(0.2f)
                .setBounciness(0.7f)
                .setBuoyancy(0.1f);
    }

    public static PhysicsProperties floating() {
        return new PhysicsProperties()
                .setMass(0.6f)
                .setDrag(0.08f)
                .setFriction(0.4f)
                .setBounciness(0.4f)
                .setBuoyancy(1.0f);
    }

    public static PhysicsProperties flammable() {
        return new PhysicsProperties()
                .setMass(0.8f)
                .setDrag(0.06f)
                .setFriction(0.5f)
                .setBounciness(0.3f)
                .setThermalConductivity(0.6f)
                .setMeltingPoint(400.0f)
                .setBehaviorType("BURN");
    }

    public static PhysicsProperties fragile() {
        return new PhysicsProperties()
                .setMass(1.1f)
                .setDrag(0.04f)
                .setFriction(0.3f)
                .setBounciness(0.6f)
                .setBreakThreshold(2.0f);
    }

    public static PhysicsProperties explosive() {
        return new PhysicsProperties()
                .setMass(1.5f)
                .setDrag(0.04f)
                .setFriction(0.4f)
                .setBounciness(0.2f)
                .setBreakThreshold(1.0f)
                .setMeltingPoint(300.0f)
                .setBehaviorType("EXPLODE");
    }

    public static PhysicsProperties icy() {
        return new PhysicsProperties()
                .setMass(0.9f)
                .setDrag(0.02f)
                .setFriction(0.1f)
                .setBounciness(0.5f)
                .setBuoyancy(0.92f)
                .setMeltingPoint(0.0f)
                .setBehaviorType("MELT");
    }
}