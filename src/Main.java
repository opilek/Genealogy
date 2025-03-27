import java.time.LocalDate;
import java.util.ArrayList;


public class Main
{
    public static void main(String[] args)
    {
        //Tworzymy nową listę
        ArrayList<Person> list=new ArrayList<>();

        //Dodajemy osoby do listy (LocalDate.of(yyyy,mm,dd) podaje format daty
        list.add(new Person("Julia", "Krzowska", LocalDate.of(2005, 1, 13)));
        list.add(new Person("Jan", "Opiła", LocalDate.of(2005, 8, 11)));
        list.add(new Person("Julia", "Opiła", LocalDate.of(2000, 1, 3)));


        //Rozszerzona pętla for do wyświetlania osób
        for(Person p: list)
        {
            System.out.println(p.toString());
        }


    }



}