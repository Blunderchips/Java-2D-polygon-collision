/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Matthew 'siD' Van der Bijl
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

import java.awt.Point;
import java.awt.Polygon;

import java.util.Arrays;
import java.util.Objects;
import java.util.ArrayList;

/**
 * A wrapper around the values needed for a malleable 2D polygon collision
 * class.
 *
 * <p>
 * will detect - but not resolve - collisions. It uses an efficient data search
 * structure to quickly find intersecting <code>Collidable</code> as well as
 * offering general utilities to the <code>Collidable</code>.
 * </p>
 *
 * @author Matthew 'siD' Van der Bijl
 *
 * @see java.awt.Point
 * @see java.lang.Object
 * @see java.lang.Cloneable
 */
public class Collidable extends java.lang.Object implements Cloneable {

    /**
     * the position of the <code>Collidable</code>.
     */
    private Point position;
    /**
     * the vertices that make up the <code>Collidable</code>.
     */
    private ArrayList<Point> vertices;

    /**
     * Default constructor.
     */
    public Collidable() {
        super();
    }

    public Collidable(Point[] vertices) {
        this(new Point(0, 0), vertices);
    }

    public Collidable(Point position, Point[] vertices) {
        super();

        this.position = position;

        this.vertices = new ArrayList<>();
        this.vertices.addAll(Arrays.asList(vertices));
    }

    public Collidable(int xPos, int yPos, Point[] vertices) {
        this(new Point(xPos, yPos), vertices);
    }

    public Collidable(int[] xPoints, int[] yPoints) {
        this(new Point(0, 0), null);
        this.setVertices(xPoints, yPoints);
    }

    public Collidable(Point position, int[] xPoints, int[] yPoints) {
        this(position, null);
        this.setVertices(xPoints, yPoints);
    }

    /**
     * creates a new <code>Collidable</code> from a inputed
     * <code>Collidable</code>.
     *
     * @param Collidable the inputed <code>Collidable</code>.
     */
    public Collidable(Collidable Collidable) {
        this(Collidable.getPosition(), Collidable.getVertices());
    }

    /**
     * @return all vertices as a Points array of the <code>Collidable</code>.
     */
    public Point[] getVertices() {
        Point[] points = new Point[vertices.size()];

        for (int i = 0; i < vertices.size(); i++) {
            points[i] = new Point(vertices.get(i));
        }

        return points;
    }

    public void addPoint(Point point) {
        this.vertices.add(point);
    }

    public void addPoint(int xPos, int yPos) {
        this.addPoint(new Point(xPos, yPos));
    }

    public void addPoints(Point[] points) {
        for (Point point : points) {
            this.addPoint((int) point.getX(), (int) point.getY());
        }
    }

    public Collidable get() {
        return this;
    }

    public void set(Collidable coll) {
        this.setVertices(coll.getVertices());
        this.setPosition(coll.getPosition());
    }

    /**
     * creates a new <code>Collidable</code> with the <code>Point</code> arrays
     * given.
     *
     * @param vertices an array of the Points coordinates of the
     * <code>Collidable</code>
     */
    public void setVertices(Point[] vertices) {
        ArrayList<Point> temp = new ArrayList<>();
        for (Point point : vertices) {
            temp.add(point);
        }
        this.vertices = temp;
    }

    public void setVertices(Polygon p) {
        int[] xpoints = new int[p.npoints];
        int[] ypoints = new int[p.npoints];

        for (int i = 0; i < p.npoints; i++) {
            xpoints[i] = p.xpoints[i];
            xpoints[i] = p.ypoints[i];
        }

        this.setVertices(xpoints, ypoints);
    }

    /**
     * The total number of points. The value of <code>vertices.size()</code>
     * represents the number of valid points in this <code>Collidable</code> and
     * might be less than the number of elements in vertices . This value can be
     * NULL.
     *
     * @return the total number of points in the vertices ArrayList
     */
    public int getNumVertices() {
        return this.vertices.size();
    }

    /**
     * creates a new <code>Collidable</code> with the arrays given.This method
     * is called from within the constructor to initialize the
     * <code>Collidable</code>. WARNING: Do NOT modify this code.
     *
     * @throws IllegalArgumentException if the the number of X points does not
     * equal the number of Y points or if they contain less than 3 points.
     *
     * @param xPoints an array of the x coordinates of the polygon.
     * @param yPoints an array of the y coordinates of the polygon.
     */
    public final void setVertices(int[] xPoints, int[] yPoints) throws IllegalArgumentException {
        if (xPoints == null || yPoints == null) {
            throw new NullPointerException(
                    "Collidables requires non-null X and Y vertices.");
        } else if (xPoints.length < 3) {
            throw new IllegalArgumentException(
                    "Collidables requires at least 3 X values. Found " + xPoints.length);
        } else if (yPoints.length < 3) {
            throw new IllegalArgumentException(
                    "Collidables requires at least 3 Y values. Found " + yPoints.length);
        } else if (xPoints.length != yPoints.length) {
            throw new IllegalArgumentException(
                    "Collidables requires the same amount of X and Y values. Found "
                    + xPoints.length + "," + yPoints.length);
        } else {
            ArrayList<Point> temp = new ArrayList<>(xPoints.length);

            for (int i = 0; i < xPoints.length; i++) {
                temp.add(new Point(xPoints[i], yPoints[i]));
            }

            this.vertices = temp;
        }
    }

    /**
     * Determines whether the specified coordinates are inside this
     * <code>Collidable</code>.
     *
     * @param point the point t be tested
     * @return <code>true</code> if this <code>Collidable</code> contains the
     * specified coordinates <code>(xPos; yPos)</code> {@code false} otherwise.
     */
    public boolean contains(Point point) {
        return this.contains(point.getX(), point.getY());
    }

    /**
     * Determines whether the specified coordinates are inside this
     * <code>Collidable</code>.
     *
     * @param xPos the specified X coordinate to be tested
     * @param yPos the specified Y coordinate to be tested
     * @return <code>true</code> if this <code>Collidable</code> contains the
     * specified coordinates <code>(xPos; yPos)</code> <code>false</code>
     * otherwise.
     */
    public boolean contains(double xPos, double yPos) {

        int hits = 0;

        double lastPosX = getVertices()[getNumVertices() - 1].getX() + getPosition().getX() + 1;
        double lastPosY = getVertices()[getNumVertices() - 1].getY() + getPosition().getY() + 1;
        double curPosX, curPosY;

        for (int i = 0; i < getNumVertices(); lastPosX = curPosX, lastPosY = curPosY, i++) {
            curPosX = getVertices()[i].getX() + getPosition().getX() + 1;
            curPosY = getVertices()[i].getY() + getPosition().getY() + 1;

            if (curPosY == lastPosY) {
                continue;
            }

            double leftPosX;
            if (curPosX < lastPosX) {
                if (xPos >= lastPosX) {
                    continue;
                }
                leftPosX = curPosX;
            } else {
                if (xPos >= curPosX) {
                    continue;
                }
                leftPosX = lastPosX;
            }

            double testA, testB;
            if (curPosY < lastPosY) {
                if (yPos < curPosY || yPos >= lastPosY) {
                    continue;
                }
                if (xPos < leftPosX) {
                    hits++;
                    continue;
                }
                testA = xPos - curPosX;
                testB = yPos - curPosY;
            } else {
                if (yPos < lastPosY || yPos >= curPosY) {
                    continue;
                }
                if (xPos < leftPosX) {
                    hits++;
                    continue;
                }
                testA = xPos - lastPosX;
                testB = yPos - lastPosY;
            }

            if (testA < (testB / (lastPosY - curPosY) * (lastPosX - curPosX))) {
                hits++;
            }
        }

        return ((hits & 1) != 0);
    }

    /**
     * Translates the vertices of the <code>Collidable</code> by
     * <code>deltaX</code> along the X axis and by <code>deltaY</code> along the
     * Y axis.
     *
     * @param deltaX the amount to translate along the X axis (double)
     * @param deltaY the amount to translate along the Y axis (double)
     */
    public void translate(int deltaX, int deltaY) {
        this.position.setLocation(this.getPosX() + deltaX, this.getPosY() + deltaY);
    }

    /**
     * Translates the vertices of the <code>Collidable</code> by
     * <code>deltaX</code> along the X axis.
     *
     * @param deltaX the amount to translate along the X axis. (double)
     */
    public void translateX(int deltaX) {
        this.position.setLocation(this.getPosX() + deltaX, this.getPosY());
    }

    /**
     * Translates the vertices of the <code>Collidable</code> by
     * <code>deltaY</code> along the Y axis.
     *
     * @param deltaY the amount to translate along the Y axis (double)
     */
    public void translateY(int deltaY) {
        this.position.setLocation(position.getX(), position.getY() + deltaY);
    }

    /**
     * Resets this <code>Collidable</code> object to an empty
     * <code>Collidable</code> by setting the vertices ArrayList equal to a new
     * ArrayList of Point.
     */
    @SuppressWarnings("Convert2Diamond")
    public void reset() {
        this.vertices = new ArrayList<>();
    }

    /**
     * Returns the <code>Collidable</code> as a new Abstract Window Toolkit
     * (AWT) <code>Polygon</code>. The <code>Polygon</code> class encapsulates a
     * description of a closed, two-dimensional region within a coordinate
     * space.
     *
     * @see java.awt.Polygon
     * @return a new Java AWT Polygon created by the points in the vertices
     * ArrayList
     */
    public java.awt.Polygon toPolygon() {
        int[] xPoints = new int[getNumVertices()];
        int[] yPoints = new int[getNumVertices()];

        for (int i = 0; i < getNumVertices(); i++) {
            xPoints[i] = (int) this.getVertices()[i].getX();
            yPoints[i] = (int) this.getVertices()[i].getY();
        }

        return new java.awt.Polygon(xPoints, yPoints, getNumVertices());
    }

    /**
     * Returns Average of the height of the <code>Collidable</code>, in pixels.
     *
     * @return average of the height, in pixels.
     */
    public float getAverageHeight() {
        float result = 0;

        for (Point point : getVertices()) {
            result += point.getX();
        }

        result /= getNumVertices();

        return result;
    }

    /**
     * Returns Average of the width of the <code>Collidable</code>, in pixels.
     *
     * @return average of the width, in pixels.
     */
    public float getAverageWidth() {
        float result = 0;

        for (Point point : getVertices()) {
            result += point.getX();
        }

        return result / getNumVertices();
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Class Object is the root of the class hierarchy. Every class has Object
     * as a superclass. All objects, including arrays, implement the methods of
     * this class.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(vertices);
        hash = 79 * hash + Objects.hashCode(position);
        return hash;
    }

    /**
     * Returns true if the <code>this</code> is equal to the argument and false
     * otherwise. Consequently, if both argument are null, true is returned,
     * false is returned. Otherwise, equality is determined by using the equals
     * method of the first argument.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * @return true if the argument is equal to <code>this</code> other and
     * false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        final Collidable coll = (Collidable) obj;

        if (!Objects.equals(this.getVertices(), coll.getVertices())) {
            return false;
        } else if (Objects.equals(this.getPosition(), coll.getPosition())) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Collidable[" + "position: " + getPosition() + ", " + "points: " + Arrays.toString(getVertices()) + "]";
    }

    /**
     * Returns the position of the center of the <code>Collidable</code>.
     *
     * @return the position of the center of the <code>Collidable</code>.
     */
    public Point getCenter() {
        return new Point(
                (int) this.getCenterX().getX(),
                (int) this.getCenterY().getY()
        );
    }

    /**
     * Returns the coordinate of the center of the <code>Collidable</code> in
     * the horizontal axis.
     *
     * @return the coordinate of the center of the <code>Collidable</code> in
     * the horizontal axis
     */
    public Point getCenterX() {
        return new Point(
                this.getPosX() - (int) this.getAverageWidth(),
                this.getPosY()
        );
    }

    /**
     * Returns the coordinate of the center of the <code>Collidable</code> in
     * the vertical axis.
     *
     * @return the coordinate of the center of the <code>Collidable</code> in
     * the vertical axis
     */
    public Point getCenterY() {
        return new Point(
                this.getPosX(),
                this.getPosY() - (int) this.getAverageWidth()
        );
    }

    /**
     * returns true if the <code>Collidable</code> have intersected with this
     * <code>Collidable</code>.
     *
     * @see dwarf.Collidable#contains(dwarf.util.Point2D)
     *
     * @param coll - the <code>Collidable</code> to be tested
     * @return true if the <code>Collidable</code> has intersected/collided with
     * this
     */
    public boolean intersects(Collidable coll) {
        if (coll.getNumVertices() != 0) {
            for (int i = 0; i < coll.getNumVertices();) {
                if (this.contains(
                        coll.getVertices()[i].getX() + coll.getPosX(),
                        coll.getVertices()[i].getY() + coll.getPosY())) {
                    return true;
                }

                if (coll.getNumVertices() >= 30) {
                    i += (35 * coll.getNumVertices()) / 100;
                } else {
                    i++;
                }
            }
        }

        if (this.getNumVertices() != 0) {
            for (int i = 0; i < this.getNumVertices();) {
                if (coll.contains(
                        this.getVertices()[i].getX() + this.getPosX(),
                        this.getVertices()[i].getY() + this.getPosY())) {
                    return true;
                }

                if (this.getNumVertices() >= 30) {
                    i += (35 * this.getNumVertices()) / 100;
                } else {
                    i++;
                }
            }
        }

        return false;
    }

    public void setPosition(int xPos, int yPos) {
        this.setPosition(new Point(xPos, yPos));
    }

    public final Collidable getCollidable() {
        return this;
    }

    @Override
    public Collidable clone() throws CloneNotSupportedException {
        return new Collidable(this);
    }

    public int getPosX() {
        return (int) this.position.getX();
    }

    public int getPosY() {
        return (int) this.position.getY();
    }

    public Point getVertex(int index) {
        return this.vertices.get(index);
    }
}
