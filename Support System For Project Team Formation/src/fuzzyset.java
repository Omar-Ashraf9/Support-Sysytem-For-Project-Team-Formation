import java.util.Vector;

public class fuzzyset
{
    private Vector<point> points;
    private double memberShipValue;
    private String name;

    public fuzzyset()
    {
        points = new Vector<point>();
    }
    public fuzzyset(String n , Vector<point> v)
    {
        this.points = v;
        this.name = n;
    }
    public Vector<point> getPoints()
    {
        return points;
    }

    public void setPoints(Vector<point> points)
    {
        this.points = points;
    }

    public double getMemberShipValue()
    {
        return memberShipValue;
    }

    public void setMemberShipValue(double memberShipValue)
    {
        this.memberShipValue = memberShipValue;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}
