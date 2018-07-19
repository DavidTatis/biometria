/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometria;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author David
 */
public class FingerPrintImage {
    int[][] FingerPrintImage;
    int width;
    int height;
    

    public FingerPrintImage(int width, int height) {
        this.FingerPrintImage=new int[width][height];
        this.width=width;
        this.height=height;
    }
    
    public void ConvertiraGris(BufferedImage img){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb=img.getRGB(i,j);
                int r= (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                int gris= (int) (0.2126*r + 0.7152*g + 0.0722*b);
                this.FingerPrintImage[i][j]=gris;
            }
        }
    }
    
    public void setPixel(int x, int y, int valor){
        this.FingerPrintImage[x][y]=valor;
    }
    
    public void Draw(JPanel panel){
       Graphics g=panel.getGraphics();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                 g.setColor(new Color(this.FingerPrintImage[i][j],this.FingerPrintImage[i][j],this.FingerPrintImage[i][j]));
                 g.fillRect(i, j, 1, 1);
            }
        }
    }
    
    
    public float getValorUmbral(){

        float valor=0;
        for (int i = 50; i < width-50; i++) {
            for (int j = 50; j < height-50; j++) {
                valor+=FingerPrintImage[i][j];
            }
        }
        valor=valor/((width*height)-200);
        return valor;
    }
    
    public void DrawBinary(JPanel panel){
       Graphics g=panel.getGraphics();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                 g.setColor(new Color(this.FingerPrintImage[i][j]*255,this.FingerPrintImage[i][j]*255,this.FingerPrintImage[i][j]*255));
                 g.fillRect(i, j, 1, 1);
            }
        }
    }
    
    public void InvertirBinario(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (FingerPrintImage[i][j]==1) {
                    this.FingerPrintImage[i][j]=0;
                }else{
                    this.FingerPrintImage[i][j]=1;
                }
                
            }
        }
    }

    public void setFingerPrintImage(int[][] FingerPrintImage) {
        this.FingerPrintImage = FingerPrintImage;
    }
    
    public void filtroBinario1(){
        int[][] copia=this.FingerPrintImage;
        for (int x = 1; x < width-1; x++) {
            for (int y = 1; y < height-1; y++) {
                 boolean a=false,b=false,c=false,d=false,e=false,f=false,g=false,h=false,p=false;
                if (FingerPrintImage[x-1][y-1]==1) {
                    a=true;
                }
                if (FingerPrintImage[x][y-1]==1) {
                    b=true;
                }
                if (FingerPrintImage[x+1][y-1]==1) {
                    c=true;
                }
                if (FingerPrintImage[x-1][y]==1) {
                    d=true;
                }
                if (FingerPrintImage[x+1][y]==1) {
                    e=true;
                }
                if (FingerPrintImage[x-1][y+1]==1) {
                    f=true;
                }
                if (FingerPrintImage[x][y+1]==1) {
                    g=true;
                }
                if (FingerPrintImage[x+1][y+1]==1) {
                    h=true;
                }
                if (FingerPrintImage[x][y]==1) {
                    p=true;
                }
                p=p ||( b&&g&&(d||e)||d&&e&&(b||g));
                if (p) {
                    copia[x][y]=1;
                }else{
                    copia[x][y]=0;
                }
        
            }
        }
        
        this.FingerPrintImage=copia;
       
    }
    
       public void DetectarMinutias(JPanel panel, JList jList){
         
           ArrayList<String> minutiaTipo1=new ArrayList<String>();
           ArrayList<String> minutiaTipo3=new ArrayList<String>();
           ArrayList<Double> anguloTipo1=new ArrayList<Double>();
           ArrayList<Double> anguloTipo3=new ArrayList<Double>();
           DefaultListModel<String> model = new DefaultListModel<>();
           
          //Pair<Pair<ArrayList<String>,ArrayList<Double>>,Pair<ArrayList<String>,ArrayList<Double>>> minutiaYangulos;
           
           
           
         
        for (int x = 1; x < width-1; x++) {
            for (int y = 1; y < height-1; y++) {
                 int p1,p2,p3,p4,p5,p6,p7,p8,p9,p;
                 p=FingerPrintImage[x][y];
                 if (p==1) {
                    
                
                p4=FingerPrintImage[x-1][y-1];
                p3=FingerPrintImage[x][y-1];
                p2=FingerPrintImage[x+1][y-1];
                p5=FingerPrintImage[x-1][y];
                p1=FingerPrintImage[x+1][y];
                p6=FingerPrintImage[x-1][y+1];
                p7=FingerPrintImage[x][y+1];
                p8=FingerPrintImage[x+1][y+1];
                int suma=Math.abs(p1-p2)+Math.abs(p2-p3)+Math.abs(p3-p4)+Math.abs(p4-p5)+Math.abs(p5-p6)+Math.abs(p6-p7)+Math.abs(p7-p8)+Math.abs(p8-p1);
                
                suma=suma/2;
                
                    if(suma==1){
                        Graphics g=panel.getGraphics();
                        System.out.println("punto de terminacion: "+x+","+y);
                        g.setColor(Color.BLUE);
                        g.fillRect(x-2, y-2, 4, 4);
                        minutiaTipo1.add(x+","+y);
                        anguloTipo1.add(CalcularAnguloMinutiaTipo1(x, y));
                        
                        
                    }
                    if (suma==3) {
                        Graphics g=panel.getGraphics();
                        System.out.println("punto de bifurcacion: "+x+","+y);
                        g.setColor(Color.GRAY);
                        g.fillRect(x-2, y-2, 4, 4);
                        double a1=0,a2=0,a3=0,a4=0,a5=0,a6=0,a7=0,a8=0;
                        if (FingerPrintImage[x-1][y-1]==1) {
                            a1=CalcularAnguloMinutiaTipo2(x-1, y-1,x,y);
                        }
                        if (FingerPrintImage[x][y-1]==1) {
                            a2=CalcularAnguloMinutiaTipo2(x, y-1,x,y);
                        }
                        if (FingerPrintImage[x+1][y-1]==1) {
                            a3=CalcularAnguloMinutiaTipo2(x+1, y-1,x,y);
                        }
                        if (FingerPrintImage[x-1][y]==1) {
                            a4=CalcularAnguloMinutiaTipo2(x-1,y,x,y);
                        }
                        if (FingerPrintImage[x+1][y]==1) {
                            a5=CalcularAnguloMinutiaTipo2(x+1, y,x,y);
                        }
                        if (FingerPrintImage[x-1][y+1]==1) {
                            a6=CalcularAnguloMinutiaTipo2(x-1, y+1,x,y);
                        }
                        if(FingerPrintImage[x][y+1]==1){
                            a7=CalcularAnguloMinutiaTipo2(x, y+1,x,y);
                        }
                        if(FingerPrintImage[x+1][y+1]==1){
                            a8=CalcularAnguloMinutiaTipo2(x+1, y+1,x,y);
                        }
                        double anguloPromedio=(a1+a2+a3+a4+a5+a6+a7+a8)/3;
                        System.out.println("angulo promedio:"+anguloPromedio);
                         minutiaTipo3.add(x+","+y);
                        anguloTipo3.add(anguloPromedio); 
                    }
                }
        
            }
        }
        
        //mostrar minutias tipo3:
           for (int i = 0; i < minutiaTipo3.size(); i++) {
            model.add(0,minutiaTipo3.get(i)+" angulo: "+anguloTipo3.get(i));
           }
        model.add(0,"**************Minutias tipo 3:**********************");
        
           for (int i = 0; i < minutiaTipo1.size(); i++) {
            model.add(0,minutiaTipo1.get(i)+" angulo: "+ anguloTipo1.get(i));
           }
        model.add(0,"******************Minutias tipo 1:*************************");
        jList.setModel(model);
        
    }
       
//       si se encuentra una minutia tipo 1:
//	se recorren 6 pixeles siguientes a esta arista
//	si no hay 6 seguidos?
//	si antes del sexto viene otra minutia tipo bifur?
//	
//    si se encuentra una minutia tipo 3:
//            se recorren las 3 aristas y se hacen los calculos igual que arriba
       private double CalcularAnguloMinutiaTipo1(int x, int y){
           double angulo=0;
           int adyacentes=0;
           ArrayList<String> vistos=new ArrayList<String>();
           vistos.add(new String(x+","+y));
           int copiaX=x;
           int copiaY=y;
           while (adyacentes<6){
               int copia=adyacentes;
               if(FingerPrintImage[x-1][y-1]==1 && !vistos.contains(new String((x-1)+","+(y-1)))){
                  vistos.add(new String((x-1)+","+(y-1)));
                  x=x-1;
                  y=y-1;
                  adyacentes++;
               }
               if(FingerPrintImage[x][y-1]==1 && !vistos.contains(new String((x)+","+(y-1)))){
                  vistos.add(new String((x)+","+(y-1)));
                   x=x;
                  y=y-1;
                  adyacentes++;
               }
               if(FingerPrintImage[x+1][y-1]==1 && !vistos.contains(new String((x+1)+","+(y-1)))){
                   vistos.add(new String((x+1)+","+(y-1)));
                   x=x+1;
                  y=y-1;
                  adyacentes++;
               }
               if(FingerPrintImage[x-1][y]==1 && !vistos.contains(new String((x-1)+","+(y)))){
                   vistos.add(new String((x-1)+","+(y)));
                   x=x-1;
                  y=y;
                  adyacentes++;
               }
               if(FingerPrintImage[x+1][y]==1 && !vistos.contains(new String((x+1)+","+(y)))){
                   vistos.add(new String((x-1)+","+(y-1)));
                   x=x-1;
                  y=y-1;
                  adyacentes++;
               }
               if(FingerPrintImage[x-1][y+1]==1 && !vistos.contains(new String((x-1)+","+(y+1)))){
                   vistos.add(new String((x-1)+","+(y+1)));
                   x=x-1;
                  y=y+1;
                  adyacentes++;
               }
               if(FingerPrintImage[x][y+1]==1 && !vistos.contains(new String((x)+","+(y+1)))){
                   vistos.add(new String((x)+","+(y+1)));
                   x=x;
                  y=y+1;
                  adyacentes++;
               }
               if(FingerPrintImage[x+1][y+1]==1 && !vistos.contains(new String((x+1)+","+(y+1)))){
                  vistos.add(new String((x+1)+","+(y+1)));
                   x=x+1;
                  y=y+1;
                  adyacentes++;
               }
               if (adyacentes==copia) {
                   break;
               }
           }
           if(adyacentes==6){
               int gx=x-copiaX;
               int gy=y-copiaY;
               if(gx==0){
                   angulo=90;
               }else{
                   angulo=Math.toDegrees(Math.atan(gy/gx));
               }
               
               System.out.println("angulo: "+angulo);
           }    
           
           
           return angulo;
       }
       
       
       private boolean alrededorMinutia(int x, int y, int x2, int y2){
           ArrayList<String> original=new ArrayList<String>();
           original.add((x2-1)+","+(y2-1));
           original.add((x2)+","+(y2-1));
           original.add((x2+1)+","+(y2-1));
           original.add((x2-1)+","+(y2));
           original.add((x2+1)+","+(y2));
           original.add((x2-1)+","+(y2+1));
           original.add((x2)+","+(y2+1));
           original.add((x2+1)+","+(y2+1));
           original.add(x2+","+y2);
           System.out.println(original.get(1));
           if (original.contains(x+","+y)) {
               return false;
           }else return true;
           
           
       }
       private double CalcularAnguloMinutiaTipo2(int x, int y,int xvisto, int yvisto){
           double angulo=0;
           int adyacentes=0;
           ArrayList<String> vistos=new ArrayList<String>();
           vistos.add(xvisto+","+yvisto);
           vistos.add(x+","+y);
           int copiaX=x;
           int copiaY=y;
           
           while (adyacentes<6){
               int copia=adyacentes;
               if(FingerPrintImage[x-1][y-1]==1 && !vistos.contains(new String((x-1)+","+(y-1)))){
                  vistos.add(new String((x-1)+","+(y-1)));
                  x=x-1;
                  y=y-1;
                  adyacentes++;
               }
               if(FingerPrintImage[x][y-1]==1 && !vistos.contains(new String((x)+","+(y-1)))){
                  vistos.add(new String((x)+","+(y-1)));
                   x=x;
                  y=y-1;
                  adyacentes++;
               }
               if(FingerPrintImage[x+1][y-1]==1 && !vistos.contains(new String((x+1)+","+(y-1)))){
                   vistos.add(new String((x+1)+","+(y-1)));
                   x=x+1;
                  y=y-1;
                  adyacentes++;
               }
               if(FingerPrintImage[x-1][y]==1 && !vistos.contains(new String((x-1)+","+(y)))){
                   vistos.add(new String((x-1)+","+(y)));
                   x=x-1;
                  y=y;
                  adyacentes++;
               }
               if(FingerPrintImage[x+1][y]==1 && !vistos.contains(new String((x+1)+","+(y)))){
                   vistos.add(new String((x-1)+","+(y-1)));
                   x=x-1;
                  y=y-1;
                  adyacentes++;
               }
               if(FingerPrintImage[x-1][y+1]==1 && !vistos.contains(new String((x-1)+","+(y+1)))){
                   vistos.add(new String((x-1)+","+(y+1)));
                   x=x-1;
                  y=y+1;
                  adyacentes++;
               }
               if(FingerPrintImage[x][y+1]==1 && !vistos.contains(new String((x)+","+(y+1)))){
                   vistos.add(new String((x)+","+(y+1)));
                   x=x;
                  y=y+1;
                  adyacentes++;
               }
               if(FingerPrintImage[x+1][y+1]==1 && !vistos.contains(new String((x+1)+","+(y+1)))){
                  vistos.add(new String((x+1)+","+(y+1)));
                   x=x+1;
                  y=y+1;
                  adyacentes++;
               }
               if (adyacentes==copia) {
                   break;
               }
           }
           if(adyacentes==6){
               int gx=x-copiaX;
               int gy=y-copiaY;
               if(gx==0){
                   angulo=90;
               }else{
                   angulo=Math.toDegrees(Math.atan(gy/gx));
               }
               System.out.print("gx:"+gx+","+"gy:"+gy);
               System.out.println("angulo: "+angulo+" - "+x+","+y);
           }    
           
           
           
           return angulo;
       }
    
     public void filtroBinario2(){
        int[][] copia=this.FingerPrintImage;
        for (int x = 1; x < width-1; x++) {
            for (int y = 1; y < height-1; y++) {
                 boolean a=false,b=false,c=false,d=false,e=false,f=false,g=false,h=false,p=false;
                if (FingerPrintImage[x-1][y-1]==1) {
                    a=true;
                }
                if (FingerPrintImage[x][y-1]==1) {
                    b=true;
                }
                if (FingerPrintImage[x+1][y-1]==1) {
                    c=true;
                }
                if (FingerPrintImage[x-1][y]==1) {
                    d=true;
                }
                if (FingerPrintImage[x+1][y]==1) {
                    e=true;
                }
                if (FingerPrintImage[x-1][y+1]==1) {
                    f=true;
                }
                if (FingerPrintImage[x][y+1]==1) {
                    g=true;
                }
                if (FingerPrintImage[x+1][y+1]==1) {
                    h=true;
                }
                if (FingerPrintImage[x][y]==1) {
                    p=true;
                }
                p=p&&(((a||b||d)&&(e||g||h))||((b||c||e)&&(d||f||g)));
                if (p) {
                    copia[x][y]=1;
                }else{
                    copia[x][y]=0;
                }
        
            }
        }
        
        this.FingerPrintImage=copia;
       
    }
    
    public int getPixel(int x, int y){
        return FingerPrintImage[x][y];
    }
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    
    
    
}
