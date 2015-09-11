/*
 * My SpaceInvaders
 */
package spaceinvaders;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

public class SpaceInvaders extends JFrame implements Runnable {
    static final int XBORDER = 30;
    static final int YBORDER = 30;
    static final int YTITLE = 30;
    static final int WINDOW_BORDER = 8;
    static final int WINDOW_WIDTH = 2*(WINDOW_BORDER + XBORDER) + 500;
    static final int WINDOW_HEIGHT = YTITLE + WINDOW_BORDER + 2 * YBORDER + 500;
    
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;

    int cannonxpos;
    int cannonypos;
   
    int indexcannonball = 0;
    int numcannonballs = 250;
    int cannonballxpos[] = new int[numcannonballs];
    int cannonballypos[] = new int[numcannonballs];
    boolean cannonballvisible[] = new boolean[numcannonballs];
    int cannonballhits[] = new int[numcannonballs];
    
    int numaliens = 10;
    int alienrany[] = new int[numaliens];
    int alienranx[] = new int[numaliens];
    int alienoscx[] = new int[numaliens];
    boolean alienvisible[] = new boolean[numaliens];
    int scoreval;
    int alienscore[] = new int[numaliens];
    int alienspeedtime;
    int alienspeed;
    int alienfalltime;
    
    int alienexplodey[] = new int[numaliens];
    int alienexplodex[] = new int[numaliens];
    boolean alienexplodevisible[] = new boolean[numaliens];
    int alienexplodetime[] = new int[numaliens];
    double alienexplodescale[] = new double[numaliens];
    
    
    int level = 1;
    int highscore;
    
    boolean gameover;
    boolean toptextvis;
    
    static SpaceInvaders frame;
    public static void main(String[] args) {
        frame = new SpaceInvaders();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public SpaceInvaders() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button
                    if (gameover == true)
                        return;
                    
                        cannonballvisible[indexcannonball] = true;
                        cannonballxpos[indexcannonball] = cannonxpos;
                        cannonballypos[indexcannonball] = cannonypos;
                        indexcannonball++;
               
                        if (indexcannonball>=numcannonballs)
                            indexcannonball = 0;
                        
                        cannonballhits[indexcannonball] = 0;
// location of the cursor.
                    int xpos = e.getX();
                    int ypos = e.getY();

                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
          if (gameover == true)
            return;
          cannonxpos = e.getX() - getX(0);
        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_UP == e.getKeyCode()) {
                } else if (e.VK_DOWN == e.getKeyCode()) {
                } else if (e.VK_LEFT == e.getKeyCode()) {
                } else if (e.VK_RIGHT == e.getKeyCode()) {
                } else if (e.VK_C == e.getKeyCode()) {
                     if (gameover == true)
                        return;
                    
                        cannonballvisible[indexcannonball] = true;
                        cannonballxpos[indexcannonball] = cannonxpos;
                        cannonballypos[indexcannonball] = cannonypos;
                        indexcannonball++;
               
                        if (indexcannonball>=numcannonballs)
                            indexcannonball = 0;
                        cannonballhits[indexcannonball] = 0;
                }
                
                repaint();
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }



////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        } 
        
        Color cannon = new Color(72, 168, 89);
        Color darkgreenish = new Color(58, 192, 44);   
        Color greenish = new Color(190, 239, 44);

//fill background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        
        g.setColor(greenish);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(darkgreenish);
        g.drawPolyline(x, y, 5);
       
        
        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }
        
        if(toptextvis)
        {
        g.setColor(cannon);
        drawCannon(getX(cannonxpos),getYNormal(cannonypos),0,1,1);
        g.setColor(cannon);
        drawHand(getX(cannonxpos - 18), getYNormal(cannonypos-5),-25,2,2);
        drawHand(getX(cannonxpos + 18), getYNormal(cannonypos-5), 25,2,2);
        
        g.setColor(greenish); 
        g.setFont(new Font("Old English Text MT",Font.PLAIN,30));
        g.drawString("Score: " + scoreval, getX(20),getYNormal(getHeight2()+5)); 
       
        g.setColor(greenish); 
        g.setFont(new Font("Old English Text MT",Font.PLAIN,30));
        g.drawString("Level: " + level, getX(200),getYNormal(getHeight2()+5)); 
      
        g.setColor(greenish); 
        g.setFont(new Font("Old English Text MT",Font.PLAIN,30));
        g.drawString("Highscore: " + highscore, getX(330),getYNormal(getHeight2()+5)); 
        }
        
        for (int index=0;index<numaliens;index++)
        {
            if (alienvisible[index])
            drawGoat(getX(alienranx[index]), getYNormal(alienrany[index]),0,2.5,2.5,alienscore[index]);
        }
       
        for (int index=0;index<numcannonballs;index++)
        {
            if (cannonballvisible[index])
            {
                g.setColor(Color.red);
                drawCircle(getX(cannonballxpos[index]), getYNormal(cannonballypos[index]),0,.25,.5);    
            }
        }
        
        for (int index=0;index<numaliens;index++)
        {
            if(alienexplodevisible[index])
                drawCircle(getX(alienexplodex[index]),getYNormal(alienexplodey[index]),0,.4+alienexplodescale[index],.4+alienexplodescale[index]);
        }
        
        
        if (gameover)
        {
            g.setColor(Color.cyan); 
            g.setFont(new Font("Old English Text MT",Font.PLAIN,50));
            g.drawString("GAME OVER", getX(getWidth2() / 5),getYNormal(getHeight2() /2)); 
            
            g.setColor(Color.cyan); 
            g.setFont(new Font("Old English Text MT",Font.PLAIN,30));
            g.drawString("Score: " + scoreval, getX(getWidth2() *1/5),getYNormal(getHeight2() /2 -30)); 
            
            g.setColor(Color.cyan); 
            g.setFont(new Font("Old English Text MT",Font.PLAIN,30));
            g.drawString("Highscore: " + highscore, getX(getWidth2() *3/5),getYNormal(getHeight2() /2 -30)); 
            
            g.setColor(Color.cyan); 
            g.setFont(new Font("Old English Text MT",Font.PLAIN,25));
            g.drawString("Level: " + level, getX(getWidth2() *3/7),getYNormal(getHeight2() /2 -55)); 
        }
        gOld.drawImage(image, 0, 0, null);
    }

////////////////////////////////////////////////////////////////////////////
    public void drawCannon(int xpos,int ypos,double rot,double xscale,double yscale)
    {
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
       
        int xval[] = {  0, 10, 13,13, 0,-13,-13,-10,  0};
        int yval[] = {-15,-10, 20,30,35, 30, 20,-10,-15};
        g.fillPolygon(xval,yval,xval.length);
        
        g.setColor(Color.WHITE);
        g.fillOval(-8, -8, 7, 7);
        g.fillOval(2, -8, 7, 7);
        g.setColor(Color.BLACK);
        g.fillOval(-6, -8, 2, 2);
        g.fillOval(4, -8, 2, 2);
        
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
           ////////////////////////////////////////////////////////////////////////////
    public void drawGoat(int xpos,int ypos,double rot,double xscale,double yscale, int alienscore)
    {
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
        
         for (int index=0;index<numaliens;index++)
        {
            if(alienscore == 5)
                g.setColor(Color.MAGENTA);
            else if(alienscore < 4)
                g.setColor(Color.GRAY);
            else
                g.setColor(Color.WHITE);
        }
       
        int xval[] = {-1, 4, 6, 7,  9, 10, 11, 12, 12, 14, 15, 17,17,15,13,11,10,
            9, 8,9,10,11,9,8,7,6,5,5, 4, 3,3,2,1,1, 0,-2,-2,-1,-1,-3,-5,-6,-7,-7,-5,-5,-7};
        int yval[] = {-4,-6,-6,-9,-11,-12,-14,-14,-12,-12,-11,-10,-9,-8,-7,-6,-4,
            -2,-1,0, 3, 6,8,6,4,1,1,7,10,10,4,5,7,9,11,12, 8, 6, 5, 5, 8, 9, 9, 6, 4, 2,-1};
        g.fillPolygon(xval,yval,xval.length);
        
        
        g.setColor(Color.WHITE);
        g.fillOval(12,-11, 2, 2);
        g.setColor(Color.BLACK);
        g.fillOval(13,-11, 1, 1);
        
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
    ////////////////////////////////////////////////////////////////////////////
    public void drawHand(int xpos,int ypos,double rot,double xscale,double yscale)
    {
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        int xval[] ={ 0,-1,-2,-2,-1, 0,1, 2,2,1, 0};
        int yval[] ={15, 2, 1,-6, 0,-5,0,-6,1,2,15};

        
        g.fillPolygon(xval, yval, xval.length);
         
        g.fillRect(-1, 5, 2, 4);

        
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
   } 
////////////////////////////////////////////////////////////////////////////
    public void drawCircle(int xpos,int ypos,double rot,double xscale,double yscale)
    {
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );
       
        g.setColor(Color.red);
        g.fillOval(-10,-10,20,20);
     
        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
      

////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 0.04;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {
    
    cannonypos = 0;
    
    for (int index = 0;index<numcannonballs;index++) 
    {
    cannonballvisible[index] = false;
    cannonballxpos[index] = 0;
    cannonballypos[index] = 0;
    }
   
    for (int index = 0;index<numaliens;index++) 
    {
        alienranx[index] = (int)(Math.random()*getWidth2());
        alienrany[index] = (int)(Math.random()*getHeight2()/2 + getHeight2());
        alienvisible[index] = true;
        alienoscx[index] = (int)(Math.random()*2);
        alienscore[index] = (int)(Math.random()*5 + 1);
        
        if (alienoscx[index] < 1)
            alienoscx[index] = -1;
        
        alienexplodevisible[index] = false;
        alienexplodetime[index] = 0;
    }
    
    alienspeed = 2;
    scoreval = 0;
    gameover = false;
    toptextvis = true;
    level = 1;
    
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {
        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }
         
            reset();
            
            cannonxpos = getWidth2()/2;
        }
        if (gameover == true)
            return;
        
        
 //cannonball movement       
        for (int index = 0;index<numcannonballs;index++) 
        { 
            if (cannonballvisible[index])
                  cannonballypos[index]+=10;
     
            if (cannonballypos[index] >= getHeight2())
                  cannonballvisible[index] = false;
        }

//alien movement
        alienspeedtime++;
        for (int index = 0;index<numaliens;index++) 
        {
               if(alienspeedtime > 150)
            {
                alienspeedtime = 0;
                alienspeed++;
                level++;
            }
               
            alienrany[index] -=alienspeed;
            
            alienranx[index] += (alienoscx[index] * alienspeed);
            if (alienranx[index]>getWidth2())
                alienoscx[index] = -1;
            if (alienranx[index]<0)
                alienoscx[index] = 1;
        } 
        
//alien hit       
        for (int count = 0;count<numcannonballs;count++) 
        {
             for (int index = 0;index<numaliens;index++) 
             {
                  if(alienranx[index]-18 < cannonballxpos[count] && alienranx[index]+45 > cannonballxpos[count]
                  && alienrany[index]-20 < cannonballypos[count] && alienrany[index]+20 > cannonballypos[count]
                  && alienvisible[index] && cannonballvisible[count])
                  {    
                      scoreval += alienscore[index];
                      alienexplodex[index] = alienranx[index];
                      alienexplodey[index] = alienrany[index];
                      alienexplodevisible[index] = true;
                      alienexplodescale[index] = .1;
                      alienrany[index] = (int)(Math.random()*getHeight2()/3 + getHeight2());
                      alienranx[index] = (int)(Math.random()*getWidth2());
                      alienscore[index] = (int)(Math.random()*5 + 1);
                      cannonballhits[count]++;
                  }
             }    
        }
 //cannonball hits until invisible      
        for (int count = 0;count<numcannonballs;count++) 
        {
            if(cannonballhits[count] > 1)
                cannonballvisible[count] = false;
        }
 
//alien explosions          
         for (int index = 0;index<numaliens;index++) 
         {
             if(alienexplodevisible[index])
             {
             alienexplodetime[index]++;

             if(alienexplodetime[index]<=7)
                 alienexplodescale[index]+=.2;
             
             if(alienexplodetime[index]>10)
                 alienexplodescale[index]-=.2;
             
             if(alienexplodetime[index]>15)
                 alienexplodevisible[index] = false;
             }
         }   

//gameover
        for (int index = 0;index<numaliens;index++) 
        {
            if (alienrany[index] < 0 && alienvisible[index])
               gameover = true; 
        }

        if(gameover)
            toptextvis = false;
        
        if(highscore < scoreval)
            highscore = scoreval;
      }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
    public int getX(int x) {
        return (x + XBORDER + WINDOW_BORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE );
    }

    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    
    public int getWidth2() {
        return (xsize - 2 * (XBORDER + WINDOW_BORDER));
    }

    public int getHeight2() {
        return (ysize - 2 * YBORDER - WINDOW_BORDER - YTITLE);
    }
}
