
package com.ledungcobra.cafo.models.routing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("instruction")
    @Expose
    private Instruction instruction;
    @SerializedName("from_index")
    @Expose
    private Integer fromIndex;
    @SerializedName("to_index")
    @Expose
    private Integer toIndex;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("time")
    @Expose
    private Integer time;

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    public Integer getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(Integer fromIndex) {
        this.fromIndex = fromIndex;
    }

    public Integer getToIndex() {
        return toIndex;
    }

    public void setToIndex(Integer toIndex) {
        this.toIndex = toIndex;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
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
