import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.CaretEvent;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.List;
import java.util.HashMap;

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
 * Custom Panel
 */
class MavyPanel extends JPanel {
    public MavyPanel() {
        setBackground(MavyDataEntryProps.DARK_COLOR);
    }

    public MavyPanel(LayoutManager l) {
        this();
        setLayout(l);
    }
}

/**
 * Custom Label
 */
class MavyLabel extends JLabel {
    public MavyLabel(String text) {
        super(text);
        setForeground(MavyDataEntryProps.FORE_COLOR);
    }
    
    public MavyLabel(String text, int size) {
        this(text);
        setFont(new Font("Arial", Font.BOLD, size));
    }
}

/**
 * Custom button
 */
class MavyButton extends JButton {
    private Color fgColor = MavyDataEntryProps.FORE_COLOR;

    public MavyButton(String text, Color fgColor) {
        super(text);
        this.fgColor = fgColor;

        setBorderPainted(false);
        setFocusPainted(false);
        setCustomEnabled(true);
        setContentAreaFilled(false);
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setFont(MavyDataEntryProps.createFont(16));
        setBackground(MavyDataEntryProps.SURFACE_COLOR);

        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                if (getModel().isPressed()) {
                    setBackground(MavyDataEntryProps.SURFACE_COLOR);
                } else if (getModel().isRollover()) {
                    setBackground(MavyDataEntryProps.DISABLED_COLOR);
                } else {
                    setBackground(MavyDataEntryProps.SURFACE_COLOR);
                }
            }
        });
    }

    /**
     * Custom set enabled method for the buttons
     * 
     * @param b
     */
    public void setCustomEnabled(boolean b) {
        setEnabled(b);

        if (b) {
            setForeground(fgColor);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }
}

/**
 * Custom input
 */
class MavyInput extends JTextField {
    private Shape shape;
    private final int BORDER_RADIUS = 24;

    public MavyInput(int cols) {
        super(cols);
        setBorder(MavyDataEntryProps.PADDING);
        setForeground(MavyDataEntryProps.FORE_COLOR);
        setFont(MavyDataEntryProps.createFont(18));
        setCaretColor(MavyDataEntryProps.FORE_COLOR);
        setCustomEnabled(false);
        setOpaque(false);
    }

    public void setCustomEnabled(boolean b) {
        setEnabled(b);
        setBackground(b ? MavyDataEntryProps.SURFACE_COLOR : MavyDataEntryProps.DISABLED_COLOR);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);
        super.paintComponent(g);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1,  BORDER_RADIUS, BORDER_RADIUS);
        }

        return shape.contains(x, y);
    }
}

public class MavyDataEntry {
    /**
     * === 16 / 9 aspect ratio ===
     */
    private static final Dimension WIN_SIZE = new Dimension(1360, 765);

    /**
     * Whether is in adding state
     */
    private static boolean isAdding = false;

    public static void main(String[] args) {
        // Main frame
        JFrame frame = new JFrame("Student Data Entry Form");

        frame.setSize(WIN_SIZE);
        frame.setMinimumSize(WIN_SIZE);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * Set icon
         */

        try {
            URL image = new File(System.getProperty("user.dir") + "/mavy-icon.png").toURI().toURL();
    
            if (image != null) {
                ImageIcon icon = new ImageIcon(image);
                frame.setIconImage(icon.getImage());
            }
        }
            catch (MalformedURLException e) {
                // Do nothing
            }

        // =================== LAYOUTS =================== //

        final GridLayout inputLayout = new GridLayout(3, 2);
        final GridLayout fieldLayout = new GridLayout(2, 1);
        final GridLayout buttonLayout = new GridLayout(1, 5);

        inputLayout.setHgap(32);
        inputLayout.setVgap(32);

        buttonLayout.setHgap(32);

        // ================== COMPONENTS ================== //

        MavyPanel panel = new MavyPanel(new GridBagLayout());

        MavyPanel leftPanel = new MavyPanel(new BorderLayout());
        MavyPanel rightPanel = new MavyPanel(new BorderLayout());

        // Left panel's components
        MavyPanel leftNorthPanel = new MavyPanel(new FlowLayout(FlowLayout.LEFT));
        MavyLabel dataLabel = new MavyLabel("Total Students:", 18);
        MavyLabel totalStudentsLabel = new MavyLabel("0", 20);

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Right panel's components
        MavyPanel rightNorthPanel = new MavyPanel(new GridLayout(1, 1));
        MavyPanel rightCenterPanel = new MavyPanel(inputLayout);
        MavyPanel rightSouthPanel = new MavyPanel(buttonLayout);

        // North compoenent
        MavyLabel title = new MavyLabel("Student Information", 28);

        // Center components

        MavyPanel panelID = new MavyPanel(fieldLayout);
        MavyPanel panelAge = new MavyPanel(fieldLayout);
        MavyPanel panelFirstname = new MavyPanel(fieldLayout);
        MavyPanel panelLastname = new MavyPanel(fieldLayout);
        MavyPanel panelMI = new MavyPanel(fieldLayout);
        MavyPanel panelCourse = new MavyPanel(fieldLayout);

        MavyLabel labelID = new MavyLabel("Student ID *", 20);
        MavyLabel labelAge = new MavyLabel("Age *", 20);
        MavyLabel labelFirstname = new MavyLabel("Firstname *", 20);
        MavyLabel labelLastname = new MavyLabel("Lastname *", 20);
        MavyLabel labelMI = new MavyLabel("M.I.", 20);
        MavyLabel labelCourse = new MavyLabel("Course Taken *", 20);

        MavyInput fieldID = new MavyInput(15);
        MavyInput fieldAge = new MavyInput(15);
        MavyInput fieldFirstname = new MavyInput(15);
        MavyInput fieldLastname = new MavyInput(15);
        MavyInput fieldMI = new MavyInput(15);
        MavyInput fieldCourse = new MavyInput(15);
        
        // South components

        MavyButton addButton = new MavyButton("Add", MavyDataEntryProps.LIGHTGREEN_COLOR);
        MavyButton updateButton = new MavyButton("Update", MavyDataEntryProps.LIGHTBLUE_COLOR);
        MavyButton saveButton = new MavyButton("Save", MavyDataEntryProps.LIGHTGREEN_COLOR);
        MavyButton deleteButton = new MavyButton("Delete", MavyDataEntryProps.LIGHTRED_COLOR);
        MavyButton exitButton = new MavyButton("Exit", MavyDataEntryProps.LIGHTRED_COLOR);

        // ==================== STYLES ==================== //

        panel.setBorder(MavyDataEntryProps.PADDING);

        // Left Panel
        leftNorthPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));
        totalStudentsLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        totalStudentsLabel.setForeground(MavyDataEntryProps.LIGHTGREEN_COLOR);

        textArea.setBackground(MavyDataEntryProps.SURFACE_COLOR);
        textArea.setForeground(MavyDataEntryProps.WHITE_COLOR);
        textArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        textArea.setCaretColor(MavyDataEntryProps.WHITE_COLOR);
        textArea.setEditable(false);
        
        // Caret listener in the text field
        textArea.addCaretListener(new CaretListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void caretUpdate(CaretEvent ev) {
                try {
                    final MavyTextAreaHandler textAreaHandler = new MavyTextAreaHandler(textArea);
                    final HashMap<String, Object> highlightedData = textAreaHandler.getHighlightedData();
                
                    final int startPos = (int)highlightedData.get("startPos");
                    final int endPos = (int)highlightedData.get("endPos");
                    final int index = (int)highlightedData.get("index");
                    final HashMap<String, String> studentData = (HashMap<String, String>)highlightedData.get("studentData");

                    // Two dimensional array
                    final List<int[]> sepPos = (List<int[]>)highlightedData.get("sepPos");

                    Color highlightColor = MavyDataEntryProps.HIGHLIGHT_COLOR;

                    switch (index % 4) {
                        case 0:
                            highlightColor = MavyDataEntryProps.RED_COLOR;
                            break;
                        case 1:
                            highlightColor = MavyDataEntryProps.YELLOW_COLOR;
                            break;
                        case 2:
                            highlightColor = MavyDataEntryProps.GREEN_COLOR;
                            break;
                        case 3:
                            highlightColor = MavyDataEntryProps.BLUE_COLOR;
                            break;
                    }
    
                    final DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(highlightColor);
                    final DefaultHighlighter highlighter = (DefaultHighlighter)textArea.getHighlighter();

                    highlighter.removeAllHighlights();

                    // Add highlight to selected student data
                    highlighter.addHighlight(startPos, endPos, painter);

                    // Loop through the separator positions in the the text area
                    for (int[] pos : sepPos) {
                        // Add highlight to the separators
                        highlighter.addHighlight(pos[0], pos[1], new DefaultHighlighter.DefaultHighlightPainter(MavyDataEntryProps.DISABLED_COLOR));
                    }

                    /**
                     * Set Update and Delete button
                     */

                    // If there is a highlighted text, enable update and delete buttons
                    if (isAdding) {
                        updateButton.setCustomEnabled(endPos - startPos > 0);
                    }

                    deleteButton.setCustomEnabled(endPos - startPos > 0);

                    // When picking a data from the text area
                    // Set the value of the text fields from the selected data
                    if (isAdding) {
                        fieldID.setText(studentData != null ? (String)studentData.get("id") : "");
                        fieldAge.setText(studentData != null ? (String)studentData.get("age") : "");
                        fieldLastname.setText(studentData != null ? (String)studentData.get("lastName") : "");
                        fieldFirstname.setText(studentData != null ? (String)studentData.get("firstName") : "");
                        fieldMI.setText(studentData != null ? (String)studentData.get("MI") : "");
                        fieldCourse.setText(studentData != null ? (String)studentData.get("course") : "");
                    }
                }
                    catch (BadLocationException e) {
                        System.out.println("MavyError: " + e.getMessage());
                    }
            }
        });

        scrollPane.setBorder(null);

        leftPanel.setBorder(MavyDataEntryProps.PADDING);

        // Right Panel

        fieldID.setForeground(MavyDataEntryProps.LIGHTRED_COLOR);
        fieldAge.setForeground(MavyDataEntryProps.LIGHTRED_COLOR);
        fieldFirstname.setForeground(MavyDataEntryProps.LIGHTYELLOW_COLOR);
        fieldLastname.setForeground(MavyDataEntryProps.LIGHTYELLOW_COLOR);
        fieldMI.setForeground(MavyDataEntryProps.LIGHTGREEN_COLOR);
        fieldCourse.setForeground(MavyDataEntryProps.LIGHTGREEN_COLOR);

        // Disable buttons except add and exit button on start up
        updateButton.setCustomEnabled(false);
        deleteButton.setCustomEnabled(false);
        saveButton.setCustomEnabled(false);

        rightNorthPanel.setBorder(MavyDataEntryProps.PADDING);
        rightCenterPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 54, 16));
        rightSouthPanel.setBorder(MavyDataEntryProps.PADDING);

        rightPanel.setBorder(MavyDataEntryProps.PADDING);

        // =============== EVENT LISTENERS =============== //

        final ActionListener addEventListener = new ActionListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void actionPerformed(ActionEvent e) {
                isAdding = true;
                
                fieldID.setCustomEnabled(true);
                fieldAge.setCustomEnabled(true);
                fieldFirstname.setCustomEnabled(true);
                fieldLastname.setCustomEnabled(true);
                fieldMI.setCustomEnabled(true);
                fieldCourse.setCustomEnabled(true);

                title.setForeground(MavyDataEntryProps.WHITE_COLOR);

                labelID.setForeground(MavyDataEntryProps.LIGHTRED_COLOR);
                labelAge.setForeground(MavyDataEntryProps.LIGHTRED_COLOR);
                labelFirstname.setForeground(MavyDataEntryProps.LIGHTYELLOW_COLOR);
                labelLastname.setForeground(MavyDataEntryProps.LIGHTYELLOW_COLOR);
                labelMI.setForeground(MavyDataEntryProps.LIGHTGREEN_COLOR);
                labelCourse.setForeground(MavyDataEntryProps.LIGHTGREEN_COLOR);

                // Self sacrifice
                addButton.setCustomEnabled(false);
                saveButton.setCustomEnabled(true);

                // Enable update button when adding if only there is a selected data from the text area
                final MavyTextAreaHandler textAreaHandler = new MavyTextAreaHandler(textArea);
                final HashMap<String, Object> highlightedData = textAreaHandler.getHighlightedData();
                final HashMap<String, String> studentData = (HashMap<String, String>)highlightedData.get("studentData");

                if (studentData != null) {
                    updateButton.setCustomEnabled(true);
                }
            }
        };

        final ActionListener saveButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String STUDENT_ID = fieldID.getText();
                final String AGE = fieldAge.getText();
                final String FIRSTNAME = fieldFirstname.getText();
                final String LASTNAME = fieldLastname.getText();
                final String MI = fieldMI.getText();
                final String COURSE_TAKEN = fieldCourse.getText();

                /**
                 * Check if all fields are filled in
                 */

                final MavyTextAreaHandler textAreaHandler = new MavyTextAreaHandler(textArea);
                final int result = textAreaHandler.appendStudentData(STUDENT_ID, AGE, LASTNAME, FIRSTNAME, MI, COURSE_TAKEN);

                // If success, disable inputs 
                if (result == MavyTextAreaHandler.SUCCESS) {
                    isAdding = false;

                    // Get total students and set it to the label
                    totalStudentsLabel.setText(textAreaHandler.getTotalStudents());

                    fieldID.setText("");
                    fieldAge.setText("");
                    fieldFirstname.setText("");
                    fieldLastname.setText("");
                    fieldMI.setText("");
                    fieldCourse.setText("");

                    fieldID.setCustomEnabled(false);
                    fieldAge.setCustomEnabled(false);
                    fieldFirstname.setCustomEnabled(false);
                    fieldLastname.setCustomEnabled(false);
                    fieldMI.setCustomEnabled(false);
                    fieldCourse.setCustomEnabled(false);

                    title.setForeground(MavyDataEntryProps.FORE_COLOR);

                    labelID.setForeground(MavyDataEntryProps.FORE_COLOR);
                    labelAge.setForeground(MavyDataEntryProps.FORE_COLOR);
                    labelFirstname.setForeground(MavyDataEntryProps.FORE_COLOR);
                    labelLastname.setForeground(MavyDataEntryProps.FORE_COLOR);
                    labelMI.setForeground(MavyDataEntryProps.FORE_COLOR);
                    labelCourse.setForeground(MavyDataEntryProps.FORE_COLOR);

                    addButton.setCustomEnabled(true);
                    updateButton.setCustomEnabled(false);
                    saveButton.setCustomEnabled(false);
                }  
                    // Age is not a number
                    else if (result == MavyTextAreaHandler.AGE_NOT_NUMBER) {
                        new MavyDialog("Save Error", "Age must be a number", "Okay", MavyDialog.MESSAGE_TYPE);
                    }

                    // One of the required inputs are empty
                    else if (result == MavyTextAreaHandler.EMPTY) {
                        new MavyDialog("Save Error", "Please fill in all the required fields", "Okay", MavyDialog.MESSAGE_TYPE);
                    }
                    
                    // Student ID exists
                    else if (result == MavyTextAreaHandler.EXIST) {
                        final HashMap<String, String> studentData = textAreaHandler.getStudentDataByID(STUDENT_ID);

                        // Fail safe
                        String strToAppend = "exist";

                        if (studentData != null) {
                            final String firstName = (String)studentData.get("firstName");
                            final String lastName = (String)studentData.get("lastName");

                            strToAppend = "used by<br />\"" + firstName + " " + lastName + "\""; 
                        }

                        new MavyDialog("Save Error", "Student ID already " + strToAppend, "Okay", MavyDialog.MESSAGE_TYPE);
                    }
            }
        };

        final ActionListener updateButtonListener = new ActionListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void actionPerformed(ActionEvent e) {
                final MavyTextAreaHandler textAreaHandler = new MavyTextAreaHandler(textArea);
                final HashMap<String, Object> selected = textAreaHandler.getHighlightedData();
                final HashMap<String, String> selectedData = (HashMap<String, String>)selected.get("studentData");
                final int indexFromData = (int)selected.get("index");
                
                final String STUDENT_ID = fieldID.getText();
                final String AGE = fieldAge.getText();
                final String FIRSTNAME = fieldFirstname.getText();
                final String LASTNAME = fieldLastname.getText();
                final String MI = fieldMI.getText();
                final String COURSE_TAKEN = fieldCourse.getText();

                // Exception for Middle Name
                final boolean isDataBlank = MavyDataEntryProps.isBlank(STUDENT_ID, AGE, LASTNAME, FIRSTNAME, COURSE_TAKEN);

                if (selectedData != null && isAdding && !isDataBlank) {
                    MavyDialog dialog = new MavyDialog("Update", "Are you sure you want to update the selected data?", "Update", MavyDialog.CONFIRM_TYPE);
    
                    dialog.addAcceptListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            final int result = textAreaHandler.updateStudentData(STUDENT_ID, AGE, LASTNAME, FIRSTNAME, MI, COURSE_TAKEN, indexFromData);

                            // Update student data
                            if (result == MavyTextAreaHandler.SUCCESS) {
                                isAdding = false;

                                fieldID.setText("");
                                fieldAge.setText("");
                                fieldFirstname.setText("");
                                fieldLastname.setText("");
                                fieldMI.setText("");
                                fieldCourse.setText("");

                                fieldID.setCustomEnabled(false);
                                fieldAge.setCustomEnabled(false);
                                fieldFirstname.setCustomEnabled(false);
                                fieldLastname.setCustomEnabled(false);
                                fieldMI.setCustomEnabled(false);
                                fieldCourse.setCustomEnabled(false);

                                title.setForeground(MavyDataEntryProps.FORE_COLOR);

                                labelID.setForeground(MavyDataEntryProps.FORE_COLOR);
                                labelAge.setForeground(MavyDataEntryProps.FORE_COLOR);
                                labelFirstname.setForeground(MavyDataEntryProps.FORE_COLOR);
                                labelLastname.setForeground(MavyDataEntryProps.FORE_COLOR);
                                labelMI.setForeground(MavyDataEntryProps.FORE_COLOR);
                                labelCourse.setForeground(MavyDataEntryProps.FORE_COLOR);

                                addButton.setCustomEnabled(true);
                                saveButton.setCustomEnabled(false);

                                // Remove all highlights from text area
                                textArea.getHighlighter().removeAllHighlights();
                            }
                                // Age is not a number
                                else if (result == MavyTextAreaHandler.AGE_NOT_NUMBER) {
                                    new MavyDialog("Save Error", "Age must be a number", "Okay", MavyDialog.MESSAGE_TYPE);
                                }

                                // Student ID already exist
                                else if (result == MavyTextAreaHandler.EXIST) {
                                    final HashMap<String, String> studentData = textAreaHandler.getStudentDataByID(STUDENT_ID, indexFromData);
                            
                                    // Fail safe
                                    String strToAppend = "exist";

                                    if (studentData != null) {
                                        final String firstName = (String)studentData.get("firstName");
                                        final String lastName = (String)studentData.get("lastName");

                                        strToAppend = "used by<br />\"" + firstName + " " + lastName + "\""; 
                                    }

                                    new MavyDialog("Update Error", "Student ID already " + strToAppend, "Okay", MavyDialog.MESSAGE_TYPE);
                                }

                                // If the data updated has no changes, show dialog
                                else if (result == MavyTextAreaHandler.EQUAL) {
                                    new MavyDialog("Update Error", "No changes", "Okay", MavyDialog.MESSAGE_TYPE);
                                }

                                // One of the required inputs are empty
                                else if (result == MavyTextAreaHandler.EMPTY) {
                                    new MavyDialog("Update Error", "Please fill in all the required fields", "Okay", MavyDialog.MESSAGE_TYPE);
                                }
                        }
                    });
                }
                    else if (!isAdding) {
                        new MavyDialog("Update Error", "Click add button first", "Okay", MavyDialog.MESSAGE_TYPE);
                    }

                    else if (isDataBlank) {
                        new MavyDialog("Update Error", "Please fill in all the required fields", "Okay", MavyDialog.MESSAGE_TYPE);
                    }

                    else {
                        new MavyDialog("Update Error", "No data", "Okay", MavyDialog.MESSAGE_TYPE);
                    }
            }
        };

        final ActionListener deleteButtonListener = new ActionListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void actionPerformed(ActionEvent ev) {
                final MavyTextAreaHandler textAreaHandler = new MavyTextAreaHandler(textArea);
                final HashMap<String, Object> selected = textAreaHandler.getHighlightedData();
                final HashMap<String, String> selectedStudentData = (HashMap<String, String>)selected.get("studentData");
                final int indexFromData = (int)selected.get("index");

                if (selectedStudentData != null) {
                    final MavyDialog dialog = new MavyDialog("Delete", "Are you sure you want to delete the selected data?", "Yes", MavyDialog.CONFIRM_TYPE);
                    
                    dialog.addAcceptListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            final int result = textAreaHandler.deleteStudentData(indexFromData);
                            
                            if (result == MavyTextAreaHandler.SUCCESS) {
                                textArea.getHighlighter().removeAllHighlights();
                                
                                // Get total students and set it to the label
                                totalStudentsLabel.setText(textAreaHandler.getTotalStudents());

                                deleteButton.setCustomEnabled(false);
                                updateButton.setCustomEnabled(false);
                            }
                                else if (result == MavyTextAreaHandler.OUT_OF_BOUNDS) {
                                    new MavyDialog("Delete error", "Please select student data from text area", "OK", MavyDialog.MESSAGE_TYPE);
                                }
                        }
                    });
                }
                    else {
                        new MavyDialog("Delete error", "Please select student data from text area", "OK", MavyDialog.MESSAGE_TYPE);
                    }
            }
        };

        final ActionListener exitButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MavyDialog dialog =  new MavyDialog("Exit", "Are you sure you want to exit?", "Exit", MavyDialog.CONFIRM_TYPE);

                dialog.addAcceptListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {    
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    }
                });
            }
        };

        /**
         * Attach listeners
         */

        addButton.addActionListener(addEventListener);
        saveButton.addActionListener(saveButtonListener);
        updateButton.addActionListener(updateButtonListener);
        deleteButton.addActionListener(deleteButtonListener);
        exitButton.addActionListener(exitButtonListener);
        
        // ============ ADDITION OF COMPONENTS ============ //

        leftNorthPanel.add(dataLabel);
        leftNorthPanel.add(totalStudentsLabel);

        leftPanel.add(leftNorthPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        panelID.add(labelID);
        panelID.add(fieldID);
        panelAge.add(labelAge);
        panelAge.add(fieldAge);
        panelFirstname.add(labelFirstname);
        panelFirstname.add(fieldFirstname);
        panelLastname.add(labelLastname);
        panelLastname.add(fieldLastname);
        panelMI.add(labelMI);
        panelMI.add(fieldMI);
        panelCourse.add(labelCourse);
        panelCourse.add(fieldCourse);
        
        rightNorthPanel.add(title);

        rightCenterPanel.add(panelID);
        rightCenterPanel.add(panelAge);
        rightCenterPanel.add(panelLastname);
        rightCenterPanel.add(panelFirstname);
        rightCenterPanel.add(panelMI);
        rightCenterPanel.add(panelCourse);

        rightSouthPanel.add(addButton);
        rightSouthPanel.add(saveButton);
        rightSouthPanel.add(updateButton);
        rightSouthPanel.add(deleteButton);
        rightSouthPanel.add(exitButton);

        rightPanel.add(rightNorthPanel, BorderLayout.NORTH);
        rightPanel.add(rightCenterPanel, BorderLayout.CENTER);
        rightPanel.add(rightSouthPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        
        panel.add(leftPanel, gbc);

        gbc.weightx = 0.2;
        gbc.gridx = 1;

        panel.add(rightPanel, gbc);

        frame.add(panel);

        // ================================================ //
        
        frame.setVisible(true);
    }
}
