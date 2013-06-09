package game;

class Block
{
	static final Point[][] PENTAMINO_BLOCK_SET =
		{
			{new Point(0, 0), new Point(0, -1), new Point(0, -2), new Point(0, 1), new Point(0, 2)},
			{new Point(0, 0), new Point(0, -1), new Point(0, -2), new Point(0, 1), new Point(-1, 1)},
			{new Point(0, 0), new Point(0, -1), new Point(-1, -1), new Point(0, 1), new Point(0, 2)},
			{new Point(0, 0), new Point(0, -1), new Point(0, -2), new Point(0, 1), new Point(-1, 0)},
			{new Point(0, 0), new Point(0, -1), new Point(-1, 0), new Point(0, 1), new Point(0, 2)},
			{new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, 0), new Point(-2, 0)},
			{new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, 0), new Point(1, 0)},
			{new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, 0), new Point(-1, -1)},
			{new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, 0), new Point(-1, 1)},
			{new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(0, 1), new Point(-1, 1)},
			{new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(0, -1), new Point(1, 1)},
			{new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(-1, 1), new Point(1, -1)},
			{new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(-1, -1), new Point(1, 1)},
			{new Point(0, 0), new Point(0, -1), new Point(0, -2), new Point(-1, 0), new Point(-1, 1)},
			{new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(1, 0), new Point(1, 1)},
			{new Point(0, 0), new Point(0, -1), new Point(0, 1), new Point(-1, -1), new Point(-1, 1)},
			{new Point(0, 0), new Point(1, 0), new Point(1, -1), new Point(0, 1), new Point(-1, 1)},
			{new Point(0, 0), new Point(0, -1), new Point(0, -2), new Point(-1, 0), new Point(-2, 0)},
		};

    private final Point[] points;
    private Point[] current;

    Block(Point baseStart, Point... points)
    {
        this.points = new Point[points.length];

        for (int i = 0; i < points.length; i++)
            this.points[i] = points[i].translate(baseStart.row, baseStart.col);

        this.current = this.points;
    }

    Point getBase()
    {
        return current[0];
    }

    Point[] getCurrent()
    {
        return current;
    }

    void move(Point... points)
    {
        current = points;
    }

    Point[] down()
    {
        Point[] down = new Point[current.length];

        for (int i = 0; i < current.length; i++)
            down[i] = current[i].translate(1, 0);

        return down;
    }

    Point[] left()
    {
        Point[] left = new Point[current.length];

        for (int i = 0; i < current.length; i++)
            left[i] = current[i].translate(0, -1);

        return left;
    }

    Point[] right()
    {
        Point[] right = new Point[current.length];

        for (int i = 0; i < current.length; i++)
            right[i] = current[i].translate(0, 1);

        return right;
    }

    Point[] turn()
    {
        Point[] turn = new Point[current.length];

        for (int i = 0; i < current.length; i++)
            turn[i] = current[i].turn(current[0]);

        return turn;
    }
}
