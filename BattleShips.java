import java.util.*;

public class BattleShips {
    public static int numRows = 10;
    public static int numCols = 10;
    public static int kapalAmerika;
    public static int kapalChina;
    public static String[][] grid = new String[numRows][numCols];
    public static int[][] missedGuesses = new int[numRows][numCols];

    public static void main(String[] args){
        System.out.println("**** selamat Datang di game BATTLESHIP ****");
        System.out.println("saat ini, laut masih kosong\n");

        //Step 1 – Create the ocean map
        membuatMaps();

        //Step 2 – Deploy player’s ships
        menyebarKapalAmerika();

        //Step 3 - Deploy computer's ships
        menyebarKapalChina();

        //Step 4 Battle
        do {
            Battle();
        }while(BattleShips.kapalAmerika != 0 && BattleShips.kapalChina != 0);

        //Step 5 - Game over
        gameOver();
    }

    public static void membuatMaps(){
        //First section of Ocean Map
        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();

        //Middle section of Ocean Map
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = " ";
                if (j == 0)
                    System.out.print(i + "|" + grid[i][j]);
                else if (j == grid[i].length - 1)
                    System.out.print(grid[i][j] + "|" + i);
                else
                    System.out.print(grid[i][j]);
            }
            System.out.println();
        }

        //Last section of Ocean Map
        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();
    }

    public static void menyebarKapalAmerika(){
        Scanner input = new Scanner(System.in);

        System.out.println("\nSebar kapal Amerika:");
        //Deploying five ships for player
        BattleShips.kapalAmerika = 5;
        for (int i = 1; i <= BattleShips.kapalAmerika; ) {
            System.out.print("masukkan koordinat x  kapal " + i + ": ");
            int x = input.nextInt();
            System.out.print("masukkan koordinat y  kapal " + i + ": ");
            int y = input.nextInt();

            if((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && (grid[x][y] == " "))
            {
                grid[x][y] =   "@";
                i++;
            }
            else if((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && grid[x][y] == "@")
                System.out.println("You can't place two or more ships on the same location");
            else if((x < 0 || x >= numRows) || (y < 0 || y >= numCols))
                System.out.println("You can't place ships outside the " + numRows + " by " + numCols + " grid");
        }
        tampilanMaps();
    }

    public static void menyebarKapalChina(){
        System.out.println("\nChina menyebar kapalnya");
        //Deploying five ships for computer
        BattleShips.kapalChina = 5;
        for (int i = 1; i <= BattleShips.kapalChina; ) {
            int x = (int)(Math.random() * 10);
            int y = (int)(Math.random() * 10);

            if((x >= 0 && x < numRows) && (y >= 0 && y < numCols) && (grid[x][y] == " "))
            {
                grid[x][y] =   "x";
                System.out.println(i + ". MENYEBAR kapal");
                i++;
            }
        }
        tampilanMaps();
    }

    public static void Battle(){
        playerTurn();
        computerTurn();

        tampilanMaps();

        System.out.println();
        System.out.println("kapal Amerika: " + BattleShips.kapalAmerika + " | kapal China: " + BattleShips.kapalChina);
        System.out.println();
    }

    public static void playerTurn(){
        System.out.println("\ngiliran Amerika untuk menembak");
        int x = -1, y = -1;
        do {
            Scanner input = new Scanner(System.in);
            System.out.print("Masukkan koordinat X yang mau di tembak : ");
            x = input.nextInt();
            System.out.print("Masukkan koordinat Y yang mau di tembak : ");
            y = input.nextInt();

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols)) //valid guess
            {
                if (grid[x][y] == "x") //if computer ship is already there; computer loses ship
                {
                    System.out.println("Duarrr! Amerika menenggelamkan salah satu kapal China");
                    grid[x][y] = "!"; //Hit mark
                    --BattleShips.kapalChina;
                }
                else if (grid[x][y] == "@") {
                    System.out.println("Astuget, Amerika menembak kapalnya sendiri wkwkwkwk");
                    grid[x][y] = "!";
                    --BattleShips.kapalAmerika;

                }
                else if (grid[x][y] == " ") {
                    System.out.println("Maaf, Amerika kurang beruntung karena salah sasaran");
                    grid[x][y] = "-";
                }
            }
            else if ((x < 0 || x >= numRows) || (y < 0 || y >= numCols))  //invalid guess
                System.out.println("You can't place ships outside the " + numRows + " by " + numCols + " grid");
        }while((x < 0 || x >= numRows) || (y < 0 || y >= numCols));  //keep re-prompting till valid guess
    }

    public static void computerTurn(){
        System.out.println("\nChina menembak");
        //Guess co-ordinates
        int x = -1, y = -1;
        do {
            x = (int)(Math.random() * 10);
            y = (int)(Math.random() * 10);

            if ((x >= 0 && x < numRows) && (y >= 0 && y < numCols)) //valid guess
            {
                if (grid[x][y] == "@") //if player ship is already there; player loses ship
                {
                    System.out.println("China meneggelamkan salah satu kapal Amerika");
                    grid[x][y] = "!";
                    --BattleShips.kapalAmerika;
                }
                else if (grid[x][y] == "x") {
                    System.out.println("China menenggelamkan salah satu kapalnya sendiri wkwkwk");
                    grid[x][y] = "!";
                }
                else if (grid[x][y] == " ") {
                    System.out.println("China salah sasaran");
                    //Saving missed guesses for computer
                    if(missedGuesses[x][y] != 1)
                        missedGuesses[x][y] = 1;
                }
            }
        }while((x < 0 || x >= numRows) || (y < 0 || y >= numCols));  //keep re-prompting till valid guess
    }

    public static void gameOver(){
        System.out.println("Kapal Amerika: " + BattleShips.kapalAmerika + " | Kapal China: " + BattleShips.kapalChina);
        if(BattleShips.kapalAmerika > 0 && BattleShips.kapalChina <= 0)
            System.out.println("Hore! Amerika menang dalam Battle");
        else
            System.out.println("Hore! China menang dalam Battle");
        System.out.println();
    }

    public static void tampilanMaps(){
        System.out.println();
        //First section of Ocean Map
        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();

        //Middle section of Ocean Map
        for(int x = 0; x < grid.length; x++) {
            System.out.print(x + "|");

            for (int y = 0; y < grid[x].length; y++){
                System.out.print(grid[x][y]);
            }

            System.out.println("|" + x);
        }

        //Last section of Ocean Map
        System.out.print("  ");
        for(int i = 0; i < numCols; i++)
            System.out.print(i);
        System.out.println();
    }
}

