import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

 class ResumeBuilderEnhanced extends JFrame {
    // Input fields
    private JTextField nameField, titleField, emailField, phoneField, addressField;
    private JTextArea educationArea, skillsArea, experienceArea, projectsArea;
    private JTextField linkedinField, githubField, portfolioField;
    private JButton generateButton, exportButton, clearButton, themeToggleButton;
    private JEditorPane previewPane;
    private boolean darkTheme = false;

    // Visual constants
    private final Color ACCENT = new Color(0x0D6EFD); // bright blue accent
    private final Color BG_LIGHT = Color.WHITE;
    private final Color BG_DARK = new Color(0x1E1E1E);
    private final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 18);
    private final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 13);
    private final Font FIELD_FONT = new Font("SansSerif", Font.PLAIN, 14);

    public ResumeBuilderEnhanced() {
        setTitle("Resume Builder — Modern");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 700);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        // Left: Inputs
        JPanel left = new JPanel(new GridBagLayout());
        left.setBorder(BorderFactory.createTitledBorder("Input"));
        left.setBackground(BG_LIGHT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Helper to add label & field
        addLabel(left, "Full Name:", row, 0, gbc);
        nameField = makeField("Aditya Mahajan");
        addComponent(left, nameField, row++, 1, gbc);

        addLabel(left, "Professional Title:", row, 0, gbc);
        titleField = makeField("e.g., Java Backend Developer");
        addComponent(left, titleField, row++, 1, gbc);

        addLabel(left, "Email:", row, 0, gbc);
        emailField = makeField("you@example.com");
        addComponent(left, emailField, row++, 1, gbc);

        addLabel(left, "Phone:", row, 0, gbc);
        phoneField = makeField("91-XXXXXXXXXX");
        addComponent(left, phoneField, row++, 1, gbc);

        addLabel(left, "Address:", row, 0, gbc);
        addressField = makeField("City, State");
        addComponent(left, addressField, row++, 1, gbc);

        addLabel(left, "Education (brief):", row, 0, gbc);
        educationArea = makeTextArea("BCA — MCM DAV College Kangra (HPU) — 7.83 CGPA", 3);
        addComponent(left, new JScrollPane(educationArea), row++, 1, gbc);

        addLabel(left, "Skills (comma separated):", row, 0, gbc);
        skillsArea = makeTextArea("Java, Spring Boot, MongoDB, SQL", 2);
        addComponent(left, new JScrollPane(skillsArea), row++, 1, gbc);

        addLabel(left, "Experience (brief):", row, 0, gbc);
        experienceArea = makeTextArea("Internships, part-time, significant projects", 3);
        addComponent(left, new JScrollPane(experienceArea), row++, 1, gbc);

        addLabel(left, "Projects (title - 1 line each):", row, 0, gbc);
        projectsArea = makeTextArea("ResumeBuilder — Java Swing\nPortfolio Generator — Spring Boot", 3);
        addComponent(left, new JScrollPane(projectsArea), row++, 1, gbc);

        addLabel(left, "LinkedIn URL:", row, 0, gbc);
        linkedinField = makeField("https://linkedin.com/in/yourname");
        addComponent(left, linkedinField, row++, 1, gbc);

        addLabel(left, "GitHub URL:", row, 0, gbc);
        githubField = makeField("https://github.com/yourname");
        addComponent(left, githubField, row++, 1, gbc);

        addLabel(left, "Portfolio/Website:", row, 0, gbc);
        portfolioField = makeField("https://yourportfolio.com");
        addComponent(left, portfolioField, row++, 1, gbc);

        // Buttons row
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        generateButton = new JButton("Generate Resume");
        exportButton = new JButton("Export HTML");
        clearButton = new JButton("Clear");
        themeToggleButton = new JButton("Toggle Dark Theme");
        buttons.add(generateButton);
        buttons.add(exportButton);
        buttons.add(clearButton);
        buttons.add(themeToggleButton);

        gbc.gridwidth = 2;
        addComponent(left, buttons, row++, 0, gbc);

        // Right: Preview
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Preview"));
        previewPane = new JEditorPane();
        previewPane.setContentType("text/html");
        previewPane.setEditable(false);
        previewPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        previewPane.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // initial example preview
        previewPane.setText(buildHtmlPreview());

        right.add(new JScrollPane(previewPane), BorderLayout.CENTER);

        // Split pane
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(420);
        split.setOneTouchExpandable(true);
        root.add(split, BorderLayout.CENTER);

        // Action listeners
        generateButton.addActionListener(this::onGenerate);
        exportButton.addActionListener(this::onExport);
        clearButton.addActionListener(e -> clearAll());
        themeToggleButton.addActionListener(e -> toggleTheme());

        applyTheme(); // initial theme
    }

    private void onGenerate(ActionEvent e) {
        previewPane.setText(buildHtmlPreview());
        JOptionPane.showMessageDialog(this, "Resume generated in preview. Use Export HTML to save.");
    }

    private void onExport(ActionEvent e) {
        String html = buildHtmlPreview();
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export Resume as HTML");
        chooser.setSelectedFile(new File("resume.html"));
        int choice = chooser.showSaveDialog(this);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try (FileWriter fw = new FileWriter(f)) {
                fw.write(html);
                JOptionPane.showMessageDialog(this, "Exported to: " + f.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to export: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearAll() {
        nameField.setText("");
        titleField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        educationArea.setText("");
        skillsArea.setText("");
        experienceArea.setText("");
        projectsArea.setText("");
        linkedinField.setText("");
        githubField.setText("");
        portfolioField.setText("");
        previewPane.setText(buildHtmlPreview());
    }

    private void toggleTheme() {
        darkTheme = !darkTheme;
        applyTheme();
    }

    private void applyTheme() {
        if (darkTheme) {
            getContentPane().setBackground(BG_DARK);
            previewPane.setBackground(new Color(0x2B2B2B));
            previewPane.setForeground(Color.WHITE);
        } else {
            getContentPane().setBackground(BG_LIGHT);
            previewPane.setBackground(Color.WHITE);
            previewPane.setForeground(Color.DARK_GRAY);
        }
        // Accent for buttons
        generateButton.setBackground(ACCENT);
        generateButton.setForeground(Color.WHITE);
    }

    private String buildHtmlPreview() {
        // Escape minimal HTML characters for user-provided text
        String name = escapeHtml(nameField.getText().trim());
        if (name.isEmpty()) name = "Your Name";
        String title = escapeHtml(titleField.getText().trim());
        String email = escapeHtml(emailField.getText().trim());
        String phone = escapeHtml(phoneField.getText().trim());
        String address = escapeHtml(addressField.getText().trim());
        String education = escapeHtml(educationArea.getText().trim()).replace("\n", "<br>");
        String skills = escapeHtml(skillsArea.getText().trim()).replace("\n", "<br>");
        String experience = escapeHtml(experienceArea.getText().trim()).replace("\n", "<br>");
        String projects = escapeHtml(projectsArea.getText().trim()).replace("\n", "<br>");
        String linkedin = escapeHtml(linkedinField.getText().trim());
        String github = escapeHtml(githubField.getText().trim());
        String portfolio = escapeHtml(portfolioField.getText().trim());

        String accentHex = String.format("#%06X", (0xFFFFFF & ACCENT.getRGB()));

        StringBuilder html = new StringBuilder();
        html.append("<!doctype html><html><head><meta charset='utf-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        html.append("<style>");
        html.append("body{font-family: -apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,'Helvetica Neue',Arial; padding:20px;}");
        html.append(".header{display:flex; align-items:center; justify-content:space-between;}");
        html.append(".name{font-size:26px; font-weight:700; color:").append(accentHex).append("}");
        html.append(".title{font-size:14px; color:#555;}");
        html.append(".contact{font-size:13px; color:#333; text-align:right}");
        html.append(".section{margin-top:18px;}");
        html.append(".section h3{margin:0 0 8px 0; font-size:15px; color:").append(accentHex).append("}");
        html.append(".card{padding:12px; border-radius:8px; background:#f8f9fb}");
        html.append(".list{margin:0; padding-left:16px}");
        html.append("a{color:").append(accentHex).append("; text-decoration:none}");
        html.append("</style></head><body>");

        html.append("<div class='header'>");
        html.append("<div>");
        html.append("<div class='name'>").append(name).append("</div>");
        if (!title.isEmpty()) html.append("<div class='title'>").append(title).append("</div>");
        html.append("</div>");
        html.append("<div class='contact'>");
        if (!email.isEmpty()) html.append("<div>").append(email).append("</div>");
        if (!phone.isEmpty()) html.append("<div>").append(phone).append("</div>");
        if (!address.isEmpty()) html.append("<div>").append(address).append("</div>");
        html.append("</div>");
        html.append("</div>"); // header

        if (!education.isEmpty()) {
            html.append("<div class='section'><h3>Education</h3><div class='card'>").append(education).append("</div></div>");
        }
        if (!skills.isEmpty()) {
            html.append("<div class='section'><h3>Skills</h3><div class='card'>").append(skills).append("</div></div>");
        }
        if (!experience.isEmpty()) {
            html.append("<div class='section'><h3>Experience</h3><div class='card'>").append(experience).append("</div></div>");
        }
        if (!projects.isEmpty()) {
            html.append("<div class='section'><h3>Projects</h3><div class='card'>").append(projects).append("</div></div>");
        }

        if (!linkedin.isEmpty() || !github.isEmpty() || !portfolio.isEmpty()) {
            html.append("<div class='section'><h3>Links</h3><div class='card'>");
            if (!linkedin.isEmpty()) html.append("<div><strong>LinkedIn:</strong> <a href='").append(linkedin).append("'>").append(linkedin).append("</a></div>");
            if (!github.isEmpty()) html.append("<div><strong>GitHub:</strong> <a href='").append(github).append("'>").append(github).append("</a></div>");
            if (!portfolio.isEmpty()) html.append("<div><strong>Portfolio:</strong> <a href='").append(portfolio).append("'>").append(portfolio).append("</a></div>");
            html.append("</div></div>");
        }

        html.append("<div style='margin-top:18px; color:#999; font-size:12px'>Generated by ResumeBuilderEnhanced</div>");
        html.append("</body></html>");

        return html.toString();
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    // UI helpers
    private void addLabel(JPanel panel, String text, int gridy, int gridx, GridBagConstraints gbc) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        panel.add(label, gbc);
    }

    private void addComponent(JPanel panel, Component comp, int gridy, int gridx, GridBagConstraints gbc) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        panel.add(comp, gbc);
    }

    private JTextField makeField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setFont(FIELD_FONT);
        f.setForeground(Color.DARK_GRAY);
        // simple placeholder behavior
        f.addFocusListener(new FocusAdapter() {
            private boolean cleared = false;
            @Override
            public void focusGained(FocusEvent e) {
                if (!cleared && f.getText().equals(placeholder)) {
                    f.setText("");
                    cleared = true;
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (f.getText().trim().isEmpty()) {
                    f.setText(placeholder);
                    cleared = false;
                }
            }
        });
        return f;
    }

    private JTextArea makeTextArea(String placeholder, int rows) {
        JTextArea ta = new JTextArea(placeholder, rows, 20);
        ta.setFont(FIELD_FONT);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setForeground(Color.DARK_GRAY);
        // placeholder-like: clear on first focus
        ta.addFocusListener(new FocusAdapter() {
            private boolean cleared = false;
            @Override
            public void focusGained(FocusEvent e) {
                if (!cleared && ta.getText().equals(placeholder)) {
                    ta.setText("");
                    cleared = true;
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (ta.getText().trim().isEmpty()) {
                    ta.setText(placeholder);
                    cleared = false;
                }
            }
        });
        return ta;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ResumeBuilderEnhanced frame = new ResumeBuilderEnhanced();
            frame.setVisible(true);
        });
    }
}

