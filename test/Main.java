package test;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File; import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jnativehook.GlobalScreen; import org.jnativehook.NativeHookException; import org.jnativehook.keyboard.NativeKeyEvent; import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;

public class Main implements NativeKeyListener, NativeMouseMotionListener, NativeMouseListener, ClipboardOwner{

	boolean a= false;
	boolean w= false;
	
	static int initialx;
	static int initialy;
	
	static int finalx;
	static int finaly;
	
	static int length;
	static int width;
	
	BufferedImage entireScreen = null;
	
	static boolean screenshotTaken = false;
	

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
//	    if (e.getKeyCode() == NativeKeyEvent.VC_A) {
//	        a = true;
//	        if (w) {
//	            System.out.println("W+A");
//	        } else {//remove this else only for testing
//	            System.out.println("Only A");
//	        }
//	    } else if (e.getKeyCode() == NativeKeyEvent.VC_W) {
//	        w = true;
//	        if (a) {
//	            System.out.println("A+W");
//	        } else {//remove this else only for testing
//	            System.out.println("Only W");
//	        }
//	    }
		
		if(e.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
			GlobalScreen.addNativeMouseMotionListener(new Main());
			GlobalScreen.addNativeMouseListener(new Main());
			System.out.println("Enable mouse listeners");
			JFrame frame = new JFrame("Screen");
		    
		    //frame.setUndecorated(true);
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		    frame.setVisible(true);
		    
		    
		    
		    try {
		    Robot robot = new Robot();
		    Rectangle screen = new Rectangle( Toolkit.getDefaultToolkit().getScreenSize());
		    entireScreen = robot.createScreenCapture( screen );
		    JPanel pane = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(entireScreen, 0, 0, null);
                }
            };
            
            frame.add(pane);
		    }catch(AWTException x) {
		    	System.exit(1);
		    }
		    
            
		}
		
		else if(e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			GlobalScreen.removeNativeMouseMotionListener(new Main());
			GlobalScreen.removeNativeMouseListener(new Main());
			
			System.out.println("removing mouse listener");
			
		}
		
		
	}
	

	
	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
			
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		
	}
	

	public void screenshot() {
		try {
			Thread.sleep(120); 
            Robot r = new Robot(); 
  
            // It saves screenshot to desired path 
            String path = "/home/jon/Pictures/ScreenShotApp/"; 
            Rectangle capture = null;
            if(finalx<initialx) {
            	
            	length = initialx-finalx;
            	
            	if(finaly<initialy) {
            		width = initialy - finaly;
            		capture = new Rectangle(finalx,finaly , length, width);
            	}else if(finaly>initialy) {
            		width = finaly - initialy;
            		capture = new Rectangle(finalx, finaly-width , length, width);
            	}
            }else if(finalx>initialx) {
            	
            	length = finalx-initialx;
            	
            	if(finaly<initialy) {
            		width = initialy-finaly;
            		capture = new Rectangle( initialx, initialy-width , length, width);
            	}else if(finaly > initialy) {
            		width = finaly - initialy;
            		capture = new Rectangle(initialx,initialy , length, width);
            	}
            		
            }
            
            // Used to get ScreenSize and capture image 
            //Rectangle capture = new Rectangle(initialx,initialy, finalx-initialx, finaly-initialy); 
            BufferedImage Image = r.createScreenCapture(capture); 
            
            TransferableImage trans = new TransferableImage( Image );
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents( trans, this );
            System.out.println("added to clipboard");
            Date date = Calendar.getInstance().getTime();  
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            File outputfile = new File(path+strDate);
            
            ImageIO.write(Image, "jpg", outputfile); 
            System.out.println("Screenshot saved"); 
            screenshotTaken = true;
            GlobalScreen.removeNativeMouseMotionListener(new Main());
            GlobalScreen.removeNativeMouseListener(new Main());
			}catch (AWTException | IOException | InterruptedException ex) { 
	            System.out.println(ex); 
	            
	        } 
	}


	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
		
		// TODO Auto-generated method stu
		
		//System.out.println("Mouse Dragged: " + e.getX() + ", " + e.getY());
			SelectionRectangle rect = new SelectionRectangle();
			finalx = e.getX();
			finaly = e.getY();
		
			System.out.println("initialx "+initialx + " "+ "initialy "+initialy+"finalx "+finalx + " "+ "finaly "+finaly);
			
			//System.out.println(initialx + " "+ " "+initialy + " "+ (finalx-initialx) + " "+ (finaly-initialy));
		
	}
	
	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		if(!screenshotTaken) {
        GlobalScreen.removeNativeMouseMotionListener(new Main());
        GlobalScreen.removeNativeMouseListener(new Main());
        
		screenshot();
        
		System.out.println("unpressed");
		}
	}
	

	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {
		// TODO Auto-generated method stub
			
			initialx = e.getX();
			initialy = e.getY();
			//System.out.println("initialx "+initialx + " "+ "initialy "+initialy);
			
	}
	
	public static void main(String[] args)throws Exception{
		try {
			LogManager.getLogManager().reset();
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);
			GlobalScreen.registerNativeHook();
			/////////////////////////////////////////////////////////

            
			
			
		}catch(NativeHookException ex) {
			System.out.println("error");
			System.exit(1);
		}
		
		
		GlobalScreen.addNativeKeyListener(new Main());
		
		
	}



	@Override
	public void nativeMouseClicked(NativeMouseEvent nativeEvent) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void nativeMousePressed(NativeMouseEvent nativeEvent) {
		// TODO Auto-generated method stub
		System.out.println("pressed");
	}



	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		// TODO Auto-generated method stub
		System.out.println("lost ownership");
	}

	
}
