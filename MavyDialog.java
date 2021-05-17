import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ================================================
 * Author: Maverick G. Fabroa
 * ================================================
 * Date: May 14, 2021
 * ================================================
 * Date Modified: May 17, 2021
 * ================================================
 * Java SDK Version: 11
 * ================================================
 */

/**
 * Custom Dialog Window
 */

public class MavyDialog extends JDialog {
    private final Dimension SIZE = new Dimension(400, 225);

    public static final int CONFIRM_TYPE = 1;
    public static final int MESSAGE_TYPE = 2;

    // Accept button component
    private MavyButton cancelButton = null;
    private MavyButton acceptButton = null;

    public MavyDialog(String title, String message, String mainButtonText, int type) {
        setSize(SIZE);
        setTitle(title);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // ============== COMPONENTS =============== //

        final FlowLayout buttonLayout = new FlowLayout(FlowLayout.RIGHT);

        // ============== COMPONENTS =============== //

        // Main dialog panel
        MavyPanel dialogPanel = new MavyPanel(new GridLayout(2, 1));

        // Top and bottom panel
        MavyPanel topPanel = new MavyPanel(new BorderLayout());
        MavyPanel bottomPanel = new MavyPanel(new BorderLayout());

        // Button panel inside bottom panel
        MavyPanel buttonPanel = new MavyPanel(buttonLayout);
        
        // Message inside top panel
        MavyLabel textLabel = new MavyLabel("<html><p style=\"text-align: center;\">" + message + "</p></html>", 20);

        // Buttons inside button panel
        switch (type) {
            case MavyDialog.CONFIRM_TYPE:
                cancelButton = new MavyButton("Cancel", MavyDataEntryProps.LIGHTRED_COLOR);
                acceptButton = new MavyButton(title, MavyDataEntryProps.LIGHTGREEN_COLOR);
                break;
            case MavyDialog.MESSAGE_TYPE:
                acceptButton = new MavyButton(mainButtonText, MavyDataEntryProps.LIGHTRED_COLOR);
                break;
        }
        
        // =========== STYLE OF COMPONENTS ========== //

        dialogPanel.setBorder(MavyDataEntryProps.PADDING);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // ============ EVENT LISTENERS ============= //

        final ActionListener btnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };

        if (type == MavyDialog.CONFIRM_TYPE) {
            cancelButton.addActionListener(btnListener);
        }

        acceptButton.addActionListener(btnListener);

        // ========== ADDING OF COMPONENTS ========== //

        topPanel.add(textLabel, BorderLayout.NORTH);
        
        if (type == MavyDialog.CONFIRM_TYPE) {
            buttonPanel.add(cancelButton);
            buttonPanel.add(Box.createHorizontalStrut(10));
        }

        buttonPanel.add(acceptButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialogPanel.add(topPanel);
        dialogPanel.add(bottomPanel);

        add(dialogPanel);
        setVisible(true);
    }

    /**
     * Custom Listener for the accept button of the custom dialog
     * 
     * @param l
     */
    public void addAcceptListener(ActionListener l) {
        if (this.acceptButton != null) {
            this.acceptButton.addActionListener(l);
        }
    }
}