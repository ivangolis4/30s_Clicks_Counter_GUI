import javax.swing.*;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame implements ActionListener {
    final private Font MainFont = new Font("SansSerif", Font.BOLD, 20);
    JLabel lbMS, lbSecond, lbClick, lbClicks;
    JButton btnStart, btnClick;
    JPanel buttonPanel, TimerPanel, MainPanel, CounterPanel;
    private volatile boolean state;
    private int ms, second, click;

    public GUI() {
        initialization();
    }

    public void initialization() {
        /****** LABELS *******/
        lbSecond = new JLabel("00");
        lbSecond.setFont(MainFont);

        lbMS = new JLabel("00");
        lbMS.setFont(MainFont);

        lbClick = new JLabel("0");
        lbClick.setFont(MainFont);

        lbClicks = new JLabel("Click         :");
        lbClicks.setFont(MainFont);

        /****** BUTTONS *******/
        btnStart = new JButton("Start");
        btnStart.setFont(MainFont);
        btnStart.addActionListener(this);

        btnClick = new JButton("Click");
        btnClick.setFont(MainFont);
        btnClick.addActionListener(this);
        btnClick.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (state) {
                    click++;
                    lbClick.setText(String.valueOf(click));
                }
            }
        });

        /****** PANELS *******/
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 1, 1));
        buttonPanel.setFocusable(false);
        buttonPanel.add(btnStart);
        buttonPanel.add(btnClick);

        TimerPanel = new JPanel();
        TimerPanel.setLayout(new GridLayout(1, 2, 1, 1));
        TimerPanel.setFocusable(false);
        TimerPanel.add(lbSecond);
        TimerPanel.add(lbMS);

        CounterPanel = new JPanel();
        CounterPanel.setLayout(new GridLayout(1,2,1,1));
        CounterPanel.add(lbClicks);
        CounterPanel.add(lbClick);


        MainPanel = new JPanel();
        MainPanel.setLayout(new BorderLayout());
        MainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        MainPanel.setOpaque(false);
        MainPanel.add(TimerPanel, BorderLayout.NORTH);
        MainPanel.add(CounterPanel, BorderLayout.CENTER);
        MainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(MainPanel);
        setTitle("Fastest Click Game");
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnStart)) {
            state = true;
            ms = 0;
            second = 0;
            click = 0;

            Thread timerThread = new Thread(() -> {
                while (state) {
                    try {
                        Thread.sleep(1);
                        ms++;

                        if (ms > 1000) {
                            ms = 0;
                            second++;

                            if (second == 30) {
                                state = false;
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(MainPanel, "Time's up! \nYou got " + lbClick.getText() + " Clicks");
                                    lbMS.setText("00");
                                    lbSecond.setText("00");
                                    lbClick.setText("0");
                                });
                            }
                        }

                        SwingUtilities.invokeLater(() -> {
                            lbMS.setText(String.format("%02d", ms));
                            lbSecond.setText(String.format("%02d", second));
                        });

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            timerThread.start();
        }
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        SwingUtilities.invokeLater(() -> new GUI());

        
    }
}
