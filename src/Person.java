

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class Person implements Comparable<Person>
{

    private String firstName;
    private String lastName;
    private LocalDate birth;
    private LocalDate death;


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
    public LocalDate getDeath() {return death;}

    //Konstruktor
    public Person(String firstName,String lastName, LocalDate birth,LocalDate death )
    {
        this.firstName=firstName;
        this.lastName=lastName;
        this.birth=birth;
        this.children=new HashSet<>();// Zainicjalizowanie pustego zbioru dzieci
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








}
