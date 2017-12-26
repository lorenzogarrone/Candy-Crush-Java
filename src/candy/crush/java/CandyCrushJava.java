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

import javax.swing.JFrame;

/**
 *
 * @author Lorenzo Garrone
 */
public class CandyCrushJava {

    public static void main(String[] args) 
    {
        int NumeroCaramelleAltezza=10;
        int NumeroCaramelleLarghezza=10;
        JFrame finestra=new JFrame();
        Gioco gioco=new Gioco(NumeroCaramelleLarghezza, NumeroCaramelleAltezza);
        Thread threadGioco=new Thread(gioco);
        
        finestra.setBounds(0,0,NumeroCaramelleLarghezza*70+5,NumeroCaramelleAltezza*70-30);
        finestra.setTitle("Candy Crush Java - Lorenzo Garrone - lorenzogarrone2000@gmail.com - 4B Informatica Sarrocchi SI");
        finestra.setVisible(true);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setResizable(false);
        finestra.add(gioco);
        threadGioco.start();
        finestra.addMouseListener(gioco);
    }
    
}
