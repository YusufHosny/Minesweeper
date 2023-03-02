import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

class MinesweeperGui extends JFrame{
    private int currentXSize, currentYSize, currentMineCount;
    private boolean done, won;
    private Board board;
    private final JPanel centralArea;

    private ArrayList<JButton> buttonList;

    private final Icon[] icons;

    MinesweeperGui(String title) {
        super(title);

        // set default size and mine count
        currentXSize = 9;
        currentYSize = 9;
        currentMineCount = 10;

        // add the default icons
        icons = new Icon[13];
        icons[0] = new ImageIcon("C:\\Users\\Yusuf\\IdeaProjects\\Minesweeper\\icons\\icons\\blank.png");
        icons[9] = new ImageIcon("C:\\Users\\Yusuf\\IdeaProjects\\Minesweeper\\icons\\icons\\mine.png");
        icons[10] = new ImageIcon("C:\\Users\\Yusuf\\IdeaProjects\\Minesweeper\\icons\\icons\\flag.png");
        icons[11] = new ImageIcon("C:\\Users\\Yusuf\\IdeaProjects\\Minesweeper\\icons\\icons\\checked.png");
        icons[12] = new ImageIcon("C:\\Users\\Yusuf\\IdeaProjects\\Minesweeper\\icons\\icons\\caught.png");
        for(int i = 1; i < 9; i++) {
            icons[i] = new ImageIcon("C:\\Users\\Yusuf\\IdeaProjects\\Minesweeper\\icons\\icons\\" + i + ".png");
        }

        // initialize the Frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);

        // create a menu bar and add the selections
        JMenuBar mb = new JMenuBar();
        JMenu playMenu = new JMenu("Play");
        JMenu settingsMenu = new JMenu("Settings");
        mb.add(playMenu);
        mb.add(settingsMenu);

        JMenuItem mineNumberEditor = new JMenuItem("Number of Mines");
        mineNumberEditor.addActionListener(e -> MinesweeperMenuListeners.mineNumberEditor(this));
        JMenuItem boardSizeEditor = new JMenuItem("Board Size");
        boardSizeEditor.addActionListener(e -> MinesweeperMenuListeners.boardSizeEditor(this));
        settingsMenu.add(mineNumberEditor);
        settingsMenu.add(boardSizeEditor);

        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(e -> MinesweeperMenuListeners.newGame(this));
        playMenu.add(newGame);

        centralArea = new JPanel();

        JButton playButton = new JButton("PLay");
        centralArea.add(playButton);
        playButton.addActionListener(e -> this.generateMinesweeperGui());


        //Adding Components to the frame.
        this.getContentPane().add(BorderLayout.NORTH, mb);
        this.getContentPane().add(BorderLayout.CENTER, centralArea);

        this.setVisible(true);
    }

    public void setBoardSize(int y, int x) {
        currentYSize = y;
        currentXSize = x;
    }

    public void setBoardMineCount(int mineCount) {
        currentMineCount = mineCount;
    }

    public void setButtonIcon(int[] indices, int identifier) {
        int[] bounds = board.getBounds();
        JButton button = buttonList.get(indices[0] * bounds[1] + indices[1]);
        button.setIcon(icons[identifier]);
    }


    public void generateMinesweeperGui() {
        // set done to false as default
        done = false;

        // default board
        board = new Board(currentYSize, currentXSize, currentMineCount);
        board.setGUI(this);

        buttonList = new ArrayList<>();
        centralArea.removeAll();

        int[] bounds = board.getBounds();
        JPanel minesweeperPanel = new JPanel(new GridLayout(bounds[0], bounds[1]));

        for(int i = 0; i < bounds[0]; i++){ // y bound
            for(int j = 0; j < bounds[1]; j++){ // x bound
                JButton button = new JButton();
                button.setIcon(icons[11]);
                button.addMouseListener(new MinesweeperMouserListener(i, j, board));
                buttonList.add(button);
                minesweeperPanel.add(button);
            }
        }
        minesweeperPanel.setVisible(true);
        centralArea.add(BorderLayout.CENTER, minesweeperPanel);
        refreshContainer(centralArea);
    }

    public void refreshContainer(Container container) {
        container.setVisible(false);
        container.setVisible(true);
    }

    public void onGameEnd() {
        // show game end message
        JLabel gameEndMessage = new JLabel("Congrats!! You " + (won ? "Won" : "Lost dumbass"));
        centralArea.add(BorderLayout.NORTH, gameEndMessage);
        refreshContainer(centralArea);

        // if lost show remaining mines
        showAll();
    }

    public void showAll() {
        ArrayList<Slot> unrevealed = new ArrayList<>();

        // add all unrevealed mines
        int[] bounds = board.getBounds();
        for (int i = 0; i < bounds[0]; i++) {
            for (int j = 0; j < bounds[1]; j++) {
                Slot slot = board.getSlot(i, j);
                if (slot.isUnchecked() && slot.isMine()) {
                    unrevealed.add(slot);
                }
            }
        }

        // new unrevealed mine iterator
        Iterator<Slot> unrevealedIter = unrevealed.iterator();

        // possible memory leak lmao
        Timer timer = new Timer(600, e -> {
            if(unrevealedIter.hasNext()) {
                unrevealedIter.next().reveal();
            }
        });
        timer.start();
    }

    public void setDone(boolean won) {
        this.won = won;
        this.done = true;
        onGameEnd();
    }

    public boolean isDone() {
        return done;
    }
}