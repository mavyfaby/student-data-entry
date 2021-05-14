import javax.swing.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ================================================
 * Author: Maverick G. Fabroa
 * ================================================
 * Date: May 14, 2021
 * ================================================
 * Java SDK Version: 11
 * ================================================
 */

/**
 * Custom Student Data Handler from JTextArea component
 */
public class MavyTextAreaHandler {
    // Separator
    private static final String SEPARATOR = "==============================\n";

    // Total fields
    private static final int FIELDS = 6;

    // DATA
    private static final List<HashMap<String, String>> STUDENT_INFORMATION = new ArrayList<>();

    /**
     * Static properties
     */

    public static final int SUCCESS = 1;
    public static final int EXIST = 2;
    public static final int EMPTY = 3;
    public static final int ERROR = 4;
    public static final int OUT_OF_BOUNDS = 5;
    public static final int AGE_NOT_NUMBER = 6;
    public static final int EQUAL = 7;

    private JTextArea textArea = null;

    public MavyTextAreaHandler(JTextArea c) {
        this.textArea = c;
    }

    /**
     * Get caret position and text in the text atea
     * 
     * @param textArea
     * @return HashMap<String, Object>
     */
    public HashMap<String, Object> getHighlightedData() {
        final String TEXT = this.textArea.getText();
        final String[] LINES = TEXT.split("\n");
        final int POS = this.textArea.getCaretPosition();

        HashMap<String, Object> output = new HashMap<>();
        HashMap<String, String> studentData = null;

        int endPos = 0;
        int startPos = 0;
        int index = -1;
        int textLength = 0;

        final List<Integer> textLengths = new ArrayList<>();

        
        for (int i = 0; i < LINES.length; i++) {
            final String line = LINES[i];

            // Add to text length for caret position tracking
            textLength += line.length() + 1;

            // Add length to the array to track the separator line number
            textLengths.add(textLength);

            if (POS <  textLength) {
                final String SEP = SEPARATOR.substring(0, SEPARATOR.length() - 1);

                if (line.equals(SEP)) {
                    break;
                }

                // Check left separator
                for (int j = i - 1; j >= 0; j--) {
                    final String newLine = LINES[j];
                    
                    if (newLine.equals(SEP)) {
                        startPos = textLengths.get(j);
                        break;
                    }
                }

                // Check right separator
                for (int j = i + 1; j < LINES.length; j++) {
                    final String newLine = LINES[j];

                    textLengths.add(textLength += (newLine.length() + 1));
                    
                    if (newLine.equals(SEP)) {
                        endPos = textLengths.get(j - 1);
                        break;
                    }
                    
                }
                
                break;
            }
        }

        if (STUDENT_INFORMATION.size() > 0) {
            // Get the text from text area starting from index 0 to POS
            final String LEFT_STRING = TEXT.substring(0, POS);
            // Separator occurences
            int occurences = 0;
            int lastIndex = 0;

            while (lastIndex != -1) {
                lastIndex = LEFT_STRING.indexOf(SEPARATOR, lastIndex);

                if (lastIndex != -1) {
                    occurences++;
                    lastIndex += SEPARATOR.length();
                }
            }

            // Limit the index
            index = occurences - 1 >= STUDENT_INFORMATION.size() ? -1 : occurences - 1;

            // Get student data out of the index
            if (index >= 0 && index < STUDENT_INFORMATION.size() && (endPos - startPos > 0)) {
                studentData = STUDENT_INFORMATION.get(index);
            }
        }
        
        // If start position is greater than the end position,
        // Make it 0
        output.put("startPos", startPos > endPos ? 0 : startPos);
        output.put("endPos", endPos);
        output.put("studentData", studentData);
        output.put("index", index);
        output.put("total", STUDENT_INFORMATION.size());

        return output;
    }

    
    /**
     * Extract input from student data
     * 
     * @param line
     * @return HashMap<String, String>
     */
    public HashMap<String, String> extractStudentData(String line) {
        HashMap<String, String> output = new HashMap<String, String>();
        
        final String[] fields = line.split("~");
        
        if (fields.length == FIELDS) {
            output.put("id", fields[0]);
            output.put("age", fields[1]);
            output.put("firstName", fields[2]);
            output.put("lastName", fields[3]);
            output.put("MI", fields[4]);
            output.put("course", fields[5]);
            
            return output;
        }
        
        return null;
    }
    
    /**
     * Check if an id does exist on the student data
     * 
     * @param id
     * @return boolean
     */
    public boolean isStudentIDExist(String id) {
        final String TRIMMED_ID = id.trim();
        
        // Iterate through student information list
        for (HashMap<String, String> studentData : STUDENT_INFORMATION) {
            if (studentData != null) {
                if (TRIMMED_ID.equals((String)studentData.get("id"))) {
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * Check if an id does exist in the student data text area except the specifiedRow
     * 
     * @param id
     * @param lineException
     * @return
     */
    public boolean isStudentIDExist(String id, int indexException) {
        final String TRIMMED_ID = id.trim();
        
        for (int i = 0; i <STUDENT_INFORMATION.size(); i++) {
            // If current loop row is equal to lineException, continue to next loop
            if (i == indexException) {
                continue;
            }

            // Extract student data
            HashMap<String, String> studentData = STUDENT_INFORMATION.get(i);
            
            // Check if student id exists
            if (studentData != null && TRIMMED_ID.equals((String)studentData.get("id"))) {
                return true;
            }
        }
        
        // Otherwise, student id doesn't exists
        return false;
    }
    
    /**
     * Get student data by ID
     * 
     * @param id
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getStudentDataByID(String id) {
        final String TRIMMED_ID = id.trim();
        
        HashMap<String, String> output = null;
        
        // Iterate through lines in the text area
        for (HashMap<String, String> studentData : STUDENT_INFORMATION) {
            if (studentData != null && TRIMMED_ID.equals((String)studentData.get("id"))) {
                output = studentData;
                break;
            }
        }
        
        return output;
    }

    /**
     * Get student data by ID with index exception
     * 
     * @param id
     * @param indexException
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getStudentDataByID(String id, int indexException) {
        final String TRIMMED_ID = id.trim();
        
        HashMap<String, String> output = null;
        
        // Iterate through lines in the text area
        for (int i = 0; i < STUDENT_INFORMATION.size(); i++) {
            // If current loop row is equal to lineException, continue to next loop
            if (i == indexException) {
                continue;
            }

            // Extract student data
            HashMap<String, String> studentData = STUDENT_INFORMATION.get(i);

            // If the passed student id does exist in the text area, then return it
            if (studentData != null && TRIMMED_ID.equals((String)studentData.get("id"))) {
                output = studentData;
                break;
            }
        }
        
        return output;
    }

    /**
     * Encode student data
     * 
     * @param id
     * @param age
     * @param lastName
     * @param firstName
     * @param MI
     * @param course
     * @return
     */
    public String encodeStudentData(String id, String age, String lastName, String firstName, String MI, String course, boolean isPrintLastSeparator) {
        String output = "";

        if (this.textArea.getText().length() == 0) {
            output += SEPARATOR;
        }
    
        // Trim extra leading and trailing spaces
        output += "Student ID:    " + id.trim() + "\n";
        output += "Age:           " + age.trim() + "\n";
        output += "Last name:     " + lastName.trim() + "\n";
        output += "First name:    " + firstName.trim() + "\n";
        output += "M.I.:          " + MI.trim() + "\n";
        output += "Course Taken:  " + course.trim() + "\n";

        if (isPrintLastSeparator) {
            output += SEPARATOR;
        }

        return output;
    }

    public String encodeStudentData(HashMap<String, String> studentData, boolean isPrintLastSeparator) {
        String output = null;

        if (studentData != null) {
            // Extract data from student data
            final String ID = (String)studentData.get("id");
            final String AGE = (String)studentData.get("age");
            final String LASTNAME = (String)studentData.get("lastName");
            final String FIRSTNAME = (String)studentData.get("firstName");
            final String MI = (String)studentData.get("MI");
            final String COURSE = (String)studentData.get("course");

            // Encode using the encode student data with string params
            return encodeStudentData(ID, AGE, LASTNAME, FIRSTNAME, MI, COURSE, isPrintLastSeparator);
        }

        return output;
    }
    
    /**
     * Print student information to the text area
     */
    private void printStudentInformation() {
        // Clear the text area 
        this.textArea.setText("");

        // Set the text area to the new data, separated with a new line
        String newData = "";

        for (int i = 0; i < STUDENT_INFORMATION.size(); i++) {
            newData += encodeStudentData(STUDENT_INFORMATION.get(i), i == STUDENT_INFORMATION.size() - 1);
        }

        this.textArea.setText(newData);
    }

    /**
     * Check whether the giver student data are equal
     * 
     * @param studentData1
     * @param studentData2
     * @return boolean
     */
    private boolean isStudentDataEqual(HashMap<String, String> studentData1, HashMap<String, String> studentData2) {
        if (studentData1 != null && studentData2 != null) {
            final String[] KEYS = { "id", "age", "lastName", "firstName", "MI", "course" };

            for (String key : KEYS) {
                final String DATA1 = (String)studentData1.get(key);
                final String DATA2 = (String)studentData2.get(key);

                if (!DATA1.equals(DATA2)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    // =========================================================================== //

    /**
     * Append data to text area
     * 
     * @param ID
     * @param age
     * @param firstName
     * @param lastName
     * @param MI
     * @param course
     * @return int
     */
    public int appendStudentData(String id, String age, String lastName, String firstName, String MI, String course) {
        // Check all student data except M.I, since it's not required
        // And some students doesn't have middle names
        if (!MavyDataEntryProps.isBlank(id, age, lastName, firstName, course)) {
            // Check if the age is a number
            try {
                Double.parseDouble(age);
            } catch (NumberFormatException e) {
                // Age is not a number
                return AGE_NOT_NUMBER;
            }

            // Check if the passed student id does NOT exist
            if (!isStudentIDExist(id)) {
                // Encode the passed student data
                final String ENCODED_DATA = encodeStudentData(id, age, lastName, firstName, MI, course, true);
                // Create a copy for the student data
                final HashMap<String, String> copyStudentData = new HashMap<>();

                copyStudentData.put("id", id);
                copyStudentData.put("age", age);
                copyStudentData.put("lastName", lastName);
                copyStudentData.put("firstName", firstName);
                copyStudentData.put("MI", MI);
                copyStudentData.put("course", course);

                // Add to the student information variable
                STUDENT_INFORMATION.add(copyStudentData);

                // Append it to the text area
                this.textArea.append(ENCODED_DATA);
    
                // Append data success
                return SUCCESS;
            }
    
            // Student ID exist
            return EXIST;
        }
    
        // Input fields empty
        return EMPTY;
    }

    /**
     * Update student data
     * 
     * @param id
     * @param age
     * @param lastName
     * @param firstName
     * @param MI
     * @param course
     * @param line
     * @return int
     */
    public int updateStudentData(String id, String age, String lastName, String firstName, String MI, String course, int index) {
        if (!MavyDataEntryProps.isBlank(id, age, lastName, firstName, course)) {
            // Get the student data that will update 
            final HashMap<String, String> currentData = STUDENT_INFORMATION.get(index);

            // Check if the age is a number
            try {
                Double.parseDouble(age);
            } catch (NumberFormatException e) {
                // Age is not a number
                return AGE_NOT_NUMBER;
            }

            // If the student data selected does exist, check first id duplicates
            if (!isStudentIDExist(id, index) || id.equals((String)currentData.get("id"))) {
                final HashMap<String, String> studentData = new HashMap<>();
                
                studentData.put("id", id);
                studentData.put("age", age);
                studentData.put("lastName", lastName);
                studentData.put("firstName", firstName);
                studentData.put("MI", MI);
                studentData.put("course", course);

                // Check if the new data is the same as
                if (isStudentDataEqual(currentData, studentData)) {
                    return EQUAL;
                }
                
                // Update the line to the new student data
                STUDENT_INFORMATION.set(index, studentData);

                // Print student information to the text area
                printStudentInformation();
    
                return SUCCESS;
            }

            return EXIST;
        }

        return EMPTY;
    }

    /**
     * Delete students data
     * 
     * @param row
     * @return
     */
    public int deleteStudentData(int index) {
        // If index is not out of range of the student information
        if (STUDENT_INFORMATION.size() > 0 && index >= 0 && index < STUDENT_INFORMATION.size()) {
            STUDENT_INFORMATION.remove(index);

            // Print student information to the text area
            printStudentInformation();
        }

        // Otherwise, return out of bounds
        return OUT_OF_BOUNDS;
    }
}