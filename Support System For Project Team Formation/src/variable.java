import java.util.*;

public class variable
{
    private String typeOfShape;
    private String name;
    private String typeOfVar;
    private Vector<fuzzyset> fuzzySets;
    private double crispValue;

    public variable(String n, String t, String sT)
    {
        name = n;
        typeOfVar = t;
        typeOfShape = sT;
        fuzzySets = new Vector<fuzzyset>();
    }

    public double getVal(String val)
    {
        /** return the membership value of the given fuzzy set name. **/
        double res = 0;
        for (int i = 0; i < fuzzySets.size(); i++)
        {
            if(fuzzySets.get(i).getName().equals(val))
            {
                res = fuzzySets.get(i).getMemberShipValue();
            }
        }
        return res;
    }
    public variable()
    {
        fuzzySets = new Vector<fuzzyset>();
    }

    public String getTypeOfShape()
    {
        return typeOfShape;
    }

    public String getName()
    {
        return name;
    }

    public String getTypeOfVar()
    {
        return typeOfVar;
    }

    public Vector<fuzzyset> getFuzzySets()
    {
        return fuzzySets;
    }
    public void addFuzzySet(String n , List<Double> val)
    {
        Vector<point> v = new Vector<point>();
        if(this.typeOfShape.equals("TRAP"))
        {
            v.add(new point(val.get(0),0.0));
            v.add(new point(val.get(1),1.0));
            v.add(new point(val.get(2),1.0));
            v.add(new point(val.get(3),0.0));
        }
        else
        {
            v.add(new point(val.get(0),0.0));
            v.add(new point(val.get(1),1.0));
            v.add(new point(val.get(2),0.0));
        }
        fuzzySets.add(new fuzzyset(n,v));
    }

    public void setCrispValue(double crispValue)
    {
        this.crispValue = crispValue;
    }

    public double getCrispValue()
    {
        return crispValue;
    }


}
