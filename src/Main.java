//import java.time.LocalDate;
//import java.util.ArrayList;
//
//
//public class Main
//{
//    public static void main(String[] args)
//    {
//        //Tworzymy nową listę
//        ArrayList<Person> list=new ArrayList<>();
//
//        Person p1=new Person("Julia", "Krzowska", LocalDate.of(2005, 1, 13),null);
//        Person p2=new Person("Jan", "Opiła", LocalDate.of(2005, 8, 11),null);
//        Person p3=new Person("Julia", "Opiła", LocalDate.of(2000, 1, 3),null);
//
//        //Dodajemy osoby do listy (LocalDate.of(yyyy,mm,dd) podaje format daty
//        list.add(p1);
//        list.add(p2);
//        list.add(p3);
//
//        //Rozszerzona pętla for do wyświetlania osób
//        for(Person p: list)
//        {
//            System.out.println(p.toString());
//        }
//
//        System.out.println(p1.adopt(p2));
//        System.out.println(p1.adopt(p3));
//        System.out.println(p1.adopt(p1));
//
//
//    }
//


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

//}
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Person jan = new Person("Jan", "Kowalski", LocalDate.of(1950, 5, 15), LocalDate.of(2010, 3, 10));
        Person anna = new Person("Anna", "Kowalska", LocalDate.of(1955, 8, 20), null);
        Person piotr = new Person("Piotr", "Kowalski", LocalDate.of(1980, 1, 1), null);
        Person zuzia = new Person("Zuzia", "Kowalska", LocalDate.of(2005, 7, 12), null);


        jan.adopt(piotr);
        anna.adopt(piotr);
        piotr.adopt(zuzia);

        List<Person> people = List.of(jan, anna, piotr, zuzia);

        System.out.println(piotr.getParents());

        PlantUMLRunner.setJarPath("fgdfg");
        PlantUMLRunner.generateDiagram(Person.toPlantUmlObjectDiagram(people), "Diagrams", "test");

        System.out.println(Person.toPlantUmlObjectDiagram(people));


    }
}
