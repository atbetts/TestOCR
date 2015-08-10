import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.sun.javafx.geom.Point2D;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by abetts on 8/6/15.
 */
public class ImagePane extends WebcamPanel {
    ArrayList<Rectangle2D> textRegion = new ArrayList<>();


    public ImagePane(Webcam webcam){
        super(webcam);
       class Mouse implements MouseMotionListener,MouseListener{
           Point initPoint;

           @Override
           public void mouseClicked(MouseEvent e) {

           }

           @Override
           public void mousePressed(MouseEvent e) {
               initPoint = e.getPoint();
           }

           @Override
           public void mouseReleased(MouseEvent e) {
               if (initPoint != null) {
                   double x = initPoint.getX();
                   double y = initPoint.getY();
                   Rectangle2D r = new Rectangle2D.Float();
                   r.setFrameFromDiagonal(initPoint, e.getPoint());
                   textRegion.add(r);
               }
               initPoint = null;
               revalidate();
               repaint();
           }

           @Override
           public void mouseEntered(MouseEvent e) {

           }

           @Override
           public void mouseExited(MouseEvent e) {

           }

           @Override
           public void mouseDragged(MouseEvent e) {
               final Rectangle2D.Double e1 = new Rectangle2D.Double() {{
                   setFrameFromDiagonal(initPoint, e.getPoint());
               }};
               textRegion.add(e1);
               revalidate();
               repaint();
               textRegion.remove(e1);
               System.out.println("SQUARE");
           }

           @Override
           public void mouseMoved(MouseEvent e) {

           }
       }
        Mouse m = new Mouse();
        addMouseListener(m);
        addMouseMotionListener(m);
    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRects(g2d);

    }

    public void drawRects(Graphics2D g){
        Color c = g.getColor();
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.red);
            for(Rectangle2D r : textRegion)
                g.draw(r);
        g.setColor(c);
    }
}
