import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MinesweeperMouserListener extends MouseAdapter {

    private final int[] indices;
    private Board board;

    private MinesweeperGui gui;

    MinesweeperMouserListener(int y, int x, Board board) {
        this.gui = board.getGui();
        indices = new int[]{y, x};
        this.board = board;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int buttonPressed = e.getButton();
        if(buttonPressed == 1) {
            Slot slot = board.getSlot(indices);
            if(!slot.isFlag()) {
                while(board.isFirstSweep() && slot.isMine()) {
                    gui.randomizeBoard();
                    board = gui.getBoard();
                    slot = board.getSlot(indices);
                }
                slot.sweep();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int buttonPressed = e.getButton();
        if(buttonPressed == 3) {
            Slot slot = board.getSlot(indices);
            if(slot.isUnchecked()) {
                slot.toggleFlag();
            }
        }
    }
}
