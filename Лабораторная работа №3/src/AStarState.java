import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import javax.lang.model.type.NullType;

/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/
public class AStarState
{
  /** This is a reference to the map that the A* algorithm is navigating. **/
  private Map2D map;

  public HashMap<Location, Waypoint> openPoints = new HashMap<>();

  public HashMap<Location, Waypoint> closedPoints = new HashMap<>();

  /**
   * Initialize a new state object for the A* pathfinding algorithm to use.
   **/
  public AStarState(Map2D map)
  {
    if (map == null)
      throw new NullPointerException("map cannot be null");

    this.map = map;
  }

  /** Returns the map that the A* pathfinder is navigating. **/
  public Map2D getMap()
  {
    return map;
  }

  /**
   * This method scans through all open waypoints, and returns the waypoint
   * with the minimum total cost.  If there are no open waypoints, this method
   * returns <code>null</code>.
   **/
  public Waypoint getMinOpenWaypoint()
  {
    Waypoint res;
    if (openPoints.isEmpty()) { //если пуста, в результате NULL
      res = null;
    } else {  //Если не пуста
      ArrayList<Location> keys = new ArrayList(openPoints.keySet()); //ключи и значения openPoints для удобного доступа к ним
      ArrayList<Waypoint> values = new ArrayList(openPoints.values());
      Waypoint min = values.get(0); //минимальным принимаем первый элемент значений
      for (int i = 1; i <= values.size()-1; i++) {
        if (values.get(i).getTotalCost()< min.getTotalCost()) { //если total cost текущей точки меньше минимального
          min = values.get(i); //текущий становится минимальным
        }
      }
      res = min;
    }
    return res;
  }

  /**
   * This method adds a waypoint to (or potentially updates a waypoint already
   * in) the "open waypoints" collection.  If there is not already an open
   * waypoint at the new waypoint's location then the new waypoint is simply
   * added to the collection.  However, if there is already a waypoint at the
   * new waypoint's location, the new waypoint replaces the old one <em>only
   * if</em> the new waypoint's "previous cost" value is less than the current
   * waypoint's "previous cost" value.
   **/
  public boolean addOpenWaypoint(Waypoint newWP)
  {
    boolean res = false;
    Location location =  newWP.getLocation(); //получаем локацию новой точки
    ArrayList<Location> keys = new ArrayList(openPoints.keySet()); //ключи и значения openPoints для удобного доступа к ним
    ArrayList<Waypoint> values = new ArrayList(openPoints.values());

    if (keys.contains(location)){ //Если локация новой точки есть в openPoints(в ключах)
      int index = keys.indexOf(location);  //Находим индекс ключа
      Waypoint oldWP = values.get(index);  //По индексу ключа в первом arraylist находим значение во втором, старую точку
      if (newWP.getPreviousCost()<oldWP.getPreviousCost()){ //сравниваем previous cost у старой и новой WP
        openPoints.put(location, newWP);  //Если у новой меньше, меняем старую на новую, в результате true
        res = true;
      }
    } else {  //если локации в ключах нет
      openPoints.put(location, newWP);  //добавляем новую, результат true
      res = true;
    }
    return res;
  }


  /** Returns the current number of open waypoints. **/
  public int numOpenWaypoints()
  {
    int res = openPoints.size();
    return res;
  }


  /**
   * This method moves the waypoint at the specified location from the
   * open list to the closed list.
   **/
  public void closeWaypoint(Location loc)
  {
    closedPoints.put(loc,openPoints.get(loc)); //добавляем в закрытые точки
    openPoints.remove(loc); //убираем из открытых
  }

  /**
   * Returns true if the collection of closed waypoints contains a waypoint
   * for the specified location.
   **/
  public boolean isLocationClosed(Location loc)
  {
    boolean res = false;
    if (closedPoints.containsKey(loc)){ //если локация содержится в закрытых
      res = true;
    }
    return res;
  }
}
