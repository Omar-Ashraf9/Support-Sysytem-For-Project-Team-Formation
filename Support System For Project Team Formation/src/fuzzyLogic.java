import java.util.*;

public class fuzzyLogic
{
    Vector <variable> variables = new Vector<variable>();
    Vector <String> rules = new Vector<String>();

    LinkedHashMap <String, Double> inferenceValues = new LinkedHashMap<String, Double>();
    Map <String, Double> centroidValues = new HashMap<String, Double>();

    public void addVariable(String n, String t, String sT)
    {
        variables.add(new variable(n,t,sT));
    }

    public variable getVariable(String target)
    {
        variable v = new variable();
        for (int i = 0; i < variables.size(); i++)
        {
            if(variables.get(i).getName().equals(target))
            {
                v = variables.get(i);
                break;
            }
        }
        return v;
    }

    public void addRule(String r)
    {
        rules.add(r);
    }

    public boolean check(String n)
    {
        for (int i = 0; i < variables.size(); i++)
        {
            if(variables.get(i).getName().equals(n))
                return true;
        }
        return false;
    }

    public double execOperations(Vector<Double> values , Vector<String> operations)
    {
        /** function to evaluate the rule given it's operands and it's operations. **/

        if(operations.size() == 0) /** No operation **/
            return values.get(0);

        double res = 0.0;
        int idx = 0;
        for (int i = 0; i < operations.size(); i++)
        {
            if(i == 0)
            {
                /*first operation is performed on first and second operands and save the result in a variable to execute the rest of the operations on it.*/
                /*idx is a pointer to the next number after the operand #2 */
                if(operations.get(i).equals("and"))
                {
                    res = Math.min(values.get(i),values.get(i+1));
                    idx = i+2;
                }else
                {
                    res = Math.max(values.get(i),values.get(i+1));
                    idx = i+2;
                }
            }else
            {
                if(operations.get(i).equals("and"))
                {
                    res = Math.min(res, values.get(idx));
                    idx++;
                }else
                {
                    res = Math.max(res, values.get(idx));
                    idx++;
                }
            }
        }
        return res;
    }
    /**  Calculate the slope of the given two points **/
    public double getSlope(point a , point b)
    {
        double dY = a.getY() - b.getY();
        double dX = a.getX() - b.getX();

        double y = (double) dY / dX;
        return y;
    }

    public double lineEqu(double s, point a, double crispVal)
    {
        /* y = ax + b */
        /* b = y - ax */
        double b = a.getY() - (s*a.getX());
        double y = (s*crispVal) + b;
        return y;
    }

    /** return the points that covers the crisp value in it's range **/
    public Vector<point> getNearest(Vector<point> p, double crisp)
    {
        Vector<point> res = new Vector<point>();
        for (int i = 0; i < p.size()-1; i++)
        {
            if(p.get(i).getX() <= crisp && p.get(i+1).getX() >= crisp)
            {
                res.add(p.get(i));
                res.add(p.get(i+1));
            }
        }
        return res;
    }

    public double calcCntriod(Vector<point> points , String type)
    {
        double sum = 0.0;
        for(int i = 0; i < points.size(); i++)
        {
            sum += points.get(i).getX();
        }
        if(type.equals("TRAP"))
        {
            return sum / 4.0;
        }else
        {
            return sum / 3.0;
        }
    }

    public double calcOutput()
    {
        double sum = 0.0;
        double deNom = 0.0;

        for (Map.Entry<String, Double> entry : inferenceValues.entrySet())
        {
            sum += entry.getValue() * centroidValues.get(entry.getKey());
            deNom += entry.getValue();
        }
        return sum / deNom;
    }

    public void printInference()
    {
        for (int i = 0; i < variables.size(); i++)
        {
            if(variables.get(i).getTypeOfVar().equals("OUTPUT"))
            {
                for (Map.Entry<String, Double> entry : inferenceValues.entrySet())
                {
                    System.out.println("The membership value of the " + variables.get(i).getName() + " variable in the set " +
                            entry.getKey() + " is: " + entry.getValue());
                }
            }
        }

    }

    public void printMembership()
    {
        for(int i = 0; i < variables.size(); i++)
        {
            if(variables.get(i).getTypeOfVar().equals("INPUT"))
            {
                for (int j = 0; j < variables.get(i).getFuzzySets().size(); j++)
                {
                    System.out.println("The membership value of the input " + variables.get(i).getCrispValue() + " for the "
                            + variables.get(i).getName() + " variable in the set " + variables.get(i).getFuzzySets().get(j).getName()
                            + " is: " + variables.get(i).getFuzzySets().get(j).getMemberShipValue());
                }
            }
        }
    }

    public void fuzzification()
    {
        for (int i = 0; i < variables.size(); i++)
        {
            if(variables.get(i).getTypeOfVar().equals("INPUT"))
            {
                for (int j = 0; j < variables.get(i).getFuzzySets().size(); j++)
                {
                    Vector<point> p = getNearest(variables.get(i).getFuzzySets().get(j).getPoints(),variables.get(i).getCrispValue());
                    if(p.size() == 0)
                    {
                        variables.get(i).getFuzzySets().get(j).setMemberShipValue(0.0);
                    }
                    else /* There is a points can cover the crisp value in it's range. */
                    {
                        double s = getSlope(p.get(0),p.get(1));
                        double y = lineEqu(s, p.get(0),variables.get(i).getCrispValue());
                        variables.get(i).getFuzzySets().get(j).setMemberShipValue(y);
                    }
                }
            }
        }
        printMembership();
    }

    public void inferencing()
    {
        for(int i = 0; i < rules.size(); i++)
        {
            Vector <Double> x = new Vector<Double>();
            Vector <String> ops = new Vector<String>();

            String[] splited = rules.get(i).split(" ");

            for(int j = 0; j < splited.length; j++)
            {
                if(splited[j].equals("->"))
                {
                    break;
                }else if(check(splited[j]))
                {
                    /** Get the membership value of the given fuzzy set name **/
                    x.add(getVariable(splited[j]).getVal(splited[j+1]));
                    j++;
                }else
                {
                    ops.add(splited[j]);
                }
            }
            if(inferenceValues.containsKey(splited[splited.length - 1]))
            {
                inferenceValues.put(splited[splited.length - 1], Math.max(inferenceValues.get(splited[splited.length - 1]),execOperations(x,ops)));
            }else
            {
                inferenceValues.put(splited[splited.length - 1], execOperations(x,ops));
            }
        }
        printInference();
    }

    public double defuzzfication()
    {
        /** Calculate the centroids. **/
        for (int i = 0; i < variables.size(); i++)
        {
            if(variables.get(i).getTypeOfVar().equals("OUTPUT"))
            {
                for (int j = 0; j < variables.get(i).getFuzzySets().size(); j++)
                {
                    centroidValues.put(variables.get(i).getFuzzySets().get(j).getName(), calcCntriod(variables.get(i).getFuzzySets().get(j).getPoints(),variables.get(i).getTypeOfShape()));
                }
            }
        }
        return calcOutput();
    }

    public String getAppropriateRange(double z)
    {
        double Min = Double.MAX_VALUE;
        String nearst = "";
        for (Map.Entry<String, Double> entry : centroidValues.entrySet())
        {
            double diff = Math.abs(z - entry.getValue());
            if(diff < Min)
            {
                nearst = entry.getKey();
                Min = diff;
            }
        }
        return nearst;
    }

}
