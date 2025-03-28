import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Person implements Comparable<Person>
{

    private String firstName;
    private String lastName;
    private LocalDate birth;

    private Set<Person> children;


    //Gettery
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public LocalDate getBirth()
    {
        return birth;
    }



    //Konstruktor
    public Person(String firstName,String lastName, LocalDate birth)
    {
        this.firstName=firstName;
        this.lastName=lastName;
        this.birth=birth;
        this.children=new HashSet<>();// Zainicjalizowanie pustego zbioru dzieci
    }

    @Override

    public String toString()
    {
        return firstName+" "+lastName+" "+birth;
    }

    public boolean adopt(Person child)
    {
            if(child==null)
            {
                return false;
            }

        // Dodaje dziecko do zbioru i zwraca, czy dodanie się powiodło
            return children.add(child);
    }

    @Override
    // Porównujemy daty urodzin nadpisując metodę compareTo
    public int compareTo(Person other)
    {
        return this.birth.compareTo(other.birth);
    }


    public Person getYoungestChild()
    {
            // Sprawdzamy, czy lista dzieci jest pusta
            if(children.isEmpty())
            {
                return null;
            }
            // Tworzymy zmienną 'youngest', która będzie przechowywać referencję do najmłodszego dziecka
            Person youngest=this.children.iterator().next();
            // Przechodzimy przez wszystkie dzieci w liście 'children'
            for(Person child: children)
            {
                //if(child.getBirth().isAfter(youngest.getBirth()));
                //Rozwiązanie wykorzystanie interfejsu Compaarable

                if(child.compareTo(youngest)>0)
                {

                    youngest=child;
                }
            }

            return youngest;
    }

    // Metoda zwracająca posortowaną listę dzieci
    public ArrayList<Person> getChildren()
    {
        // Tworzymy nową ArrayList z dzieci, które są przechowywane w zbiorze 'children'
        // Zbiór 'children' jest konwertowany na listę, ponieważ Set (zbiór) nie jest posortowany.
        ArrayList<Person> sortChildren = new ArrayList<>(children);

        // Sortujemy dzieci przy użyciu Collections.sort()
        Collections.sort(sortChildren,Collections.reverseOrder());

        // Zwracamy posortowaną listę dzieci
        return sortChildren;
    }








}
