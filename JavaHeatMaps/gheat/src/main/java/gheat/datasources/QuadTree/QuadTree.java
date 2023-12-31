package gheat.datasources.QuadTree;


import gheat.PointLatLng;

import java.util.ArrayList;
import java.util.List;


public class QuadTree {


    private Node root_;
    private int count_ = 0;

    public QuadTree(double minX, double minY, double maxX, double maxY) {
        this.root_ = new Node(minX, minY, maxX - minX, maxY - minY, null);
    }

    public Node getRootNode() {
        return this.root_;
    }

    public void set(double x, double y, Object value) {

        Node root = this.root_;
        if (x < root.getX() || y < root.getY() || x > root.getX() + root.getW() || y > root.getY() + root.getH()) {
            throw new QuadTreeException("Fuera de rango : (" + x + ", " + y + ")");
        }
        if (this.insert_(root, new PointLatLng(x, y, value))) {
            this.count_++;
        }
    }

    public Object get(double x, double y, Object opt_default) {
        Node node = this.find_(this.root_, x, y);
        return node != null ? node.getPoint().getValue() : opt_default;
    }

    public Object remove(double x, double y) {
        Node node = this.find_(this.root_, x, y);
        if (node != null) {
            Object value = node.getPoint().getValue();
            node.setPoint(null);
            node.setNodeType(NodeType.EMPTY);
            this.balance_(node);
            this.count_--;
            return value;
        } else {
            return null;
        }
    }

    public boolean contains(double x, double y) {
        return this.get(x, y, null) != null;
    }

    public boolean isEmpty() {
        return this.root_.getNodeType() == NodeType.EMPTY;
    }

    public int getCount() {
        return this.count_;
    }

    public void clear() {
        this.root_.setNw(null);
        this.root_.setNe(null);
        this.root_.setSw(null);
        this.root_.setSe(null);
        this.root_.setNodeType(NodeType.EMPTY);
        this.root_.setPoint(null);
        this.count_ = 0;
    }

    public PointLatLng[] getKeys() {
        final List<PointLatLng> arr = new ArrayList<PointLatLng>();
        this.traverse_(this.root_, new Func() {
            @Override
            public void call(QuadTree quadTree, Node node) {
                arr.add(node.getPoint());
            }
        });
        return arr.toArray(new PointLatLng[arr.size()]);
    }

    public Object[] getValues() {
        final List<Object> arr = new ArrayList<Object>();
        this.traverse_(this.root_, new Func() {
            @Override
            public void call(QuadTree quadTree, Node node) {
                arr.add(node.getPoint().getValue());
            }
        });

        return arr.toArray(new Object[arr.size()]);
    }

    public PointLatLng[] searchIntersect(final double xmin, final double ymin, final double xmax, final double ymax) {
        final List<PointLatLng> arr = new ArrayList<PointLatLng>();
        this.navigate(this.root_, new Func() {
            @Override
            public void call(QuadTree quadTree, Node node) {
                PointLatLng pt = node.getPoint();
                if (pt.getLongitud() < xmin || pt.getLongitud() > xmax || pt.getLatitud() < ymin || pt.getLatitud() > ymax) {

                } else {
                    arr.add(node.getPoint());
                }

            }
        }, xmin, ymin, xmax, ymax);
        return arr.toArray(new PointLatLng[arr.size()]);
    }

    public PointLatLng[] searchWithin(final double xmin, final double ymin, final double xmax, final double ymax) {
        final List<PointLatLng> arr = new ArrayList<PointLatLng>();
        this.navigate(this.root_, new Func() {
            @Override
            public void call(QuadTree quadTree, Node node) {
                PointLatLng pt = node.getPoint();
                if (pt.getLongitud() > xmin && pt.getLongitud() < xmax && pt.getLatitud() > ymin && pt.getLatitud() < ymax) {
                    arr.add(node.getPoint());
                }
            }
        }, xmin, ymin, xmax, ymax);
        return arr.toArray(new PointLatLng[arr.size()]);
    }

    public void navigate(Node node, Func func, double xmin, double ymin, double xmax, double ymax) {
        switch (node.getNodeType()) {
            case LEAF:
                func.call(this, node);
                break;

            case POINTER:
                if (intersects(xmin, ymax, xmax, ymin, node.getNe()))
                    this.navigate(node.getNe(), func, xmin, ymin, xmax, ymax);
                if (intersects(xmin, ymax, xmax, ymin, node.getSe()))
                    this.navigate(node.getSe(), func, xmin, ymin, xmax, ymax);
                if (intersects(xmin, ymax, xmax, ymin, node.getSw()))
                    this.navigate(node.getSw(), func, xmin, ymin, xmax, ymax);
                if (intersects(xmin, ymax, xmax, ymin, node.getNw()))
                    this.navigate(node.getNw(), func, xmin, ymin, xmax, ymax);
                break;
        }
    }

    private boolean intersects(double left, double bottom, double right, double top, Node node) {
        return !(node.getX() > right ||
                (node.getX() + node.getW()) < left ||
                node.getY() > bottom ||
                (node.getY() + node.getH()) < top);
    }

   /* public QuadTree clone() {
        double x1 = this.root_.getX();
        double y1 = this.root_.getY();
        double x2 = x1 + this.root_.getW();
        double y2 = y1 + this.root_.getH();
        final QuadTree clone = new QuadTree(x1, y1, x2, y2);
        //clon para recalcular por si lo necesitas para algo
        this.traverse_(this.root_, new Func() {
            @Override
            public void call(QuadTree quadTree, Node node) {
                clone.set(node.getPoint().getLongitud(), node.getPoint().getLatitud(), node.getPoint().getValue());
            }
        });


        return clone;
    }*/


    public void traverse_(Node node, Func func) {
        switch (node.getNodeType()) {
            case LEAF:
                func.call(this, node);
                break;

            case POINTER:
                this.traverse_(node.getNe(), func);
                this.traverse_(node.getSe(), func);
                this.traverse_(node.getSw(), func);
                this.traverse_(node.getNw(), func);
                break;
        }
    }

    public Node find_(Node node, double x, double y) {
        Node respuesta = null;
        switch (node.getNodeType()) {
            case EMPTY:
                break;

            case LEAF:
                respuesta = node.getPoint().getLongitud() == x && node.getPoint().getLatitud() == y ? node : null;
                break;

            case POINTER:
                respuesta = this.find_(this.getQuadrantForPoint_(node, x, y), x, y);
                break;

            default:
                throw new QuadTreeException("nodeType erróneo");
        }
        return respuesta;
    }

    private boolean insert_(Node parent, PointLatLng point) {
        Boolean result = false;
        switch (parent.getNodeType()) {
            case EMPTY:
                this.setPointForNode_(parent, point);
                result = true;
                break;
            case LEAF:
                if (parent.getPoint().getLongitud() == point.getLongitud() && parent.getPoint().getLatitud() == point.getLatitud()) {
                    this.setPointForNode_(parent, point);
                    result = false;
                } else {
                    this.split_(parent);
                    result = this.insert_(parent, point);
                }
                break;
            case POINTER:
                result = this.insert_(
                        this.getQuadrantForPoint_(parent, point.getLongitud(), point.getLatitud()), point);
                break;

            default:
                throw new QuadTreeException("nodeType inválido en el padre");
        }
        return result;
    }

    private void split_(Node node) {
        PointLatLng oldPoint = node.getPoint();
        node.setPoint(null);

        node.setNodeType(NodeType.POINTER);

        double x = node.getX();
        double y = node.getY();
        double hw = node.getW() / 2;
        double hh = node.getH() / 2;

        node.setNw(new Node(x, y, hw, hh, node));
        node.setNe(new Node(x + hw, y, hw, hh, node));
        node.setSw(new Node(x, y + hh, hw, hh, node));
        node.setSe(new Node(x + hw, y + hh, hw, hh, node));

        this.insert_(node, oldPoint);
    }

    private void balance_(Node node) {
        switch (node.getNodeType()) {
            case EMPTY:
            case LEAF:
                if (node.getParent() != null) {
                    this.balance_(node.getParent());
                }
                break;

            case POINTER: {
                Node nw = node.getNw();
                Node ne = node.getNe();
                Node sw = node.getSw();
                Node se = node.getSe();
                Node firstLeaf = null;


                if (nw.getNodeType() != NodeType.EMPTY) {
                    firstLeaf = nw;
                }
                if (ne.getNodeType() != NodeType.EMPTY) {
                    if (firstLeaf != null) {
                        break;
                    }
                    firstLeaf = ne;
                }
                if (sw.getNodeType() != NodeType.EMPTY) {
                    if (firstLeaf != null) {
                        break;
                    }
                    firstLeaf = sw;
                }
                if (se.getNodeType() != NodeType.EMPTY) {
                    if (firstLeaf != null) {
                        break;
                    }
                    firstLeaf = se;
                }

                if (firstLeaf == null) {

                    node.setNodeType(NodeType.EMPTY);
                    node.setNw(null);
                    node.setNe(null);
                    node.setSw(null);
                    node.setSe(null);

                } else if (firstLeaf.getNodeType() == NodeType.POINTER) {
                    break;

                } else {
                    node.setNodeType(NodeType.LEAF);
                    node.setNw(null);
                    node.setNe(null);
                    node.setSw(null);
                    node.setSe(null);
                    node.setPoint(firstLeaf.getPoint());
                }

                if (node.getParent() != null) {
                    this.balance_(node.getParent());
                }
            }
            break;
        }
    }

    private Node getQuadrantForPoint_(Node parent, double x, double y) {
        double mx = parent.getX() + parent.getW() / 2;
        double my = parent.getY() + parent.getH() / 2;
        if (x < mx) {
            return y < my ? parent.getNw() : parent.getSw();
        } else {
            return y < my ? parent.getNe() : parent.getSe();
        }
    }

    private void setPointForNode_(Node node, PointLatLng point) {
        if (node.getNodeType() == NodeType.POINTER) {
            throw new QuadTreeException("no hay del tipo POINTER");
        }
        node.setNodeType(NodeType.LEAF);
        node.setPoint(point);
    }
}

