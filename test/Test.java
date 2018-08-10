/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author roger
 */
public class Test
{
    String name = "First Class";
    int number = 1;

    public Test()
    {
    }

    public Test(String name,int number){
        this.name = name;
        this.number = number;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return new Test("Clone", 2);
    }

    public static void main(String[] args)
    {
        Test test = new Test();
        try
        {
            Test test2 = (Test) test.clone();
            System.out.println("Original");
            System.out.println(test.name);
            System.out.println(test.number);
            System.out.println();

            System.out.println("Clone");
            System.out.println(test2.name);
            System.out.println(test2.number);
            System.out.println();

            System.out.println("Change in original");
            test.name = "Change original";
            test.number = 11;

            System.out.println("Testing Clone");
            System.out.println(test2.name);
            System.out.println(test2.number);
        } catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
    }
}
