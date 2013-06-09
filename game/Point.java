package game;

class Point
{
    final int row, col;

    Point(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    Point translate(int vertical, int horizantal)
    {
        return new Point(row + vertical, col + horizantal);
    }

    Point turn(Point origin)
    {
        return new Point(col - origin.col + origin.row, -row + origin.row
                + origin.col);
    }
}
