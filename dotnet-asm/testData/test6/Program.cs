using System;

class Program
{
    static void Main()
    {
        Console.Write("Hello world!");
    }
}

interface A
{

}

interface B
{

}

interface C : A, B
{

}

class Test
{
    public static Test operator +(Test c1, Test c2) 
   {
      return null;
   }

   public int Item2 {get;set;}
  
	public int this[int i]
	{
    get{return 1;} set{}
	} 
}