
Activties:
- MainActivity
- TodoListActivity
- AdditemActivity
- DetailActivity
- NewsActivity

fragments:
- loginfragment
- pinloginfragment

Adapter:
-ListviewAdapter

Helper:
listHelper

DataClass:
ListItem


ToolBar:
- Menu bar


Fab Button:
- Fab Button for addding and deleting the data.

- the mainactivity has two fragment if the user is already logged in then pinloginfragment will inflate and get pin from user and pass it back to activity.Mainactivity will Authrnticate through sharedpreference. if user didn't logged in first place then loginfragment will inflate and get data from user and store it into sharedprefrence.

-TodoListActivity will get data from dataclass which is stored in object by Sqlite helper-(WordlistHelper)-and inflate the view.
-user presses on item it'll launch detailactivity and it's layout which has delete option in it. if user presses delete button then data will be deleted and detailativity will destroyed. Todolistactivity will resume and inflate the view.
- same case in add button when user presses add button it'll launch AdditemActivity and user need to enter the data and press add button after then only data will be inserted into database and reflacted into listview.

-toolbar has two items 
	-log out: it'll clear the data from shared prefrences and moves back to mainactivity.
	-new:it'll launch the news activity and display the data which are read from file.  
