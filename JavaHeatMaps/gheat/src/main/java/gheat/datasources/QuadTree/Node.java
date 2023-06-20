package gheat.datasources.QuadTree;

import gheat.PointLatLng;

public class Node {

    private double x;
    private double y;
    private double w;
    private double h;
    private Node opt_padre;
    private PointLatLng point;
    private NodeType nodetype = NodeType.EMPTY;
    private Node nw;
    private Node ne;
    private Node sw;
    private Node se;

    public Node(double x, double y, double w, double h, Node opt_padre) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.opt_padre = opt_padre;
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

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public Node getParent() {
        return opt_padre;
    }

    public void setParent(Node opt_parent) {
        this.opt_padre = opt_parent;
    }

    public void setPoint(PointLatLng point) {
        this.point = point;
    }

    public PointLatLng getPoint() {
        return this.point;
    }

    public void setNodeType(NodeType nodetype) {
        this.nodetype = nodetype;
    }

    public NodeType getNodeType() {
        return this.nodetype;
    }


    public void setNw(Node nw) {
        this.nw = nw;
    }

    public void setNe(Node ne) {
        this.ne = ne;
    }

    public void setSw(Node sw) {
        this.sw = sw;
    }

    public void setSe(Node se) {
        this.se = se;
    }

    public Node getNe() {
        return ne;
    }

    public Node getNw() {
        return nw;
    }

    public Node getSw() {
        return sw;
    }

    public Node getSe() {
        return se;
    }
}
