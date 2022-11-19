/*
Sawyer Norquist
CS202
Program 4
Main.java

This object contains all code and implementations for program 4.  It uses the Util class to set up the input Scanner -
most subsequent classes derive from this.  My other frequently used class is the Selection class.  It contains two
ArrayLists of equal size to track option variety.  This is implemented in all subsequent food-related classes.

I experimented with reverse node derivation.  As this is intended to be used for dynamic binding exclusively, I
derived the ABC into a node, then derived the node into my final classes.  This allowed for significant reduction in
coding complexity.
 */

package com.company;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

//Utility class to provide Scanner input functionality to the remaining classes
class Util {

    protected Scanner input;

    public Util() {
        input = new Scanner(System.in);
    }
}

public class Main{

    public static void main(String[] args) {

        Order test;
        test = new Order();
        test.menu();

    }
}

//This is my primary means of handling options in my food classes.  It works off of having two ArrayList objects
//self-aligned so they're always the same size.  The class has the option of being an exclusive select (only one
//choice may be made at a time) or inclusive (multiple choices allowable).  The class also contains the name of the
//selections being used for display purposes.
class Selection extends Util {

    private ArrayList<Boolean> add_topping;  //Holds whether that index item is selected
    private ArrayList<String> topping_name;  //Holds list of names
    private Boolean exclusive;  //Whether or not exclusive select is enables
    private String header;  //Name of selection list being supported

    //Default constructor,
    public Selection()
    {
        add_topping = new ArrayList<Boolean>(0);
        topping_name = new ArrayList<String>(0);
        exclusive = false;
    }

    //Constructor, takes in two args.  First arg is the list of options included, second is the name of the list
    //Is not exclusive by default
    public Selection(String []source, String h_source)
    {
        add_topping = new ArrayList<Boolean>(0);
        topping_name = new ArrayList<String>(0);

        for(int i = 0; i < source.length; ++i)
        {
            add_topping.add(false);
            topping_name.add(source[i]);
        }

        exclusive = false;

        header = h_source;

    }

    //Same as previous constructor, but allows for exclusive flag to be set
    public Selection(String []source, String h_source, Boolean make_exclusive)
    {
        add_topping = new ArrayList<Boolean>(0);
        topping_name = new ArrayList<String>(0);

        for(int i = 0; i < source.length; ++i)
        {
            add_topping.add(false);
            topping_name.add(source[i]);
        }

        exclusive = make_exclusive;

        header = h_source;

    }

    //Displays all objects on list, along with whether they had previously been selected or not
    public void show_choices()
    {
        for(int i = 1; i <= topping_name.size(); ++i)
        {
            System.out.print(i + " - (");

            if(add_topping.get(i-1))
                System.out.print("x");

            System.out.print(") " + topping_name.get(i-1));
            System.out.println();
        }
    }

    //Primary public interface for working with choices.
    public void make_choices()
    {
        int user_in = 1;  //holder for soliciting user input

        //Two loops exist, the exclusive flag is used to determine which is needed
        if(exclusive) {
            do {

                //display user interface
                System.out.println("Please select your " + header);
                show_choices();
                System.out.print("Make selection: ");

                //get user input and do first error check
                try {

                    user_in = input.nextInt();
                } catch (InputMismatchException ex_catch) {
                    input.nextLine();
                    user_in = -1;
                }

                //clear newline
                input.nextLine();

                //check for valid input, send input to processing method if good
                if (user_in < 1 || user_in > topping_name.size())
                    System.out.println("Invalid input - expecting number from 0 to " + topping_name.size());
                else
                    change_selection(user_in);

            } while (user_in < 1 || user_in > topping_name.size());  //loop exits on first good input

        }else {

            do {

                //Display user iterface, adding exit option
                System.out.println("Please select your " + header);
                show_choices();
                System.out.println("0 - Complete selections");
                System.out.print("Make selection: ");

                //get user input and do first error check
                try {

                    user_in = input.nextInt();
                } catch (InputMismatchException ex_catch) {
                    input.nextLine();
                    user_in = -1;
                }

                //clear newline
                input.nextLine();

                //check for valid input and send to processing method if good
                if (user_in < 0 || user_in > topping_name.size())
                    System.out.println("Invalid input - expecting number from 0 to " + topping_name.size());
                else if (user_in > 0)
                    change_selection(user_in);

            } while (user_in != 0);  //Repeat loop until user chooses to leave
        }

    }

    //Wrapper for selection change functions
    //Takes in the index to change, shifts it to match the array index, and makes sure the index is within range
    //Calls the subsequent function based on the exclusive flag
    private void change_selection(int to_change)
    {
        --to_change;  //Needed to align input to array index

        if(to_change > add_topping.size() || to_change < 0)
        {
            System.out.println("Error - selection does not exist");
            return;
        }

        if(exclusive)
            change_selection_exclusive(to_change);
        else
            change_selection_inclusive(to_change);
    }

    //Inclusive selection change method.  Goes straight to input index.  False goes to true, true goes to false.
    private void change_selection_inclusive(int to_change)
    {
        if(add_topping.get(to_change))
            add_topping.set(to_change, false);
        else
            add_topping.set(to_change, true);
    }

    //Exclusive selection change method.  Iterates through array, switching non-matches to false, match to true
    private void change_selection_exclusive(int to_change)
    {
        for(int i = 0; i < add_topping.size(); ++i)
        {
            if(i == to_change)
                add_topping.set(i, true);
            else
                add_topping.set(i, false);
        }
    }

    //Formats and displays all items with accompanying true values
    public void display()
    {

        Boolean something_displayed = false;  //Used to check if anything was displayed so appropriate message can be shown
        String to_display = ""; //Display is built using string so proper formatting can be used

        //Output name of selection object, sets first letter to capital to make it look better
        System.out.print(header.substring(0, 1).toUpperCase() + header.substring(1) + ": ");

        //Output all items in array that have been selected
        //Adds some formatting to end of string for objects that have more than one item selected
        //If something was selected, also set something_displayed to true
        for(int i = 0; i < topping_name.size(); ++i)
        {
            if(add_topping.get(i))
            {
                to_display = to_display + topping_name.get(i) + ", ";
                something_displayed = true;
            }
        }

        //If the for statement didn't find anything to add, say so
        //Else remove the formatting from the last for iteration and display
        if(!something_displayed)
            System.out.println("None");
        else
        {
            to_display = to_display.substring(0, to_display.length()-2);
            System.out.println(to_display);
        }

    }

}

//Abstract base class for rest of program
//Holds base meal choices, upon which entrees will be added in derived classes
abstract class Meal extends Util{

    private Selection size;  //Which size of meal
    private Selection sides;  //What side?
    private Boolean cup;  //Drink cup?
    private String name;  //Who's ordering this meal?

    //Default constructor, loads with basic options.  Program five will see the addition of another method useful for
    //setting custom options
    public Meal()
    {
        size = new Selection(new String[]{"Small", "Medium", "Large"}, "size", true);
        sides = new Selection(new String[] {"Fruit", "Salad", "Fries", "Tater tots"}, "side", true);
        cup = false;
        name = "";
    }

    //Simple method for running through available options.  Checks if name is set before trying to set the name.
    public void make_selections()
    {
        if(name.equals(""))
            set_name();
        size.make_choices();
        sides.make_choices();
        chose_drink();
    }

    //Get input to set order's name
    private void set_name()
    {
        System.out.print("Who is this order for? ");
        name = input.nextLine();
    }

    //Solicits user input on if a drink is wanted or not
    private void chose_drink()
    {
        String user_in = ""; //User input

        //Main loop, runs until valid input is recieved
        do{
            System.out.print("Would you like to include a fountain drink? (y/n): ");
            user_in = input.nextLine();

            if(user_in.length() > 1)
                user_in = user_in.substring(0,1).toUpperCase();
            else
                user_in = user_in.toUpperCase();

            if(!user_in.contentEquals("Y") && !user_in.contentEquals("N"))
                System.out.println("Invalid input");

        }while(!user_in.contentEquals("Y") && !user_in.contentEquals("N"));

        //interprets input to set cup flag
        if(user_in.contentEquals("Y"))
            cup = true;
        else
            cup = false;
    }

    //Displays meal options, including who the meal is for
    public void display()
    {
        System.out.println("For: " + name);
        size.display();
        sides.display();
        display_drink();
    }

    //Interprets cup flag for drink output
    private void display_drink()
    {
        System.out.print("Include cup: ");
        if(cup)
            System.out.println("Yes");
        else
            System.out.println("No");
    }

    //Public interface to view name.  Used for displaying summary list of orders
    public void display_name()
    {
        System.out.println(name);
    }

    //Public interface to compare name.  Used to identify list key.
    public int compare(String comparator)
    {
        return name.compareTo(comparator);
    }

}

//Node derivation for Meal.  Used in this order as we're using dynamic binding.  This prevents us from needing to
//create three node classes.
class Meal_Node extends Meal
{
    Meal_Node next;
    Meal_Node previous;

    Meal_Node()
    {
        next = null;
    }

    void set_next(Meal_Node to_set)
    {
        next = to_set;
    }

    void set_previous(Meal_Node to_set)
    {
        previous = to_set;
    }

    Meal_Node get_next()
    {
        return next;
    }

    Meal_Node get_previous()
    {
        return previous;
    }
}

//First derived useable class - contains hamburger options
class Hamburger extends Meal_Node
{

    private Selection toppings;  //Hamburger toppings
    private Boolean vegetarian;  //veggie or beef?
    private int patties;  //How many patties?  There are no limits!

    //Constructor calls the toppings constructor with appropriate options
    //Sets to beef and no patties by default
    Hamburger()
    {
        toppings = new Selection(new String[] {"Lettuce", "Onion", "Tomato", "Cheese", "Bacon", "Mustard", "BBQ Sauce", "Ketchup"}, "toppings");
        vegetarian = false;
        patties = 0;
    }

    //Primary user interface
    //Calls base class version, then runs through remaining options
    public void make_selections()
    {
        super.make_selections();
        toppings.make_choices();
        choose_vegetarian();
        choose_patties();

    }

    //Get user input to determine if hamburger is veggie or not.  Works like the drink select method in Meal.
    private void choose_vegetarian()
    {
        String user_in;

        do{
            System.out.print("Is this vegetarian? (y/n): ");
            user_in = input.nextLine();

            if(user_in.length() > 1)
                user_in = user_in.substring(0,1).toUpperCase();
            else
                user_in = user_in.toUpperCase();

            if(!user_in.contentEquals("Y") && !user_in.contentEquals("N"))
                System.out.println("Invalid input");

        }while(!user_in.contentEquals("Y") && !user_in.contentEquals("N"));

        if(user_in == "Y")
            vegetarian = true;
        else
            vegetarian = false;
    }

    //Similar to choose_vegetarian, but interprets an int instead
    private void choose_patties()
    {

        int user_in = 1;

        do {

            System.out.print("How many patties do you want: ");

            try {

                user_in = input.nextInt();
            }

            catch(InputMismatchException ex_catch)
            {
                input.nextLine();
                user_in = -1;
            }

            input.nextLine();

            //Can't have no patty.  CAN have as many patties as you want!
            if(user_in < 1)
                System.out.println("Invalid input - expecting a number greater than 1");

        }while(user_in < 1);

        patties = user_in;

    }

    //Public interface for displaying all hamburger selections
    public void display()
    {
        super.display();
        toppings.display();
        display_patties();
        display_vegetarian();
    }

    //Interprets vegetarian flag for output
    private void display_vegetarian()
    {
        if(vegetarian)
            System.out.println("Vegetarian selected");
        else
            System.out.println("Beef selected");
    }

    //Interprets patty quantity for output
    private void display_patties()
    {
        System.out.println("Number of patties: " + patties);
    }
}

//Second derived useable class - contains pasta options
class Pasta extends Meal_Node
{
    Selection toppings;  //Which toppings do you want?
    Selection sauce;  //Any particular sauce?
    Boolean gluten_free;  //Use gluten-free noodles?

    //Constructor calls the toppings and sauce constructors with appropriate options
    //Sets gluten free to false by default
    Pasta()
    {
        toppings = new Selection(new String[] {"Broccoli", "Onion", "Tomato", "Black olives", "Pimento Olives", "Chicken", "Sausage"}, "toppings");
        sauce = new Selection(new String [] {"Tomato", "Alfredo", "Pesto", "None"}, "sauce", true);
        gluten_free = false;
    }

    //Primary user interface
    //Calls base class version, then steps through available options
    public void make_selections()
    {
        super.make_selections();
        toppings.make_choices();
        sauce.make_choices();
        choose_gluten();

    }

    //Interprets user input for whether or not this dish should be gluten-free
    private void choose_gluten()
    {
        String user_in;

        do{
            System.out.print("Is this gluten free? (y/n): ");
            user_in = input.nextLine();

            if(user_in.length() > 1)
                user_in = user_in.substring(0,1).toUpperCase();
            else
                user_in = user_in.toUpperCase();

            if(!user_in.contentEquals("Y") && !user_in.contentEquals("N"))
                System.out.println("Invalid input");

        }while(!user_in.contentEquals("Y") && !user_in.contentEquals("N"));

        if(user_in == "Y")
            gluten_free = true;
        else
            gluten_free = false;
    }

    //Public interface for displaying all pasta selections
    public void display()
    {
        super.display();
        toppings.display();
        sauce.display();
        display_gluten();
    }

    //Interprets gluten flag for output
    private void display_gluten()
    {
        if(gluten_free)
            System.out.println("Gluten free");
    }

}

//Third derived useable class - contains salad options
class Salad extends Meal_Node
{
    Selection toppings;  //What do you want on your salad?
    Selection dressing;  //Which kind of dressing?
    Boolean tossed;  //Should this be tossed?

    //Constructor calls the toppings and dressing constructors with appropriate options
    //Sets tossed to true by default
    Salad()
    {
        toppings = new Selection(new String[] {"Onion", "Tomato", "Black olives", "Pimento Olives", "Avocado", "Anchovies"}, "toppings");
        dressing = new Selection(new String [] {"Ranch", "Thousand Island", "Caesar", "Vinaigrette"}, "dressing", true);
        tossed = true;
    }

    //Primary user interface
    //Calls base class version, then steps through available options
    public void make_selections()
    {
        super.make_selections();
        toppings.make_choices();
        dressing.make_choices();
        choose_tossed();

    }

    //Solicit user input to set the tossed flag
    private void choose_tossed()
    {
        String user_in;

        do{
            System.out.print("Do you want your salad tossed? (y/n): ");
            user_in = input.nextLine();

            if(user_in.length() > 1)
                user_in = user_in.substring(0,1).toUpperCase();
            else
                user_in = user_in.toUpperCase();

            if(!user_in.contentEquals("Y") && !user_in.contentEquals("N"))
                System.out.println("Invalid input");

        }while(!user_in.contentEquals("Y") && !user_in.contentEquals("N"));

        if(user_in == "Y")
            tossed = true;
        else
            tossed = false;
    }

    //Public interface for displaying all salad selections
    public void display()
    {
        super.display();
        toppings.display();
        dressing.display();
        display_tossed();
    }

    //Helper to interpret the display of whether the salad is tossed or not
    private void display_tossed()
    {
        if(tossed)
            System.out.println("Salad is tossed");
        else
            System.out.println("Salad is not tossed");
    }

}

//Meal node management class, implemented as a doubly-linked list
class Meal_List
{
    private Meal_Node head;
    private Meal_Node tail;
    private int size;  //Tracks current size of list.  Intended for helping calling functions identify the size of the
    //list without traversing it, as well as assisting with list position searches.  Currently this field is
    //being maintained, but not used.

    //Default constructor
    public Meal_List()
    {
        head = null;
        tail = null;
        size = 0;
    }

    //Primary insert funcion.  Takes in a Meal_Node object created by the calling class, and inserts it to the rear
    //of the list.
    public void insert(Meal_Node source)
    {

        if(source == null)
            return;

        if(head == null)
        {
            head = source;
        }else {
            tail.set_next(source);
            source.set_previous(tail);
        }

        tail = source;
        ++size;

    }

    //Wrapper for display function.  Checks if head is null.
    public void display()
    {
        if(head == null)
            System.out.println("No meals on list");

        display(head);
    }

    //Recursive portion of the display function.  Uses tail recursion to show the list.
    private void display(Meal_Node current)
    {
        if(current == null)
            return;

        current.display();
        System.out.println();
        display(current.get_next());
        return;
    }

    //Wrapper for display function that specifically shows the names of the users, checks if head is null
    public void display_names()
    {
        if(head == null)
            System.out.println("No meals on list");

        display_names(head);
    }

    //Recursive portion of the name display function.  Uses tail recursion to show the list.
    private void display_names(Meal_Node current)
    {
        if(current == null)
            return;

        current.display_name();
        display_names(current.get_next());
        return;
    }

    //Wrapper for the extract function.  Takes in an index, uses this to navigate to that number on the list,
    //and returns the address.  Returns null if the index is out of bounds.
    public Meal_Node extract(int index)
    {
        if(head == null)
            return null;

        return extract(head, index);
    }

    //Recursive portion of the extract function.  Each cycle counts down one in index, and once index is zero we've
    //arrived.  Returns the address of current at that point.  Returns null if index is still positive when the end
    //of the list is reached.
    private Meal_Node extract(Meal_Node current, int index)
    {
        if(current == null)
            return null;

        if(index == 0)
            return current;

        return extract(current.get_next(), --index);
    }

    //Wrapper function for the second extract implementation.  This one checks the name for a match using the match arg
    public Meal_Node extract(String index)
    {
        if(head == null)
            return null;

        return extract(head, index);
    }

    //Recursive portion of the name-based extract function.  Returns address once a match is found
    private Meal_Node extract(Meal_Node current, String index)
    {
        if(current == null)
            return null;

        if(current.compare(index) == 0)
            return current;

        return extract(current.get_next(), index);
    }

    //Wrapper for checking if an order exists or not.  Argument is string to be compared, returns true if match is
    //found
    public Boolean order_exists(String index)
    {
        if(head == null)
            return false;

        return order_exists(head, index);
    }

    //Recursive portion of order_exists method
    private Boolean order_exists(Meal_Node current, String index)
    {
        if(current == null)
            return false;

        if(current.compare(index) == 0)
            return true;

        return order_exists(current.get_next(), index);
    }

    //Empties list
    public void remove_all()
    {
        head = null;
        tail = null;
        size = 0;
    }

    //Wrapper for removal function.  Argument is position on list to delete
    //Checks for several cases to make sure the argument is valid, and if there's only a single item on the list.
    public void remove(int index)
    {
        if(head == null)
            return;

        if(index < 0)
            return;

        if(head == tail)
        {
            if(index == 0)
            {
                head = null;
                tail = null;
                size = 0;
            }
            return;
        }

        remove(head, index);

    }

    //Recursive portion of the remove by index number.  If index reaches zero before the end of the list,
    //a helper function to delete the node is called
    private void remove(Meal_Node current, int index)
    {
        if(current == null)
            return;

        if(index == 0)
            delete_node(current);

        remove(current.get_next(), index);
    }

    //Wrapper for name-match version of the remove function.  Argument is name of object to delete.
    //As with the previous method, this checks for several initial cases before passing into the recursive method
    public void remove(String index)
    {
        if(head == null)
            return;

        if(index.equals(""))
            return;

        if(head == tail)
        {
            if(head.compare(index) == 0)
            {
                head = null;
                tail = null;
                size = 0;
            }
            return;
        }

        remove(head, index);

    }

    //Recursive method for removing a node by name.  If a match is found prior to reaching the end of the list,
    //a helper function is called to delete the node.
    private void remove(Meal_Node current, String index)
    {
        if(current == null)
            return;

        if(current.compare(index) == 0)
            delete_node(current);

        remove(current.get_next(), index);
    }

    //Helper function to delete a node.  Argument is the node to delete.
    //checks for three primary cases - node at head, node at tail, and node in the middle.
    private void delete_node(Meal_Node to_delete)
    {
        if(to_delete.get_previous() == null)
        {
            head = to_delete.get_next();
            head.set_previous(null);
        }else if(to_delete.get_next() == null) {
            tail = to_delete.get_previous();
            tail.set_next(null);
        }else
        {
            to_delete.get_next().set_previous(to_delete.get_previous());
            to_delete.get_previous().set_next(to_delete.get_next());
        }

        --size;

    }
}

//Main UI class.  Contains an instance of the Meal_List object.  Also stores the name of whoever is using the class
//This functionality is intended to be used with program 5.
class Order extends Util
{
    Meal_List order_list;  //DLL of Meal_Nodes
    String name;  //Name of person using class, will become name of vendor in program 5

    //Constructor, allocates memory for the Meal_List and sets name to nothing.
    Order()
    {
        order_list = new Meal_List();
        name = "";
    }

    //Second constructor, sets name to argument
    Order(String source)
    {
        order_list = new Meal_List();
        name = source;
    }

    //Primary user interface
    public void menu()
    {
        int user_in = 1;  //Hold user response

        set_name();  //We're entering this class for the first time, need to set user

        //Primary menu loop - exists per user requirement.
        do {

            //print menu
            System.out.println("1 - Order burger");
            System.out.println("2 - Order pasta");
            System.out.println("3 - Order salad");
            System.out.println("4 - Show order");
            System.out.println("5 - Edit order");
            System.out.println("0 - Exit");
            System.out.print("Make selection: ");

            //Get user input and check for errors
            try
            {
                user_in = input.nextInt();
            }

            catch(InputMismatchException ex_catch)
            {
                input.nextLine();
                user_in = -1;
            }

            //clear buffer
            input.nextLine();

            //validate user input, and send the response to the response processor if good
            if(user_in < 0 || user_in > 5)
                System.out.println("Invalid input - expecting number from 0 to 5");
            else
                menu_select(user_in);

        }while(user_in != 0);

    }

    //Function to interpret user response.  Argument is the menu option chosen in the menu method
    private void menu_select(int option)
    {
        //double-check validation - redundant
        if(option < 0 || option > 5)
            System.out.println("Option not available");

        //Switch case calls helper functions or prints appropriate responses directly
        switch(option)
        {
            case 1:
                order_hamburger();
                break;

            case 2:
                order_pasta();
                break;

            case 3:
                order_salad();
                break;

            case 4:
                order_list.display();
                break;

            case 5:
                edit_order();
                break;

            case 0:
                System.out.println("Have a nice day!");
        }
    }

    //Menu helper to create a hamburger.  Creates a new object, calls the selection function, and puts it on the list
    private void order_hamburger()
    {
        Hamburger to_add = new Hamburger();
        to_add.make_selections();
        order_list.insert(to_add);
    }

    //Menu helper to create pasta.  Creates a new object, calls the selection function, and puts it on the list
    private void order_pasta()
    {
        Pasta to_add = new Pasta();
        to_add.make_selections();
        order_list.insert(to_add);
    }

    //Menu helper to create a salad.  Creates a new object, calls the selection function, and puts it on the list
    private void order_salad()
    {
        Salad to_add = new Salad();
        to_add.make_selections();
        order_list.insert(to_add);
    }

    //Solicits user input for name, sets name to this input
    private void set_name()
    {
        System.out.print("What is your name? ");
        name = input.nextLine();
    }

    //Function to edit an order.  Solicits user input for which order to edit, validates against list contents,
    //pulls object from the list, then gets user info on which function to call
    private void edit_order()
    {

        int user_in = 1;  //User input handle
        String name_in = "";  //Order name handle
        Boolean repeat = false;  //Check if need to repeat again (error validation)

        //Display all names on the list, prompt input, return input to name_in
        order_list.display_names();
        System.out.println("Who's order do you want to edit? ");
        name_in = input.nextLine();

        //Check if order name is valid, errors and returns to previous function if invalid
        if(!order_list.order_exists(name_in))
        {
            System.out.println("Order doesn't exist - try again");
            return;
        }

        //If valid, enter menu loop to chose which option to execute. Exists loop after a valid option has selected
        do {

            //Menu of options
            System.out.println("What do you want to do with " + name_in +"'s order?");
            System.out.println("1 - Edit order");
            System.out.println("2 - Delete order");
            System.out.println("0 - Cancel");
            System.out.print("Make selection: ");

            //Fetch and validate user input
            try
            {

                user_in = input.nextInt();
            }

            catch(InputMismatchException ex_catch)
            {
                input.nextLine();
                user_in = -1;
            }

            //clear buffer
            input.nextLine();

            //validate entry against options and execute the input processor as needed
            if(user_in < 0 || user_in > 2)
                System.out.println("Invalid input - expecting number from 0 to 2");
            else
                edit_select(user_in, name_in);

        }while(user_in < 0 || user_in > 2);

    }

    //Selection processor for the edit menu.  Takes in an int to indicate the menu option, as well as the name
    //of the order to search.
    private void edit_select(int option, String name_in)
    {
        //validate option selection (redundant)
        if(option < 0 || option > 2)
            System.out.println("Option not available");

        //Call right function for option.  First option needs an additional helper function.  Second option can
        //simply call the remove function from the list class.
        switch(option)
        {
            case 1:
                edit_order(name_in);
                break;

            case 2:
                order_list.remove(name_in);
                break;
        }
    }

    //Helper to edit an order.  Calls the address of an order and calls the selection function again
    private void edit_order(String name_in)
    {
        Meal_Node to_edit = order_list.extract(name_in);
        to_edit.make_selections();
    }

}