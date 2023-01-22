import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class LMS {
//    static List<CourseDetails> coursesList = new ArrayList<>();
    static String[] studentsData;
    public static void main(String[] args) {

        String studentTextFile = getTxtFileData("/home/amira/A EG-FWD/Project1-LMS/out/production/Project1-LMS/Resources/student-data.txt");
        studentsData = convertTxtFileToCsv(studentTextFile);
        printAllStudents(studentsData);

        System.out.println("\n");
        System.out.println("----- All Courses List -----" + "\n");
        try {
            readCoursesData("/home/amira/A EG-FWD/Project1-LMS/out/production/Project1-LMS/Resources/coursedata.xml");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        //Testing JSON object
        System.out.println("\n");
        System.out.println("----- Print Courses enrolled by Students -----" + "\n");
        JSONObject jsonObject
                = constructStudentCourseDetailsJsonFile("1", new JSONObject(), new JSONArray("[1, 2, 3, 4]"));
        jsonObject = constructStudentCourseDetailsJsonFile("2", jsonObject, new JSONArray("[2, 4, 6]"));
        jsonObject = constructStudentCourseDetailsJsonFile("3", jsonObject, new JSONArray("[1, 3, 5]"));
        System.out.println(jsonObject);

    }

    public static String getTxtFileData(String path) {
        File file = new File(path);
        String fileData = null;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                fileData = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return fileData;
    }

    public static String[] convertTxtFileToCsv(String TxtfileData) {
        TxtfileData = TxtfileData.replace("#", ", ");
        TxtfileData = TxtfileData.replace("$", "\n");
        String[] newTxtFileData = TxtfileData.split("\n");

        for (int i = 0; i < newTxtFileData.length; i++) {
            if (i == 0) {
                newTxtFileData[i] = "id, " + newTxtFileData[i];
            } else {
                newTxtFileData[i] = i + ", " + newTxtFileData[i];
            }
        }
        return newTxtFileData;
    }

    public static void readCoursesData(String filePath) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document newFile = dBuilder.parse(file);
        newFile.getDocumentElement().normalize();

        NodeList nodelist = newFile.getElementsByTagName("row");
        System.out.println("id, " + "Course Name, " + "Instructor, " + "Course Duration, " + "Course Time, " + "Location");

        for (int i = 0; i < nodelist.getLength(); i++) {
            Node tag = nodelist.item(i);
            if (tag.getNodeType() == tag.ELEMENT_NODE) {
                Element tagElement = (Element) tag;
                CourseDetails courseDetail = new CourseDetails(tagElement.getElementsByTagName("id").item(0).getTextContent(),
                        tagElement.getElementsByTagName("CourseName").item(0).getTextContent(),
                        tagElement.getElementsByTagName("Instructor").item(0).getTextContent(),
                        tagElement.getElementsByTagName("CourseDuration").item(0).getTextContent(),
                        tagElement.getElementsByTagName("CourseTime").item(0).getTextContent(),
                        tagElement.getElementsByTagName("Location").item(0).getTextContent());

                System.out.print(courseDetail.id + ", ");
                System.out.print(courseDetail.courseName + ", ");
                System.out.print(courseDetail.courseInstructor + ", ");
                System.out.print(courseDetail.courseDuration + ", ");
                System.out.print(courseDetail.courseTime + ", ");
                System.out.println(courseDetail.courseHall);

            }
        }
    }

    public static void printAllStudents(String[] studentsFile) {
        System.out.println("------------------------------");
        System.out.println("Current Student List");
        System.out.println("------------------------------");
        for (String s : studentsFile) {
            System.out.println(s);
        }

    }

    public static void printStudentDataWithId (String[]studentsFile,int id){
        if (id == 0 || id > studentsFile.length) {
            System.out.println("Invalid ID, please enter ID between 1 to 100");
        } else {
            System.out.println("===================================================");
            System.out.println("Student Details page");
            System.out.println("===================================================");
            String selectedStudent = studentsFile[id];
            String[] selectedStudentData = selectedStudent.split(",");
            System.out.println("Name: " + selectedStudentData[1]);
            System.out.println("Grade: " + selectedStudentData[2]);
            System.out.println("Email: " + selectedStudentData[3]);
            System.out.println("---------------------------------------------------");

            System.out.println("Enrolled Courses.");
            // print enrolled courses
            }
        }

    public static JSONObject constructStudentCourseDetailsJsonFile(String studentId, JSONObject jsonObject, JSONArray enrolledCoursesIdList) {
            jsonObject.put(studentId, enrolledCoursesIdList);
        int numberOfcourses = ((JSONArray) jsonObject.get(studentId)).length();
        if (numberOfcourses > 6) {
            throw new RuntimeException("Number of courses exceed 6 courses.");
        }
        return jsonObject;
    }

    static class CourseDetails {
        String id;
        String courseName;
        String courseInstructor;
        String courseDuration;
        String courseTime;
        String courseHall;

        public CourseDetails(String id, String courseName, String courseInstructor, String courseDuration, String courseTime, String courseHall) {
            this.id = id;
            this.courseName = courseName;
            this.courseInstructor = courseInstructor;
            this.courseDuration = courseDuration;
            this.courseTime = courseTime;
            this.courseHall = courseHall;
        }
    }
}
