import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Family
{
    // Mapa przechowująca osoby, kluczem jest napis "imię nazwisko", a wartością obiekt Person
    private Map<String,Person> familyMap;



    // Konstruktor klasy Family, który inicjalizuje mapę
    public Family()
    {
        this.familyMap=new HashMap<>();
    }

   /* public void add(Person person)
    {
        // Tworzymy klucz w postaci "imię nazwisko"
        String key= person.getFirstName()+" "+person.getLastName();

        familyMap.put(key,person);
    }*/

    public void add(Person... persons)
    {
        for(Person person: persons)
        {
            // Tworzymy klucz w postaci "imię nazwisko"
            String key=person.getFirstName()+person.getLastName();
            // Dodajemy każdą osobę do mapy
            familyMap.put(key, person);
        }
    }

    public Person get(String key)
    {
        // Zwracamy obiekt Person odpowiadający kluczowi lub null, jeśli klucz nie istnieje w mapie
        return familyMap.get(key);
    }


}
