import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaintCanvas extends JPanel implements MouseMotionListener {
    private static List<Point> points = new ArrayList<>();
    private static List<Color> colors = new ArrayList<>();
    private static List<Integer> sizes = new ArrayList<>();

    public static Color col = Color.BLACK;
    public static Color background = Color.WHITE;

    public static Integer size = 10;

    public PaintCanvas() {
        setBackground(background);
        addMouseMotionListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        points.add(e.getPoint());// Dodawanie punktów do listy
        colors.add(col);
        sizes.add(size);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            Color c = colors.get(i);
            g.setColor(c);
            g.fillRect(p.x, p.y, sizes.get(i), sizes.get(i)); // Rysowanie punktów z listy
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Paint Application");
            frame.setIconImage(new ImageIcon("src/resources/color-wheel.png").getImage());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            PaintCanvas canvas = new PaintCanvas();
            frame.add(canvas);

            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
            menuBar.setBackground(Color.lightGray);
            menuBar.add(fileMenu);
            fileMenu.getAccessibleContext().setAccessibleDescription("Dealing with Files");
            frame.setJMenuBar(menuBar);

            fileMenu.add(new AbstractAction("Reset canvas")
            {
                public void actionPerformed(ActionEvent event)
                {
                    background = Color.WHITE;
                    canvas.setBackground(background);
                    points.clear();
                    colors.clear();

                }
            });
            ImageIcon newIcon = new ImageIcon("src/resources/new.png");
            newIcon = new ImageIcon(newIcon.getImage().getScaledInstance(15,15, java.awt.Image.SCALE_SMOOTH));
            fileMenu.getItem(0).setIcon(newIcon);

            fileMenu.add(new AbstractAction("Quick Save") {
                public void actionPerformed(ActionEvent event) {
                    BufferedImage img = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB); // dostosowanie wielkosci obrazu do wielkosci plótna przez co po dowolnym resizowaniu okienka program robi screena w takiej rozdzielczosci jaka jest ustawiona
                    Graphics2D g2 = img.createGraphics();
                    canvas.paintComponent(g2); // Rysowanie całego rysunku
                    try {
                        File file = new File("src/resources/images/image.png");
                        int i = 0;
                        String path = "src/resources/images/image.png";
                        while(file.exists()){
                            i++;
                            path = "src/resources/images/image"+i+".png";
                            file = new File(path);

                        }
                        ImageIO.write(img, "png", file); // img write
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            fileMenu.getItem(1).setAccelerator(KeyStroke.getKeyStroke("ctrl S")); // wybieramy 1 item z file i dodajemy accelerator ctrl + S
            ImageIcon saveIcon = new ImageIcon("src/resources/save.png");
            saveIcon = new ImageIcon(saveIcon.getImage().getScaledInstance(15,15, java.awt.Image.SCALE_SMOOTH));
            fileMenu.getItem(1).setIcon(saveIcon);

            fileMenu.add(new AbstractAction("Save as")
            {
                public void actionPerformed(ActionEvent event)
                {
                    BufferedImage img = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB); // dostosowanie wielkosci obrazu do wielkosci plótna przez co po dowolnym resizowaniu okienka program robi screena w takiej rozdzielczosci jaka jest ustawiona
                    Graphics2D g2 = img.createGraphics();
                    canvas.paintComponent(g2);
                    JFileChooser fileChooser = new JFileChooser("src/resources/images/");
                    fileChooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
                    fileChooser.setFileFilter(new FileNameExtensionFilter("*.jpg", "jpg"));

                    fileChooser.setDialogTitle("Save your image");
                    fileChooser.setSelectedFile(new File("image.png"));
                    int selection = fileChooser.showSaveDialog(null);

                    if (selection == JFileChooser.APPROVE_OPTION) {
                        File image = fileChooser.getSelectedFile();
                        try {
                            if(fileChooser.getFileFilter().getDescription().equals("*.png")){
                                ImageIO.write(img, "png" , image);
                            }
                            if(fileChooser.getFileFilter().getDescription().equals("*.jpg")){
                                ImageIO.write(img, "jpg" , image);
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
            ImageIcon fullSave = new ImageIcon("src/resources/fullsave.jpg");
            fullSave = new ImageIcon(fullSave.getImage().getScaledInstance(15,15, java.awt.Image.SCALE_SMOOTH));
            fileMenu.getItem(2).setIcon(fullSave);
            fileMenu.addSeparator();

            fileMenu.add(new AbstractAction("Exit")
            {
                public void actionPerformed(ActionEvent event)
                {
                    System.exit(0);
                }
            });

            JMenu toolsMenu = new JMenu("Tools");
            menuBar.add(toolsMenu);

            JMenu brushResize = new JMenu("Change brush size");
            toolsMenu.add(brushResize);

            brushResize.add(new AbstractAction("+") {
                public void actionPerformed(ActionEvent event) {
                    if(size<100){
                        size++;
                    }

                }
            });
            brushResize.add(new AbstractAction("-") {
                public void actionPerformed(ActionEvent event) {
                    if(size>1){
                        size--;
                    }

                }
            });
            brushResize.add(new AbstractAction("4") {
                public void actionPerformed(ActionEvent event) {
                    size = 4;

                }
            });
            brushResize.add(new AbstractAction("8") {
                public void actionPerformed(ActionEvent event) {
                    size = 8;

                }
            });
            brushResize.add(new AbstractAction("16") {
                public void actionPerformed(ActionEvent event) {
                    size = 16;

                }
            });
            brushResize.add(new AbstractAction("24") {
                public void actionPerformed(ActionEvent event) {
                    size = 16;

                }
            });
            brushResize.add(new AbstractAction("32") {
                public void actionPerformed(ActionEvent event) {
                    size = 32;

                }
            });
            brushResize.add(new AbstractAction("48") {
                public void actionPerformed(ActionEvent event) {
                    size = 48;

                }
            });
            brushResize.add(new AbstractAction("64") {
                public void actionPerformed(ActionEvent event) {
                    size = 64;

                }
            });
            brushResize.add(new AbstractAction("80") {
                public void actionPerformed(ActionEvent event) {
                    size = 80;

                }
            });
            brushResize.add(new AbstractAction("96") {
                public void actionPerformed(ActionEvent event) {
                    size = 96;

                }
            });

            toolsMenu.add(new AbstractAction("Eraser") {
                public void actionPerformed(ActionEvent event) {
                    col = Color.WHITE;

                }
            });
            ImageIcon eraserIcon = new ImageIcon("src/resources/eraser.png");
            eraserIcon = new ImageIcon(eraserIcon.getImage().getScaledInstance(15,15, java.awt.Image.SCALE_SMOOTH));
            toolsMenu.getItem(1).setIcon(eraserIcon);

            toolsMenu.add(new AbstractAction("Change brush color") {
                public void actionPerformed(ActionEvent event) {
                    col = JColorChooser.showDialog(null, "Pick your color", col);

                }
            });

            ImageIcon colorIcon = new ImageIcon("src/resources/palette.png");
            colorIcon = new ImageIcon(colorIcon.getImage().getScaledInstance(15,15, java.awt.Image.SCALE_SMOOTH));
            toolsMenu.getItem(2).setIcon(colorIcon);

            toolsMenu.add(new AbstractAction("Change background color") {
                public void actionPerformed(ActionEvent event) {
                    background = JColorChooser.showDialog(null, "Pick your color", background);
                    canvas.setBackground(background);
                }
            });
            ImageIcon backIcon = new ImageIcon("src/resources/backpalette.png");
            backIcon = new ImageIcon(backIcon.getImage().getScaledInstance(15,15, java.awt.Image.SCALE_SMOOTH));
            toolsMenu.getItem(3).setIcon(backIcon);



            JMenu viewMenu = new JMenu("View");
            menuBar.add(viewMenu);

            JMenu resChange = new JMenu("Change canvas size");
            viewMenu.add(resChange);

            resChange.add(new AbstractAction("1080x720") {
                public void actionPerformed(ActionEvent event) {
                    canvas.setSize(1080,720);
                    frame.setSize(1080, 780);
                }
            });
            resChange.add(new AbstractAction("800x600") {
                public void actionPerformed(ActionEvent event) {
                    canvas.setSize(800,600);
                    frame.setSize(800, 660);
                }
            });
            resChange.add(new AbstractAction("400x400") {
                public void actionPerformed(ActionEvent event) {
                    canvas.setSize(400,400);
                    frame.setSize(400, 460);
                }
            });



            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }
}
