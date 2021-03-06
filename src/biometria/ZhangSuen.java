package biometria;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.*;
import javax.swing.JPanel;
 
public class ZhangSuen {
    
    int[][] grid;
    
    final static int[][] nbrs = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1},
        {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};
 
    final static int[][][] nbrGroups = {{{0, 2, 4}, {2, 4, 6}}, {{0, 2, 6},
        {0, 4, 6}}};
 
    static List<Point> toWhite = new ArrayList<>();

    public ZhangSuen(int[][] grid) {
        this.grid = grid;
    }
    
 
   
 
     int[][] thinImage() {
        boolean firstStep = false;
        boolean hasChanged;
 
        do {
            hasChanged = false;
            firstStep = !firstStep;
 
            for (int r = 1; r < grid.length - 1; r++) {
                for (int c = 1; c < grid[0].length - 1; c++) {
 
                    if (grid[r][c] != 1)
                        continue;
 
                    int nn = numNeighbors(r, c);
                    if (nn < 2 || nn > 6)
                        continue;
 
                    if (numTransitions(r, c) != 1)
                        continue;
 
                    if (!atLeastOneIsWhite(r, c, firstStep ? 0 : 1))
                        continue;
 
                    toWhite.add(new Point(c, r));
                    hasChanged = true;
                }
            }
 
            for (Point p : toWhite)
                grid[p.y][p.x] = 0;
            toWhite.clear();
 
        } while (firstStep || hasChanged);
        return grid;
    }
 
     int numNeighbors(int r, int c) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (grid[r + nbrs[i][1]][c + nbrs[i][0]] == 1)
                count++;
        return count;
    }
 
     int numTransitions(int r, int c) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (grid[r + nbrs[i][1]][c + nbrs[i][0]] == 0) {
                if (grid[r + nbrs[i + 1][1]][c + nbrs[i + 1][0]] == 1)
                    count++;
            }
        return count;
    }
 
     boolean atLeastOneIsWhite(int r, int c, int step) {
        int count = 0;
        int[][] group = nbrGroups[step];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < group[i].length; j++) {
                int[] nbr = nbrs[group[i][j]];
                if (grid[r + nbr[1]][c + nbr[0]] == 0) {
                    count++;
                    break;
                }
            }
        return count > 1;
    }
    
    public void DrawBinary(JPanel panel,int width,int height){
       Graphics g=panel.getGraphics();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                 g.setColor(new Color(grid[i][j]*255,grid[i][j]*255,grid[i][j]*255));
                 g.fillRect(i, j, 1, 1);
            }
        }
    }
    
    
}