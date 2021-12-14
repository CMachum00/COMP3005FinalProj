import java.util.*;
import java.sql.*;


public class Application
{
    String activeCustId;
    Scanner input;
    ArrayList<String> Cart;
    Statement st;
    public static int customerIDs;

    Application(Statement stat)
    {
        input = new Scanner(System.in);
        Cart = new ArrayList<String>();
        st = stat; 
        try
        (
            ResultSet getIds = st.executeQuery
            (
                "select cust_id from customer order by cust_id asc"
            )
        ) 
        {
            while(getIds.next())
            {
                customerIDs = getIds.getInt("cust_id");
                customerIDs++;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }


    public void openingScreen()
    {
        do 
        {
            System.out.println("Please enter your choice: ");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("3: Quit");

            int choice = input.nextInt();
            input.nextLine();

            if(choice == 1)
            {
                login();
            }
            else if (choice == 2)
            {
                register();
            }
            else if(choice == 3)
            {
                System.exit(0);
            }
            else
            {
                System.out.println("Invalid choice!");
            }
        }while(true);
    }

    public void login()
    {
        do
        {
        System.out.println("Please enter your username:");
        String UN = input.nextLine();

        System.out.println("Please enter your password:");
        String pass = input.nextLine();

        //run SQL to check for login and set CustId if good, otherwise invalid creds
        
        if (UN.equals("admin") && pass.equals("admin"))
        {
            activeCustId = "1";
            mainMenu();
            break;
        }

        try
        (   
            ResultSet count = st.executeQuery
            (
                "select count(*) from customer where u_name = '" + UN +"';"
            )
        )
        {
            count.next();
            if(!count.getString("count").equals("0"))
            {
                try 
                (
                    ResultSet CustId = st.executeQuery
                    (
                        "select cust_id from customer where u_name = '" + UN + "' and pass = '" + pass + "';"
                    )
                )
                {
                    while(CustId.next())
                    {
                        activeCustId = CustId.getString("cust_id");
                        mainMenu();
                        break;
                    }
                    
                    break;
                }
                catch (SQLException e)
                {
                    System.out.println("ERROR: " + e);
                }
            }
            else
            {
                System.out.println("Invalid credentials, please try again!");
            }
        }
            catch (SQLException e)
            {
                System.out.println("ERROR: " + e);
            }
    
        }while(true);
    }

    public void register()
    {
        String UN;
        do
        {
            System.out.println("Please enter username: ");
            UN = input.nextLine();

            try
            (
                ResultSet count = st.executeQuery
                (
                    "select count(*) from customer where u_name = '" + UN +"';"
                )
            )
            {
                count.next();
            if(count.getString("count").equals("0"))
            {
                break;
            }
            else
            {
                System.out.println("Username is already taken!");
            }
            }
             catch (Exception e) {
                System.out.println("Error: " + e);
            }
             
        }while(true);
        
        System.out.println("Username is valid!");
        System.out.println("Please enter password:");
        String pass = input.nextLine();

        System.out.println("Please enter your card number:");
        String card_num = input.nextLine();

        System.out.println("Please enter your billing address:");
        String billing_addr = input.nextLine();

        System.out.println("Please enter your shipping address:");
        String shipping_addr = input.nextLine();

        try 
        {
            st.executeQuery
            ("insert into customer values (" + (customerIDs++) + ",'" +card_num + "' , '" + billing_addr + "', '" + shipping_addr + "','" + UN + "','" + pass + "');");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

    public void mainMenu()
    {
        if(activeCustId!="1")
        {
            do
            {
                System.out.println("Welcome to ZeBookStore, please select an option");
                System.out.println("1: Search for a book");
                System.out.println("2: View Cart");
                System.out.println("3: Checkout");
                System.out.println("4: Track an order");
                System.out.println("5: Logout");

                int choice = input.nextInt();

                if(choice==1)
                {
                    LookForBook();
                }
                else if(choice ==2)
                {
                    ViewCart();
                }
                else if(choice ==3)
                {
                    Checkout();
                }
                else if(choice ==4)
                {
                    trackOrder();
                }
                else if (choice == 5)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid Choice, please re-select!");
                }

            }while(true);
   
        }
        else
        {
            do
            {
                System.out.println("Welcome admin, please select an option");
                System.out.println("1: Insert a new book"); //book must have a publisher ID entered
                System.out.println("2: Delete an existing book");
                System.out.println("3: Create a new publisher"); //,must have created publisher before making book
                System.out.println("4: Delete an existing publisher");
                System.out.println("5: Generate reports");
                System.out.println("6: Check for low stock on books");
                System.out.println("7: Modify book info (prices, publishers, etc.)");
                System.out.println("8: Logout");

                int choice = input.nextInt();

                if(choice==1)
                {
                    insertBook();
                }
                else if(choice ==2)
                {
                    deleteBook();
                }
                else if(choice ==3)
                {
                    insertPublisher();
                }
                else if(choice ==4)
                {
                    deletePublisher();
                }
                else if (choice == 5)
                {
                    generateReports();
                }
                else if(choice ==6)
                {
                    checkBookStock();
                }
                else if(choice ==7)
                {
                    modifyBooks(); //can change publishers or price of the book
                }
                else if(choice ==8)
                {
                    break;
                }
                else
                {
                    System.out.println("Invalid Choice, please re-select!");
                }

            }while(true);
   
        }
    }
    //all functions used for users
    public void LookForBook()
    {
        do{
            System.out.println("What would you like to search by");
            System.out.println("1: Title");
            System.out.println("2: ISBN");
            System.out.println("3: Author");
            System.out.println("4: Genre");
            System.out.println("5: Back to main menu");

            int choice = input.nextInt();

            if(choice ==1)
            {
                System.out.println("Enter the title of the book you're looking for: ");
                String bookTitle = input.nextLine();
                bookTitle = input.nextLine();

                try
                (
                    ResultSet result = st.executeQuery
                    (
                        "select * from book where b_name = '"+bookTitle+"' and quant>0;"
                    )
                )
                {
                    checkForBook(result);
                }
                catch(Exception e)
                {
                    System.out.println("Error: " + e);
                }

            }
            else if(choice==2)
            {
                System.out.println("Enter the ISBN of the book you're looking for: ");
                String bookISBN = input.nextLine();
                bookISBN = input.nextLine();
                try
                (
                    ResultSet result = st.executeQuery
                    (
                        "select * from book where isbn = '"+bookISBN+"' and quant>0 ;"
                    )
                )
                {
                    checkForBook(result);
                }
                catch(Exception e)
                {
                    System.out.println("Error: " + e);
                }
            }
            else if(choice==3)
            {
                System.out.println("Enter the author you're looking for: ");
                String booksAuthor = input.nextLine();
                booksAuthor = input.nextLine();
                try
                (
                    ResultSet result = st.executeQuery
                    (
                        "select * from book where auth = '"+booksAuthor+"' and quant>0;"
                    )
                )
                {
                    checkForBooks(result);
                }
                catch(Exception e)
                {
                    System.out.println("Error: " + e);
                }
            }
            else if(choice==4)
            {
                System.out.println("Enter the genre you're looking for: ");
                String booksGenre = input.nextLine();
                booksGenre = input.nextLine();
                try
                (
                    ResultSet result = st.executeQuery
                    (
                        "select * from book where genre = '"+booksGenre+"' and quant>0;"
                    )
                )
                {
                    checkForBooks(result);
                }
                catch(Exception e)
                {
                    System.out.println("Error: " + e);
                }
            }
            else if(choice==5)
            {
                break;
            }
            else
            {
                System.out.println("Invalid choice, please re-enter!");
            }

        }while(true);
    }

    public void checkForBook(ResultSet result)
    {
        String ISBN;
        int storeQuant;
        try {
            if(result.next())
                        {
                            do
                            {
                                System.out.println("Book found: ");
                                System.out.println("Name: "+result.getString("b_name"));
                                System.out.println("Author: "+result.getString("auth"));
                                ISBN = result.getString("isbn");
                                System.out.println("ISBN: "+ result.getString("isbn"));
                                System.out.println("Genre: "+result.getString("genre"));
                                System.out.println("Pages: "+result.getString("pages"));
                                System.out.println("Price: "+result.getString("price"));
                                storeQuant = result.getInt("quant");
                                System.out.println("Quantity: "+result.getString("quant"));
                            }while(result.next());

                            System.out.println("Would you like to add to your cart? ");
                            System.out.println("1: Yes");
                            System.out.println("2: No");

                            int choice = input.nextInt();
                            input.nextLine();
                            if(choice == 1)
                            {
                                System.out.println("How many copies would you like?");
                                int orderQuant = input.nextInt();
                                if(orderQuant<storeQuant)
                                {
                                    Cart.add(ISBN + "/" + orderQuant);
                                }
                                else
                                {
                                    System.out.println("Order quantity exceeds store quantity!");
                                }
                            }
                            
                        }
                        else
                        {
                            System.out.println("Book not found, please search something else");
                        }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void checkForBooks(ResultSet result)
    {
        String ISBN;
        int choice;
        int storeQuant;
        try {
            if(result.next())
                        {
                            do
                            {
                                System.out.println("Book found: ");
                                System.out.println("Name: "+result.getString("b_name"));
                                System.out.println("Author: "+result.getString("auth"));
                                ISBN = result.getString("isbn");
                                System.out.println("ISBN: "+ result.getString("isbn"));
                                System.out.println("Genre: "+result.getString("genre"));
                                System.out.println("Pages: "+result.getString("pages"));
                                System.out.println("Price: "+result.getString("price"));
                                storeQuant = result.getInt("quant");
                                System.out.println("Quantity: "+result.getString("quant"));

                                System.out.println("Would you like to add to your cart? ");
                                System.out.println("1: Yes");
                                System.out.println("2: No");

                                choice = input.nextInt();
                                input.nextLine();
                                if(choice == 1)
                                {
                                    System.out.println("How many copies would you like?");
                                    int orderQuant = input.nextInt();
                                    if(orderQuant<storeQuant)
                                {
                                    Cart.add(ISBN + "/" + orderQuant);
                                }
                                else
                                {
                                    System.out.println("Order quantity exceeds store quantity!");
                                }
                                }

                                System.out.println("Would you like to continue viewing books in this category? ");
                                System.out.println("1: Yes");
                                System.out.println("2: No");

                                choice = input.nextInt();
                                input.nextLine();

                                if(choice==2)
                                {
                                    break;
                                }
                                
                            }while(result.next());

                            if(choice!=2)
                            {
                            System.out.println("End of the queue, returning to search screen... ");
                            }
                        }
                        else
                        {
                            System.out.println("No books found, please search something else");
                        }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void ViewCart()
    {
        if(Cart.isEmpty())
        {
            System.out.println("Your cart is empty");
        }
        else
        {
            System.out.println("This is your cart: ");
            for(String s:Cart)
            {
                String isbn = s.substring(0, s.indexOf("/"));
                String quant = s.substring(s.indexOf("/")+1, s.length());

                try
                (
                    ResultSet result = st.executeQuery
                    (
                        "select b_name from book where isbn = '" + isbn + "';"
                    )

                ) 
                {
                    result.next();
                    System.out.println("Name: " + result.getString("b_name") + " | Quantity: " + quant);
                } 
                catch (Exception e) 
                {
                    System.out.println("Error: " + e);
                }
            
            }   
        }
    }

    public void Checkout()
    {
        int orderID=111111;
        if(Cart.isEmpty())
        {
            System.out.println("Your cart is empty");
        }
        else
        {
            for(String s:Cart)
            {
                String isbn = s.substring(0, s.indexOf("/"));
                String quant = s.substring(s.indexOf("/")+1, s.length());
                
                

                try
                (
                    ResultSet GetID = st.executeQuery
                    (
                        "select order_id from orders order by order_id asc"
                    )
                ) 
                {
                    while(GetID.next())
                    {
                        orderID = GetID.getInt("order_id");
                        orderID++;
                    }
                } 
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }

                System.out.println("Would you like to use your pre-entered card number and shipping info");
                System.out.println("1: Yes");
                System.out.println("2: No");

                int choice = input.nextInt();

                if(choice == 1)
                {
                    try
                    (
                        ResultSet result = st.executeQuery
                        (
                            "with ei as ( select card_num, billing_addr, shipping_addr from customer where cust_id ='" +activeCustId + "') "+ 
                            "insert into orders values("+ orderID +  ",'"  + isbn +  "',"  + quant + ",current_date, (select card_num from ei), (select billing_addr from ei), (select shipping_addr from ei), 'Order Recieved' );" +
                            "insert into places values (" + activeCustId + "," + orderID + ");" +
                            "UPDATE book SET quant = (select quant from book where isbn = '"+  isbn+"') - " + quant + " WHERE isbn='"+isbn + "';" 
                        )
    
                    ) 
                    {
                        
                    } 
                    catch (Exception e) 
                    {
                        System.out.println("Error: " + e);
                    }
                }
                else
                {
                    input.nextLine();
                    System.out.println("Please enter the card number you would like to use");
                    String card_num = input.nextLine(); 
                    System.out.println("Please enter your billing address");
                    String billing_addr = input.nextLine();
                    System.out.println("Please enter your shipping address");
                    String shipping_addr = input.nextLine();
                    try
                    (
                        ResultSet result = st.executeQuery
                        ( 
                            "insert into orders values("+ orderID +  ",'"  + isbn +  "',"  + quant + ",current_date, '"+card_num + "', '" + billing_addr + "', '"+ shipping_addr + "', 'Order Recieved' );" +
                            "insert into places values (" + activeCustId + "," + orderID + ");" +
                            "UPDATE book SET quant = (select quant from book where isbn = '"+ isbn+"') - " + quant + " WHERE isbn='"+isbn + "';" 
                        )
    
                    ) 
                    {
                        
                    } 
                    catch (Exception e) 
                    {
                        System.out.println("Error: " + e);
                    }
                }
            }
            System.out.println("Order placed, your order number is: " + orderID);
        }
    }

    public void trackOrder()
    {
        input.nextLine();
        System.out.println("Please enter the order number you would like tracked");

        String orderToTrack = input.nextLine();

        try
        (
            ResultSet result = st.executeQuery
            (
                "select status, o_date from orders where order_id = " + orderToTrack + ";"
            )
        ) 
        {
            result.next();
            Calendar c = Calendar.getInstance();
            c.setTime(result.getDate("o_date"));
            c.add(Calendar.DATE, 7);
            System.out.println("Order status: " + result.getString("status"));
            System.out.println("Estimated arrival date: " + (c.getTime()));          
        } 
        catch (Exception e) {
            System.out.println("Error:" + e);
        }
    }

    //admin functions

    public void insertBook()
    {
        input.nextLine();
        System.out.println("Please enter the title of the book");
        String b_name = input.nextLine();
        System.out.println("Please enter the author of the book");
        String auth = input.nextLine();
        System.out.println("Please enter the ISBN of the book");
        String isbn = input.nextLine();
        System.out.println("Please enter the genre of the book");
        String genre = input.nextLine();
        System.out.println("Please enter the number of pages");
        String pages = input.nextLine();
        System.out.println("Please enter the quantity of stock");
        String quant = input.nextLine();
        System.out.println("Please enter the price of the book");
        String price = input.nextLine();
        System.out.println("Please enter the ID of the publisher of this book");
        String publisher_id = input.nextLine();
        System.out.println("Please enter the percent of commission the publiser receives (no % sign)");
        String commission = input.nextLine();

        try 
        (
            ResultSet r = st.executeQuery
            (
                "insert into book values ('"+ b_name +"' ,'"+ auth +"' ,'"+ isbn +"' ,'"+ genre +"' ,"+pages + "," + quant + ","+price +");"+
                "insert into published values ('" +isbn + "'," + publisher_id + "," + commission + ")"
            )
        )
        {
            
        } 
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void deleteBook()
    {
        input.nextLine();
        System.out.println("Enter the ISBN of the book you wish to remove");
        String isbnToDelete = input.nextLine();
        
        try 
        (
            ResultSet r = st.executeQuery
            (
                "UPDATE book SET quant = -1 where isbn ='" + isbnToDelete + "';" //quant = -1 removes them from search results but allows for historical orders and proper analytics to be generated
            )
        )
        {
            
        } 
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void insertPublisher()
    {
        input.nextLine();
        int publisherID = 111111;
        try
                (
                    ResultSet GetID = st.executeQuery
                    (
                        "select publisher_id from publisher order by publisher_id asc"
                    )
                ) 
                {
                    while(GetID.next())
                    {
                        publisherID = GetID.getInt("publisher_id");
                        publisherID++;
                    }
                } 
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }

                System.out.println("Please enter the name of the publisher");
                String p_name = input.nextLine();
                System.out.println("Please enter the publisher's address");
                String addr = input.nextLine();
                System.out.println("Please enter the publisher's email");
                String email = input.nextLine();
                System.out.println("Please enter the publisher's account number for deposits");
                String bank_acct = input.nextLine();

                try
                (
                    ResultSet r = st.executeQuery
                    (
                        "insert into publisher values ("+publisherID+",'"+p_name+"','"+addr+"','"+email+"','"+bank_acct+"')"
                    )
                ) 
                {
                    
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
    }

    public void deletePublisher()
    {
        input.nextLine();
        System.out.println("Please enter the ID of the publisher you'd like to remove");
        String idToRemove = input.nextLine();

        try
        (
            ResultSet r = st.executeQuery
            (
                "delete from published where publisher_id = " + idToRemove+";"+
                "delete from publisher where publisher_id = " + idToRemove + ";"
            )
        ) 
        {
            
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void generateReports()
    {
        input.nextLine();
        do
        {
            System.out.println("Please select what report to run:");
            System.out.println("1: Sales vs. expenditures");
            System.out.println("2: Sales by author"); 
            System.out.println("3: Sales by genre");
            System.out.println("4: Go back");

            int choice = input.nextInt();

            input.nextLine();

            if(choice ==1)
            {
                //sales vs expenditures    
                try
                (
                    ResultSet r = st.executeQuery
                    (
                        "select b_name, publisher_id, sum(o.quant) as books_sold, commission, price"+
                        " from orders as o join book on book.isbn = o.isbn join published on o.isbn = published.isbn"+
                        " group by b_name, publisher_id, commission, price"
                    )
                )
                {
                    if(r.next())
                    {
                        do
                        {
                            System.out.println("Sales for " +r.getString("b_name") +": " + (r.getInt("books_sold")*r.getInt("price")) + "$ | Expenditures: " + ((r.getDouble("commission")/100) * (r.getInt("books_sold")*r.getInt("price")))+ "$");
                        }while(r.next());
                    }
                    else
                    {
                        System.out.println("No sales found");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }

            }
            else if (choice ==2)
            {
                //sales by author

                try
                (
                    ResultSet r = st.executeQuery
                    (
                       "select auth, SUM(orders.quant) as total_sales from orders join book on book.isbn=orders.isbn group by auth "
                    )
                )
                {
                    if(r.next())
                    {
                        do
                        {
                            System.out.println("Author: " + r.getString("auth") + " | Number of books sold: " + r.getString("total_sales"));
                        }while(r.next());
                    }
                    else
                    {
                        System.out.println("No sales found");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }
            else if(choice ==3)
            {
                //sales by genre

                try
                (
                    ResultSet r = st.executeQuery
                    (
                        "select genre, SUM(orders.quant) as total_sales from orders join book on book.isbn=orders.isbn group by genre "
                    )
                )
                {
                    if(r.next())
                    {
                        do
                        {
                            System.out.println("Genre: " + r.getString("genre") + " | Number of books sold: " + r.getString("total_sales"));
                        }while(r.next());
                    }
                    else
                    {
                        System.out.println("No sales found");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }
            else if(choice==4)
            {
                break;
            }
            else
            {
                System.out.println("Invalid choice! Please re-select");
            }
        }while(true);
    }

    public void checkBookStock()
    {
        Calendar prevMonth = Calendar.getInstance();
            prevMonth.add(Calendar.MONTH, -1);
        Calendar currMonth = Calendar.getInstance();

            
        try
        (
            ResultSet result = st.executeQuery
            (
                "with quantities as (select isbn, SUM(quant) as total_orders from orders " + 
                "where o_date > '"+prevMonth.get(Calendar.YEAR)+"-"+(prevMonth.get(Calendar.MONTH)+1)+"-01' and o_date < '"+currMonth.get(Calendar.YEAR)+"-"+(currMonth.get(Calendar.MONTH)+1)+"-30'"+
                "group by isbn)"+
                "select b_name, total_orders, quant from book join quantities on book.isbn=quantities.isbn where quant<5 and quant>-1"
            )
        ) 
        {
            if(result.next())
            {
                do
                {
                    System.out.println("Book: " + result.getString("b_name") + " was ordered " + result.getString("total_orders") + " times in the past month and needs to be re-ordered.");
                    System.out.println("Current Quantity: " + result.getString("quant"));
                }while(result.next());
            }
            else
            {
                System.out.println("No books need re-ordering");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void modifyBooks()
    {
        input.nextLine();
        do
        {
            System.out.println("Please select what you wish to modify: ");
            System.out.println("1: Book Quantity");
            System.out.println("2: Publisher of a book");
            System.out.println("3: Commission payed to the publisher");
            System.out.println("4: Go Back");

            int choice = input.nextInt();

            input.nextLine();

            if (choice ==1)
            {
                System.out.println("Please enter the ISBN of the book you wish to adjust: ");
                String isbn = input.nextLine();
                System.out.println("Please enter the adjusted quantity for the book: ");
                String newQuant = input.nextLine();

                try
                (
                    ResultSet r = st.executeQuery
                    (
                        "update book set quant = " + newQuant + " where isbn = '" + isbn + "';"
                    )
                )
                {
                    
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }
            else if(choice ==2)
            {
                System.out.println("Please enter the ISBN of the book you wish to adjust: ");
                String isbn = input.nextLine();
                System.out.println("Please enter the new publisher ID for the book: ");
                String newPublisher = input.nextLine();
                System.out.println("Please enter the commission percentage (no % sign): ");
                String commission = input.nextLine();

                try
                (
                    ResultSet r = st.executeQuery
                    (
                        "delete from published where isbn = '" + isbn + "';"+
                        "insert into published values ('"+ isbn+ "'," + newPublisher + "," + commission + ");"
                    )
                )
                {
                    
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }
            else if(choice ==3)
            {
                System.out.println("Please enter the ISBN of the book you wish to adjust: ");
                String isbn = input.nextLine();
                System.out.println("Please enter the publisher ID of the book: ");
                String publisher = input.nextLine();
                System.out.println("Please enter the adjusted commission percentage for this book (no % sign): ");
                String newCommission = input.nextLine();

                try
                (
                    ResultSet r = st.executeQuery
                    (
                        "update published set commission = " + newCommission + " where isbn = '" + isbn + "'and publisher_id = " + publisher + ";"
                    )
                )
                {
                    
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                
            }
            else if(choice==4)
            {
                break;
            }
            else
            {
                System.out.println("Invalid choice! Please re-select");
            }
        }while(true);
    }
    public static void main(String[] args) 
    {
        //change to match your db info
        try(Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/3005Proj","postgres" ,"Bubbie00")) 
        {
            Statement stat = conn.createStatement();
            Application app = new Application(stat);
            app.openingScreen();
        }catch(SQLException e){
            System.out.println("ERROR in starting search: " + e);
        };
    }

}



