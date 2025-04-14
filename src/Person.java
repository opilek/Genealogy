

import java.io.*;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


public class Person implements Comparable<Person>
{

    private String firstName;
    private String lastName;
    private LocalDate birth;
    private LocalDate death;


    private Set<Person> children;
    private Set<Person> parents;


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
    public LocalDate getDeath() {return death;}
    public Set<Person> getParents() {return parents;}

    //Konstruktor
    public Person(String firstName,String lastName, LocalDate birth,LocalDate death )
    {
        this.firstName=firstName;
        this.lastName=lastName;
        this.birth=birth;
        this.children=new HashSet<>();// Zainicjalizowanie pustego zbioru dzieci
        this.parents=new HashSet<>();// Zainicjalizowanie pustego zbioru rodziców
        this.death=death;

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



    //Metoda wytwórcz która przyjmuje linie z pliku csv
    public static Person fromCsvLine(String line) throws NegativeLifespanException
    {   // Podzielenie wiersza CSV na części na podstawie przecinków
        String[] parts=line.split(",");
        String firstName=parts[0].split(",")[0];
        String lastName=parts[0].split(" ")[1];

        DateTimeFormatter formater=DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate birth=LocalDate.parse(parts[1],formater);
        LocalDate death=null;

        //Jeżeli death nie jest puste odczytujemy datę śmierci
        if(!parts[2].isEmpty())
        {
            death=LocalDate.parse(parts[2],formater);

            System.out.println(" Data urodzin: " + birth);
            System.out.println(" Data śmierci: " + death);

            //Obsługa przypadku gdy data Smierci jest wcześniej od daty urodzin
            if(death.isBefore(birth))
            {
                throw new NegativeLifespanException("Data śmierci osoby " +firstName+lastName+ " nie zgadza się");
            }
        }


        return new Person(firstName,lastName,birth,death);

    }

    //Metoda zwracająca listę obiektów Person odczytanych z pliku csv
    ArrayList<Person> fromCsv(String filePath) throws NegativeLifespanException
    {
        // Tworzymy nową listę, która będzie przechowywać obiekty typu Person
       ArrayList<Person> people=new ArrayList<>();

       // Tworzymy zestaw do przechowywania pełnych imion i nazwisk osób, aby wykryć duplikaty
       Set<String> peoplefullName=new HashSet<>();

        // Mapowanie pełnych imion i nazwisk na obiekty Person, aby móc łatwo odnaleźć rodziców
       Map<String,Person> peopleMap=new HashMap<>();

       // Rozpoczynamy blok try-with-resources, który automatycznie zamknie BufferedReader po zakończeniu pracy
        try(BufferedReader br=new BufferedReader(new FileReader(filePath)))
        {
            //Przechowuje każdą linię w pliku
            String line;

            // Pętla do odczytu pliku linia po linii
           while((line = br.readLine())!=null)
            {

                String[] lineParts=line.split(",");


                // Wywołujemy metodę fromCsvLine, aby utworzyć obiekt Person z danej linii
                Person person = Person.fromCsvLine(line);

                // Tworzymy pełne imię i nazwisko osoby, łącząc imię i nazwisko
                String fullName=person.getFirstName()+" "+person.getLastName();

                // Sprawdzamy, czy już istnieje osoba o tym samym imieniu i nazwisku
                if(peoplefullName.contains(fullName))
                {
                    // Jeśli osoba o tym samym imieniu i nazwisku już istnieje, rzucamy wyjątek
                    throw new AmbiguousPersonException("W pliku istnieją 2 takie same osoby (Imię i Nazwisko)");
                }

                if(person!=null)
                {
                    // Dodajemy osobę do listy people
                    people.add(person);
                    // Dodajemy pełne imię i nazwisko do zestawu, by uniknąć duplikatów
                    peoplefullName.add(fullName);
                    // Mapujemy pełne imię i nazwisko na obiekt person, by łatwo znaleźć rodziców
                    peopleMap.put(fullName,person);

                    // Jeśli linia zawiera informacje o rodzicach (kolumna 3 - parent1)
                    if(lineParts.length>3)
                    {
                        if(!lineParts[3].isEmpty())
                        {
                            Person parent1=peopleMap.get(lineParts[3]);




                        }
                    }

                    // Jeśli linia zawiera informacje o rodzicach (kolumna 4 - parent4)
                    if(lineParts.length>4)
                    {
                        if(!lineParts[4].isEmpty())
                        {
                            Person parent2=peopleMap.get(lineParts[4]);
                        }
                    }
                }
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Nie znaleziono pliku w metodzie fromCsv w klasie Person");
        }
        catch (IOException e)
        {
            System.out.println("Błąd IOException w metodzie fromCsv w klasie Person");
        }
        // Obsługuje nasz wyjątek AmbiguousPersonException
        catch (AmbiguousPersonException e)
        {
            // Rzucamy wyjątek dalej, opakowując go w RuntimeException
            throw new RuntimeException(e);
        }

        return people;

    }
    //Metoda zapisująca dane do pliku binarnego
    public static void toBinaryFile(ArrayList<Person> people,String filePath)
    {
        // Tworzymy strumień wyjściowy ObjectOutputStream, który zapisuje obiekty do pliku binarnego.
        try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(filePath)))
        {
            // Używamy metody writeObject(), aby zapisać obiekt people do pliku.
            out.writeObject(people);
        }
        catch(IOException e)
        {
            System.out.println("Błąd podczas zapisu do pliku binarnego: " + e.getMessage());
        }
    }

    //Metoda odczytująca dane z pliku binarnego
    public static ArrayList<Person> fromBinaryFile(String filePath)
    {
        // Tworzymy strumień wejściowy ObjectInputStream, który odczytuje obiekty z pliku binarnego.
        try(ObjectInputStream in =new ObjectInputStream(new FileInputStream(filePath)))
        {
            // Odczytujemy obiekt zapisany w pliku binarnym za pomocą metody readObject().
            // Zwrócony obiekt jest typu Object, więc musimy go rzutować na ArrayList<Person>.
            Object obj=in.readObject();

            if(obj instanceof ArrayList<?>)
            {
                // Rzutujemy odczytany obiekt na typ ArrayList<Person> i zwracamy go.
                return (ArrayList<Person>) obj;
            }
            else
            {
                System.out.println("Plik nie zawiera listy osób");
            }
        }
        catch(IOException | ClassNotFoundException e)
        {
            System.out.println("Błąd podczas odczytu z pliku binarnego: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    //Zad 2
    public String toPlantUmlObjectDiagram()
    {
        String result="@startuml\n";

        // Tworzy unikalny identyfikator dla tej osoby (np. p_Anna-Kowalska)
        String personId="p_" + this.firstName + "-" + this.lastName;

        // Dodaje obiekt reprezentujący tę osobę do diagramu UML
        result+="object "  + personId + "{\n" + " "+ firstName + "-" + lastName + "\n" +"}\n";


        // Pętla po wszystkich rodzicach tej osoby
        for(Person parent: parents)
        {
            String parentId="p_" + parent.getFirstName() + "-" + parent.getLastName();

            result+="object " + parentId + "{\n" + " "+ parent.getFirstName()+ "-" + parent.getLastName() + "\n" +"}\n";

            // Dodaje relację (strzałkę) między osobą a jej rodzicem z podpisem "dziecko"
            result+=personId + "-->" + parentId + " :dziecko\n";
        }

        result+="@enduml";

        return result;

    }

    //Zad 3
    public static String toPlantUmlObjectDiagram(List<Person> persons)
    {
        String result="@startuml\n";

        // Mapa przypisująca każdemu obiektowi Person unikalny identyfikator tekstowy
        Map<Person,String> idMap=new HashMap<>();

        // 1. Pętla przypisująca ID każdej osobie (np. p_Anna-Kowalska)
        for(Person person: persons)
        {
            idMap.put(person,"p_" + person.getFirstName() + "-" + person.getLastName());
        }

        // 2. Tworzenie definicji obiektów UML dla każdej osoby
        for(Person person: persons)
        {
            String id=idMap.get(person);

            result+="object " + id + "{\n" + " " + person.getFirstName() + "-" + person.getLastName() + "\n" +"}\n";
        }

        // 3. Tworzenie relacji między osobą a jej rodzicami
        for(Person person: persons)
        {
            for(Person parent: person.getParents())
            {
                // Sprawdzamy, czy rodzic też jest na liście osób — jeśli tak, dodajemy relację
                if(idMap.containsKey(parent))
                {
                    result+=idMap.get(person)+ "-->" + idMap.get(parent) + " :dziecko\n";
                }
            }
        }

        result+="@enduml";


        return result;
    }


    //Zad 4
    public static List<Person> filterByNameSubstring(List<Person> persons,String substring)
    {
        if(substring==null || substring.isEmpty())
        {
            return null;
        }

        // Zamieniamy szukany fragment na małe litery, żeby porównywać bez względu na wielkość liter
        String lowersubstring=substring.toLowerCase();

        // Tworzymy nową listę na osoby, które pasują do filtra
        List<Person> result=new ArrayList<>();

        // Iterujemy po wszystkich osobach z listy wejściowej
        for(Person person: persons)
        {
            // Iterujemy po wszystkich osobach z listy wejściowej
            if(person.getFirstName().toLowerCase().contains(lowersubstring) || person.getLastName().toLowerCase().contains(lowersubstring))
            {
                result.add(person);
            }
        }

        return result;

    }

    //Zad 5
    public static List<Person> sortByBirthYear(List<Person> persons)
    {
        // Tworzymy nową listę na podstawie wejściowej, żeby nie zmieniać oryginału
        List<Person> sorted=new ArrayList<>(persons);

        // Sortujemy listę osób według roku urodzenia (rosnąco — od najstarszych do najmłodszych)
        sorted.sort(Comparator.comparing(p ->p.getBirth().getYear()));

        // p -> - lambda, czyli funkcja anonimowa, czyli funkcja zdefiniowana bez imienia.
        // bardzo przydatne gdy chcemy utworzyć jakś prościutką funkcje np a + b

        return sorted;
    }

    //Zad 6 (pomocnicza)
    public long getLifeSpam()
    {
        if(death==null)
        {
            return -1;
        }
        // Obliczamy liczbę dni między datą urodzenia a śmierci
        return ChronoUnit.DAYS.between(birth,death);
    }
    //Zad 6
    public static List<Person> sortByDeathYear(List<Person> persons)
    {
        List<Person> sorted=new ArrayList<>();

        for(Person person: persons)
        {
            if(person.getDeath()!=null)
            {
                sorted.add(person);
            }
        }

        // Sortowanie listy 'sorted' za pomocą komparatora, który porównuje osoby po długości życia
        Collections.sort(sorted, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2)
            {
                // Porównanie długości życia dwóch osób (p1 i p2)
                // Używamy Long.compare() do porównania wartości long (długość życia) w sposób bezpieczny
                // Zwracamy wartość dodatnią, jeśli p2 żył dłużej niż p1 (bo chcemy sortować malejąco)

                return Long.compare(p2.getLifeSpam(),p1.getLifeSpam());
            }
        });

        return sorted;
    }

    //Zad 7
    public static Person getOldestLivingPerson(List<Person> persons)
    {

        Person oldest=null;

        for(Person person: persons)
        {
           if(person.getDeath()!= null)
           {
               if(oldest==null || person.getBirth().isBefore(oldest.getBirth()))
               {
                   oldest=person;
               }
           }
        }

        return oldest;
    }

    //Zad 8
    // Przyjmuje funkcję 'postProcess' do przetworzenia każdej linii przed jej dodaniem do wyniku.
    public String toPlantUMLWithParents(Function<String, String> postProcess)
    {
        // Inicjalizujemy zmienną 'result', która przechowa cały kod PlantUML
        // Dodajemy początek diagramu
        String result = "@startuml\n";

        // Tworzymy unikalny identyfikator dla osoby, używając jej tożsamości w pamięci
        String personId = "obj" + System.identityHashCode(this);

        // Tworzymy linię tekstową dla reprezentacji osoby w diagramie, zawierając imię i nazwisko
        String personLine = "object \"" + this.firstName + " " + this.lastName + "\" as" + personId;

        // Zastosowanie funkcji 'postProcess' do przetworzenia linii reprezentującej osobę
        result += postProcess.apply(personLine);
        result += "\n"; // Dodajemy nową linię po obiekcie osoby

        // Iterujemy po rodzicach osoby, aby dodać ich do diagramu
        for (Person parent : this.parents)
        {
            // Tworzymy unikalny identyfikator dla każdego rodzica, używając jego tożsamości w pamięci
            String parentId = "obj" + System.identityHashCode(parent);

            // Tworzymy linię tekstową dla reprezentacji rodzica w diagramie
            String parentLine = "object \"" + parent.getFirstName() + " " + parent.getLastName() + "\" as" + parentId;

            // Zastosowanie funkcji 'postProcess' do przetworzenia linii reprezentującej rodzica
            result += postProcess.apply(parentLine);
            result += "\n"; // Dodajemy nową linię po obiekcie rodzica

            // Dodajemy połączenie między osobą a jej rodzicem w diagramie
            result += personId + " --> " + parentId + "\n";
        }

        // Kończymy diagram PlantUML
        result += "@enduml\n";

        // Zwracamy wynikowy tekst diagramu
        return result;
    }


    // Metoda generująca diagram UML dla listy osób, uwzględniająca ich dzieci.
// Przyjmuje również funkcję 'postProcess' do przetwarzania linii tekstowych oraz
// 'condition', który sprawdza, czy dany obiekt spełnia określony warunek.
    public static String toUML(List<Person> people, Function<String, String> postProcess, Predicate<Person> condition)
    {
        // Inicjalizujemy zmienną 'result', która będzie przechowywać cały wygenerowany kod PlantUML.
        // Na początku dodajemy początek diagramu.
        String result = "@startuml\n";

        // Tworzymy mapę 'idMap', która przechowuje przypisania osób do unikalnych identyfikatorów.
        Map<Person, String> idMap = new HashMap<>();

        // Iterujemy po liście osób, aby dodać każdą osobę do diagramu
        for (Person person : people)
        {
            // Tworzymy unikalny identyfikator dla każdej osoby na podstawie jej tożsamości w pamięci.
            String personId = "obj" + System.identityHashCode(person);

            // Przypisujemy osobie unikalny identyfikator w mapie 'idMap'.
            idMap.put(person, personId);

            // Tworzymy linię reprezentującą obiekt osoby w diagramie, używając jej imienia i nazwiska.
            String personLine = "object \"" + person.getFirstName() + " " + person.getLastName() + "\" as" + personId;

            // Jeśli osoba spełnia warunek z Predicate, stosujemy funkcję 'postProcess' do przetworzenia tej linii.
            if (condition.test(person))
            {
                personLine = postProcess.apply(personLine);
            }

            // Dodajemy przetworzoną linię do wyniku (diagramu UML).
            result += personLine + "\n";
        }

        // Iterujemy po wszystkich osobach i ich dzieciach, aby dodać połączenia między rodzicami a dziećmi.
        for (Person person : people)
        {
            for (Person child : person.getChildren())
            {
                // Pobieramy identyfikatory rodzica i dziecka z mapy 'idMap'.
                String parentId = idMap.get(person);
                String childId = idMap.get(child);

                // Tworzymy połączenie między rodzicem a dzieckiem w diagramie (strzałka UML).
                result += parentId + " --> " + childId + "\n";
            }
        }

        // Kończymy diagram UML.
        result += "@enduml\n";

        // Zwracamy wygenerowany tekst (kod PlantUML).
        return result;
    }

    








}
