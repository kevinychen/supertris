package game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Scoreboard
{
    private static final String FILENAME = "highscores.dat";
    private static final int MAX_SIZE = 10;
    private ArrayList<Stat> stats;

    private Scoreboard()
    {
    };

    static Scoreboard getScoreboard() throws InputMismatchException
    {
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.stats = new ArrayList<Stat>();

        try
        {
            Scanner input = new Scanner(new File(FILENAME));
            while (input.hasNext())
                scoreboard.stats.add(new Stat(input.nextInt(), input.nextLine()
                        .trim()));
            input.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return scoreboard;
    }

    boolean makesList(int score)
    {
        return stats.size() < MAX_SIZE
                || score > stats.get(stats.size() - 1).score;
    }

    void add(String name, int score)
    {
        name = name.trim();

        for (int i = 0; i < stats.size(); i++)
            if (name.equalsIgnoreCase(stats.get(i).name))
                if (score > stats.get(i).score)
                    stats.remove(i);
                else
                    return;

        int i = 0;
        while (score < stats.get(i).score)
            i++;
        stats.add(i, new Stat(score, name));
    }

    void write() throws IOException
    {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
                FILENAME)));

        for (Stat stat : stats)
            writer.println(stat.score + " " + stat.name);

        writer.close();
    }
}