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

import java.util.Random;

/**
 *
 * @author Lorenzo Garrone
 */
public class Casella 
{
    final boolean Spawna_caramelle;
    
    private final int Caramella_assente=0;
    private final int Caramella_blu=1;
    private final int Caramella_gialla=2;
    private final int Caramella_rossa=3;
    private final int Caramella_verde=4;
    
    private int Caramella;
    
    Casella(boolean Spawna_caramelle)
    {
        this.Spawna_caramelle=Spawna_caramelle;
        if(Spawna_caramelle)
        {
            this.generaCaramella();
        }
        else
        {
            this.Caramella=this.Caramella_assente;
        }
        
    }
    Casella(int Caramella, boolean Spawna_caramelle)
    {
        this.Spawna_caramelle=Spawna_caramelle;
        this.Caramella=Caramella;
        
    }
    
    
    private int generaNumeroCasuale(int max)
    {
        int numero;
        
        Random random=new Random();
        numero=random.nextInt(max)+1;
        
        return numero;
    }
    
    public void generaCaramella()
    {
        this.Caramella=this.generaNumeroCasuale(4);
    }
    
    public void Rimuovi_Caramella()
    {
        this.Caramella=this.Caramella_assente;
    }
    
    public void Imposta_caramella(int Caramella)
    {
        this.Caramella=Caramella;
    }
    
    public int Restituisci_caramella()
    {
        return this.Caramella;
    }
}
