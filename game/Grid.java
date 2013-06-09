package game;

class Grid
{
    private final int width, height;
    private final boolean[][] grid;

    Grid(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.grid = new boolean[height][width];
        clear();
    }

    boolean get(Point point)
    {
        if (inBounds(point))
            return grid[point.row][point.col];
        else
            return false;
    }

    void clear()
    {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                grid[i][j] = false;
    }

    boolean inPlay(Point point)
    {
        return point.col >= 0 && point.row < height && point.col < width;
    }

    boolean inBounds(Point point)
    {
        return point.row >= 0 && inPlay(point);
    }

    boolean valid(Point... points)
    {
        for (Point p : points)
            if (!inPlay(p) || get(p))
                return false;

        return true;
    }

    void place(Point... points)
    {
        for (Point p : points)
            if (inBounds(p))
                grid[p.row][p.col] = true;
    }

    void erase(Point... points)
    {
        for (Point p : points)
            if (inBounds(p))
                grid[p.row][p.col] = false;
    }

    /* returns the number of lines destroyed */
    int destroyLines()
    {
        int counter = 0;

        for (int i = 0; i < height; i++)
            if (covered(i))
            {
                for (int j = 0; j < width; j++)
                {
                    for (int k = i; k > 0; k--)
                        grid[k][j] = grid[k - 1][j];

                    grid[0][j] = false;
                }

                counter++;
            }

        return counter;
    }

    private boolean covered(int row)
    {
        for (int i = 0; i < width; i++)
            if (!grid[row][i])
                return false;

        return true;
    }
}
