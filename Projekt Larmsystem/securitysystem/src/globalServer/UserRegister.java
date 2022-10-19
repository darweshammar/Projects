package globalServer;



import java.util.*;

/**@author Ammar Darwesh  @coauthor**/
public class UserRegister extends Observable {

    private LinkedList<User> users = new LinkedList<>();

    public void addUser(User user){
        this.users.add(user);
        getUserInformation();
    }

    public void deleteUser(User user){
        users.remove(user);
        getUserInformation();
    }

    public User getUser(int index) {
        return users.get(index);
    }

    public void getUserInformation(){

        String[][] userString = new String[users.size()][8];

        for(int i = 0 ; i<users.size(); i++){
            for (int j = 0; j< userString[i].length; j++){
                userString[i][j] = users.get(i).getUserInfo()[j];
            }
        }
        setChanged();
        notifyObservers(userString);
    }
}
