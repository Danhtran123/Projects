import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/*
1. Collect the whole file to a string type
2. Read the entire string and set each question into an array without the numbers
3. Create method for shuffling the questions
3a. Its as easy as using the prebuilt Collections.shuffle method
4. Create method for shuffling the answers per question
4a. For loop to questions array length; Store the answers in an array and shuffle it;
4b. re-append the answer array into the questions array index
 */


public class TestShuffler {
    //File pathname should be the location of the file you want to shuffle.
    final static File file = new File("C:\\Users\\Danh\\Desktop\\test.txt");

    public static void shuffleQuestions(ArrayList a) {
        Collections.shuffle(a);
    }

    public static void shuffleAnswers(ArrayList<String> a) {
        int count = 0;
        for(String s : a) {
            StringBuilder sb = new StringBuilder();
            ArrayList<String> answersList = new ArrayList<>();
            String currentLine;
            char letter = 'a';
            try {
                BufferedReader reader = new BufferedReader(new StringReader(s));
                while ((currentLine = reader.readLine()) != null) {
                    //removes newline between each question to make formatting easier
                    if(currentLine.equals(""))
                        continue;
                    //To keep the format of the original we append a new line when
                    //the current line is not the start of an answer
                    if(!currentLine.contains(letter + ". "))
                        sb.append("\n");
                    else {
                        answersList.add(sb.toString());
                        sb.setLength(0);
                        letter++;
                    }
                    sb.append(currentLine);
                }
                //adds the last answer
                answersList.add(sb.toString());
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            sb.setLength(0);
            //add question to sb then remove from answerslist
            sb.append((count+1) + ". " + answersList.get(0).trim());
            answersList.remove(0);
            Collections.shuffle(answersList);
            letter = 'a';
            for(String j : answersList) {
                sb.append("\n" + letter + ". " + j.substring(3));
                letter++;
            }
            a.set(count,sb.toString());
            count++;
        }
    }

    public static void createFile(ArrayList l) {
        String newFile = file.getParent() + "\\" + "New" + file.getName();
        Path file1 = Paths.get(newFile);
        try {
            Files.write(file1, l, StandardCharsets.UTF_8);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[]args) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String>questionList = new ArrayList<>();
        String currentLine;
        String questionCount = "1";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while((currentLine = reader.readLine()) != null) {
                //Once we reach a new question, Add the current question into the arraylist
                //empty the SB and increment the question count for the conditional statement
                //remove the question number from the line
                if(currentLine.contains(questionCount + ". ")) {
                    questionList.add(sb.toString());
                    sb.setLength(0);
                    currentLine = currentLine.substring(currentLine.indexOf(questionCount + ". ")+3);
                    questionCount = Integer.toString(Integer.parseInt(questionCount) + 1);
                }
                sb.append(currentLine);
                sb.append("\n");
            }
            questionList.remove(0);
            //adds the last question into the question list
            questionList.add(sb.toString());
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        shuffleQuestions(questionList);
        shuffleAnswers(questionList);
        createFile(questionList);
    }
}
