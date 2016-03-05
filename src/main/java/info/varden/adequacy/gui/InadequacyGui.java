package info.varden.adequacy.gui;

import info.varden.adequacy.ClassUtils;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Graphical user interface for indicating an outdated Java runtime.
 */
public abstract class InadequacyGui extends javax.swing.JFrame {
	
	/**
	 * Mutual exclusion object. Prevents the game from continuing execution while displaying the frame.
	 */
	final Object mutex = new Object();

    /**
     * Creates new form InadequacyGui
     */
    public InadequacyGui() {
        initComponents();
    }
    
    /**
     * Defined in subclasses. Provides means to shut down the running game.
     */
    public abstract void shutdownGame();
    
    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
    	
    	// Set system look and feel
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (InstantiationException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		} catch (UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}
    	
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Your Java runtime is outdated!");
        setBackground(new java.awt.Color(255, 255, 255));
        
        addWindowListener(new WindowAdapter() {
        	/**
        	 * Release mutual exclusion lock
        	 */
        	@Override
        	public void windowClosing(WindowEvent e) {
        		synchronized (mutex) {
        			mutex.notify();
        		}
        	}
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/adequacy/outdated.png")));
        jLabel2.setText("Your Java runtime is outdated!");
        jLabel2.setFocusTraversalPolicyProvider(true);
        jLabel2.setIconTextGap(16);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel3.setText("<html><p>One or more of the mods you are playing with requires a Java version more recent ("
        		+ ClassUtils.getLastNewTestedVersion()
        		+ ") than the one you have installed.</p><br />"
        		+ "<p>You will not be able to play with these mods until you update Java to the latest version."
        		+ "Updating is easy: Simply download the latest version from the official Java website and install it.</p><br />"
        		+ "<p>After installing the latest Java version, restart the game, and you should be able to play.</p></html>");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jButton1.setText("Download the latest Java version here!");
        jButton1.addActionListener(new ActionListener() {

        	/**
        	 * Browse to the Java download site in default web browser
        	 */
			@Override
			public void actionPerformed(ActionEvent e) {
				Desktop d = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
				try {
					d.browse(new URI("https://www.java.com/en/download/"));
					synchronized (mutex) {
						mutex.notify();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
        	
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE))
                        .addGap(20, 20, 20))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        // Center on screen
        setLocationRelativeTo(null);
        setVisible(true);
        toFront();
        /**
         * Halt execution while GUI is active
         */
        synchronized (mutex) {
        	try {
        		mutex.wait();
        	} catch (InterruptedException ex) {
        	}
        }
        // Shut down the game because it will crash if it continues execution
        shutdownGame();
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
}
