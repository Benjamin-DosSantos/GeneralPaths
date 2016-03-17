package com.blackpensoftware.generalpath;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.OutputStream;
import java.util.Vector;

public class SaveTests extends Applet implements ActionListener {
     
    private ImageCanvas canvas;
    private TextField filename;
    private Button save, clear;
     
    public void init() {
        setLayout( new BorderLayout() );
        canvas = new ImageCanvas();
        add( canvas, BorderLayout.CENTER );
        Panel p = new Panel();
        filename = new TextField();
        save = new Button( "Save" );
        save.addActionListener( this );
        clear = new Button( "Clear" );
        clear.addActionListener( this );
        p.setLayout( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = .70;
        p.add( filename, c );
        c.weightx = .15;
        p.add( save, c );
        c.weightx = .15;
        p.add( clear, c );
        add( p, BorderLayout.SOUTH );
    }
     
    public void actionPerformed(ActionEvent e) { 
        if ( e.getSource() == save ) {
            save.setEnabled( false );
            filename.setEnabled( false );
            filename.setEditable( false );
            Thread t = new Thread( new SaveButtonPressed() );
            t.start();
        } else if ( e.getSource() == clear ) {
            canvas.clear();
        }
    }
     
    private static class ImageCanvas extends Canvas implements MouseMotionListener {
        private Vector points;
         
        public ImageCanvas() {
            points = new Vector();
            addMouseMotionListener( this );
        }
         
        public void mouseDragged(MouseEvent e) {
            Point p = e.getPoint();
            points.add( p );
            repaint();
        }
         
        public void mouseMoved(MouseEvent e) {
        }
         
        public void clear() {
            points.removeAllElements();
            repaint();
        }
         
        public void paint( Graphics g ) {
            int size = points.size();
            g.setColor( Color.black );
            if ( size > 0 ) {
                Point p1 = (Point)points.get( 0 );
                for ( int i = 0; i < size; i++ ) {
                    Point p2 = (Point)points.get( i );
                    g.drawLine( p1.x, p1.y, p2.x, p2.y );
                    p1 = p2;
                }
            }
        }
    }
     
    private class SaveButtonPressed implements Runnable {
        public void run() {
            Thread t = new Thread( new SaveRunnable() );
            t.start();
            try {
                t.join();
            } catch( InterruptedException x ) { 
                x.printStackTrace();
            }
            finally {
                save.setEnabled( true );
                filename.setEnabled( true );
                filename.setEditable( true );
            }
        }
         
    }
     
    private class SaveRunnable implements Runnable {
        public void run() {
            int width = canvas.getSize().width;
            int height = canvas.getSize().height;
            ColorModel cm = canvas.getColorModel();
            int[] pixels = new int[ width * height ];
            Image image = canvas.createImage( width, height );
            Graphics g = image.getGraphics();
            canvas.paint( g );
            PixelGrabber pg = new PixelGrabber( image, 0, 0, width, height, pixels, 0, width );
            boolean success = false;
            try {
                success = pg.grabPixels();
            }
            catch (InterruptedException e2) {
                e2.printStackTrace();
                return;
            }
            if ( success ) {
                String mimeType = "invalid";
                String extension = filename.getText();
                int index = extension.indexOf( '.' );
                extension = extension.substring( index );
                if ( extension.equalsIgnoreCase( "GIF" ) ) {
                    mimeType = "image/gif";
                } else if ( extension.equalsIgnoreCase( "JPG" )  ||
                        extension.equalsIgnoreCase( "JPEG" ) ) {
                    mimeType = "image/jpeg";
                } else if ( extension.equalsIgnoreCase( "PNG" ) ) {
                    mimeType = "image/png";
                }
                 
                // You can add more options here for the different image types 
                // you want to support.
                 
                if ( mimeType.equals( "invalid" ) ) {
                    System.err.println( "Could not get a valid mime type from file extension!" );
                    return;
                } else {
                    try {
                        OutputStream imageOutput = null;
                         
                        // Do something here to get an OutputStream pointing to the socket, 
                        // URL of servlet, etc. that you are going to write your image to.
                         
                    } catch ( Exception x ) {
                        x.printStackTrace();
                        return;
                    }
                }
            } else {
                System.err.println( "PixelGrabber failed!" );
            }
        }
         
    }
}
