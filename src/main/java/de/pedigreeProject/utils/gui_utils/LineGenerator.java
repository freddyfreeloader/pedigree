package de.pedigreeProject.utils.gui_utils;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Line;

/**
 * Generator for a {@link Line} between {@link Node}s to draw in a target pane.
 * The lines starting and ending points are dynamically bounded on the nodes by listeners.
 */
public class LineGenerator {

    /**
     * The {@link Node} respectively the {@link javafx.scene.layout.Pane} where the line should be drawn.
     */
    private final Node targetNode;

    /**
     * @see LineGenerator
     */
    public LineGenerator(Node targetNode) {
        this.targetNode = targetNode;
    }

    /**
     * Generates a Line between the given Nodes, dynamically bounded by Listeners.<br>
     * The line starts at centerX/maxY of the parent Node, <br>
     * and ends at centerX/minY of the child Node.<br>
     * <p>The listeners are set to the given Nodes.</p>
     *
     * @return a line between the Nodes
     */
    public Line getLine(Node parent, Node child) {
        Line line = new Line();
        setStart(line, parent);
        setEnd(line, child);
        addListenerToNodes(line, parent, child);

        return line;
    }

    private void setStart(Line line, Node parent) {
        Bounds targetBoundsInScene = targetNode.localToScene(targetNode.getBoundsInLocal());
        Bounds boundsInScene = parent.localToScene(parent.getBoundsInLocal());

        double xParent = boundsInScene.getCenterX();
        double yParent = boundsInScene.getMaxY();

        double xTarget = targetBoundsInScene.getMinX();
        double yTarget = targetBoundsInScene.getMinY();

        line.setStartX(xParent - xTarget);
        line.setStartY(yParent - yTarget);
    }

    private void setEnd(Line line, Node child) {
        Bounds targetBoundsInScene = targetNode.localToScene(targetNode.getBoundsInLocal());
        Bounds boundsInScene = child.localToScene(child.getBoundsInLocal());

        double xChild = boundsInScene.getCenterX();
        double yChild = boundsInScene.getMinY();

        double xTarget = targetBoundsInScene.getMinX();
        double yTarget = targetBoundsInScene.getMinY();

        line.setEndX(xChild - xTarget);
        line.setEndY(yChild - yTarget);
    }

    private void addListenerToNodes(Line line, Node parent, Node child) {

        parent.localToParentTransformProperty().addListener((observable, oldValue, newValue) -> setStart(line, parent));
        parent.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> setStart(line, parent));
        parent.localToSceneTransformProperty().addListener((observable, oldValue, newValue) -> setStart(line, parent));

        child.localToParentTransformProperty().addListener((observable, oldValue, newValue) -> setEnd(line, child));
        child.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> setEnd(line, child));
        child.localToSceneTransformProperty().addListener((observable, oldValue, newValue) -> setEnd(line, child));
    }
}
