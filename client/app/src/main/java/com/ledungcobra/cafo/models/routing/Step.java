
package com.ledungcobra.cafo.models.routing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("instruction")
    @Expose
    private Instruction instruction;
    @SerializedName("from_index")
    @Expose
    private Double fromIndex;
    @SerializedName("to_index")
    @Expose
    private Double toIndex;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("time")
    @Expose
    private Double time;

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }


    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(Double fromIndex) {
        this.fromIndex = fromIndex;
    }

    public Double getToIndex() {
        return toIndex;
    }

    public void setToIndex(Double toIndex) {
        this.toIndex = toIndex;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Step{" +
                "instruction=" + instruction +
                ", fromIndex=" + fromIndex +
                ", toIndex=" + toIndex +
                ", distance=" + distance +
                ", time=" + time +
                '}';
    }
}
