package view.view3D;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


public class ViewListener implements KeyListener, MouseMotionListener, MouseListener, MouseWheelListener {

    private View3D upper;
    private Point lastPosition = null;
    private ViewUpdate calculateur;
    	
	public ViewListener(ViewUpdate calc, View3D up) {
		upper = up;
		calculateur = calc;
	}

   public void keyPressed(KeyEvent e) {
	   if (e.isControlDown()) {
	       if (e.getKeyCode() == KeyEvent.VK_UP) {
	    	   calculateur.changeCameraPosition("lookUp");
	    	   upper.display();
	       }
	       if (e.getKeyCode() == KeyEvent.VK_DOWN) {
	    	   calculateur.changeCameraPosition("lookDown");
	    	   upper.display();
	       }
	       if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	    	   calculateur.changeCameraPosition("lookLeft");
	    	   upper.display();
	       }
	       if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	    	   calculateur.changeCameraPosition("lookRight");
	      	   upper.display();
	       }
	       if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
	    	   calculateur.changeCameraPosition("lookInto");
	    	   upper.display();
	       }
	       if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
	    	   calculateur.changeCameraPosition("lookOut");
	      	   upper.display();
	       }
	   } else {
	       if (e.getKeyCode() == KeyEvent.VK_UP) {
	    	   calculateur.changeCameraPosition("moveUp");
	    	   upper.display();
	       }
	       if (e.getKeyCode() == KeyEvent.VK_DOWN) {
	    	   calculateur.changeCameraPosition("moveDown");
	    	   upper.display();
	       }
	       if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	    	   calculateur.changeCameraPosition("moveLeft");
	    	   upper.display();
	       }
	       if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	    	   calculateur.changeCameraPosition("moveRight");
	      	   upper.display();
	       }
	       if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
	    	   calculateur.changeCameraPosition("moveInto");
	    	   upper.display();
	       }
	       if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
	    	   calculateur.changeCameraPosition("moveOut");
	      	   upper.display();
	       }
	   }
   }

   public void keyReleased(KeyEvent arg0) {   }

   public void keyTyped(KeyEvent arg0) {   }

   public void mouseDragged(MouseEvent arg0) {
	// Mouvement sur la page
	   Point pt = arg0.getPoint();
	// Descente en x
	   if (lastPosition.x > pt.x) {
		   for (int i = pt.x; i < lastPosition.x; i++) calculateur.changeCameraPosition("moveRight");
      	   upper.display();
	   }
		// Monte en x
	   if (lastPosition.x < pt.x) {
		   for (int i = lastPosition.x; i < pt.x; i++) calculateur.changeCameraPosition("moveLeft");
      	   upper.display();
	   }
		// Descente en y
	   if (lastPosition.y > pt.y) {
		   for (int i = pt.y; i < lastPosition.y; i++) calculateur.changeCameraPosition("moveDown");
      	   upper.display();
	   }
		// Monte en y
	   if (lastPosition.y < pt.y) {
		   for (int i = lastPosition.y; i < pt.y; i++) calculateur.changeCameraPosition("moveUp");
      	   upper.display();
	   }
	   lastPosition.setLocation(pt);
   }

   public void mouseMoved(MouseEvent arg0) {
   }

	public void mouseClicked(MouseEvent arg0) {
		//  Auto-generated method stub
		
	}
	
	public void mouseEntered(MouseEvent arg0) {
		//  Auto-generated method stub
		
	}
	
	public void mouseExited(MouseEvent arg0) {
		//  Auto-generated method stub
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		lastPosition = arg0.getPoint();
		
	}
	
	public void mouseReleased(MouseEvent arg0) {
		//  Auto-generated method stub
		
	}
	
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// G�re la profondeur de chant
		if (arg0.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
			for (int pos = arg0.getScrollAmount(); pos > 0 ; pos --) {
				if (arg0.getWheelRotation() == -1) calculateur.changeCameraPosition("moveInto");;
				if (arg0.getWheelRotation() == 1) calculateur.changeCameraPosition("moveOut");
			}
	      	upper.display();
		}
		
	}

}
