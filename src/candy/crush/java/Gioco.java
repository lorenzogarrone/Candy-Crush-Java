/*
 * This file is part of Candy Crush Java.
 *
 *  Candy Crush Java is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *    
 *  Candy Crush Java is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Candy Crush Java.  If not, see <http://www.gnu.org/licenses/>.
 */
package candy.crush.java;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Lorenzo Garrone
 */
public class Gioco extends JPanel implements Runnable, MouseListener
{
    /*
    private final AudioClip suonoCascata;
    private final AudioClip suonoEsplosione;
    */
    
    private BufferedImage[]Caramella_immagine={null, null, null, null, null};
    
    private int xMouse;
    private int yMouse;
    
    private final String percorso_immagine_caramella_blu="/Img/Blu.png";
    private final String percorso_immagine_caramella_gialla="/Img/Gialla.png";
    private final String percorso_immagine_caramella_rossa="/Img/Rossa.png";
    private final String percorso_immagine_caramella_verde="/Img/Verde.png";
    
    private final int Dimensione_caramella=70;
    //private final int Velocita_caduta;
    private final int Velocita_spostamento=4;
    
    private final int Caramella_assente=0;
    private final int Caramella_blu=1;
    private final int Caramella_gialla=2;
    private final int Caramella_rossa=3;
    private final int Caramella_verde=4;
    
    private final int direzione_destra=0;
    private final int direzione_sinistra=1;
    private final int direzione_sopra=2;
    private final int direzione_sotto=3;
    
    private int direzioneSpostamento=-1;
    
    private int xSpostata=-1;
    private int ySpostata=-1;
    private int CaramellaSpostata=-1;
    
    private int xMossa=-1;
    private int yMossa=-1;
    private int CaramellaMossa=-1;
    
    private Casella[][]Tabellone;
    private Casella[][]TabelloneCopia;
    
    private int Caramella_cascante=-1;
    private int x_caramella_cascante=-1;
    private int y_caramella_cascante=-1;
    
    Gioco(int NumeroCaramelleLarghezza, int NumeroCaramelleAltezza)
    {
        Tabellone=new Casella[NumeroCaramelleAltezza][NumeroCaramelleLarghezza];
        Caramella_immagine[this.Caramella_blu]=this.caricaImmagine(percorso_immagine_caramella_blu);
        Caramella_immagine[this.Caramella_gialla]=this.caricaImmagine(percorso_immagine_caramella_gialla);
        Caramella_immagine[this.Caramella_rossa]=this.caricaImmagine(percorso_immagine_caramella_rossa);
        Caramella_immagine[this.Caramella_verde]=this.caricaImmagine(percorso_immagine_caramella_verde);
        
        System.out.println("Sono nel costruttore, Fai gli spostmenti a sinistra, in alto e in basso, e perfeziona quello a destra");
        creaTabellone(Tabellone);
        
    }
    
    private void resetValoriCaramellaSpostate()
    {
        this.direzioneSpostamento=-1;
        
        this.xSpostata=-1;
        this.xMossa=-1;
        this.ySpostata=-1;
        this.yMossa=-1;
        this.CaramellaSpostata=-1;
        this.CaramellaMossa=-1;
    }
    
    private BufferedImage caricaImmagine (String PercorsoImmagine)
    {
        BufferedImage immagine=null;
        try {
            immagine=ImageIO.read(CandyCrushJava.class.getResourceAsStream(PercorsoImmagine));
        } catch (IOException ex) {
            System.out.println("Impossibile caricare l'immagine al percorso "+PercorsoImmagine);
        }
        return immagine;
    }
    
    private void creaTabellone(Casella[][]Tabellone)
    {
        for(int i=0;i<Tabellone.length;i++)
        {
            for(int j=0;j<Tabellone[0].length;j++)
            {
                Tabellone[i][j]=new Casella(i==0);
            }
        }
    }
    
    @Override
    public void paint(Graphics g)
    {
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        for(int i=1;i<Tabellone.length;i++)
        {
            for(int j=0;j<Tabellone[0].length;j++)
            {
                disegnaCaramella(this.Caramella_immagine[Tabellone[i][j].Restituisci_caramella()], (j*(this.Dimensione_caramella)), (i-1)*this.Dimensione_caramella, g);
            }
        }
        if(this.Caramella_cascante!=-1)
            disegnaCaramella(this.Caramella_immagine[this.Caramella_cascante], x_caramella_cascante, y_caramella_cascante-this.Dimensione_caramella, g);
        if(this.CaramellaSpostata!=-1&&this.CaramellaMossa!=-1)
        {
            disegnaCaramella(this.Caramella_immagine[this.CaramellaMossa], xMossa, yMossa, g);
            disegnaCaramella(this.Caramella_immagine[this.CaramellaSpostata], xSpostata, ySpostata, g);
        }
    }
    
    private void disegnaCaramella(BufferedImage Caramella, int x, int y, Graphics g)
    {
        if(Caramella==null)
        {
            g.setColor(Color.BLACK);
            g.fillRect(x, y, this.Dimensione_caramella, this.Dimensione_caramella);
        }
        else
        {
            g.fillRect(x, y, this.Dimensione_caramella, this.Dimensione_caramella);
            g.drawImage(Caramella, x, y, null);
        }
    }

    @Override
    public void run() 
    {
        while(true)
        {
            repaint();
            controllaTabellone(Tabellone);
            spawnaCaramelle(Tabellone);
            this.controllaEsplosione(Tabellone);
            
            spostaCaramelle();
        }
    }
    
    private void spostaCaramelle()
    {
        if(this.CaramellaSpostata!=-1&&this.CaramellaMossa!=-1)
        {
            if(this.direzioneSpostamento==this.direzione_destra)
            {
                int xSpostata=this.xSpostata;
                Tabellone[yMossa/this.Dimensione_caramella+1][xMossa/this.Dimensione_caramella].Rimuovi_Caramella();
                Tabellone[ySpostata/this.Dimensione_caramella+1][this.xSpostata/this.Dimensione_caramella].Rimuovi_Caramella();
                repaint();
                while(this.xMossa<xSpostata)
                {
                    this.xMossa++;
                    this.xSpostata--;
                    attendi(this.Velocita_spostamento);
                    repaint();
                }
                this.resetValoriCaramellaSpostate();
                Tabellone=TabelloneCopia;
            }
            
            else if(this.direzioneSpostamento==this.direzione_sinistra)
            {
                int xMossa=this.xMossa;
                Tabellone[yMossa/this.Dimensione_caramella+1][xMossa/this.Dimensione_caramella].Rimuovi_Caramella();
                Tabellone[ySpostata/this.Dimensione_caramella+1][this.xSpostata/this.Dimensione_caramella].Rimuovi_Caramella();
                repaint();
                while(this.xSpostata<xMossa)
                {
                    this.xMossa--;
                    this.xSpostata++;
                    attendi(this.Velocita_spostamento);
                    repaint();
                }
                this.resetValoriCaramellaSpostate();
                Tabellone=TabelloneCopia;
            }
            
            else if(this.direzioneSpostamento==this.direzione_sopra)
            {
                int yMossa=this.yMossa;
                Tabellone[yMossa/this.Dimensione_caramella+1][xMossa/this.Dimensione_caramella].Rimuovi_Caramella();
                Tabellone[ySpostata/this.Dimensione_caramella+1][this.xSpostata/this.Dimensione_caramella].Rimuovi_Caramella();
                repaint();
                while(this.ySpostata<yMossa)
                {
                    this.yMossa--;
                    this.ySpostata++;
                    attendi(this.Velocita_spostamento);
                    repaint();
                }
                this.resetValoriCaramellaSpostate();
                Tabellone=TabelloneCopia;
            }
            
            else if(this.direzioneSpostamento==this.direzione_sotto)
            {
                System.out.println("LIDL");
                int ySpostata=this.ySpostata;
                Tabellone[yMossa/this.Dimensione_caramella+1][xMossa/this.Dimensione_caramella].Rimuovi_Caramella();
                Tabellone[ySpostata/this.Dimensione_caramella+1][this.xSpostata/this.Dimensione_caramella].Rimuovi_Caramella();
                repaint();
                while(this.yMossa<ySpostata)
                {
                    System.out.println("Lidl");
                    this.yMossa++;
                    this.ySpostata--;
                    attendi(this.Velocita_spostamento);
                    repaint();
                }
                this.resetValoriCaramellaSpostate();
                Tabellone=TabelloneCopia;
            }
            
            
        }
    }
    
    private boolean controllaEsplosione(Casella[][] Tabellone)
    {
        if(this.controllaEsplosioneOrizzontale(Tabellone))
            return true;
        return this.controllaEsplosioneVerticale(Tabellone);
    }
    
    private boolean controllaEsplosioneVerticale(Casella[][]Tabellone)
    {
        boolean esploso=false;
        for(int i=1;i<=4;i++)
        {
            for(int j=0;j<Tabellone[0].length;j++)
            {
                int posIniziale=-1;
                int posFinale=-1;
                
                for(int k=1;k<Tabellone.length;k++)
                {
                    if(Tabellone[k][j].Restituisci_caramella()==i)
                    {
                        if(posIniziale==-1)
                        {
                            posIniziale=k;
                        }
                        posFinale=k;
                    }
                    else
                    {
                        if(posFinale-posIniziale>=2)
                        {
                            this.scoppiaCaramelleColonna(Tabellone, j, posIniziale, posFinale);
                            esploso=true;
                        }
                        posIniziale=-1;
                        posFinale=-1;
                    }
                    if(k==Tabellone.length-1)
                    {
                        if(posFinale-posIniziale>=2)
                        {
                            this.scoppiaCaramelleColonna(Tabellone, j, posIniziale, posFinale);
                            esploso=true;
                        }
                    }
                }
            }
        }
        return esploso;
    }
    
    private boolean controllaEsplosioneOrizzontale(Casella[][]Tabellone)
    {
        boolean esploso=false;
        
        for(int i=1;i<=4;i++)
        {
            for(int j=1;j<Tabellone.length;j++)
            {
                int posIniziale=-1;
                int posFinale=-1;
                
                for(int k=0;k<Tabellone[0].length;k++)
                {
                    if(Tabellone[j][k].Restituisci_caramella()==i)
                    {
                        if(posIniziale==-1)
                        {
                            posIniziale=k;
                        }
                        posFinale=k;
                    }
                    else
                    {
                        if(posFinale-posIniziale>=2)
                        {
                            this.scoppiaCaramelleFila(Tabellone, j, posIniziale, posFinale);
                            esploso=true;
                        }
                        posIniziale=-1;
                        posFinale=-1;
                    }
                    if(k==Tabellone[0].length-1)
                    {
                        if(posFinale-posIniziale>=2)
                        {
                            this.scoppiaCaramelleFila(Tabellone, j, posIniziale, posFinale);
                            esploso=true;
                        }
                    }
                }
            }
        }
        return esploso;
    }
    
    private void scoppiaCaramelleColonna(Casella Tabellone[][], int j, int posIniziale, int posFinale)
    {
        for(int k=posIniziale;k<=posFinale;k++)
        {
            Tabellone[k][j].Rimuovi_Caramella();
        }
    }
    
    private void scoppiaCaramelleFila(Casella Tabellone[][], int i, int posIniziale, int posFinale)
    {
        for(int k=posIniziale;k<=posFinale;k++)
        {
            Tabellone[i][k].Rimuovi_Caramella();
        }
    }
    
    
    private void spawnaCaramelle(Casella[][]Tabellone)
    {
        for(int j=0;j<Tabellone[0].length;j++)
        {
            if(Tabellone[0][j].Restituisci_caramella()==this.Caramella_assente)
            {
                Tabellone[0][j].generaCaramella();
            }
        }
    }
    
    private void controllaTabellone(Casella[][] Tabellone)
    {
        int Caramella;
        
        for(int i=Tabellone.length-1;i>=0;i--)
        {
            this.spawnaCaramelle(Tabellone);
            
            for(int j=0;j<Tabellone[0].length;j++)
            {
                if(Tabellone[i][j].Restituisci_caramella()==this.Caramella_assente)
                {
                    int k=i;
                    while(Tabellone[k][j].Restituisci_caramella()==this.Caramella_assente)
                    {
                        k--;
                    }
                    Caramella=Tabellone[k][j].Restituisci_caramella();
                    Tabellone[k][j].Imposta_caramella(this.Caramella_assente);
                    Caramella_casca(Caramella, k*this.Dimensione_caramella, i*this.Dimensione_caramella, j*this.Dimensione_caramella, i, j);
                }
            }
        }
    }
    
    private void Caramella_casca(int Caramella, int yIniziale, int yFinale, int x, int i, int j)
    {
        this.x_caramella_cascante=x;
        this.y_caramella_cascante=yIniziale;
        this.Caramella_cascante=Caramella;
        
        while(yIniziale<yFinale)
        {
            yIniziale+=2;
            this.y_caramella_cascante+=2;
            repaint();
            attendi(1);
        }
        
        Tabellone[i][j].Imposta_caramella(Caramella);
        this.x_caramella_cascante=-1;
        this.y_caramella_cascante=-1;
        this.Caramella_cascante=-1;
    }
    
    private void attendi(int millisecondi)
    {
        try {
            Thread.sleep(millisecondi);
        } catch (InterruptedException ex) {
            Logger.getLogger(Gioco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) 
    {}

    @Override
    public void mousePressed(MouseEvent e) 
    {
        int xMouse=e.getX();
        int yMouse=e.getY()+5;
        
        this.xMouse=xMouse;
        this.yMouse=yMouse;
        
        System.out.println(yMouse);
        
    }
    
    private void copiaTabellone(Casella[][] TabelloneCopia)
    {
        for(int i=0;i<Tabellone.length;i++)
        {
            for(int j=0;j<Tabellone[0].length;j++)
            {
                TabelloneCopia[i][j]=new Casella(Tabellone[i][j].Restituisci_caramella(), Tabellone[i][j].Spawna_caramelle);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {
        int xMouse=e.getX();
        int yMouse=e.getY()+5;
        
        if(xMouse-this.xMouse>=50)
        {
            Casella[][]TabelloneCopia=new Casella[Tabellone.length][Tabellone[0].length];
            copiaTabellone(TabelloneCopia);
            int Caramella=TabelloneCopia[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Restituisci_caramella();
            TabelloneCopia[yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Imposta_caramella(TabelloneCopia[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella+1].Restituisci_caramella());
            TabelloneCopia[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella+1].Imposta_caramella(Caramella);
            
            if(this.controllaEsplosione(TabelloneCopia))
            {
                this.direzioneSpostamento=this.direzione_destra;
                
                this.TabelloneCopia=TabelloneCopia;
                this.CaramellaSpostata=Tabellone[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella+1].Restituisci_caramella();
                this.CaramellaMossa=Tabellone[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Restituisci_caramella();
                this.xSpostata=(this.xMouse/this.Dimensione_caramella)*this.Dimensione_caramella+this.Dimensione_caramella;
                this.ySpostata=((this.yMouse/this.Dimensione_caramella)*this.Dimensione_caramella)-this.Dimensione_caramella;
                this.xMossa=(this.xMouse/this.Dimensione_caramella)*this.Dimensione_caramella;
                this.yMossa=((this.yMouse/this.Dimensione_caramella)*this.Dimensione_caramella)-this.Dimensione_caramella;
            }
        }
        
        else if(this.xMouse-xMouse>=50)
        {
            Casella[][]TabelloneCopia=new Casella[Tabellone.length][Tabellone[0].length];
            copiaTabellone(TabelloneCopia);
            
            int Caramella=TabelloneCopia[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Restituisci_caramella();
            TabelloneCopia[yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Imposta_caramella(TabelloneCopia[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella+1].Restituisci_caramella());
            TabelloneCopia[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella-1].Imposta_caramella(Caramella);
            
            if(this.controllaEsplosione(TabelloneCopia))
            {
                this.direzioneSpostamento=this.direzione_sinistra;
                
                this.TabelloneCopia=TabelloneCopia;
                this.CaramellaSpostata=Tabellone[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella-1].Restituisci_caramella();
                this.CaramellaMossa=Tabellone[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Restituisci_caramella();
                this.xSpostata=(this.xMouse/this.Dimensione_caramella)*this.Dimensione_caramella-this.Dimensione_caramella;
                this.ySpostata=((this.yMouse/this.Dimensione_caramella)*this.Dimensione_caramella)-this.Dimensione_caramella;
                this.xMossa=(this.xMouse/this.Dimensione_caramella)*this.Dimensione_caramella;
                this.yMossa=((this.yMouse/this.Dimensione_caramella)*this.Dimensione_caramella)-this.Dimensione_caramella;
            }
        }
        
        else if(this.yMouse-yMouse>=50)
        {
            Casella[][]TabelloneCopia=new Casella[Tabellone.length][Tabellone[0].length];
            copiaTabellone(TabelloneCopia);
            
            int Caramella=TabelloneCopia[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Restituisci_caramella();
            TabelloneCopia[yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Imposta_caramella(TabelloneCopia[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella+1].Restituisci_caramella());
            TabelloneCopia[this.yMouse/this.Dimensione_caramella-1][this.xMouse/this.Dimensione_caramella].Imposta_caramella(Caramella);
            
            if(this.controllaEsplosione(TabelloneCopia))
            {
                this.direzioneSpostamento=this.direzione_sopra;
                
                this.TabelloneCopia=TabelloneCopia;
                this.CaramellaSpostata=Tabellone[this.yMouse/this.Dimensione_caramella-1][this.xMouse/this.Dimensione_caramella].Restituisci_caramella();
                this.CaramellaMossa=Tabellone[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Restituisci_caramella();
                this.xSpostata=(this.xMouse/this.Dimensione_caramella)*this.Dimensione_caramella;
                this.ySpostata=((this.yMouse/this.Dimensione_caramella)*this.Dimensione_caramella)-this.Dimensione_caramella*2;
                this.xMossa=(this.xMouse/this.Dimensione_caramella)*this.Dimensione_caramella;
                this.yMossa=((this.yMouse/this.Dimensione_caramella)*this.Dimensione_caramella)-this.Dimensione_caramella;
            }
        }
        
        else if(yMouse-this.yMouse>=40)
        {
            Casella[][]TabelloneCopia=new Casella[Tabellone.length][Tabellone[0].length];
            copiaTabellone(TabelloneCopia);
            int Caramella=TabelloneCopia[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Restituisci_caramella();
            TabelloneCopia[yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Imposta_caramella(TabelloneCopia[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella+1].Restituisci_caramella());
            TabelloneCopia[this.yMouse/this.Dimensione_caramella+1][this.xMouse/this.Dimensione_caramella].Imposta_caramella(Caramella);
            
            if(this.controllaEsplosione(TabelloneCopia))
            {
                this.direzioneSpostamento=this.direzione_sotto;
                
                this.TabelloneCopia=TabelloneCopia;
                this.CaramellaSpostata=Tabellone[this.yMouse/this.Dimensione_caramella+1][this.xMouse/this.Dimensione_caramella].Restituisci_caramella();
                this.CaramellaMossa=Tabellone[this.yMouse/this.Dimensione_caramella][this.xMouse/this.Dimensione_caramella].Restituisci_caramella();
                this.xSpostata=(this.xMouse/this.Dimensione_caramella)*this.Dimensione_caramella;
                this.ySpostata=((this.yMouse/this.Dimensione_caramella)*this.Dimensione_caramella)-this.Dimensione_caramella+this.Dimensione_caramella;
                this.xMossa=(this.xMouse/this.Dimensione_caramella)*this.Dimensione_caramella;
                this.yMossa=((this.yMouse/this.Dimensione_caramella)*this.Dimensione_caramella)-this.Dimensione_caramella;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
