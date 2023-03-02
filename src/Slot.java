public class Slot {
    private final int[] position;
    private final boolean mine;
    private boolean flag;
    private boolean checked;

    private MinesweeperGui gui;

    private final Board board;

    Slot(int[] pos, boolean isAMine, Board board) {
        this.position = pos;
        this.mine = isAMine;
        this.flag = false;
        this.board = board;
        this.checked = false;
    }

    public int getMinesAround(){
        int mineCount = 0;

        // check around
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                try {
                    if (board.getSlot(position[0] + i, position[1] + j).isMine()) {
                        mineCount++;
                    }
                } catch(Exception e) {
                    // do nothing lol
                }
            }
        }

        return mineCount;
    }

    public void reveal(int identifier) {
        setChecked();
        gui.setButtonIcon(position, identifier);
    }

    public void reveal() {
        setChecked();
        if(mine) gui.setButtonIcon(position, 9);
        else gui.setButtonIcon(position, getMinesAround());
    }

    public void sweep() {
        // if slot is already checked do nothing
        // if the game is done do nothing
        if(checked || gui.isDone()) {
            return;
        } else if (board.isFirstSweep()) {
            board.setFirstSweep(false);
        }

        // if slot is a mine end game
        if(mine) {
            reveal(12); // reveal as a clicked mine
            gui.setDone(false);
        }
        else {
            // set to correct number icon
            reveal(getMinesAround());
            // recursively sweep surrounding slots
            if (getMinesAround() == 0){ // if blank
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        try {
                            Slot slot = board.getSlot(position[0] + i, position[1] + j);
                            if (!slot.isMine() && slot.isUnchecked()) { // get all surrounding unchecked slots
                                slot.sweep();
                            }
                        } catch (Exception e) {
                            // do nothing lol
                        }
                    }
                }
            }
        }

        // check for a win
        if(board.isComplete()) {
            gui.setDone(true);
        }

    }

    public void setFlag(boolean hasFlag) {
        this.flag = hasFlag;
        gui.setButtonIcon(position, hasFlag ? 10 : 11);
    }

    public void setChecked(){
        this.checked = true;
    }

    public boolean isFlag() {
        return flag;
    }

    public boolean isMine() {
        return mine;
    }

    public void toggleFlag(){
        setFlag(!flag);
    }

    @Override
    public String toString() {
        return "Slot at " + position[0] + position[1] + " mine: " + mine;
    }

    public void setGui(MinesweeperGui gui) {
        this.gui = gui;
    }

    public boolean isUnchecked() {
        return !checked;
    }
}
