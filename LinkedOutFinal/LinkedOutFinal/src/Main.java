import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        JSON_Reader.ReadFile();
        Graph graph = new Graph();
        graph.addVertices();
        Graph.userArrayList.get(0).findClosestFiveBFS(graph);
        Graph.userArrayList.get(0).suggestionList();
//        System.out.println(Graph.userArrayList.get(0).scores.get(0));
        Interface.mainMenu();

//        for (int i = 0; i < Graph.userArrayList.get(76).closestFiveBFS.size(); i++) {
//            System.out.println(Graph.userArrayList.get(0).closestFiveBFS.get(i));
//        }
    }
}
class JSON_Reader{
    static Object object;

    static {
        try {
            object = new JSONParser().parse(new FileReader("C:\\Users\\rezac\\IdeaProjects\\LinkedOutFinal\\project-final-khakh\\users.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    static void ReadFile(){
        JSONArray jsonArray = (JSONArray) object;
        Iterator<JSONObject> jsonObjectIterator = jsonArray.iterator();
        while (jsonObjectIterator.hasNext()){
            JSONObject jsonObject = jsonObjectIterator.next();
            String id = (String ) jsonObject.get("id");
            String name = (String) jsonObject.get("name");
            String dateOfBirth = (String ) jsonObject.get("dateOfBirth");
            String universityLocation = (String) jsonObject.get("universityLocation");
            String field = (String ) jsonObject.get("field");
            String workplace = (String) jsonObject.get("workplace");
            User user = new User(id, name, dateOfBirth, universityLocation, field, workplace);

            JSONArray specialtiesArray = (JSONArray) jsonObject.get("specialties");
            JSONArray connectionIdArray = (JSONArray) jsonObject.get("connectionId");
            user.specialties.addAll(specialtiesArray);
            user.connectionId.addAll(connectionIdArray);

            User.userArrayList.add(user);
            User.userMap.put(user.id, user);
        }
    }
    JSON_Reader() throws IOException, ParseException {
    }
}
class User{
    String id;
    String name;
    String dateOfBirth;
    String universityLocation;
    String field;
    String workplace;

    ArrayList<String> specialties = new ArrayList<>();
    ArrayList<String> connectionId = new ArrayList<>();
    ArrayList<User> closestFiveBFS = new ArrayList<>();
    ArrayList<User> connectedUsers = new ArrayList<>();
    static ArrayList<User> userArrayList = new ArrayList<>();
    static Map<String, User> userMap = new HashMap<>();
    Map<String, Integer> levelsOfClosestFiveBFS = new HashMap<>();
    Map<String, Integer> scores = new HashMap<>();


    public User(String id, String name, String dateOfBirth, String universityLocation, String field, String workplace) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.universityLocation = universityLocation;
        this.field = field;
        this.workplace = workplace;
    }
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", universityLocation='" + universityLocation + '\'' +
                ", field='" + field + '\'' +
                ", workplace='" + workplace + '\'' +
                ", specialties=" + specialties +
                ", connectionId=" + connectionId +
                '}';
    }
//    void findClosest20(Graph graph){
//        User user = this;
//        Deque<User> queue = new ArrayDeque<>();
//        Map<String, Boolean> visited = new HashMap<>();
//        for (int i = 0; i < User.userArrayList.size(); i++) {
//            visited.put(User.userArrayList.get(i).id, Boolean.FALSE);
//        }
//        System.out.println(visited.get("143"));
//        System.out.println(visited.size());
//        visited.put(user.id, true);
//        queue.add(user);
//        while (queue.size() != 0){
//            user = queue.poll();
//            if(this.closest20.size() < 20)
//                this.closest20.add(user);
//            else
//                break;
//
//            Iterator<User> iterator = user.connectedUsers.listIterator();
//            while (iterator.hasNext()){
//                User user1 = iterator.next();
//                if(!visited.get(user1.id)){
//                    visited.put(user1.id, true);
//                    queue.add(user1);
//                }
//            }
//        }
//
//    }
    void findClosestFiveBFS(Graph graph){
        User user = this;
        Map<String, Boolean> visited = new HashMap<>();
        Deque<User> queue = new ArrayDeque<>();
        for (int i = 0; i < User.userArrayList.size(); i++) {
            visited.put(User.userArrayList.get(i).id, false);
            levelsOfClosestFiveBFS.put(User.userArrayList.get(i).id, 0);
        }

        queue.add(user);
        levelsOfClosestFiveBFS.put(user.id, 0);
        visited.put(user.id, true);


        while (queue.size() > 0){
            user = queue.poll();
            for (int i = 0; i < user.connectedUsers.size(); i++) {
                User user1 = user.connectedUsers.get(i);
                if(!visited.get(user1.id)){
                    queue.add(user1);
                    levelsOfClosestFiveBFS.put(user1.id, levelsOfClosestFiveBFS.get(user.id) + 1);
                    if(levelsOfClosestFiveBFS.get(user1.id) <= 5 && levelsOfClosestFiveBFS.get(user1.id) > 1) {
                        closestFiveBFS.add(user1);
                    }
//                    else {
//                        endOfLevelFive = true;
//                        break;
//                    }
                    visited.put(user1.id, true);
                }
            }
        }
    }
    void suggestionList(){
        for (int i = 0; i < closestFiveBFS.size(); i++){
            int score = 0;
            User user = closestFiveBFS.get(i);
//            if(levelsOfClosestFiveBFS.get(user.id) < 1)
//                continue;
            score += (1 / levelsOfClosestFiveBFS.get(user.id)) * 100;
            if(this.universityLocation.equals(user.universityLocation))
                score += 100;
            if(this.field.equals(user.field))
                score += 200;
            if(this.workplace.equals(user.workplace))
                score += 125;
            for (int j = 0; j < this.specialties.size(); j++) {
                for (int k = 0; k < user.specialties.size(); k++) {
                    if(this.specialties.get(j).equals(user.specialties.get(k)))
                        score += 125;
                }
            }
            scores.put(user.id, score);
        }
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        for (Integer integer: list) {
            for (Map.Entry<String, Integer> entry: scores.entrySet()) {
                if(entry.getValue().equals(integer)){
                    sortedMap.put(entry.getKey(), integer);
                }
            }
        }
        System.out.println(sortedMap);
    }


}
class Graph{
//    class Vertex{
//        User user;
//        ArrayList<Vertex> vertices = new ArrayList<>();
//        public Vertex(User user) {
//            this.user = user;
//        }
//    }
    static ArrayList<User> userArrayList = new ArrayList<>();
//    static Map<User, Vertex> userVertexMap = new HashMap<>();
    void addVertices(){
        for (int i = 0; i < User.userArrayList.size(); i++) {
            User user = User.userArrayList.get(i);
            for (int j = 0; j < user.connectionId.size(); j++) {
                String id = user.connectionId.get(j);
                user.connectedUsers.add(User.userMap.get(id));
            }
            userArrayList.add(user);
        }
    }
}
class Interface{
    static User user = null;
    public static void mainMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n\n========= MainMenu ========= ");
        System.out.println("[1] Sign in");
        System.out.println("[2] UserList");
        System.out.println("[3] Search");
        System.out.println("============================");
        System.out.println("Please enter a number: ");

        int InputMenu = input.nextInt();

        switch (InputMenu) {
            case 1:
                signIn();
                break;

            case 2:
                userList();
                break;

            default:
                System.out.println("                 Please enter a valid number...");
                mainMenu();
                break;
        }

    }
    public static void signIn() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n\n========= Customer SignIn ========= ");
        System.out.println("(1) Please enter your id:");
        String id = input.nextLine();
        System.out.println("============================");
                User user = User.userMap.get(id);
        userPanel();
    }
    public static void userPanel() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n\n========= Panel ========= ");
        System.out.println("[1] Profile");
        System.out.println("[2] Suggestions");
        System.out.println("[3] Connect");
        System.out.println("[4] Log out");
        System.out.println("============================");
        System.out.println("Please enter a number: ");

        int InputMenu = input.nextInt();

        switch (InputMenu) {
            case 1:

                break;

            case 2:

                break;

            case 3:

                break;

            case 4:
                mainMenu();
                break;

            default:
                System.out.println("Please enter a valid number...");
                userPanel();
                break;
        }
    }
    public static void userList() {
        System.out.println("\n\n========= User List ========= ");
        for (int i = 0; i < User.userArrayList.size(); i++) {
            System.out.println(User.userArrayList.get(i));
        }
        System.out.println("============================");
//        User user = User.userMap.get(id);
        userPanel();
    }
}