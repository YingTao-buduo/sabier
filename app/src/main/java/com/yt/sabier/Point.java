package com.yt.sabier;

import com.alibaba.fastjson.JSONObject;


public class Point {
    private double x;
    private double y;
    private int value;

    public Point(){}

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public Point centerPoint(Point point) {
        return new Point((this.x + point.getX()) / 2, (this.y + point.getY()) / 2);
    }

    public double distanceTo(Point point) {
        return Math.sqrt(Math.pow(this.x - point.getX(), 2) + Math.pow(this.y - point.getY(), 2));
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("value", value);
        return jsonObject;
    }

    public int getIntX() {
        return (int) x;
    }

    public int getIntY() {
        return (int) y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
