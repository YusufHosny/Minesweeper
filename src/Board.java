import java.util.Random;

public class Board {
    private final Slot[][] slots;
    private MinesweeperGui gui;

    Board(int dimensionY, int dimensionX, int mineCount) {
        // initialize slot array
        slots = new Slot[dimensionY][dimensionX];

        // create list of all indices
        int[] indices = new int[dimensionX * dimensionY];
        for(int i = 0; i < indices.length; i++){
            indices[i] = i;
        }

        // randomize indices
        Random rand = new Random();
        for (int i = 0; i < indices.length; i++) {
            int randomIndexToSwap = rand.nextInt(indices.length);
            int storedIndex = indices[randomIndexToSwap];
            indices[randomIndexToSwap] = indices[i];
            indices[i] = storedIndex;
        }


        // take first mineCount indices as the mineIndices
        int[] mineIndices = new int[mineCount];
        System.arraycopy(indices, 0, mineIndices, 0, mineCount);

        // instantiate all the slots
        boolean mine;
        for(int i = 0; i < dimensionY; i++){
            for(int j = 0; j < dimensionX; j++){
                // generate counter for current position on board
                int counter = i * dimensionX + j;

                // check if it's a mine
                mine = false;
                for(int index : mineIndices){
                    if(counter == index){
                        mine = true;
                        break;
                    }
                }

                // get the position tuple
                int[] position = new int[2];
                position[0] = counter / dimensionX;
                position[1] = counter % dimensionX;

                // create new slot object with no flag by default
                slots[i][j] = new Slot(position, mine, this);
            }
        }

    }

    public void setGUI(MinesweeperGui gui) {
        this.gui = gui;
        for(Slot[] slotRow: slots) {
            for(Slot slot: slotRow) {
                slot.setGui(gui);
            }
        }

    }

    public Slot getSlot(int y, int x){
        return slots[y][x];
    }

    public Slot getSlot(int[] indices) {
        return slots[indices[0]][indices[1]];
    }

    // check if all non mine slots have been revealed
    public boolean isComplete() {
        for (Slot[] slotRow : slots) {
            for (Slot slot : slotRow) {
                if(slot.isUnchecked() && !slot.isMine()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[] getBounds(){
        return new int[] {slots.length, slots[0].length};
    }

    // for troubleshooting
    public void printBoard(){
        for(Slot[] slotRow : slots){
            for (Slot slot : slotRow){
                System.out.println(slot.toString());
            }
        }
    }


}
