import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MinesweeperMouserListener extends MouseAdapter {

    private final int[] indices;
    private final Board board;

    MinesweeperMouserListener(int y, int x, Board board) {
        indices = new int[]{y, x};
        this.board = board;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int buttonPressed = e.getButton();
        if(buttonPressed == 1) {
            Slot slot = board.getSlot(indices);
            if(!slot.isFlag()) {
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
