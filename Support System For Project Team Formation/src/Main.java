import java.util.*;

public class Main
{
    static fuzzyLogic toolBox = new fuzzyLogic();
    public static void takeInput()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter number of input variables: ");
        int numOfVar = input.nextInt();
        System.out.println("Please enter the name of each variable followed by the number that corresponds the shape of it's fuzzy sets. 1 for Trapezoidal, 2 for Triangle.");
        String varName;
        int shapeOfSet;
        double crispVal;
        for (int i = 0; i < numOfVar; i++)
        {
            varName = input.next();
            shapeOfSet = input.nextInt();
            if(shapeOfSet == 1)
            {
                toolBox.addVariable(varName,"INPUT", "TRAP");
            }else
            {
                toolBox.addVariable(varName,"INPUT", "TRI");
            }
            System.out.println("Enter the crisp value for the " + varName + " variable: ");
            crispVal = input.nextInt();
            toolBox.getVariable(varName).setCrispValue(crispVal);
        }

    }
    public static void runFuzzyAlgo()
    {
        toolBox.fuzzification();
        toolBox.inferencing();
        double y = toolBox.defuzzfication();
        String r = toolBox.getAppropriateRange(y);

        System.out.println("Predicted Value: " + y);
        System.out.println("and it will be: " + r);
    }
    public static void main(String[] args)
    {
        takeInput();
        /** for adding a fuzzy sets for each variable please use the same name you've entered before **/
        toolBox.getVariable("project_funding").addFuzzySet("VL",Arrays.asList(new Double[]{0.0,0.0,10.0,30.0}));
        toolBox.getVariable("project_funding").addFuzzySet("L" ,Arrays.asList(new Double[]{10.0,30.0,40.0,60.0}));
        toolBox.getVariable("project_funding").addFuzzySet("M" ,Arrays.asList(new Double[]{40.0,60.0,70.0,90.0}));
        toolBox.getVariable("project_funding").addFuzzySet("H" ,Arrays.asList(new Double[]{70.0,90.0,100.0,100.0}));

        toolBox.getVariable("team_experience_level").addFuzzySet("B",Arrays.asList(new Double[]{0.0,15.0,30.0}));
        toolBox.getVariable("team_experience_level").addFuzzySet("I",Arrays.asList(new Double[]{15.0,30.0,45.0}));
        toolBox.getVariable("team_experience_level").addFuzzySet("E",Arrays.asList(new Double[]{30.0,60.0,60.0}));

        /** Here you enter the details of output variable **/
        toolBox.addVariable("risk","OUTPUT", "TRI");
        toolBox.getVariable("risk").addFuzzySet("H",Arrays.asList(new Double[]{0.0,25.0,50.0}));
        toolBox.getVariable("risk").addFuzzySet("N",Arrays.asList(new Double[]{25.0,50.0,75.0}));
        toolBox.getVariable("risk").addFuzzySet("L",Arrays.asList(new Double[]{50.0,100.0,100.0}));

        /** Adding rules of the system **/
        toolBox.addRule("project_funding H or team_experience_level E -> risk L");
        toolBox.addRule("project_funding M and team_experience_level I or team_experience_level B -> risk N");
        toolBox.addRule("project_funding VL -> risk H");
        toolBox.addRule("project_funding L and team_experience_level B -> risk H");

        runFuzzyAlgo();
    }
}
